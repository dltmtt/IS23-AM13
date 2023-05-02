package it.polimi.ingsw.server;

import it.polimi.ingsw.commons.CommunicationInterface;
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
    protected static final int PORT_RMI = 1099;
    protected static final int PORT_SOCKET = 1234;
    protected static final String HOSTNAME = "localhost";
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
    public String sendMessage(String clientMessage) throws FullRoomException {
        String category = parser.getMessageCategory(clientMessage);

        if (category.equals("username")) {
            // TODO: parse the JSON, this is just a mock
            // Maybe we should use different methods for different requests
            String username = parser.getUsername(clientMessage);
            boolean checking = controller.checkUsername(username);
            if (checking) {
                controller.addPlayerByUsername(username);
                System.out.println(username + " requested login.");
                return "Welcome, " + username + "!\n"; // This should be a JSON that the view will parse and display
            } else {
                //TODO: actual retry
                return "retry";
            }

        } else if (category.equals("age")) {
            int age = parser.getAge(clientMessage);
            controller.addPlayerAge(age);
            return age >= 8 ? "ok" : "no";
        } else if (category.equals("firstGame")) {
            boolean firstGame = parser.getFirstGame(clientMessage);
            controller.addPlayerFirstGame(firstGame);
//            System.out.println("First game: " + firstGame);
            return controller.startRoom(); // if the current client is the first one to join, we need to show the chooseNumOfPlayerScreen()
        } else if (category.equals("numPlayer")) {
            System.out.println("number recived!");
            return "ok";
        } else {
            System.out.println(clientMessage + " requested unknown");
            return "Unknown request";
        }

    }

}
