package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.GameCliView;
import it.polimi.ingsw.client.view.GameView;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.CommunicationInterface;
import it.polimi.ingsw.utils.FullRoomException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static it.polimi.ingsw.server.CommunicationInterface.HOSTNAME;
import static it.polimi.ingsw.server.CommunicationInterface.PORT_RMI;
import static it.polimi.ingsw.utils.CliUtilities.*;

public class ClientRmi extends Client {
    private final ClientParser parser = new ClientParser();
    public Thread loginThread;
    GameView gameView = new GameCliView(); // TODO: this should be injected by the controller (cli or gui depending on user)
    GameController controller = new GameController(null, gameView, this);
    int myPosix;
    private Registry registry;
    private CommunicationInterface server;
    private Message message;

    public ClientRmi() {
        // All these messages should probably be moved to the view
        System.out.print("Connecting to server... ");

        // TODO: create a connect method in the Client class and maybe
        // use something else for the timer
        boolean connected = false;
        long now = System.currentTimeMillis();
        long timeout = now + 10 * 1000; // 10 seconds

        // This is done in order to be able to start the client and
        // the server in parallel from the IDE (otherwise the client
        // could start before the server and fail to connect)
        while (System.currentTimeMillis() < timeout) {
            try {
                registry = LocateRegistry.getRegistry(HOSTNAME, PORT_RMI);
                server = (CommunicationInterface) registry.lookup("CommunicationInterface");

                // If we get here, no exception was thrown and we are connected
                connected = true;
                break;
            } catch (RemoteException | NotBoundException e) {
                // Unable to connect to server, retrying...
            }
        }

        if (connected) {
            System.out.println(SUCCESS_COLOR + "connected" + RESET);
        } else {
            System.err.println(ERROR_COLOR + "error" + RESET);
            System.out.println("Unable to connect to the server. Is it running?");
            System.exit(1);
        }

        loginThread = new Thread(() -> {
            controller.showLoginScreen();
        });

    }

    public void start() {
        loginThread.start();
    }

    public void login(String username) {
        int age = 0;
        boolean firstGame;
        try {
            String responseMessage = parser.getMessage(server.sendMessage(parser.sendUsername(username))); // This message will be a JSON
            // TODO: parse the JSON (now it's plain text)
            if (responseMessage.startsWith("Welcome")) {
                age = controller.showAgeScreen();
            } else {
                System.out.println("Response message is " + responseMessage + ". Retry");
//                System.out.println("Retry"); // TODO: actually retry
            }
            gameView.showMessage(responseMessage);
            String ageResponse = parser.getMessage(server.sendMessage(parser.sendAge(age)));
            if (!ageResponse.startsWith("ok")) {
                System.out.println("Remember that you need to be supervised by an adult to play this game.");
            }
            firstGame = controller.showFirstGamescreen();
            int nextStep = parser.getPosix(server.sendMessage(parser.sendFirstGame(firstGame)));
            if (nextStep == 1) {
                int numPlayer = controller.showNumberOfPlayersScreen();
                String numPlayerResponse = parser.getMessage(server.sendMessage(parser.sendNumPlayer(numPlayer)));
                while (numPlayerResponse.startsWith("retry")) {
                    System.out.println("Illegal number of players. Retry.");
                    numPlayer = controller.showNumberOfPlayersScreen();
                    numPlayerResponse = parser.getMessage(server.sendMessage(parser.sendNumPlayer(numPlayer)));
                }
                //end of login
            }
            myPosix = nextStep;
            System.out.println("Your position is " + myPosix);
            waitingRoom();
        } catch (RemoteException e) {
            throw new RuntimeException(e); // TODO: handle this exception
        } catch (FullRoomException | IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void waitingRoom() throws FullRoomException, IOException, ParseException {
        System.out.println("Waiting for other players to join...");
        String response = parser.getMessage(server.sendMessage(parser.sendReady()));
        while (response == null) {
            response = parser.getMessage(server.sendMessage(parser.sendReady()));
        }
        startGame();
    }

    public void startGame() throws FullRoomException, IOException, ParseException {
        Message myGame = server.sendGame(myPosix);
        controller.showPersonalGoal(parser.getPersonalGoal(myGame));
//        System.out.println("Game started!");
    }
}
