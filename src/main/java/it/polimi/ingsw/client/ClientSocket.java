package it.polimi.ingsw.client;

// Client2 class that
// sends data and receives also

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static it.polimi.ingsw.commons.CommunicationInterface.HOSTNAME;
import static it.polimi.ingsw.commons.CommunicationInterface.PORT_SOCKET;

public class ClientSocket extends Client {
    public Socket s = null;
    public DataOutputStream dos;
    public BufferedReader br, kb;
    public Thread listenThread;
    public Thread sendThread;


    /**
     * Constructor to create DataOutputStream and BufferedReader
     */
    public ClientSocket() {
        // Create client socket
        try {
            s = new Socket(HOSTNAME, PORT_SOCKET);
        } catch (IOException e) {
            System.err.println("Unable to connect to server");
            throw new RuntimeException(e);
        }

        // to send data to the server
        try {
            dos = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            System.err.println("Unable to create output stream");
            throw new RuntimeException(e);
        }

        // to read data coming from the server
        try {
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            System.err.println("Unable to create input stream");
            throw new RuntimeException(e);
        }
        // to read data from the keyboard
        kb = new BufferedReader(new InputStreamReader(System.in));

        //Listen for messages from the server
        listenThread = new Thread(() ->
                // receive from the server
        {
            while (true) {
                try {
                    System.out.println("From " + s.getInetAddress() + ": " + br.readLine());
                } catch (IOException e) {
                    System.out.println("server disconnected, unable to read");
                    break;
                }
            }
        });

        //Send messages to the server
        sendThread = new Thread(() ->
                //sender thread
        {
            String str;
            while (true) {
                //send keyboard input to the server
                try {
                    sendInput();
                } catch (IOException e) {
                    System.out.println("server disconnected, unable to send");
                    break;
                }
            }
        }
        );
    }

    @Override
    public void run() {
        listenThread.start();
        sendThread.start();
    }

    public void sendMessage(String str) {
        // repeat as long as exit
        // is not typed at client
        if (!(str.equals("exit"))) {
            // send to the server
            try {
                dos.flush();
                dos.writeBytes(str + "\n");
            } catch (IOException e) {
                System.err.println("server disconnected, unable to send messages");
            }
        } else {
            close();
        }
    }

    public void sendInput() throws IOException {
        String str;
        if (!(str = kb.readLine()).equals("exit")) {
            // send to the server
            dos.writeBytes(str + "\n");
        } else {
            close();
        }
    }

    public void close() {
        // close connection.
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

    @Override
    public void login(String username) {

    }

    @Override
    public void logout() {

    }
}
