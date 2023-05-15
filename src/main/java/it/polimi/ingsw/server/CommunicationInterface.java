package it.polimi.ingsw.server;

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
    ServerParser parser = new ServerParser();
    ServerController controller = new ServerController();

    default Message sendMessage(Message message) throws RemoteException, FullRoomException, IllegalAccessException {
        String category = parser.getMessageCategory(message);
        switch (category) {
            case "ping" -> {
                controller.pingReceived(message.getUsername());
                return null;
            }
            case "username" -> {
                String username = parser.getUsername(message);
                int checkStatus = controller.checkUsername(username);
                if (checkStatus == 1) {
                    // The username is available, a new player is added
                    controller.addPlayerByUsername(username);
                    System.out.println(username + " logged in.");
                    return parser.sendMessage("Welcome, " + username + "!\n"); // TODO: This should be a JSON that the view will parse and display
                } else if (checkStatus == 0) {
                    // The username has already been taken, retry
                    System.out.println(username + " requested login, but the username is already taken.");
                    return parser.sendMessage("retry");
                } else {
                    // The username is already taken, but the player was disconnected and is trying to reconnect
                    System.out.println(username + " reconnected.");
                    return parser.sendPosition(controller.getPositionByUsername(username));
                }
            }
            case "age" -> {
                int age = parser.getAge(message);
                controller.addPlayerAge(age);
                return parser.sendMessage(age >= 8 ? "ok" : "no");
            }
            case "firstGame" -> {
                boolean firstGame = parser.getFirstGame(message);
                controller.addPlayerFirstGame(firstGame);
                return parser.sendPosition(controller.startRoom()); // If the current client is the first one to join, we need to show the chooseNumOfPlayerScreen()
            }
            case "numPlayer" -> {
                int numPlayer = parser.getNumPlayer(message);
                return parser.sendMessage(controller.checkNumPlayer(numPlayer));
            }
            case "ready" -> {
                return parser.sendMessage(controller.checkRoom());
            }
            case "index" -> {
                int position = parser.getPosition(message);
                return sendGame(position);
            }
            case "turn" -> {
                int position = parser.getPosition(message);
                return sendTurn(position);
            }
            case "pick" -> {
                if ("ok".equals(controller.pick(parser.getPick(message)))) {
                    return parser.sendPicked(controller.getPicked(parser.getPick(message)));
                } else {
                    return parser.sendMessage("retry");
                }
            }
            case "insert" -> {
                if (controller.checkInsert(parser.getInsert(message))) {
                    return sendUpdate(message);
                } else {
                    return parser.sendMessage("retry");
                }
            }
            case "sort" -> {
                controller.rearrangePicked(parser.getSort(message));
                return parser.sendMessage("ok");
            }
            case "board" -> {
                return parser.sendBoard("board", controller.getBoard());
            }
            case "endGame" -> {
                return parser.sendWinners(controller.getWinnersNickname());
            }
            default -> {
                System.out.println(message + " requested unknown");
                return parser.sendMessage("Unknown request.");
            }
        }
    }

    Message sendGame(int position) throws RemoteException;

    Message sendUpdate(Message message) throws RemoteException;

    default Message sendTurn(int position) throws RemoteException {
        return parser.sendMessage1("turn", "turn", controller.yourTurn(position));
    }
}
