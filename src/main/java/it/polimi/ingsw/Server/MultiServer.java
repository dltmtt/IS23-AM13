package it.polimi.ingsw.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {
    private static final int port = 1000;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);


        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection from " + clientSocket.getInetAddress() + " accepted");
                new SThread(clientSocket);
            }

        } catch (IOException e) {
            System.out.println("Error");
        }
        serverSocket.close();

    }
}
