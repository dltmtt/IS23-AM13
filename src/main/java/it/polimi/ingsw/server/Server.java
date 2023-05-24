package it.polimi.ingsw.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static it.polimi.ingsw.utils.CliUtilities.GRAY;
import static it.polimi.ingsw.utils.CliUtilities.RESET;

// TODO: add model

/**
 * Complete server class, which starts both RMI and socket servers.
 * <p>
 * The server can be stopped by typing {@value #SHUTDOWN_COMMAND} in the console.
 * This will stop both the RMI and the socket servers.
 */
public class Server implements CommunicationInterface, ServerInterface {

    private static final String SHUTDOWN_COMMAND = "exit";
    private ServerRmi rmiServer;
    private ServerTcp socketServer;

    public Server() {
        try {
            this.rmiServer = new ServerRmi();
        } catch (RemoteException e) {
            e.printStackTrace();
            System.err.println("Unable to create RMI server.");
        }

        try {
            this.socketServer = new ServerTcp();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to create socket server.");
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

    @Override
    public void start() {
        rmiServer.start();
        socketServer.start();
        System.out.println("Server started.");
    }

    @Override
    public void stop() {
        rmiServer.stop();
        socketServer.stop();
        System.out.println("Server shut down.");
        System.exit(0);
    }

    @Override
    public void sendClient() throws RemoteException, NotBoundException {

    }
}
