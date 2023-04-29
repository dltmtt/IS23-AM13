package it.polimi.ingsw.client.socket;

// Client2 class that
// sends data and receives also

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public Socket s = null;
    public DataOutputStream dos;
    public BufferedReader br, kb;

    public Client() {
        // Create client socket
        try {
            s = new Socket("localhost", 888);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // to send data to the server
        try {
            dos = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // to read data coming from the server
        try {
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        kb = new BufferedReader(new InputStreamReader(System.in));
        // to read data from the keyboard

        new Thread(() ->
                // receive from the server
                //qui ascolta
        {
            try {
                System.out.println(br.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        ).start();
    }

    public void sendMessage(String str) {
        // repeat as long as exit
        // is not typed at client
        if (!(str.equals("exit"))) {
            // send to the server
            try {
                dos.writeBytes(str + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            close();
        }
    }

    public void sendInput() {
        String str;
        try {
            if (!(str = kb.readLine()).equals("exit")) {
                // send to the server
                dos.writeBytes(str + "\n");
            } else {
                close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        // close connection.
        try {
            dos.close();
            br.close();
            kb.close();
            s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
