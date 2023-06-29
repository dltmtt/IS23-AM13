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

import static it.polimi.ingsw.client.MyShelfie.HOSTNAME;
import static it.polimi.ingsw.server.ServerCommunicationInterface.PORT_SOCKET;

/**
 * This class is the TCP implementation of the client.
 * It extends the abstract class Client and implements the ClientCommunicationInterface.
 */
public class ClientTcp extends Client implements ClientCommunicationInterface {

    /**
     * Used to send messages to the server.
     */
    public final DataOutputStream dataOutputStream;
    /**
     * Used to read messages from the server.
     */
    public final BufferedReader serverBufferedReader;
    /**
     * The socket used to communicate with the server.
     */
    public Socket socket;
    /**
     * The thread that listens for messages from the server.
     */
    public Thread listenThread;

    /**
     * Constructor used to create DataOutputStream and BufferedReader.
     * It creates socket, DataOutputStream, BufferedReader from the server and keyboard.
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
                try {
                    Message receivedMessage = receiveMessage();
                    callBackSendMessage(receivedMessage);
                } catch (Exception e) {
                    // Don't do anything: if the server is down, the client will
                    // notice itself and will exit.
                }
            }
        });
        listenThread.start();
    }

    /**
     * Used to send a message.
     * @param message the message to send.
     */
    @Override
    public void sendMessage(Message message) {
        String stringMessage = message.getJSONstring();

        try {
            synchronized (dataOutputStream) {
                dataOutputStream.flush();
                dataOutputStream.writeBytes(stringMessage + "\n"); // Send the message to the server (with newline)
            }
        } catch (IOException e) {
            // Don't do anything: if the server is down, the client will
            // notice itself and will exit.
        }
    }

    /**
     * Receives a message from the server by reading it from the
     * <code>serverBufferedReader</code>.
     * If the message is not a valid JSON string, it is returned as a <code>String</code> object.
     * If <code>serverBufferedReader.readLine()</code> throws an exception, the client is closed.
     *
     * @return the message received from the server.
     */
    public Message receiveMessage() {
        String serverMessageString;
        try {
            serverMessageString = serverBufferedReader.readLine(); // Read a message from the server

            try {
                JSONParser parser = new JSONParser();
                JSONObject messageFromClient = (JSONObject) parser.parse(serverMessageString);
                return new Message(messageFromClient);
            } catch (ParseException e) {
                System.err.println("Unable to parse message from server.");
                System.out.println(serverMessageString);
                return new Message(serverMessageString);
            } catch (NullPointerException e) {
                // Don't do anything: if the server is down, the client will
                // notice itself and will exit.
            }
        } catch (IOException e) {
            // Don't do anything: if the server is down, the client will
            // notice itself and will exit.
        }
        return null;
    }

    /**
     * Used to connect to the server.
     * @throws IOException if the connection fails.
     */
    @Override
    public void connect() throws IOException {
        socket = new Socket(HOSTNAME, PORT_SOCKET);
    }
}
