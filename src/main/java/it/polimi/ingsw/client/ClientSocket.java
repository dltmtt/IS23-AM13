package it.polimi.ingsw.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static it.polimi.ingsw.server.CommunicationInterface.HOSTNAME;
import static it.polimi.ingsw.server.CommunicationInterface.PORT_SOCKET;

public class ClientSocket extends Client {

    public Socket s;
    public DataOutputStream dos;
    public BufferedReader br, kb;
    public Thread listenThread;
    public Thread sendThread;

    /**
     * Constructor to create DataOutputStream and BufferedReader
     */
    public ClientSocket() {
        // Create the client socket
        try {
            s = new Socket(HOSTNAME, PORT_SOCKET);
        } catch (IOException e) {
            System.err.println("Unable to connect to server");
            throw new RuntimeException(e);
        }

        // This is to send data to the server
        try {
            dos = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            System.err.println("Unable to create output stream");
            throw new RuntimeException(e);
        }

        // This is used to read data coming from the server
        try {
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            System.err.println("Unable to create input stream");
            throw new RuntimeException(e);
        }
        // To read data from the keyboard
        kb = new BufferedReader(new InputStreamReader(System.in));

        // Listen for messages coming from the server
        listenThread = new Thread(() -> {
            while (true) {
                try {
                    System.out.println("From " + s.getInetAddress() + ": " + br.readLine());
                } catch (IOException e) {
                    System.out.println("Server disconnected, unable to read.");
                    break;
                }
            }
        });

        // Send messages to the server
        sendThread = new Thread(() -> {
            while (true) {
                // Send keyboard input to the server
                try {
                    sendInput();
                } catch (IOException e) {
                    System.out.println("Server disconnected, unable to send.");
                    break;
                }
            }
        });
    }

    @Override
    public void start() {
        listenThread.start();
        sendThread.start();
    }

    @Override
    public void login(String username) {
        sendMessage("login " + username);
    }

    public void sendMessage(String str) {
        try {
            dos.flush();
            dos.writeBytes(str + "\n");
        } catch (IOException e) {
            System.err.println("Server disconnected, unable to send messages.");
        }
    }

    public void sendInput() throws IOException {
        String str = kb.readLine();
        dos.writeBytes(str + "\n");
    }

    public void close() {
        try {
            dos.close();
            br.close();
            kb.close();
            s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        listenThread.interrupt();
        sendThread.interrupt();
        System.exit(0);
    }
}
