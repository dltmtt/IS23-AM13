package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.GameCliView;
import it.polimi.ingsw.client.view.GameView;
import it.polimi.ingsw.commons.CommunicationInterface;
import it.polimi.ingsw.utils.FullRoomException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static it.polimi.ingsw.utils.CliUtilities.*;

public class ClientRmi extends Client {
    private final ClientParser parser = new ClientParser();
    GameView gameView = new GameCliView(); // TODO: this should be injected by the controller (cli or gui depending on user)
    GameController controller = new GameController(null, gameView, this);
    private Registry registry;
    private CommunicationInterface server;

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

    }

    @Override
    public void run() {
        controller.showLoginScreen();
    }

    @Override
    public void login(String username) {
        int age = 0;
        boolean firstGame = false;
        try {
            String responseMessage = server.sendMessage("username" + username); // This message will be a JSON
            // TODO: parse the JSON (now it's plain text)
            if (responseMessage.startsWith("Welcome")) {
                age = controller.showAgeScreen();
            } else {
                System.out.println("Response message is " + responseMessage + ". Retry");
//                System.out.println("Retry"); // TODO: actually retry
            }
            gameView.showMessage(responseMessage);
            String ageResponse = server.sendMessage("age" + age);
            if (!ageResponse.startsWith("ok")) {
                System.out.println("Remember that you need to be supervised by an adult to play this game.");
            }
            firstGame = controller.showFirstGamescreen();
            String nextStep = server.sendMessage("firstGame" + firstGame);
        } catch (RemoteException e) {
            throw new RuntimeException(e); // TODO: handle this exception
        } catch (FullRoomException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void logout() {
        // Implementation
    }
}
