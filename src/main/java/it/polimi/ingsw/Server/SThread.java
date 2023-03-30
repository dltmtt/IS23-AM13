package it.polimi.ingsw.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class SThread extends Thread {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedWriter bufferedWriter;

    public SThread(Socket sk) {
        try {
            socket = sk;

            // Creation of the input stream
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            // Creation of the output stream
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            out = new PrintWriter(bufferedWriter, true);
            start();

            System.out.println("Server started");
        } catch (IOException e) {
            System.err.println("error");
        }
    }

    public void run() {
        try {
            while (true) {
                String str = in.readLine();
                // if (str...)  break
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error");
        }

        // Socket closure
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

