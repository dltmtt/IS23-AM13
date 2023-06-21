package it.polimi.ingsw.server;

import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.utils.Utils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;

public class SocketClientHandler implements Runnable {

    /**
     * The place to read incoming messages from the client.
     */
    public final BufferedReader clientBufferedReader;
    private final Socket socket;
    private final OutputStream outputStream;
    /**
     * The place to write outgoing messages to the client.
     */
    public PrintStream clientPrintStream;
    /**
     * The thread that listens for messages coming from the client.
     */
    public Thread communicationThread;
    private String username;

    public SocketClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        outputStream = socket.getOutputStream();

        // To send data to the client
        try {
            clientPrintStream = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // To read data coming from the client
        try {
            clientBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Listen for messages coming from the client and send them to the server
        communicationThread = new Thread(() -> {
            String clientString;
            while (true) {
                try {
                    synchronized (clientBufferedReader) {
                        // Read the message from the client
                        clientString = clientBufferedReader.readLine();

                        // Parse the message
                        JSONParser parser = new JSONParser();
                        JSONObject messageFromClient = null;
                        try {
                            messageFromClient = (JSONObject) parser.parse(clientString);
                        } catch (ParseException e) {
                            System.err.println("Unable to parse message from client");
                        } catch (NullPointerException e) {
                            Utils.showDebugInfo(e);
                            // disconnect(username);
                            break;
                        }

                        Message message = new Message(messageFromClient);

                        // Send the message to the server
                        synchronized (outputStream) {
                            outputStream.write(message.getJSONstring().getBytes());
                            outputStream.flush();
                        }
                    }
                } catch (IOException e) {
                    Utils.showDebugInfo(e);
                    // We are here because the client disconnected (probably)
                    System.err.println("IOException");
                    // disconnect(username);
                    break;
                }
            }
        });
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void run() {
        communicationThread.start();
    }

    public void sendString(String message) {
        synchronized (clientPrintStream) {
            clientPrintStream.println(message);
        }
    }

    public void close() {
        sendString("Connection closed.");

        communicationThread.interrupt();
        clientPrintStream.close();
        try {
            clientBufferedReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while closing buffered reader or socket.");
        }

        System.exit(0);
    }

    public void sendMessageToClient(Message message) {
        synchronized (clientPrintStream) {
            clientPrintStream.println(message.getJSONstring());
        }
    }
}
