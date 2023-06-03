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
            case "pong" -> {}
            case "numOfPlayersMessage" -> {
                int numberOfPlayers = message.getNumPlayer();
                System.out.println("Number of players: " + numberOfPlayers);
                String isOk = controller.checkNumPlayer(numberOfPlayers);
                if (!isOk.equals("ok")) {
                    client.callBackSendMessage(new Message("numOfPlayersNotOK"));
                } else {
                    controller.setNumPlayer(numberOfPlayers);
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
        new Thread(() -> {
            String username = null;
            try {
                username = client.getUsername();
            } catch (RemoteException e) {
                System.err.println("Error while getting username from client.");
            }

            while (true) {
                try {
                    client.callBackSendMessage(new Message("ping"));
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    System.err.println("Ping thread interrupted");
                    break;
                } catch (RemoteException e) {
                    // We fall in this branch when the client disconnects
                    System.err.println(username + " disconnected.");
                    controller.disconnect(username);
                    break;
                }
            }
        }).start();
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

        // sendAll(new Message("reconnected", client.getUsername()));
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
        sendAllExceptCurrentPlayer(otherTurn);
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

    default Message sendTurn(int position) throws RemoteException {
        return new Message("turn", "turn", controller.yourTurn(position));
    }

    default void sendPong(ClientCommunicationInterface client) throws RemoteException {
        client.callBackSendMessage(new Message("pong"));
    }

    default void sendPong(SocketClientHandler client) throws RemoteException {
        client.sendMessageToClient(new Message("pong"));
    }

    default void sendAll(Message message) throws RemoteException {
        HashMap<String, ClientCommunicationInterface> rmiClients = controller.getRmiClients();
        HashMap<String, SocketClientHandler> tcpClients = controller.getTcpClients();
        for (String username : tcpClients.keySet()) {
            tcpClients.get(username).sendMessageToClient(message);
        }
        for (String username : rmiClients.keySet()) {
            rmiClients.get(username).callBackSendMessage(message);
        }
    }

    default void sendAllExceptCurrentPlayer(Message message) throws RemoteException {
        HashMap<String, ClientCommunicationInterface> rmiClients = controller.getRmiClients();
        HashMap<String, SocketClientHandler> tcpClients = controller.getTcpClients();
        for (String username : tcpClients.keySet()) {
            if (!username.equals(controller.getCurrentPlayer())) {
                tcpClients.get(username).sendMessageToClient(message);
            }
        }
        for (String username : rmiClients.keySet()) {
            if (!username.equals(controller.getCurrentPlayer())) {
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
