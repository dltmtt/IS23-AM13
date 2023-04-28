package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.rmi.RMIInterface;
import it.polimi.ingsw.utils.FullRoomException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    static Player p;
    static int port = 50573;

    private RMIClient() {
        super();
    }

    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
            //invocation of the stub
            RMIInterface stub = (RMIInterface) registry.lookup("server");
            boolean pong = false;
            //invoca i metodi
            stub.ping("Valeria");
            while (!pong) {
                pong = stub.pong();
            }

            stub.login("Valeria", 22, true);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (FullRoomException e) {
            throw new RuntimeException(e);
        }
    }
}

