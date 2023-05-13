package it.polimi.ingsw.server;

import it.polimi.ingsw.commons.Message;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;

public class SocketClientHandler implements Runnable {

    private final Socket socket;
    public PrintStream ps;
    public BufferedReader br, kb;

    public DataOutputStream dos;

    public Thread listenThread, sendThread;

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

        // This is to send data to the server
        try {
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Unable to create output stream");
            throw new RuntimeException(e);
        }

        listenThread = new Thread(() -> {
            String str;
            while (true) {
                try {
                    str = br.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject messageFromClient = (JSONObject) parser.parse(str);
                    Message message = new Message(messageFromClient);
                } catch (NullPointerException e) {
                    System.out.println("Client disconnected.");
                    break;
                } catch (ParseException e) {
                    System.out.println(str);
                }
            }
        });

        sendThread = new Thread(() -> {
            while (true) {
                sendInput();
            }
        });
    }

    public void run() {
        listenThread.start();
        //sendThread.start();
    }

    /*
        public void receiveMessage() {
            String str = null;
            try {
                str = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    */
    // problema secondario
    //boh raga manda un messaggio su due da tastiera
    public void sendInput() {
        String str1;
        // read from keyboard
        try {
            str1 = kb.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // send to client
        ps.println(str1);
    }

    public void sendInput_old() {
        String str = null;
        try {
            // str =
            dos.writeBytes(kb.readLine() + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) {
        ps.println(message);
    }

    public void close() {
        sendMessage("Connection closed.");
        listenThread.interrupt();
        sendThread.interrupt();
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
