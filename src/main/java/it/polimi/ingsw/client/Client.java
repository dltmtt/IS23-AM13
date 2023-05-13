package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.GameView;

import java.io.IOException;
import java.rmi.NotBoundException;

import static it.polimi.ingsw.utils.CliUtilities.RESET;
import static it.polimi.ingsw.utils.CliUtilities.SUCCESS_COLOR;

// This is abstract (non instantiable) because each client will either
// be an RMI client or a Socket client
public abstract class Client {

    protected final ClientParser parser = new ClientParser();
    public Thread loginThread;
    public GameView gameView;

    public Client() {
        // All these messages should probably be moved to the view
        System.out.print("Connecting to server... ");

        boolean connected = false;
        long now = System.currentTimeMillis();
        long timeout = now + 10 * 1500; // 15 seconds

        // This is done in order to be able to start the client and
        // the server in parallel from the IDE (otherwise the client
        // could start before the server and fail to connect)
        while (System.currentTimeMillis() < timeout) {
            try {
                connect();

                // If we get here, no exception was thrown and we are connected
                connected = true;
                break;
            } catch (NotBoundException | IOException e) {
                // Unable to connect to server, retrying...
            }
        }

        if (connected) {
            System.out.println(SUCCESS_COLOR + "connected" + RESET);
        } else {
            System.err.println("Unable to connect to the server. Is it running?");
            System.exit(1);
        }

        loginThread = new Thread(this::login);
    }

    public void setView(GameView gameView) {
        this.gameView = gameView;
    }

    public void start() {
        loginThread.start();
    }

    public abstract void login();

    public abstract void connect() throws IOException, NotBoundException;
}
