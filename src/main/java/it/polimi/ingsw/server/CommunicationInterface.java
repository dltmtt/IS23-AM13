package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.utils.FullRoomException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Here go the methods that the client can call on the server.
 */
public interface CommunicationInterface extends Remote {

    int PORT_RMI = 1099;
    int PORT_SOCKET = 888;
    String HOSTNAME = "localhost"; // Shared by RMI and socket
    ServerController controller = new ServerController();

    default Message sendMessage(Message message) throws RemoteException, FullRoomException, IllegalAccessException {
        String category = message.getCategory();

        switch (category) {
            case "ping" -> {
                controller.pingReceived(message.getUsername());
                return sendPong();
            }
            case "username" -> {
                String username = message.getUsername();
                int checkStatus = controller.checkUsername(username);
                if (checkStatus == 1) {
                    // The username is available, a new player can be added
                    return new Message("");
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
                        return new Message("retry");
                    } else {
                        System.out.println(username + " reconnected.");
                        return new Message(controller.getPositionByUsername(username));
                    }
                } else {
                    // The username is already taken, but the player was disconnected and is trying to reconnect
                    System.out.println(username + " reconnected.");
                    return new Message(controller.getPositionByUsername(username));
                }
            }
            case "age" -> {
                int age = message.getAge();
                return new Message(age >= 8 ? "ok" : "no");
            }
            case "numPlayer" -> {
                int numPlayer = message.getNumPlayer();
                return new Message(controller.checkNumPlayer(numPlayer));
            }
            case "ready" -> {
                return new Message(controller.checkRoom());
            }
            case "index" -> {
                int position = message.getPosition();
                return sendGame(position);
            }
            case "turn" -> {
                int position = message.getPosition();
                return sendTurn(position);
            }
            case "pick" -> {
                if ("ok".equals(controller.pick(message.getPick()))) {
                    return new Message(controller.getPicked(message.getPick()));
                } else {
                    return new Message("retry");
                }
            }
            case "insert" -> {
                if (controller.checkInsert(message.getInsert())) {
                    return sendUpdate(message);
                } else {
                    return new Message("retry");
                }
            }
            case "sort" -> {
                controller.rearrangePicked(message.getSort());
                return new Message("ok");
            }
            case "board" -> {
                return new Message("board", controller.getBoard());
            }
            case "endGame" -> {
                return new Message(controller.getWinnersNickname().size(), controller.getWinnersNickname());
            }
            case "completeLogin" -> {
                controller.addPlayer(message.getUsername(), message.getAge(), message.getFirstGame());
                System.out.println(message.getUsername() + " logged in.");
                return new Message(controller.startRoom()); // If the current client is the first one to join, we need to show the chooseNumOfPlayerScreen()
            }
            default -> {
                System.out.println(message + " requested unknown");
                return new Message("Unknown request.");
            }
        }
    }

    default Message sendGame(int position) throws RemoteException {
        System.out.println("Sending game to " + position);
        return new Message(controller.getPersonalGoalCard(position), controller.getCommonGoals(), controller.getBookshelf(position), controller.getBoard());
    }

    default void setNewStatus() throws RemoteException {
        // gameStatus = parser.sendStartGame(controller.getCurrentPlayerPersonalGoal(), controller.getCommonGoals(), controller.getCurrentPlayerBookshelf(), controller.getBoard());
    }

    default Message sendUpdate(Message message) throws RemoteException {
        setNewStatus();
        return new Message("update", controller.getCurrentPlayerBookshelf(), controller.getBoard(), controller.getCurrentPlayerScore());
    }

    default Message sendTurn(int position) throws RemoteException {
        return new Message("turn", "turn", controller.yourTurn(position));
    }

    default Message sendPong() throws RemoteException {
        return new Message("pong");
    }

    default void sendClient(Client c) {
        controller.addClient(c.getUsername(), c);
    }
}
