package it.polimi.ingsw.server.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// Server class
public class TCPServer {
    private final List<Socket> socketList = new ArrayList<>();
    private final List<ClientHandler> clientHandlerList = new ArrayList<>();
    private ServerSocket server = null;
    private String line;

    public String getLine() {
        return line;
    }

    public ServerSocket getServer() {
        return server;
    }

    public List<Socket> getSocketList() {
        return socketList;
    }

    public void run() {
        try {

            // server is listening on port 1234
            server = new ServerSocket(1234);
            server.setReuseAddress(true);

            // running infinite loop for getting
            // client request
            while (true) {

                // socket object to receive incoming client
                // requests
                Socket client = server.accept();
                socketList.add(client);

                // Displaying that new client is connected
                // to server
                System.out.println("New client connected"
                        + client.getInetAddress()
                        .getHostAddress());

                // create a new thread object
                ClientHandler clientSock
                        = new ClientHandler(client);

                // This thread will handle the client
                // separately
                new Thread(clientSock).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverClose();
        }
    }

    public void socketClose(Socket client) {
        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void serverClose() {
        if (server != null) {
            try {
                for (ClientHandler clients : clientHandlerList) {
                    clients.clientSocket.close();
                    clients.close();
                }
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isRunning() {
        return server != null && !server.isClosed();
    }

    // ClientHandler class
    private class ClientHandler implements Runnable {
        private final Socket clientSocket;

        // Constructor
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void close() {
        }

        public void run() {
            PrintWriter out = null;
            BufferedReader in = null;
            try {

                // get the outputstream of client
                out = new PrintWriter(
                        clientSocket.getOutputStream(), true);

                // get the inputstream of client
                in = new BufferedReader(
                        new InputStreamReader(
                                clientSocket.getInputStream()));


                while ((line = in.readLine()) != null) {
                    //System.out.println("Received from client: " + line);
                    if (line.equalsIgnoreCase("exit")) {
                        System.out.println("Client " + clientSocket + " sends exit...");
                        System.out.println("Closing this connection.");
                        clientSocket.close();
                        System.out.println("Connection closed");
                        break;
                    } else if (line.equalsIgnoreCase("shutdown")) {
                        System.out.println("Server shutting down...");
                        for (Socket client : socketList) {
                            socketClose(client);
                        }
                        serverClose();
                        System.out.println("Server shutdown");
                        break;

                    } else {
                        // writing the received message from
                        // client
                        System.out.printf(" Sent from the client: %s\n", line);

                        //COMUNICAZIONE SERVER -> CLIENT
                        out.println(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
