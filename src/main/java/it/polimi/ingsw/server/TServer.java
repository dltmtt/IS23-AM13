package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TServer {
    private final int portNumber;
    ServerSocket serverSocket = null;

    public TServer(int port) {
        this.portNumber = port;
    }

    public static void main(String[] args) {
        TServer echoServer = new TServer(1234);
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
                Socket socket = serverSocket.accept();
                ex.submit(new ClientHandler(socket));
            } catch (IOException e) {
                break;
            }
        }
        ex.shutdown();
    }
}
