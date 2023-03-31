package it.polimi.ingsw.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {
    public static final int port = 1050;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection from " + clientSocket.getInetAddress() + " accepted");
                new SThread(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Error");
        }
        serverSocket.close();

         
    }


}