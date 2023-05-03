package it.polimi.ingsw.server.socket;

// Server2 class that
// receives data and sends data

import it.polimi.ingsw.server.ServerInterface;
import it.polimi.ingsw.server.model.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.polimi.ingsw.commons.CommunicationInterface.PORT_SOCKET;

public class MultiEchoServer implements ServerInterface {
    public List<EchoServerClientHandler> clientHandlers;

    public HashMap<EchoServerClientHandler, Player> connectedPlayers;

    public ServerSocket ss;

    public Socket s = null;
    public ExecutorService executor;


    public MultiEchoServer() throws IOException {
        clientHandlers = new ArrayList<>();
        connectedPlayers = new HashMap<>();
        executor = Executors.newCachedThreadPool();
        ss = new ServerSocket(PORT_SOCKET);
        System.out.println("Socket server configuration completed.");
    }

    @Override
    public void start() {
        EchoServerClientHandler newClient = null;
        while (true) {
            try {
                s = ss.accept();
                System.out.println("Connection established");
                newClient = new EchoServerClientHandler(s);
                //clientHandlers.add(new EchoServerClientHandler(s));
                //new Thread(clientHandlers.get(clientHandlers.size() - 1));
                executor.submit(newClient);
                newClient.sendMessage("Welcome to the server!");
                sendToAllExcept("A new player has joined the game!", newClient);
                clientHandlers.add(newClient);
            } catch (IOException e) {
                System.out.println("Accept failed: 888");
                System.exit(-1);
                break;
            }
        }
    }

    @Override
    public void stop() throws RemoteException, NotBoundException {
        //TODO
        System.out.println("Stopping socket server...");
        sendToAll("Server is shutting down...");
        try {
            ss.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        closeAllConnections();
        executor.shutdownNow();
    }

    public void closeAllConnections() {
        for (EchoServerClientHandler clientHandler : clientHandlers) {
            clientHandler.close();
        }
    }

    public void sendToAll(String message) {
        for (EchoServerClientHandler clientHandler : clientHandlers) {
            clientHandler.sendMessage(message);
        }
    }

    public void sendToAllExcept(String message, EchoServerClientHandler clientHandler) {
        for (EchoServerClientHandler client : clientHandlers) {
            if (client != clientHandler) {
                client.sendMessage(message);
            }
        }
    }

    public int getConnectedPlayers() {
        return clientHandlers.size();
    }
}
