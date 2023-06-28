package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientCommunicationInterface;
import it.polimi.ingsw.commons.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

/**
 * Here go the methods that the client can call on the server.
 */
public interface ServerCommunicationInterface extends Remote {

    int PORT_RMI = 1099;
    int PORT_SOCKET = 888;
    ServerController controller = new ServerController();

    // This method's implementation is basically identical to receiveMessage.
    // I still have to figure out how to unify them: I could create a sendMessageToClient method
    // in RMI too, but I still (think) I need different signatures for controller.addClient()

    /**
     * Receive a message from a socket client and act accordingly.
     *
     * @param message the message received from the client.
     * @param client  the client that sent the message.
     */
    default void receiveMessage(Message message, SocketClientHandler client) throws IllegalAccessException, RemoteException {
    }

    /**
     * Receive a message from an RMI client and act accordingly.
     *
     * @param message the message received from the client.
     * @throws RemoteException if something goes wrong.
     */
    default void receiveMessage(Message message, ClientCommunicationInterface client) throws RemoteException {
        String category = message.getCategory();

        switch (category) {
            case "ping" -> {
                System.out.println("Received ping from " + client.getUsername());
                controller.pong(client.getUsername());
                controller.addPongLost(client.getUsername());
                try {
                    client.callBackSendMessage(new Message("pong"));
                } catch (RemoteException e) {
                    System.err.println("Error while sending pong to client.");

                }
            }
            case "numOfPlayersMessage" -> {
                int numberOfPlayers = message.getNumPlayer();
                System.out.println("Number of players: " + numberOfPlayers);
                String isOk = controller.checkNumPlayer(numberOfPlayers);
                if (!isOk.equals("ok")) {
                    try {
                        client.callBackSendMessage(new Message("numOfPlayersNotOK"));
                    } catch (RemoteException e) {
                        System.err.println("Error while sending numOfPlayersNotOK to client.");
                    }
                } else {
                    controller.setNumberOfPlayers(numberOfPlayers);
                    if (controller.checkRoom() == 1) {
                        startGame();
                    } else if (controller.checkRoom() == -1) {
                        removePlayers();
                        startGame();
                    } else {
                        try {
                            client.callBackSendMessage(new Message("waitingRoom"));
                        } catch (RemoteException e) {
                            System.err.println("Error while sending waitingRoom to client.");
                        }
                    }
                }
            }
            case "pick" -> {
                if ("ok".equals(controller.checkPick(message.getPick()))) {
                    try {
                        client.callBackSendMessage(new Message(controller.getPicked(message.getPick())));
                    } catch (RemoteException e) {
                        System.err.println("Error while sending picked to client.");
                    }
                } else {
                    try {
                        client.callBackSendMessage(new Message("pickRetry"));
                    } catch (RemoteException e) {
                        System.err.println("Error while sending pickRetry to client.");
                    }
                }
            }
            case "insertMessage" -> {
                if (controller.checkInsert(message.getInsert()) == 1) {
                    System.out.println("Inserting " + message.getInsert());
                    sendUpdate();
                    nextTurn();
                } else if (controller.checkInsert(message.getInsert()) == 0) {
                    try {
                        client.callBackSendMessage(new Message("insertRetry", "notValidNumber"));
                    } catch (RemoteException e) {
                        System.err.println("Error while sending insertRetry to client.");
                    }

                } else if (controller.checkInsert(message.getInsert()) == -1) {
                    try {
                        client.callBackSendMessage(new Message("insertRetry", "notEnoughFreeCells"));
                    } catch (RemoteException e) {
                        System.err.println("Error while sending insertRetry to client.");
                    }

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

    /**
     * Starts a Thread that will check periodically (every 20 seconds) if a client is still connected. If a pong is missed, the thread waits ither 10 seconds before checking again. If the client is not connected, it is removed from the rmi/tcp Clients hashMap.
     *
     * @param client the client that sent the message.
     * @throws RemoteException if something goes wrong.
     */

    default void startPingThread(ClientCommunicationInterface client) throws RemoteException {
        String username = null;
        try {
            username = client.getUsername();
        } catch (RemoteException e) {
            System.err.println("Error while getting username from client.");
        }
        String finalUsername = username;
        Thread pingThread = new Thread(() -> {

            while (true) {
                try {
                    client.callBackSendMessage(new Message("ping"));
                    Thread.sleep(10000);
                } catch (InterruptedException | RemoteException e) {
                    System.err.println("Ping thread interrupted");
                    break;
                }
            }
        });

        Thread checkThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(20000);
                    if (!controller.pongReceived.contains(finalUsername) && !controller.disconnectedPlayers.contains(finalUsername)) {
                        System.err.println("Ping not received from " + finalUsername + ". Disconnecting.");
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

    /**
     * Adds a player to the disconnected players list and changes the turn if the
     * disconnected player was the current player.
     *
     * @param username the username of the disconnected player
     */
    default void disconnect(String username) throws RemoteException {
        System.err.println(username + " disconnected.");
        controller.disconnect(username);
        if (controller.disconnectedPlayers.size() == controller.numberOfPlayers - 1) {
            // If there is only one player left, sendAll actually sends the message to the only player left,
            // telling him that they are alone. They will wait for the other players to reconnect, and if none of them
            // reconnects, they will win.
            sendAll(new Message("youAloneBitch"));
        } else {
            if (controller.getCurrentPlayer().equals(username)) {
                nextTurn();
            }
        }
        controller.removeClientByUsername(username);
    }

    /**
     * Removes the players that have logged in after the game has started.
     *
     * @throws RemoteException if the connection fails
     */

    default void removePlayers() throws RemoteException {
        HashMap<String, ClientCommunicationInterface> rmiClients = controller.getRmiClients();
        HashMap<String, SocketClientHandler> tcpClients = controller.getTcpClients();
        List<String> toRemove = controller.getExtraPlayers();
        for (String username : toRemove) {
            if (rmiClients.containsKey(username)) {
                try {
                    rmiClients.get(username).callBackSendMessage(new Message("removePlayer"));
                    rmiClients.remove(username);
                } catch (RemoteException e) {
                    System.err.println("Error while removing player " + username);
                }

            } else {
                tcpClients.get(username).sendMessageToClient(new Message("removePlayer"));
                tcpClients.remove(username);
            }
        }
    }

    /**
     * Sends the game back to the client that has reconnected.
     *
     * @param client the client that has reconnected
     * @throws RemoteException if the connection fails
     */
    default void resendGameToReconnectedClient(ClientCommunicationInterface client) throws RemoteException {
        int position = controller.getPositionByUsername(client.getUsername());
        System.out.println("Sending game to " + client.getUsername() + ", who just reconnected.");

        try {
            client.callBackSendMessage(new Message("username", client.getUsername()));
            Message game = new Message(controller.getPersonalGoalCard(position), controller.getCommonGoals(), controller.getBookshelves(), controller.getBoard(), controller.getTopOfScoring(), controller.getFirstPlayer(), controller.getAllCurrentPoints());
            client.callBackSendMessage(game);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // This is one of the few differences from the Socket implementation
        startPingThread(client);

        sendAllExcept(client.getUsername(), new Message("reconnected", client.getUsername(), controller.gameModel.getCurrentPlayer().getNickname()));
        System.out.println("Sent reconnected message to all clients except " + client.getUsername());
        sendTurn(client);
    }

    /**
     * Tells a client if it's his turn or another player's turn.
     *
     * @param client the client to whom the message is sent
     * @throws RemoteException if the connection fails
     */
    default void sendTurn(ClientCommunicationInterface client) throws RemoteException {
        String currentPlayer = controller.getCurrentPlayer();
        if (currentPlayer.equals(client.getUsername())) {
            try {
                client.callBackSendMessage(new Message("turn"));
            } catch (RemoteException e) {
                System.err.println("Error while sending turn to " + client.getUsername());
            }

        } else {
            try {
                client.callBackSendMessage(new Message("otherTurn", currentPlayer));

            } catch (RemoteException e) {
                System.err.println("Error while sending otherTurn to " + client.getUsername());
            }

        }
    }

    /**
     * Changes the turn, checks if the game has ended or if it's the
     * last round and saves the game.
     *
     * @throws RemoteException if this connection fails
     */
    default void nextTurn() throws RemoteException {
        controller.saveGame();
        controller.changeTurn();

        if (controller.checkGameStatus() == -1) {
            // The game has ended
            controller.setWinner();
            sendAll(new Message(controller.getWinners(), controller.getLosers()));
            controller.resetSavedGame();
        } else if (controller.checkGameStatus() == 0) {
            // It's the last round
            sendAll(new Message("lastRound"));
            turn();
        } else {
            // The game is still going
            turn();
        }
    }

    /**
     * Sends the starter game to all the clients.
     *
     * @throws RemoteException if the connection fails
     */
    default void startGame() throws RemoteException {
        HashMap<String, ClientCommunicationInterface> rmiClients = controller.getRmiClients();
        HashMap<String, SocketClientHandler> tcpClients = controller.getTcpClients();

        for (String username : tcpClients.keySet()) {
            int position = controller.getPositionByUsername(username);
            Message myGame = new Message(controller.getPersonalGoalCard(position), controller.getCommonGoals(), controller.getBookshelves(), controller.getBoard(), controller.getTopOfScoring(), controller.getFirstPlayer(), controller.getInitialPoints());
            tcpClients.get(username).sendMessageToClient(myGame);
        }
        for (String username : rmiClients.keySet()) {
            int position = controller.getPositionByUsername(username);
            Message myGame = new Message(controller.getPersonalGoalCard(position), controller.getCommonGoals(), controller.getBookshelves(), controller.getBoard(), controller.getTopOfScoring(), controller.getFirstPlayer(), controller.getInitialPoints());
            try {
                rmiClients.get(username).callBackSendMessage(myGame);
            } catch (RemoteException e) {
                // e.printStackTrace();
                System.err.println("Error while sending game to " + username);
            }
        }

        controller.saveGame();
        turn();
    }

    /**
     * Sends a turn message to all client, there are two types:
     * <ul>
     *     <li> "turn": sent to the current player</li>
     *     <li> "otherTurn", username: sent to all the other players</li>
     *</ul>
     * @throws RemoteException if the connection fails
     */
    default void turn() throws RemoteException {
        String currentPlayer = controller.getCurrentPlayer();
        Message otherTurn = new Message("otherTurn", currentPlayer);
        Message turn = new Message("turn");
        System.out.println("sending turn to " + currentPlayer);
        sendAllExcept(currentPlayer, otherTurn);
        if (controller.getRmiClients().containsKey(currentPlayer)) {
            try {
                controller.getRmiClients().get(currentPlayer).callBackSendMessage(turn);
            } catch (RemoteException e) {
                System.err.println("Error while sending turn to " + currentPlayer);
            }
        }
        if (controller.getTcpClients().containsKey(currentPlayer))
            controller.getTcpClients().get(currentPlayer).sendMessageToClient(turn);
    }

    /**
     * Sends an update message to all the clients.
     *
     * @throws RemoteException if the connection fails
     */
    default void sendUpdate() throws RemoteException {
        HashMap<String, ClientCommunicationInterface> rmiClients = controller.getRmiClients();
        HashMap<String, SocketClientHandler> tcpClients = controller.getTcpClients();
        for (String username : tcpClients.keySet()) {
            int position = controller.getPositionByUsername(username);
            Message myGame = new Message("update", controller.getBookshelves(), controller.getBoard(), controller.allPoints(position), controller.getTopOfScoring());
            tcpClients.get(username).sendMessageToClient(myGame);
        }
        for (String username : rmiClients.keySet()) {
            int position = controller.getPositionByUsername(username);
            Message myGame = new Message("update", controller.getBookshelves(), controller.getBoard(), controller.allPoints(position), controller.getTopOfScoring());
            try {
                rmiClients.get(username).callBackSendMessage(myGame);
            } catch (RemoteException e) {
                //e.printStackTrace();
                System.err.println("Impossible to send update to " + username + " because they probably disconnected.");
            }
        }
    }

    /**
     * Sends a message to all the clients.
     *
     * @param message the message to send
     * @throws RemoteException if the connection fails
     */
    default void sendAll(Message message) throws RemoteException {

        HashMap<String, ClientCommunicationInterface> rmiClients = controller.getRmiClients();
        HashMap<String, SocketClientHandler> tcpClients = controller.getTcpClients();
        for (String username : tcpClients.keySet()) {
            tcpClients.get(username).sendMessageToClient(message);

        }
        for (String username : rmiClients.keySet()) {
            try {
                rmiClients.get(username).callBackSendMessage(message);
            } catch (RemoteException e) {
                System.err.println("Impossible to send message to " + username + " because they probably disconnected.");
            }


        }
    }

    /**
     * Sends a message to all the clients except the one that identifies
     * the player's name passed as parameter.
     *
     * @param player  the player that won't receive the message
     * @param message the message to send
     * @throws RemoteException if the connection fails
     */
    default void sendAllExcept(String player, Message message) throws RemoteException {
        HashMap<String, ClientCommunicationInterface> rmiClients = controller.getRmiClients();
        HashMap<String, SocketClientHandler> tcpClients = controller.getTcpClients();
        for (String username : tcpClients.keySet()) {
            if (!controller.disconnectedPlayers.contains(username) && !username.equals(player)) {
                tcpClients.get(username).sendMessageToClient(message);
            }
        }
        for (String username : rmiClients.keySet()) {
            if (!controller.disconnectedPlayers.contains(username) && !username.equals(player)) {
                try {
                    rmiClients.get(username).callBackSendMessage(message);
                } catch (RemoteException e) {
                    //e.printStackTrace();
                    System.err.println("Impossible to send message to " + username + " because they probably disconnected.");
                }
            }
        }
    }

    /**
     * Checks the game status and sends the appropriate message to the client. There are 3 different situations:
     * <ul>
     *     <li> checkStatus 1: the username is valid, the player can login unless the game is already started </li>
     *     <li> checkStatus 2: the username is not valid, the player has to change their nickname unless they have disconnected and the server has not noticed yet </li>
     *     <li> checkStatus 3: the username is not valid butthe player can login because they have disconnected and the server already noticed </li>
     *</ul>
     * @param client      the client to send the message to
     * @param username    the username of the client
     * @param firstGame   true if it's the first game of the client
     * @param checkStatus the status of the check
     * @throws RemoteException if the connection fails
     */

    default void checkUsername(ClientCommunicationInterface client, String username, boolean firstGame, int checkStatus) throws RemoteException {
        switch (checkStatus) {
            case 1 -> {
                if (controller.isGameStarted()) {
                    try {
                        client.callBackSendMessage(new Message("gameAlreadyStarted"));
                    } catch (RemoteException e) {
                        System.err.println("Error while sending message to " + username);
                        throw new RemoteException();
                    }

                } else {
                    try {
                        client.callBackSendMessage(new Message("username", username));
                    } catch (RemoteException e) {
                        System.err.println("Error while sending message to " + username);
                        throw new RemoteException();
                    }


                    controller.addPlayer(username, 0, firstGame);
                    System.out.println(username + " logged in.");

                    client.setUsername(username);
                    controller.addClient(username, client);

                    // This is one of the few differences from the Socket implementation
                    startPingThread(client);

                    controller.startRoom();

                    if (controller.isFirst()) {
                        // Let the first player choose the number of players
                        try {
                            client.callBackSendMessage(new Message("chooseNumOfPlayer"));
                        } catch (RemoteException e) {
                            System.err.println("Error while sending message to " + username);
                            throw new RemoteException();
                        }

                    } else {
                        try {
                            client.callBackSendMessage(new Message("waitingRoom"));
                        } catch (RemoteException e) {
                            System.err.println("Error while sending message to " + username);
                            throw new RemoteException();
                        }

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
                // The username is already taken
                try {
                    client.callBackSendMessage(new Message("checkingDisconnection"));
                } catch (RemoteException e) {
                    System.err.println("Error while sending message to " + username);
                    throw new RemoteException();
                }
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    System.err.println("Error while sleeping.");

                }
                if (controller.checkUsername(username) == -1) {
                    System.out.println(username + " reconnected.");
                    client.setUsername(username);
                    controller.addClient(username, client);
                    resendGameToReconnectedClient(client);
                } else {
                    System.out.println(username + " requested login, but the username is already taken.");
                    try {
                        client.callBackSendMessage(new Message("UsernameRetry"));
                        throw new RemoteException();
                    } catch (RemoteException e) {
                        System.err.println("Error while sending message to " + username);
                    }

                }
            }
            case -1 -> {
                // The username is already taken, but the player was disconnected and is trying to reconnect
                System.out.println(username + " reconnected.");
                client.setUsername(username);
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
    default void resendToReconnectAfterServerDown(ClientCommunicationInterface client) throws RemoteException {
        int position = controller.getPositionByUsername(client.getUsername());
        // System.out.println("Sending game to " + client.getUsername() + ", who just reconnected.");
        startPingThread(client);
        try {
            client.callBackSendMessage(new Message("username", client.getUsername()));
            Message game = new Message(controller.getPersonalGoalCard(position), controller.getCommonGoals(), controller.getBookshelves(), controller.getBoard(), controller.getTopOfScoring(), controller.getFirstPlayer(), controller.getAllCurrentPoints());
            client.callBackSendMessage(game);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (controller.getRmiClients().size() + controller.getTcpClients().size() != controller.numberOfPlayers) {
            try {
                client.callBackSendMessage(new Message("waitingRoomForReconnect"));
            } catch (RemoteException e) {
                System.err.println("Error while sending message to " + client.getUsername());
            }

        } else {
            controller.setIsLoaded(false);
            sendAll(new Message("AllIn"));
            nextTurn();
        }

    }
}
