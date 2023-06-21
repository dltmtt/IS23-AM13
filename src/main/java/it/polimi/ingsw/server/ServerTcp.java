package it.polimi.ingsw.server;

import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.utils.FullRoomException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ServerTcp implements ServerInterface, ServerCommunicationInterface {

    private final ServerSocket serverSocket;

    /**
     * The list of the connected clients. The username associated with
     * a client can be retrieved by calling {@link SocketClientHandler#getUsername()}.
     */
    public List<SocketClientHandler> connectedClients;

    /**
     * The thread that accepts new connections.
     */
    public Thread acceptConnectionsThread;

    public ServerTcp() throws IOException {
        connectedClients = new ArrayList<>();

        serverSocket = new ServerSocket(PORT_SOCKET); // Throws IOException

        acceptConnectionsThread = new Thread(() -> {
            while (true) {
                try {
                    // Accept incoming connections
                    Socket clientSocket = serverSocket.accept();

                    // Handle the new connection in a new thread
                    new Thread(() -> handleClient(clientSocket)).start();
                } catch (IOException e) {
                    System.err.println("Server is closed, unable to accept a connection.");
                    System.exit(0);
                }
            }
        });
    }

    private void handleClient(Socket clientSocket) {
        try {
            // Get the input stream from the client socket (to read data from the client)
            InputStream inputStream = clientSocket.getInputStream();

            // Create a new client handler and add it to the list of connected clients
            SocketClientHandler clientHandler = new SocketClientHandler(clientSocket);
            connectedClients.add(clientHandler);

            // Listen for messages from the client
            while (true) {
                // Read the message sent by the client
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String messageString = reader.readLine();

                // Parse the string into a JSON and create a Message object
                Message message = Message.fromString(messageString);

                // Handle the message received from the client (e.g., add the player to the game)
                handleMessage(message, clientHandler);
            } // This probably need not be in a separate thread because it's the only thing this thread does
        } catch (IOException e) { // This exception is thrown when the client disconnects
            e.printStackTrace();
        } catch (FullRoomException e) {
            // This exception shouldn't end up here
        } catch (NullPointerException e) {
            // The string received from the client is null, which means the client disconnected
        }
    }

    @Override
    public void start() {
        acceptConnectionsThread.start();
    }

    @Override
    public void stop() {
        sendToAll("Server is shutting down...");
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to stop the socket server.");
        }
        closeAllConnections();
        System.out.println("Socket server stopped.");
        acceptConnectionsThread.interrupt();
    }

    /**
     * Closes all the connections with the clients.
     */
    public void closeAllConnections() {
        for (SocketClientHandler clientHandler : connectedClients) {
            clientHandler.close();
        }
    }

    // TODO: check if this method is needed
    public void sendToAll(String message) {
        for (SocketClientHandler clientHandler : connectedClients) {
            clientHandler.sendString(message);
        }
    }

    // TODO: check if this method is needed
    public void sendToAllExcept(String message, SocketClientHandler excludedClientHandler) throws IllegalArgumentException {
        if (excludedClientHandler == null || message == null)
            throw new IllegalArgumentException("null parameter handler cannot be null.");
        connectedClients.stream().filter(client -> client != excludedClientHandler).forEach(client -> client.sendString(message));
    }

    // TODO: check if this method is needed
    public int getConnectedPlayers() {
        return connectedClients.size();
    }

    @Override
    public void handleMessage(Message message, SocketClientHandler clientHandler) throws RemoteException, FullRoomException {
        String category = message.getCategory();

        switch (category) {
            // Maybe the controller should do something with the pong.
            case "ping" -> {
                System.out.println("Received pong from " + clientHandler.getUsername());
                controller.pong(clientHandler.getUsername());
                controller.addPongLost(clientHandler.getUsername());
                if (controller.disconnectedPlayers.contains(clientHandler.getUsername())) {
                    System.out.println("Player " + clientHandler.getUsername() + " reconnected");
                    startPingThread(clientHandler);
                    sendAll(new Message("reconnected", clientHandler.getUsername()));
                    controller.disconnectedPlayers.remove(clientHandler.getUsername());
                }
                clientHandler.sendMessageToClient(new Message("pong"));
            }
            case "numOfPlayersMessage" -> {
                int numberOfPlayers = message.getNumPlayer();
                System.out.println("Number of players: " + numberOfPlayers);
                String isOk = controller.checkNumPlayer(numberOfPlayers);
                if (!isOk.equals("ok")) {
                    clientHandler.sendMessageToClient(new Message("numOfPlayersNotOK"));
                } else {
                    controller.setNumberOfPlayers(numberOfPlayers);
                    if (controller.checkRoom() == 1) {
                        startGame();
                    } else if (controller.checkRoom() == -1) {
                        removePlayers();
                        startGame();
                    } else {
                        clientHandler.sendMessageToClient(new Message("waitingRoom"));
                    }
                }
            }
            case "pick" -> {
                if ("ok".equals(controller.checkPick(message.getPick()))) {
                    clientHandler.sendMessageToClient(new Message(controller.getPicked(message.getPick())));
                } else {
                    clientHandler.sendMessageToClient(new Message("pickRetry"));
                }
            }
            case "insertMessage" -> {
                if (controller.checkInsert(message.getInsert()) == 1) {
                    System.out.println("Inserting " + message.getInsert());
                    sendUpdate();
                    nextTurn();
                } else if (controller.checkInsert(message.getInsert()) == 0) {
                    clientHandler.sendMessageToClient(new Message("insertRetry", "notValidNumber"));
                } else if (controller.checkInsert(message.getInsert()) == -1) {
                    clientHandler.sendMessageToClient(new Message("insertRetry", "notEnoughFreeCells"));
                }
                // TODO: return an error message if the insert is not valid, otherwise the game will freeze
            }
            case "sort" -> controller.rearrangePicked(message.getSort());
            case "completeLogin" -> {
                String username = message.getUsername();
                int checkStatus = controller.checkUsername(username);
                checkUsername(clientHandler, message.getUsername(), message.getFirstGame(), checkStatus);
            }
            default -> System.out.println(message + " requested unknown");
        }
    }

    public void startPingThread(SocketClientHandler clientHandler) throws RemoteException {
        String finalUsername = clientHandler.getUsername();
        Thread pingThread = new Thread(() -> {
            while (true) {
                try {
                    clientHandler.sendMessageToClient(new Message("ping"));
                    Thread.sleep(1000);
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
                        if (!controller.pongReceived.contains(finalUsername)) {
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

    public void resendGameToReconnectedClient(SocketClientHandler clientHandler) throws RemoteException {
        int position = controller.getPositionByUsername(clientHandler.getUsername());
        System.out.println("Sending game to " + clientHandler.getUsername() + ", who just reconnected.");

        Message myGame = new Message("update", controller.getBookshelves(), controller.getBoard(), controller.allPoints(position), controller.getTopOfScoring());
        clientHandler.sendMessageToClient(myGame);

        controller.addClient(clientHandler.getUsername(), clientHandler);

        startPingThread(clientHandler);

        sendAllExcept(clientHandler.getUsername(), new Message("reconnected", clientHandler.getUsername()));
        System.out.println("Sent reconnected message to all clients except " + clientHandler.getUsername());
        sendTurn(clientHandler);
    }

    public void sendTurn(SocketClientHandler clientHandler) {
        String currentPlayer = controller.getCurrentPlayer();
        if (currentPlayer.equals(clientHandler.getUsername())) {
            clientHandler.sendMessageToClient(new Message("turn"));
        } else {
            clientHandler.sendMessageToClient(new Message("otherTurn", currentPlayer));
        }
    }

    public void checkUsername(SocketClientHandler clientHandler, String username, boolean firstGame, int checkStatus) throws RemoteException, FullRoomException {
        switch (checkStatus) {
            case 1 -> {
                if (controller.isGameStarted()) {
                    clientHandler.sendMessageToClient(new Message("gameAlreadyStarted"));
                } else {
                    clientHandler.sendMessageToClient(new Message("username", username));

                    controller.addPlayer(username, 0, firstGame);
                    System.out.println(username + " logged in.");

                    clientHandler.setUsername(username);
                    controller.addClient(username, clientHandler);

                    startPingThread(clientHandler);

                    controller.startRoom();

                    if (controller.isFirst()) {
                        // Let the first player choose the number of players
                        clientHandler.sendMessageToClient(new Message("chooseNumOfPlayer"));
                    } else {
                        clientHandler.sendMessageToClient(new Message("waitingRoom"));
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
                    clientHandler.sendMessageToClient(new Message("UsernameRetry"));
                } else {
                    System.out.println(username + " reconnected.");
                    clientHandler.setUsername(username);
                    resendGameToReconnectedClient(clientHandler);
                }
            }
            case -1 -> {
                // The username is already taken, but the player was disconnected and is trying to reconnect
                System.out.println(username + " reconnected.");

                // TODO: the following line is different from RMI, why?
                clientHandler.sendMessageToClient(new Message("username", username));
                clientHandler.setUsername(username);
                resendGameToReconnectedClient(clientHandler);
            }
        }
    }
}
