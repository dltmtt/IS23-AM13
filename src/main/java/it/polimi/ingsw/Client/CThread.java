package it.polimi.ingsw.Client;

import java.io.*;
import java.net.Socket;

public class CThread extends Thread {
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private final int max = 4;
    private String str;

    //new code
    public CThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                str = in.readLine();
                if (str == null) {
                    socket.close();
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }


    }









    /*
    public CThread(InetAddress address) {
        try {
            socket = new Socket(address, MultiServer.port);
           // System.out.println("Client n. ");
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
    //hostname is null for the loopback address
    public void connect(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);
    }

    /*
    public void run() {
        // Write what the method does
        //...
        try {
            // Client disconnecting
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     */
}

