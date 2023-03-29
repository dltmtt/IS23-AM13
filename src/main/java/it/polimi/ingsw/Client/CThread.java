package it.polimi.ingsw.Client;

import it.polimi.ingsw.Server.MultiServer;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class CThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;


    public CThread(InetAddress address) {
        try {

            socket = new Socket(address, MultiServer.port);
            System.out.println("Client n. ");

        } catch (IOException e) {
            //creation of socket not done successfully
            e.printStackTrace();
        }

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            start();
        } catch (IOException e) {
            //socket closure
            try {
                socket.close();
            } catch (IOException ex) {
                System.err.println("error");
            }

        }


    }


    public void run() {
        //write what the method does
        //...
        try {
            //client disconnecting
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
