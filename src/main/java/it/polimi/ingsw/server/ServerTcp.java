package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import static it.polimi.ingsw.server.ServerCommunicationInterface.PORT_SOCKET;

public class ServerTcp implements ServerInterface {

    private final ServerSocket serverSocket;

    /**
     * The list of the connected clients.
     */
    public List<SocketClientHandler> connectedClients;

    /**
     * The map between the connected clients and the players.
     */
    public HashMap<SocketClientHandler, Player> connectedPlayers;

    /**
     * The executor service used to handle the clients.
     */
    public ExecutorService executor;

    /**
     * The thread that accepts new connections.
     */
    public Thread acceptConnectionsThread;

    public ServerTcp() throws IOException {
        connectedClients = new ArrayList<>();
        connectedPlayers = new HashMap<>();
        executor = Executors.newCachedThreadPool();

        serverSocket = new ServerSocket(PORT_SOCKET); // Throws IOException
        System.out.println("Server socket started on port " + serverSocket.getLocalPort() + ".");

        acceptConnectionsThread = new Thread(() -> {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    SocketClientHandler clientHandler;
                    try {
                        clientHandler = new SocketClientHandler(clientSocket);
                        try {
                            executor.submit(clientHandler);
                            connectedClients.add(clientHandler);
                        } catch (RejectedExecutionException | NullPointerException e) {
                            System.err.println("Socket " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + ": socket client handler cannot be submitted to the executor.");
                        }
                    } catch (IOException e) {
                        System.err.println("Socket " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + ": socket client handler cannot be created.");
                    }
                } catch (IOException e) {
                    System.err.println("Server is closed, unable to accept a connection.");
                    System.exit(0);
                }
            }
        });
    }

    @Override
    public void start() {
        acceptConnectionsThread.start();
    }

    @Override
    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to stop the socket server.");
        }
        closeAllConnections();
        executor.shutdownNow();
        System.out.println("Socket server stopped.");
        acceptConnectionsThread.interrupt();
    }

    /**
     * Closes all the connections with the clients.
     */
    public void closeAllConnections() {
        for (SocketClientHandler clientHandler : connectedClients) {
            clientHandler.close();
        }
    }
}
