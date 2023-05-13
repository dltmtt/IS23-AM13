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
import java.rmi.NotBoundException;

import static it.polimi.ingsw.server.CommunicationInterface.HOSTNAME;
import static it.polimi.ingsw.server.CommunicationInterface.PORT_SOCKET;

public class ClientSocket extends Client {

    public Socket s;

    // data output
    public DataOutputStream dos;

    // buffered reader and keyboard
    public BufferedReader br, kb;

    // threads to listen and send
    public Thread listenThread;
    public Thread sendThread;

    public GameController controller;

    /**
     * Constructor to create DataOutputStream and BufferedReader
     * Creates socket, DataOutputStream, BufferedReader from the server and keyboard
     */
    public ClientSocket(GameController controller) {
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
        this.controller = controller;
    }

    public Message receiveMessage() {
        String str;
        do {
            //System.out.println("cevef");
            try {
                // read a string
                str = br.readLine();
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject messageFromClient = (JSONObject) parser.parse(str);
                    Message message = new Message(messageFromClient);
                    return message;
                } catch (ParseException e) {
                    System.out.println(str);
                    return new Message(str);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } while (str == null);
    }

    /**
     * Starts the threads to listen from the server and send data
     */
    @Override
    public void start() {
        // Start the threads
        //listenThread.start();
        //sendThread.start();
        login();
        System.out.println("Login successful.");
    }

    /**
     * Sends the login message to the server
     */
    @Override
    public void login() {
        Message responseMessage;
        controller.startGame();
        do {
            String username = controller.showLoginScreen();
            Message usernameMessage = parser.sendUsername(username);
            sendMessage(usernameMessage.getJSONstring());

            responseMessage = receiveMessage();

            //System.out.println(responseMessage.getCategory());
            //System.out.println(responseMessage.getMessage());

            if (responseMessage.getCategory().equals("retry")) {
                System.out.println("Username already taken. Retry.");
            }
        } while ("retry".equals(responseMessage.getCategory()));
        System.out.println(responseMessage.getMessage());
    }

    @Override
    public void connect() throws IOException, NotBoundException {
        s = new Socket(HOSTNAME, PORT_SOCKET);
        System.out.println("Connected to server " + s.getInetAddress().getHostName() + ":" + s.getPort());
    }

    /**
     * Sends a string message to the server
     *
     * @param str the message to send
     */
    public void sendMessage(String str) {
        try {
            dos.flush();
            dos.writeBytes(str + "\n");
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
