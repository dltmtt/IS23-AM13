package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.client.ClientController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer {
    private static final int minAge = 8;
    private final int portNumber;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public TCPServer(int port) {
        this.portNumber = port;
    }

    public static void main(String[] args) {
        TCPServer echoServer = new TCPServer(1234);
        echoServer.run();
    }

    public void run() {
        // ExecutorService Creates new threads when it's necessary,
        // but uses the existing ones when possible.
        ExecutorService ex = Executors.newCachedThreadPool();

        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If here, the server is ready
        System.out.println("The server is ready");

        while (true) {
            try {
                // Accepting the connection, if possible
                assert serverSocket != null;
                clientSocket = serverSocket.accept();
                ex.submit(new ClientController(clientSocket));
                try {
                    log();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                break;
            }
        }
        ex.shutdown();
    }

    public void log() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        String name;
        name = in.readLine();
        out.println("The name is: " + name);

        int age;
        age = in.read();
        if (age < minAge) {
            out.println("The player " + name + " must be accompanied by an adult to play.");
        }

        in.close();
        out.flush();
        out.close();
    }
}
