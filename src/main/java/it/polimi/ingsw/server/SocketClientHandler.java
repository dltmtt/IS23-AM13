package it.polimi.ingsw.server;

import it.polimi.ingsw.commons.Message;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class SocketClientHandler implements Runnable, ServerCommunicationInterface {

    /**
     * The place to read incoming messages from the client.
     */
    public final BufferedReader clientBufferedReader;
    private final Socket socket;
    /**
     * The place to write outgoing messages to the client.
     */
    public PrintStream clientPrintStream;
    /**
     * The thread that listens for messages coming from the client.
     */
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
                            //                            disconnect(username);
                            System.err.println("Error while reading from client.");
                            break;
                        }

                        Message message = new Message(messageFromClient);

                        try {
                            receiveMessage(message, this);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (IOException e) {
                    // We are here because the client disconnected (probably)
                    System.err.println("Error while reading from client. IO");

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

    public void close() {
        listenThread.interrupt();
        clientPrintStream.close();
        try {
            clientBufferedReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while closing buffered reader or socket.");
        }
    }

    @Override
    public void receiveMessage(Message message, SocketClientHandler client) throws IllegalAccessException, RemoteException {
        String category = message.getCategory();

        switch (category) {
            case "ping" -> {
                System.out.println("Received ping from " + client.getUsername());
                controller.pong(client.getUsername());
                controller.addPongLost(client.getUsername());
                client.sendMessageToClient(new Message("pong"));
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
                if ("ok".equals(controller.checkPick(message.getPick()))) {
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
        String finalUsername = client.getUsername();

        Thread checkThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(20000);
                    if (!controller.pongReceived.contains(finalUsername) && !controller.disconnectedPlayers.contains(finalUsername)) {
                        System.err.println("Ping not received from " + finalUsername + ". Disconnecting.");
                        Thread.sleep(10000);
                        if (!controller.pongReceived.contains(finalUsername)) {
                            disconnect(finalUsername);
                            break;
                        }
                    } else {
                        controller.pongReceived.remove(finalUsername);
                    }
                } catch (InterruptedException | RemoteException ignored) {
                    System.out.println("Ping thread interrupted.");
                }
            }
        });
        controller.addCheckThread(checkThread);
        checkThread.start();
    }

    /**
     * Sends the game back to the client that has reconnected.
     *
     * @param client the client that has reconnected
     * @throws RemoteException if the connection fails
     */
    public void resendGameToReconnectedClient(SocketClientHandler client) throws RemoteException {
        int position = controller.getPositionByUsername(getUsername());
        System.out.println("Sending game to " + getUsername() + ", who just reconnected.");
        sendMessageToClient(new Message("username", username));

        Message myGame = new Message(controller.getPersonalGoalCard(position), controller.getCommonGoals(), controller.getBookshelves(), controller.getBoard(), controller.getTopOfScoring(), controller.getFirstPlayer(), controller.getAllCurrentPoints());
        client.sendMessageToClient(myGame);

        sendAllExcept(client.getUsername(), new Message("reconnected", client.getUsername(), controller.gameModel.getCurrentPlayer().getNickname()));

        startPingThread(client);
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

    public void checkUsername(SocketClientHandler client, String username, boolean firstGame, int checkStatus) throws RemoteException {
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
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                checkStatus = controller.checkUsername(username);
                if (checkStatus == 0) {
                    System.out.println(username + " requested login, but the username is already taken.");
                    client.sendMessageToClient(new Message("UsernameRetry"));
                } else {
                    System.out.println(username + " reconnected.");
                    setUsername(username);
                    controller.addClient(username, client);
                    resendGameToReconnectedClient(client);
                }
            }
            case -1 -> {
                // The username is already taken, but the player was disconnected and is trying to reconnect
                System.out.println(username + " reconnected.");
                client.sendMessageToClient(new Message("username", username));
                setUsername(username);
                controller.addClient(username, client);
                if (!controller.isGameLoaded) {
                    resendGameToReconnectedClient(client);
                } else {
                    resendToReconnectAfterServerDown(client);
                }
            }
        }
    }

    /**
     * Sends the game to the specified client after the server has gone back online.
     *
     * @param client the client to send the game to
     * @throws RemoteException if the connection fails
     */
    public void resendToReconnectAfterServerDown(SocketClientHandler client) throws RemoteException {
        int position = controller.getPositionByUsername(getUsername());

        sendMessageToClient(new Message("username", username));
        startPingThread(client);
        Message myGame = new Message(controller.getPersonalGoalCard(position), controller.getCommonGoals(), controller.getBookshelves(), controller.getBoard(), controller.getTopOfScoring(), controller.getFirstPlayer(), controller.getAllCurrentPoints());
        client.sendMessageToClient(myGame);
        if (controller.getRmiClients().size() + controller.getTcpClients().size() != controller.numberOfPlayers) {
            sendMessageToClient(new Message("waitingRoomForReconnect"));
        } else {
            controller.setIsLoaded(false);
            sendAll(new Message("AllIn"));
            nextTurn();
        }
    }
}
