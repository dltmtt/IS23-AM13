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

import static it.polimi.ingsw.server.CommunicationInterface.PORT_SOCKET;

public class ServerTCP implements ServerInterface {

    private final ServerSocket serverSocket;
    public List<SocketClientHandler> connectedClients;
    public HashMap<SocketClientHandler, Player> connectedPlayers;
    public Socket s = null;
    public ExecutorService executor;

    public Thread acceptConnectionsThread;

    public ServerTCP() throws IOException {

        connectedClients = new ArrayList<>();
        connectedPlayers = new HashMap<>();
        executor = Executors.newCachedThreadPool();
        try {
            serverSocket = new ServerSocket(PORT_SOCKET);
            System.out.println("Server socket started on port " + serverSocket.getLocalPort() + ".");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Unable to start the server socket.");
        }

        acceptConnectionsThread = new Thread(() -> {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Connection accepted from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + ".");
                    SocketClientHandler clientHandler = null;
                    try {
                        clientHandler = new SocketClientHandler(clientSocket);
                        try {
                            executor.submit(clientHandler);
                            //clientHandler.sendMessage("Welcome to the server!");
                            //sendToAllExcept("A new player has joined the game!", clientHandler);
                            connectedClients.add(clientHandler);
                        } catch (RejectedExecutionException | NullPointerException e) {
                            System.err.println("Socket " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + ": socket client handler cannot be submitted to the executor.");
                        }
                    } catch (IOException e) {
                        System.err.println("Socket " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + ": socket client handler cannot be created.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Unable to accept a connection.");
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
        sendToAll("Server is shutting down...");
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

    public void closeAllConnections() {
        for (SocketClientHandler clientHandler : connectedClients) {
            clientHandler.close();
        }
    }

    public void sendToAll(String message) {
        for (SocketClientHandler clientHandler : connectedClients) {
            clientHandler.sendString(message);
        }
    }

    public void sendToAllExcept(String message, SocketClientHandler excludedPlayer) throws IllegalArgumentException {
        if (excludedPlayer == null || message == null)
            throw new IllegalArgumentException("null parameter handler cannot be null.");
        connectedClients.stream().filter(client -> client != excludedPlayer).forEach(client -> client.sendString(message));
    }

    public int getConnectedPlayers() {
        return connectedClients.size();
    }
}
