package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer {
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
        //it creates new Threads when it's necessary,
        //but it uses the existing ones until it's possible to
        ExecutorService ex = Executors.newCachedThreadPool();

        //opening a new socket
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //if here, the server is ready
        System.out.println("server is ready");
        while (true) {
            try {
                //accepting the connection, it possible
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
        String name;
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        name = in.readLine();
        System.out.println("The name is: " + name);
        int age;
        age = in.read();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        if (age >= 8) {
            out.println(name + ", you can play!");
        } else {
            out.println(name + "you're too young!");
        }
        in.close();
        out.flush();
        out.close();
    }
}
