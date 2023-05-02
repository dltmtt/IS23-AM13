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

public class MultiEchoServer implements ServerInterface {
    public List<EchoServerClientHandler> clientHandlers;

    public HashMap<EchoServerClientHandler, Player> connectedPlayers;

    public MultiEchoServer() {
        clientHandlers = new ArrayList<>();
        connectedPlayers = new HashMap<>();
    }

    @Override
    public void start() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket ss = null;
        try {
            // Create server Socket
            ss = new ServerSocket(888);
        } catch (IOException e) {
            try {
                ss.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Could not listen on port 888");
            System.exit(-1);
        }
        System.out.println("Listening for a connection");

        Socket s = null;
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
        try {
            ss.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        executor.shutdown();
    }

    @Override
    public void stop() throws RemoteException, NotBoundException {
        //TODO
        System.out.println("Stopping soket server (not implemented yet)...");
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
