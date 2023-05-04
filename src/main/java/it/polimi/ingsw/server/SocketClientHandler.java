package it.polimi.ingsw.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class SocketClientHandler implements Runnable {

    private final Socket socket;
    public PrintStream ps;
    public BufferedReader br, kb;

    public SocketClientHandler(Socket socket) throws IOException {
        this.socket = socket;

        // Send data to the client
        try {
            ps = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // To read data coming from the client
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // To read data from the keyboard
        kb = new BufferedReader(new InputStreamReader(System.in));
    }

    public void run() {
        new Thread(() -> {
            String str;
            while (true) {
                try {
                    str = br.readLine();
                    System.out.println("From " + socket.getInetAddress().getHostName() + " :" + str);
                } catch (IOException | NullPointerException e) {
                    System.out.println("Client disconnected.");
                    break;
                }
            }
        }).start();

        new Thread(() -> {
            String str;
            while (true) {
                sendInput();
            }
        }).start();
    }

    public void sendInput() {
        String str1;
        try {
            str1 = kb.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // send to client
        ps.println(str1);
    }

    public void sendMessage(String message) {
        ps.println(message);
    }

    public void close() {
        sendMessage("Connection closed.");
        ps.close();
        try {
            br.close();
            kb.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while closing buffered reader or socket.");
        }

        // terminate application
        System.exit(0);
    }
}
