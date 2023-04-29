package it.polimi.ingsw.server.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class EchoServerClientHandler implements Runnable {
    private final Socket socket;
    public PrintStream ps;
    public BufferedReader br, kb;

    public EchoServerClientHandler(Socket socket) throws IOException {
        this.socket = socket;

        //send data to the client
        try {
            ps = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // to read data coming from the client
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // to read data from the keyboard
        kb = new BufferedReader(new InputStreamReader(System.in));
        run();
    }

    public void run() {
        new Thread(() ->
        {
            try {
                System.out.println(br.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        ).start();
    }

    public void sendMessage() {
        String str1;
        try {
            str1 = kb.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // send to client
        ps.println(str1);
    }

    public void close() {
        // close connection
        ps.close();
        try {
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            kb.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // terminate application
        System.exit(0);
    }
}
