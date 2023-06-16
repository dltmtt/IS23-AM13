package it.polimi.ingsw.server;

import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.utils.FullRoomException;
import it.polimi.ingsw.utils.Utils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

public class SocketClientHandler implements Runnable, ServerCommunicationInterface {

    public final BufferedReader clientBufferedReader;
    private final Socket socket;
    public PrintStream clientPrintStream;
    public DataOutputStream dataOutputStream;
    public Thread listenThread;
    private String username;

    public SocketClientHandler(Socket socket) throws IOException {
        this.socket = socket;

        // To send data to the client
        try {
            clientPrintStream = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // To read data coming from the client
        try {
            clientBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // This is to send data to the server
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Unable to create output stream");
            throw new RuntimeException(e);
        }

        // Listen for messages coming from the client
        listenThread = new Thread(() -> {
            String clientString;
            while (true) {
                try {
                    synchronized (clientBufferedReader) {
                        clientString = clientBufferedReader.readLine();
                        JSONParser parser = new JSONParser();
                        JSONObject messageFromClient = null;
                        try {
                            messageFromClient = (JSONObject) parser.parse(clientString);
                        } catch (ParseException e) {
                            System.err.println("Unable to parse message from client");
                        } catch (NullPointerException e) {
                            Utils.showDebugInfo(e);
                            // disconnect(username);
                            break;
                        }

                        Message message = new Message(messageFromClient);

                        try {
                            receiveMessageTcp(message, this);
                        } catch (IllegalAccessException | FullRoomException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (IOException e) {
                    Utils.showDebugInfo(e);
                    // We are here because the client disconnected (probably)
                    System.err.println("IOException");
                    // try {
                    //     disconnect(username);
                    // } catch (RemoteException ex) {
                    //     throw new RuntimeException(ex);
                    // }
                    break;
                }
            }
        });
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void run() {
        listenThread.start();
    }

    public void sendString(String message) {
        clientPrintStream.println(message);
    }

    public void close() {
        sendString("Connection closed.");
        listenThread.interrupt();
        clientPrintStream.close();
        try {
            clientBufferedReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while closing buffered reader or socket.");
        }

        // terminate application
        System.exit(0);
    }

    @Override
    public void receiveMessageTcp(Message message, SocketClientHandler client) throws IllegalAccessException, RemoteException, FullRoomException {
        String category = message.getCategory();

        switch (category) {
            // Maybe the controller should do something with the pong.
            case "pong" -> {
                System.out.println("Received pong from " + client.getUsername());
                controller.pong(client.getUsername());
                controller.addPongLost(client.getUsername());
                if (controller.disconnectedPlayers.contains(client.getUsername())) {
                    System.out.println("Player " + client.getUsername() + " reconnected");
                    startPingThread(client);
                    sendAll(new Message("reconnected", client.getUsername()));
                    controller.disconnectedPlayers.remove(client.getUsername());
                }
            }
            case "numOfPlayersMessage" -> {
                int numPlayer = message.getNumPlayer();
                String isOk = controller.checkNumPlayer(numPlayer);
                if (!isOk.equals("ok")) {
                    sendMessageToClient(new Message("numOfPlayersNotOK"));
                } else {
                    controller.setNumberOfPlayers(numPlayer);
                    if (controller.checkRoom() == 1) {
                        startGame();
                    } else if (controller.checkRoom() == -1) {
                        removePlayers();
                        startGame();
                    } else {
                        sendMessageToClient(new Message("waitingRoom"));
                    }
                }
            }
            case "pick" -> {
                if ("ok".equals(controller.pick(message.getPick()))) {
                    sendMessageToClient(new Message(controller.getPicked(message.getPick())));
                } else {
                    sendMessageToClient(new Message("pickRetry"));
                }
            }
            case "insertMessage" -> {
                if (controller.checkInsert(message.getInsert()) == 1) {
                    sendUpdate();
                    nextTurn();
                } else if (controller.checkInsert(message.getInsert()) == 0) {
                    client.sendMessageToClient(new Message("insertRetry", "notValidNumber"));
                } else if (controller.checkInsert(message.getInsert()) == -1) {
                    client.sendMessageToClient(new Message("insertRetry", "notEnoughFreeCells"));
                }
                // TODO: return an error message if the insert is not valid, otherwise the game will freeze
            }
            case "sort" -> controller.rearrangePicked(message.getSort());
            case "completeLogin" -> {
                String username = message.getUsername();
                int checkStatus = controller.checkUsername(username);
                checkUsername(client, message.getUsername(), message.getFirstGame(), checkStatus);
            }
            default -> System.out.println(message + " requested unknown");
        }
    }

    public void startPingThread(SocketClientHandler client) throws RemoteException {
        String username = null;
        username = client.getUsername();
        String finalUsername = username;
        Thread pingThread = new Thread(() -> {

            while (true) {
                try {
                    client.sendMessageToClient(new Message("ping"));
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    System.err.println("Ping thread interrupted");
                    break;
                }
            }
        });
        pingThread.start();

        Thread checkThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(20000);
                    if (!controller.pongReceived.contains(finalUsername) && !controller.disconnectedPlayers.contains(finalUsername)) {
                        System.err.println("Pong not received from " + finalUsername + ". Disconnecting.");
                        Thread.sleep(10000);
                        if (controller.pongLost.get(finalUsername) != 0) {
                            disconnect(finalUsername);
                            pingThread.interrupt();
                            break;
                        }
                    } else {
                        controller.pongReceived.remove(finalUsername);
                    }
                } catch (InterruptedException | RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        checkThread.start();
    }

    public void resendGameToReconnectedClient(SocketClientHandler client) throws RemoteException {
        int position = controller.getPositionByUsername(getUsername());
        System.out.println("Sending game to " + getUsername() + ", who just reconnected.");

        Message game = new Message(controller.getPersonalGoalCard(position), controller.getCommonGoals(), controller.getBookshelves(), controller.getBoard());
        client.sendMessageToClient(game);

        controller.addClient(getUsername(), client);
        sendAllExcept(client.getUsername(), new Message("reconnected", client.getUsername()));
        sendTurn(client);
    }

    public void sendTurn(SocketClientHandler client) {
        String currentPlayer = controller.getCurrentPlayer();
        if (currentPlayer.equals(getUsername())) {
            client.sendMessageToClient(new Message("turn"));
        } else {
            client.sendMessageToClient(new Message("otherTurn", currentPlayer));
        }
    }

    public void sendMessageToClient(Message message) {
        clientPrintStream.println(message.getJSONstring());
    }

    public void checkUsername(SocketClientHandler client, String username, boolean firstGame, int checkStatus) throws RemoteException, FullRoomException {
        switch (checkStatus) {
            case 1 -> {
                if (controller.isGameStarted()) {
                    client.sendMessageToClient(new Message("gameAlreadyStarted"));
                } else {
                    client.sendMessageToClient(new Message("username", username));

                    controller.addPlayer(username, 0, firstGame);
                    System.out.println(username + " logged in.");

                    setUsername(username);
                    controller.addClient(username, client);
                    startPingThread(client);
                    controller.startRoom();

                    if (controller.isFirst()) {
                        // Let the first player choose the number of players
                        client.sendMessageToClient(new Message("chooseNumOfPlayer"));
                    } else {
                        client.sendMessageToClient(new Message("waitingRoom"));
                        if (controller.checkRoom() == 1) {
                            startGame();
                            System.out.println("Game started.");
                        } else if (controller.checkRoom() == -1) {
                            removePlayers();
                        }
                    }
                }
            }
            case 0 -> {
                // The username has already been taken, retry
                // try {
                //     Thread.sleep(3000);
                // } catch (InterruptedException e) {
                //     e.printStackTrace();
                // }
                checkStatus = controller.checkUsername(username);
                if (checkStatus == 0) {
                    System.out.println(username + " requested login, but the username is already taken.");
                    client.sendMessageToClient(new Message("UsernameRetry"));
                } else {
                    System.out.println(username + " reconnected.");
                    setUsername(username);
                    resendGameToReconnectedClient(client);
                }
            }
            case -1 -> {
                // The username is already taken, but the player was disconnected and is trying to reconnect
                System.out.println(username + " reconnected.");
                setUsername(username);
                resendGameToReconnectedClient(client);
            }
        }
    }
}
