package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Client;
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
            case "ping" -> controller.pingReceived(message.getUsername());
            case "numOfPlayersMessage" -> {
                System.out.println("Received numOfPlayers");
                System.out.println("Number of players: " + message.getNumPlayer());
                int numPlayer = message.getNumPlayer();
                String isOk = controller.checkNumPlayer(numPlayer);
                if (!isOk.equals("ok")) {
                    client.callBackSendMessage(new Message("numOfPlayersNotOK"));
                } else {
                    controller.setNumPlayer(numPlayer);
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
                    client.callBackSendMessage(new Message("PickRetry"));
                }
            }
            case "insertMessage" -> {
                if (controller.checkInsert(message.getInsert())) {
                    sendUpdate();
                    nextTurn();
                }
                // TODO: return an error message if the insert is not valid, otherwise the game will freeze
            }
            case "sort" -> controller.rearrangePicked(message.getSort());
            case "completeLogin" -> {

                String username = message.getUsername();
                int checkStatus = controller.checkUsername(username);
                if (checkStatus == 1) {
                    // The username is available, a new player can be added
                    if (controller.isGameStarted()) {
                        client.callBackSendMessage(new Message("gameAlreadyStarted"));
                    } else {
                        client.callBackSendMessage(new Message("username", username));
                        controller.addPlayer(message.getUsername(), 0, message.getFirstGame());
                        System.out.println(message.getUsername() + " logged in.");
                        controller.addClient(message.getUsername(), client);
                        controller.startRoom();
                        if (controller.isFirst()) {
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
                        client.callBackSendMessage(new Message("UsernameRetry"));
                    } else {
                        System.out.println(username + " reconnected.");
                    }
                } else {
                    // The username is already taken, but the player was disconnected and is trying to reconnect
                    System.out.println(username + " reconnected.");
                    client.callBackSendMessage((new Message("update", controller.getBookshelves(), controller.getBoard(), controller.getCurrentPlayerScore())));
                }
            }
            default -> System.out.println(message + " requested unknown");
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
        System.out.println(tcpClients.keySet());
        System.out.println(rmiClients.keySet());
        for (String username : tcpClients.keySet()) {
            int position = controller.getPositionByUsername(username);
            System.out.println("Sending game to " + username + " at position " + position);
            Message myGame = new Message(controller.getPersonalGoalCard(position), controller.getCommonGoals(), controller.getBookshelves(), controller.getBoard());
            tcpClients.get(username).sendMessageToClient(myGame);
        }
        for (String username : rmiClients.keySet()) {
            int position = controller.getPositionByUsername(username);
            System.out.println("Sending game to " + username + " at position " + position);
            Message myGame = new Message(controller.getPersonalGoalCard(position), controller.getCommonGoals(), controller.getBookshelves(), controller.getBoard());
            rmiClients.get(username).callBackSendMessage(myGame);
        }
        turn();
    }

    default void turn() throws RemoteException {
        String currentPlayer = controller.getCurrentPlayer();
        Message otherTurn = new Message("otherTurn", currentPlayer);
        Message turn = new Message("turn");
        sendAllExceptCurrentPlayer(otherTurn);
        if (controller.getRmiClients().containsKey(currentPlayer))
            controller.getRmiClients().get(currentPlayer).callBackSendMessage(turn);
        if (controller.getTcpClients().containsKey(currentPlayer))
            controller.getTcpClients().get(currentPlayer).sendMessageToClient(turn);
    }

    // default Message sendGame(int position) throws RemoteException {
    //     System.out.println("Sending game to " + position);
    //     return new Message(controller.getPersonalGoalCard(position), controller.getCommonGoals(), controller.getBookshelf(position), controller.getBoard());
    // }

    default void sendUpdate() throws RemoteException {
        HashMap<String, ClientCommunicationInterface> rmiClients = controller.getRmiClients();
        HashMap<String, SocketClientHandler> tcpClients = controller.getTcpClients();
        System.out.println(tcpClients.keySet());
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

    default Message sendPong() throws RemoteException {
        return new Message("pong");
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

    default Message setNumPlayers(Client c) throws RemoteException {
        int numOfPlayer = c.numOfPlayers().getNumPlayer();
        return new Message(controller.checkNumPlayer(numOfPlayer));
    }
}
