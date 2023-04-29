/*
package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.client.socket.ClientCSSocket;
import it.polimi.ingsw.utils.SettingLoader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

// Server class
public class ServerCSHandler {
    private final HashMap<Socket, ClientCSSocket> socketList = new HashMap<>();
    //private final List<ClientHandler> clientHandlerList = new ArrayList<>();
    private ServerSocket server = null;
    private String line;

    public String getLine() {
        return line;
    }

    public ServerSocket getServer() {
        return server;
    }

    public HashMap<Socket, ClientCSSocket> getSocketList() {
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
                try {
                    Socket clientServer = server.accept();


                    //create a ServerClient socket
                    ClientCSSocket serverClient = new ClientCSSocket(clientServer.getInetAddress().getHostAddress(), SettingLoader.getServerPort());

                    socketList.put(clientServer, serverClient);

                    // Displaying that new clientServer is connected
                    // to server
                    System.out.println("New clientServer connected"
                            + clientServer.getInetAddress()
                            .getHostAddress());

                    // create a new thread object
                    ClientHandler clientSock
                            = new ClientHandler(clientServer);

                    // This thread will handle the clientServer
                    // separately
                    new Thread(clientSock).start();
                } catch (IOException e) {
                    System.out.println("Server closed");
                    break;
                }

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
                for (ClientCSSocket clients : socketList.values()) {
                    clients.close();
                    socketList.get(clients).close();
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
                        for (ClientCSSocket client : socketList.values()) {
                            socketClose(client.getSocket());
                        }
                        serverClose();
                        System.out.println("Server shutdown");
                        break;

                    } else {
                        // writing the received message from the client

                        //convert the line string in a Json object
                        JSONParser parser = new JSONParser();
                        try {
                            JSONObject json = (JSONObject) parser.parse(line);
                            System.out.println(" Sent from the client: \t" + json.toJSONString());
                            //TODO
                            //Inserire chiamata al parser


                        } catch (ParseException e) {
                            //A string is received
                            System.out.println(" Sent from the client: \t" + line);
                        }
                        //COMUNICAZIONE SERVER -> CLIENT
                        //out.println(line);
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
*/