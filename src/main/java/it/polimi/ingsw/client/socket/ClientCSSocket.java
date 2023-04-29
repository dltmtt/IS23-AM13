/*
package it.polimi.ingsw.client.socket;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// Client class
public class ClientCSSocket {

    private final Socket socket;
    private final PrintWriter out;

    // reading from server
    private final BufferedReader in;

    // object of scanner class
    private final Scanner sc;
    private String line = null;


    public ClientCSSocket(String ip, int port) throws RuntimeException {
        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sc = new Scanner(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // driver code
    //public static void main(String[] args) {
    //    ClientCSSocket client = new ClientCSSocket();
    //client.run();
    //}

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }

    public void sendJsonObjet(JSONObject json) {
        out.println(json.toJSONString());
        out.flush();
    }

    public void sendInput() {
        while (!"exit".equalsIgnoreCase(line)) {
            // reading from user
            line = sc.nextLine();

            // sending the user input to server
            out.println(line);
            out.flush();
            // displaying server reply
            try {
                System.out.println("Server replied " + in.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
*/