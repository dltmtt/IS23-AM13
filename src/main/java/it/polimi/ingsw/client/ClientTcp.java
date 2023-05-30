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

import static it.polimi.ingsw.server.ServerCommunicationInterface.HOSTNAME;
import static it.polimi.ingsw.server.ServerCommunicationInterface.PORT_SOCKET;

public class ClientTcp extends Client implements ClientCommunicationInterface {

    public final DataOutputStream dataOutputStream;
    public Socket socket;
    public BufferedReader serverBufferedReader;
    public Thread listenThread;

    /**
     * Constructor to create DataOutputStream and BufferedReader
     * Creates socket, DataOutputStream, BufferedReader from the server and keyboard
     */
    public ClientTcp() throws RemoteException {
        super();

        // This is to send data to the server
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Unable to create output stream");
            throw new RuntimeException(e);
        }

        // This is used to read data coming from the server
        try {
            serverBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Unable to create input stream");
            throw new RuntimeException(e);
        }

        // Listen for messages coming from the server
        listenThread = new Thread(() -> {
            while (true) {
                Message receivedMessage = receiveMessage();
                parseReceivedMessage(receivedMessage);
            }
        });
        listenThread.start();
    }

    @Override
    public void sendMessage(Message message) {
        String stringMessage = message.getJSONstring();

        try {
            synchronized (dataOutputStream) {
                dataOutputStream.flush();
                dataOutputStream.writeBytes(stringMessage + "\n"); // Send the message to the server (with newline)
            }
        } catch (IOException e) {
            // System.err.println("Unable to send message to server. Is it still running?");
            System.err.println("Unable to send message to server.");
            close();
        }
    }


    public Message receiveMessage() {
        String serverMessageString;
        try {
            serverMessageString = serverBufferedReader.readLine(); // Read the message from the server
            try {
                JSONParser parser = new JSONParser();
                JSONObject messageFromClient = (JSONObject) parser.parse(serverMessageString);
                return new Message(messageFromClient);
            } catch (ParseException e) {
                System.out.println(serverMessageString);
                return new Message(serverMessageString);
            }
        } catch (IOException e) {
            System.err.println("\nLost connection to server.");
            close();
        }
        return null;
    }

    @Override
    public void connect() throws IOException {
        socket = new Socket(HOSTNAME, PORT_SOCKET);
    }

    /**
     * Closes the socket and the streams
     */
    public void close() {
        try {
            dataOutputStream.close();
            serverBufferedReader.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        listenThread.interrupt();
        System.exit(0);
    }
}
