package it.polimi.ingsw.Client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CThread extends Thread {
    private Socket socket;

    private BufferedReader in;
    private PrintWriter out;
}
