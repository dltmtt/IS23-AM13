package it.polimi.ingsw.server;

import it.polimi.ingsw.commons.CommunicationInterface;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.utils.FullRoomException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static it.polimi.ingsw.utils.CliUtilities.GRAY;
import static it.polimi.ingsw.utils.CliUtilities.RESET;

/**
 * Complete server class, which starts both RMI and socket servers.
 * <p>
 * The server can be stopped by typing {@value #SHUTDOWN_COMMAND} in the console.
 * This will stop both the RMI and the socket servers.
 */
public class Server implements ServerInterface, CommunicationInterface {
    private static final String SHUTDOWN_COMMAND = "exit";
    private final ServerRmi rmiServer;
    private final ServerParser parser = new ServerParser();
    private final ServerController controller = new ServerController();
    private GameModel gameModel = null;

    public Server() {
//        this.gameModel = new GameModel();
        try {
            this.rmiServer = new ServerRmi(this.gameModel);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to create RMI server.");
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();

        // Handle server shutdown
        new Thread(() -> {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String line;

            System.out.println("Type " + GRAY + SHUTDOWN_COMMAND + RESET + " to stop the server.");

            while (true) {
                try {
                    line = in.readLine();
                    if (line.equals(SHUTDOWN_COMMAND)) {
                        server.stop();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public void start() {
        try {
            rmiServer.start();
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("The server is ready.");
    }

    @Override
    public void stop() {
        System.out.println("Shutting down...");
        try {
            rmiServer.stop();
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    @Override
    public Message sendMessage(Message clientMessage) throws FullRoomException {
        String category = parser.getMessageCategory(clientMessage);
        switch (category) {
            case "username" -> {
                // TODO: parse the JSON, this is just a mock
                // Maybe we should use different methods for different requests
                String username = parser.getUsername(clientMessage);
                boolean checking = controller.checkUsername(username);
                if (checking) {
                    controller.addPlayerByUsername(username);
                    System.out.println(username + " requested login.");
                    return parser.sendMessage("Welcome, " + username + "!\n"); // This should be a JSON that the view will parse and display
                } else {
                    //TODO: actual retry
                    return parser.sendMessage("retry");
                }
            }
            case "age" -> {
                int age = parser.getAge(clientMessage);
                controller.addPlayerAge(age);
                return parser.sendMessage(age >= 8 ? "ok" : "no");
            }
            case "firstGame" -> {
                boolean firstGame = parser.getFirstGame(clientMessage);
                controller.addPlayerFirstGame(firstGame);
//            System.out.println("First game: " + firstGame);
                return parser.sendPosix(controller.startRoom()); // if the current client is the first one to join, we need to show the chooseNumOfPlayerScreen()
            }
            case "numPlayer" -> {
                int numPlayer = parser.getNumPlayer(clientMessage);
                return parser.sendMessage(controller.checkNumPlayer(numPlayer));
            }
            case "ready" -> {
                return parser.sendMessage(controller.checkRoom());
            }
            case "index" -> {
                int posix = parser.getPosix(clientMessage);
                return sendGame(posix);
            }
            default -> {
                System.out.println(clientMessage + " requested unknown");
                return parser.sendMessage("Unknown request.");
            }
        }
    }

    public Message sendGame(int posix) {
        System.out.println("Sending game to " + posix);
        return parser.sendStartGame(controller.getPersonalGoalCard(posix), controller.getCommonGoals(), controller.getBookshelf(posix), controller.getBoard());
    }
}
