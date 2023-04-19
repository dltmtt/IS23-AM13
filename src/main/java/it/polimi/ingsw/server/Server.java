package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    public static int minNumPlayers = 2;
    public static int maxNumPlayers = 4;
    public static int port = 1234;
    public static int rmi_port = 1234;
    ServerSocket serverSocket;
    RMIServer rmiServer;

    public Server() throws IOException {
        HashMap<Player, Room> turns = new HashMap<>();
        ArrayList<Room> rooms = new ArrayList<>();
        serverSocket = new ServerSocket();
        rmiServer = new RMIServer();
    }

    public static void main(String[] args) {
        int len = args.length;
        if (len >= 2) {
            try {
                port = Integer.parseInt(args[0]);
                rmi_port = Integer.parseInt(args[1]);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(-1);
            }
        }
    }

    public void run(int port, int rmi_port) {
        //fa partire entrambi
    }

    public void runRMIServer(int rmi_port) {
        //
    }

    public void runSocketServer(int rmi_port) {
        //
    }


}
