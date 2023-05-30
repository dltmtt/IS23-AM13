package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientCommunicationInterface;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.utils.FullRoomException;
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
                        }

                        Message message = new Message(messageFromClient);

                        try {
                            receiveMessageTcp(message, this);
                        } catch (IllegalAccessException | FullRoomException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (IOException e) {
                    // We are here because the client disconnected
                    System.err.println(username+ " disconnected.");
                    controller.addDisconnection(username);
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
            case "pong"->{
                controller.pongReceived(username);
            }
            case "ping" -> {
                sendPong(client);
            }
            case "numOfPlayersMessage" -> {
                int numPlayer = message.getNumPlayer();
                String isOk = controller.checkNumPlayer(numPlayer);
                if (!isOk.equals("ok")) {
                    sendMessageToClient(new Message("numOfPlayersNotOK"));
                } else {
                    controller.setNumPlayer(numPlayer);
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
                setUsername(username);
                int checkStatus = controller.checkUsername(username);
                if (checkStatus == 1) {
                    // The username is available, a new player can be added
                    if (controller.isGameStarted()) {
                        sendMessageToClient(new Message("gameAlreadyStarted"));
                    } else {
                        sendMessageToClient(new Message("username", username));
                        controller.addPlayer(message.getUsername(), 0, message.getFirstGame());
                        System.out.println(message.getUsername() + " logged in.");
                        controller.addClient(message.getUsername(), client);
                        startThread(client);
                        controller.startRoom();
                        if (controller.isFirst()) {
                            sendMessageToClient(new Message("chooseNumOfPlayer"));
                        } else {
                            sendMessageToClient(new Message("waitingRoom"));
                            if (controller.checkRoom() == 1) {
                                System.out.println(controller.checkRoom());

                                startGame();

                                System.out.println("Game started.");
                            } else if (controller.checkRoom() == -1) {
                                System.out.println(controller.checkRoom());
                                removePlayers();
                            }
                        }
                    }
                } else if (checkStatus == 0) {
                    // The username has already been taken, retry
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    checkStatus = controller.checkUsername(username);
                    if (checkStatus == 0) {
                        System.out.println(username + " requested login, but the username is already taken.");
                        sendMessageToClient(new Message("usernameRetry"));
                    } else {
                        System.out.println(username + " reconnected.");
                        sendGame(client);
                    }
                } else {
                    // The username is already taken, but the player was disconnected and is trying to reconnect
                    System.out.println(username + " reconnected.");
                    sendGame(client);
                }
            }
            default -> System.out.println(message + " requested unknown");
        }
    }
    public void startThread(SocketClientHandler client){
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    client.sendMessageToClient(new Message("ping"));
                    Thread.sleep(3000);
                    // if(!controller.isAlive(username)){
                    //     System.out.println(client.getUsername() + " disconnected.");
                    //     controller.addDisconnection(client.getUsername());
                    //     break;
                    // }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void sendGame(SocketClientHandler client) throws RemoteException {
        int position = controller.getPositionByUsername(client.getUsername());
        Message myGame = new Message(controller.getPersonalGoalCard(position), controller.getCommonGoals(), controller.getBookshelves(), controller.getBoard());
        client.sendMessageToClient(myGame);
        startThread(client);
    }

    public void sendMessageToClient(Message message) {
        clientPrintStream.println(message.getJSONstring());
    }
}
