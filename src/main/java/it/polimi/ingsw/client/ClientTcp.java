package it.polimi.ingsw.client;

import it.polimi.ingsw.commons.Message;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.RemoteException;

import static it.polimi.ingsw.server.CommunicationInterface.HOSTNAME;
import static it.polimi.ingsw.server.CommunicationInterface.PORT_SOCKET;

public class ClientTcp extends Client {

    public final DataOutputStream dos;
    public Socket s;
    // buffered reader and keyboard
    public BufferedReader br, kb;

    public Thread listenThread;
    public Thread sendThread;
    public Thread pingThread;

    /**
     * Constructor to create DataOutputStream and BufferedReader
     * Creates socket, DataOutputStream, BufferedReader from the server and keyboard
     */
    public ClientTcp() throws RemoteException {
        super();

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
                    String str = br.readLine();
                    System.out.println("From " + s.getInetAddress() + ": " + br.readLine());
                } catch (IOException e) {
                    System.out.println("Server disconnected, unable to read.");
                    close();
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
                    close();
                    break;
                }
            }
        });
    }

    @Override
    public void sendMessage(Message message) {
        String stringMessage = message.getJSONstring();
        try {
            synchronized (dos) {
                dos.flush();
                dos.writeBytes(stringMessage + "\n");
            }
        } catch (IOException e) {
            System.err.println("Unable to send message to server. Is it still running?");
        }
        receivedMessage(message);
        // return receiveMessage();
    }

    @Override
    public void receivedMessage(Message message) {

    }

    @Override
    public Message numOfPlayers() {
        return null;
    }

    public Message receiveMessage() {
        String str;
        try {
            // read a string
            str = br.readLine();
            try {
                JSONParser parser = new JSONParser();
                JSONObject messageFromClient = (JSONObject) parser.parse(str);
                return new Message(messageFromClient);
            } catch (ParseException e) {
                System.out.println(str);
                return new Message(str);
            }
        } catch (IOException e) {
            System.err.println("Lost connection to server.");
        }
        return null;
    }

    @Override
    public void connect() throws IOException {
        s = new Socket(HOSTNAME, PORT_SOCKET);
    }

    @Override
    public void sendMe() {

    }

    @Override
    public void startGame(Message message) {

    }

    /**
     * Sends a string message to the server
     *
     * @param str the message to send
     */
    public void sendMessage(String str) {

        try {
            synchronized (dos) {
                dos.flush();
                dos.writeBytes(str + "\n");
            }
        } catch (IOException e) {
            System.err.println("unable to send message, output not available...");
        }
    }

    /**
     * Sends the input from the keyboard to the server
     */
    public void sendInput() throws IOException {
        String str = kb.readLine();
        dos.writeBytes(str + "\n");
    }

    /**
     * Closes the socket and the streams
     */
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
