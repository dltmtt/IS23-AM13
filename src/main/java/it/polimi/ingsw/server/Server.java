package it.polimi.ingsw.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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
public class Server implements CommunicationInterface {
    private static final String SHUTDOWN_COMMAND = "exit";
    private ServerRmi rmiServer;
    private ServerTCP socketServer;

    public Server() {
        try {
            this.rmiServer = new ServerRmi();
        } catch (RemoteException e) {
            e.printStackTrace();
            System.err.println("Unable to create RMI server.");
        }

        try {
            this.socketServer = new ServerTCP();
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

    public void start() {
        try {
            rmiServer.start();
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
            System.err.println("Unable to start the RMI server.");
        }

        socketServer.start();

        System.out.println("Server started.");
    }

    public void stop() {
        System.out.println("Shutting down... ");
        try {
            rmiServer.stop();
            socketServer.stop();
        } catch (RemoteException | NotBoundException e) {
            System.err.println("Unable to stop the server.");
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Server stopped.");
        System.exit(0);
    }
}
