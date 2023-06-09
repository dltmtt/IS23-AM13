package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientCommunicationInterface;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.utils.FullRoomException;

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
    String HOSTNAME = "localhost"; // Shared by RMI and socket
    ServerController controller = new ServerController();

    // This method's implementation is basically identical to receiveMessage.
    // I still have to figure out how to unify them: I could create a sendMessageToClient method
    // in RMI too, but I still (think) I need different signatures for controller.addClient()
    void receiveMessageTcp(Message message, SocketClientHandler client) throws IllegalAccessException, RemoteException, FullRoomException;

    default void receiveMessage(Message message, ClientCommunicationInterface client) throws Exception, FullRoomException {
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
                int numberOfPlayers = message.getNumPlayer();
                System.out.println("Number of players: " + numberOfPlayers);
                String isOk = controller.checkNumPlayer(numberOfPlayers);
                if (!isOk.equals("ok")) {
                    client.callBackSendMessage(new Message("numOfPlayersNotOK"));
                } else {
                    controller.setNumberOfPlayers(numberOfPlayers);
                    if (controller.checkRoom() == 1) {
                        startGame();
                    } else if (controller.checkRoom() == -1) {
                        removePlayers();
                        startGame();
                    } else {
                        client.callBackSendMessage(new Message("waitingRoom"));
                    }
                }
            }
            case "pick" -> {
                if ("ok".equals(controller.pick(message.getPick()))) {
                    client.callBackSendMessage(new Message(controller.getPicked(message.getPick())));
                } else {
                    client.callBackSendMessage(new Message("pickRetry"));
                }
            }
            case "insertMessage" -> {
                if (controller.checkInsert(message.getInsert()) == 1) {
                    sendUpdate();
                    nextTurn();
                } else if (controller.checkInsert(message.getInsert()) == 0) {
                    client.callBackSendMessage(new Message("insertRetry", "notValidNumber"));
                } else if (controller.checkInsert(message.getInsert()) == -1) {
                    client.callBackSendMessage(new Message("insertRetry", "notEnoughFreeCells"));
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
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    System.err.println("Ping thread interrupted");
                    break;
                } catch (RemoteException e) {
                    // We fall in this branch when the client disconnects
                    try {
                        disconnect(finalUsername);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                }
            }
        });
        pingThread.start();

        Thread checkThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3000);
                    if (!controller.pongReceived.contains(finalUsername) && !controller.disconnectedPlayers.contains(finalUsername)) {
                        System.out.println("Pong not received from " + finalUsername + ". Disconnecting.");
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
    // default void disconnect(String username) throws RemoteException {
    //     System.err.println(username + " disconnected.");
    //     controller.disconnect(username);
    //     if (controller.get.size() == controller.numberOfPlayers - 1) {
    //         // If there is only one player left, sendAll actually sends the message to the only player left,
    //         // telling him that he's alone. He will wait for the other players to reconnect, and if none of them
    //         // reconnects, he will win.
    //         sendAll(new Message("youAloneBitch"));
    //     } else {
    //         if (controller.getCurrentPlayer().equals(username)) {
    //             nextTurn();
    //         }
    //     }
    // }

    /**
     * Adds a player to the disconnected players list and changes the turn if the
     * disconnected player was the current player.
     *
     * @param username the username of the disconnected player
     */
    default void disconnect(String username) throws RemoteException {
        System.err.println(username + " disconnected. in disconnected");
        controller.disconnect(username);
        if (controller.disconnectedPlayers.size() == controller.numberOfPlayers - 1) {
            // If there is only one player left, sendAll actually sends the message to the only player left,
            // telling him that he's alone. He will wait for the other players to reconnect, and if none of them
            // reconnects, he will win.
            sendAll(new Message("youAloneBitch"));
        } else {
            if (controller.getCurrentPlayer().equals(username)) {
                nextTurn();
            }
        }
    }

    default void removePlayers() throws RemoteException {
        HashMap<String, ClientCommunicationInterface> rmiClients = controller.getRmiClients();
        HashMap<String, SocketClientHandler> tcpClients = controller.getTcpClients();
        List<String> toRemove = controller.getExtraPlayers();
        for (String username : toRemove) {
            if (rmiClients.containsKey(username)) {
                rmiClients.get(username).callBackSendMessage(new Message("removePlayer"));
                rmiClients.remove(username);
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
            Message game = new Message(controller.getPersonalGoalCard(position), controller.getCommonGoals(), controller.getBookshelves(), controller.getBoard());
            client.callBackSendMessage(game);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        controller.addClient(client.getUsername(), client);

        // This is one of the few differences from the Socket implementation
        startPingThread(client);

        sendAllExcept(client.getUsername(), new Message("reconnected", client.getUsername()));
        System.out.println("Sent reconnected message to all clients except " + client.getUsername());
        sendTurn(client);
    }

    default void sendTurn(ClientCommunicationInterface client) throws RemoteException {
        String currentPlayer = controller.getCurrentPlayer();
        if (currentPlayer.equals(client.getUsername())) {
            client.callBackSendMessage(new Message("turn"));
        } else {
            client.callBackSendMessage(new Message("otherTurn", currentPlayer));
        }
    }

    default void nextTurn() throws RemoteException {
        controller.changeTurn();
        controller.saveGame();
        if (controller.checkGameStatus() == -1) {
            // the game has ended
            List<String> winnersNickname = controller.getWinnersNickname();
            sendAll(new Message(winnersNickname.size(), winnersNickname, controller.getWinnersScore()));
        } else if (controller.checkGameStatus() == 0) {
            // it's the last round
            sendAll(new Message("lastRound"));
            turn();
        } else {
            // the game is still going
            turn();
        }
    }

    default void startGame() throws RemoteException {
        HashMap<String, ClientCommunicationInterface> rmiClients = controller.getRmiClients();
        HashMap<String, SocketClientHandler> tcpClients = controller.getTcpClients();

        for (String username : tcpClients.keySet()) {
            int position = controller.getPositionByUsername(username);
            Message myGame = new Message(controller.getPersonalGoalCard(position), controller.getCommonGoals(), controller.getBookshelves(), controller.getBoard());
            tcpClients.get(username).sendMessageToClient(myGame);
        }
        for (String username : rmiClients.keySet()) {
            int position = controller.getPositionByUsername(username);
            Message myGame = new Message(controller.getPersonalGoalCard(position), controller.getCommonGoals(), controller.getBookshelves(), controller.getBoard());
            rmiClients.get(username).callBackSendMessage(myGame);
        }

        controller.saveGame();
        turn();
    }

    default void turn() throws RemoteException {
        String currentPlayer = controller.getCurrentPlayer();
        Message otherTurn = new Message("otherTurn", currentPlayer);
        Message turn = new Message("turn");
        System.out.println("sending turn to " + currentPlayer);
        sendAllExcept(currentPlayer, otherTurn);
        if (controller.getRmiClients().containsKey(currentPlayer)) {
            controller.getRmiClients().get(currentPlayer).callBackSendMessage(turn);
        }
        if (controller.getTcpClients().containsKey(currentPlayer))
            controller.getTcpClients().get(currentPlayer).sendMessageToClient(turn);
    }

    default void sendUpdate() throws RemoteException {
        HashMap<String, ClientCommunicationInterface> rmiClients = controller.getRmiClients();
        HashMap<String, SocketClientHandler> tcpClients = controller.getTcpClients();
        for (String username : tcpClients.keySet()) {
            int position = controller.getPositionByUsername(username);
            Message myGame = new Message("update", controller.getBookshelves(), controller.getBoard(), controller.getScore(position));
            tcpClients.get(username).sendMessageToClient(myGame);
        }
        for (String username : rmiClients.keySet()) {
            int position = controller.getPositionByUsername(username);
            Message myGame = new Message("update", controller.getBookshelves(), controller.getBoard(), controller.getScore(position));
            rmiClients.get(username).callBackSendMessage(myGame);
        }
    }

    default void sendAll(Message message) throws RemoteException {
        HashMap<String, ClientCommunicationInterface> rmiClients = controller.getRmiClients();
        HashMap<String, SocketClientHandler> tcpClients = controller.getTcpClients();
        for (String username : tcpClients.keySet()) {
            if (!controller.disconnectedPlayers.contains(username)) {
                tcpClients.get(username).sendMessageToClient(message);
            }
        }
        for (String username : rmiClients.keySet()) {
            if (!controller.disconnectedPlayers.contains(username)) {
                rmiClients.get(username).callBackSendMessage(message);
            }
        }
    }

    default void sendAllExcept(String player, Message message) throws RemoteException {
        HashMap<String, ClientCommunicationInterface> rmiClients = controller.getRmiClients();
        HashMap<String, SocketClientHandler> tcpClients = controller.getTcpClients();
        for (String username : tcpClients.keySet()) {
            if (!username.equals(player)) {
                tcpClients.get(username).sendMessageToClient(message);
            }
        }
        for (String username : rmiClients.keySet()) {
            if (!username.equals(player)) {
                rmiClients.get(username).callBackSendMessage(message);
            }
        }
    }

    default void checkUsername(ClientCommunicationInterface client, String username, boolean firstGame, int checkStatus) throws RemoteException, FullRoomException {
        switch (checkStatus) {
            case 1 -> {
                if (controller.isGameStarted()) {
                    client.callBackSendMessage(new Message("gameAlreadyStarted"));
                } else {
                    client.callBackSendMessage(new Message("username", username));

                    controller.addPlayer(username, 0, firstGame);
                    System.out.println(username + " logged in.");

                    client.setUsername(username);
                    controller.addClient(username, client);

                    // This is one of the few differences from the Socket implementation
                    startPingThread(client);

                    controller.startRoom();

                    if (controller.isFirst()) {
                        // Let the first player choose the number of players
                        client.callBackSendMessage(new Message("chooseNumOfPlayer"));
                    } else {
                        client.callBackSendMessage(new Message("waitingRoom"));
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
                    client.callBackSendMessage(new Message("UsernameRetry"));
                } else {
                    System.out.println(username + " reconnected.");
                    client.setUsername(username);
                    resendGameToReconnectedClient(client);
                }
            }
            case -1 -> {
                // The username is already taken, but the player was disconnected and is trying to reconnect
                System.out.println(username + " reconnected.");
                client.setUsername(username);
                resendGameToReconnectedClient(client);
            }
        }
    }
}
