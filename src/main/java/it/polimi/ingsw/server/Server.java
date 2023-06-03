package it.polimi.ingsw.server;

import it.polimi.ingsw.commons.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;

import static it.polimi.ingsw.utils.CliUtilities.*;

/**
 * Complete server class, which starts both RMI and socket servers.
 * <p>
 * The server can be stopped by typing {@value #SHUTDOWN_COMMAND} in the console.
 * This will stop both the RMI and the socket servers.
 */
public class Server implements ServerCommunicationInterface, ServerInterface {

    private static final String SHUTDOWN_COMMAND = "exit";
    private ServerRmi rmiServer;
    private ServerTcp socketServer;

    /**
     * This method creates a new server.
     * It initializes both the RMI and the socket servers.
     */
    public Server() {
        try {
            this.rmiServer = new ServerRmi();
            this.socketServer = new ServerTcp();
        } catch (IOException e) {
            // e.printStackTrace();
            System.err.println("The server is not ready.\nThe game cannot start");
        }
    }

    public static void main(String[] args) {
        Server server = new Server();

        try {
            server.start();
        } catch (Exception e) {
            System.err.println("Unable to start the server.");
        }

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

    /**
     * This method starts both the RMI and the socket servers.
     * It also sends a message to all clients to notify them that the server is up.
     */
    @Override
    public void start() {
        rmiServer.start();
        socketServer.start();
        System.out.println("Server started.");
        if (controller.isGameSaved()) {
            boolean load = askYesNoQuestion("A saved game has been found. Do you want to load it?", "n");
            if (load) {
                try {
                    controller.loadLastGame();
                } catch (IOException e) {
                    System.err.println("Unable to load the game.");
                }
            }
        }
    }

    /**
     * This method stops both the RMI and the socket servers.
     * It also sends a message to all clients to notify them that the server is shutting down.
     */
    @Override
    public void stop() {
        rmiServer.stop();
        socketServer.stop();
        System.out.println("Server shut down.");
        System.exit(0);
    }

    @Override
    public void receiveMessageTcp(Message message, SocketClientHandler client) throws RemoteException {
        // Not used
    }
}
