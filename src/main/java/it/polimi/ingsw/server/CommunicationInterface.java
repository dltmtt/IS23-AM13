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
    String HOSTNAME = "192.168.236.121"; // Shared by RMI and socket
    ServerParser parser = new ServerParser();
    ServerController controller = new ServerController();

    default Message sendMessage(Message message) throws RemoteException, FullRoomException, IllegalAccessException {
        String category = parser.getMessageCategory(message);
        switch (category) {
            case "username" -> {
                String username = parser.getUsername(message);
                boolean checking = controller.checkUsername(username);
                if (checking) {
                    controller.addPlayerByUsername(username);
                    System.out.println(username + " requested login.");
                    return parser.sendMessage("Welcome, " + username + "!\n"); // This should be a JSON that the view will parse and display
                } else {
                    return parser.sendMessage("retry");
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
                    return parser.sendUpdate("update", controller.getCurrentePlayerBookshelf(), controller.getBoard(), controller.getCurrentPlayerScore());
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

    default Message sendGame(int position) throws RemoteException {
        System.out.println("Sending game to " + position);
        return parser.sendStartGame(controller.getPersonalGoalCard(position), controller.getCommonGoals(), controller.getBookshelf(position), controller.getBoard());
    }

    default Message sendTurn(int position) throws RemoteException {
        return parser.sendMessage1("turn", "turn", controller.yourTurn(position));
    }
}
