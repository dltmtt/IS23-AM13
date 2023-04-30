package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.GameModel;

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
public class Server implements ServerInterface {
    protected static final int PORT_RMI = 1099;
    protected static final int PORT_SOCKET = 1234;
    protected static final String HOSTNAME = "localhost";
    private static final String SHUTDOWN_COMMAND = "exit";
    private final GameModel gameModel;
    private final ServerRmi rmiServer;


    public Server() {
        this.gameModel = new GameModel();
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

    public void start() {
        try {
            rmiServer.start();
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("The server is ready.");
    }

    public void stop() {
        System.out.println("Shutting down...");
        try {
            rmiServer.stop();
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}