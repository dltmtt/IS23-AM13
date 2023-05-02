package it.polimi.ingsw.server.socket;

// Server2 class that
// receives data and sends data

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiEchoServer {
    public List<EchoServerClientHandler> clientHandlers;

    public MultiEchoServer() {
        clientHandlers = null;
    }

    public void startServer() throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket ss = null;
        try {
            // Create server Socket
            ss = new ServerSocket(888);
        } catch (IOException e) {
            ss.close();
            System.out.println("Could not listen on port 888");
            System.exit(-1);
        }
        System.out.println("Listening for a connection");

        Socket s = null;
        while (true) {
            try {
                s = ss.accept();
                System.out.println("Connection established");
                //clientHandlers.add(new EchoServerClientHandler(s));
                //new Thread(clientHandlers.get(clientHandlers.size() - 1));
                executor.submit(new EchoServerClientHandler(s));
            } catch (IOException e) {
                System.out.println("Accept failed: 888");
                System.exit(-1);
                break;
            }
        }
        ss.close();
        executor.shutdown();
    }
}
