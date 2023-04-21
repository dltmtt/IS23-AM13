package it.polimi.ingsw.client;

import it.polimi.ingsw.server.RMIInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    static int port = 50573;

    private Client() {
        super();
    }

    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
            RMIInterface stub = (RMIInterface) registry.lookup("loggable");

            //invocazione dei metodi
            // stub.reset();

            System.out.println("ciao");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}

