package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {
    public int port = 1234;
    public String address = "127.0.0.1";
    List<Object> clients = Collections.synchronizedList(new ArrayList<>());


    //costruttore
    public RMIServer() throws RemoteException {
        super();
    }
    //implementazione del metodo dichiarato nell'interfaccia

    public static void main(String[] args) {
        try {
            RMIServer serverRMI = new RMIServer();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(-1);
        }
    }

    @Override
    //invece di player va client, ma ancora non abbiamo la classe
    public void insertNewClient(Player player) throws RemoteException {
        clients.add(player);
    }

    @Override
    public void reset() throws RemoteException {
        //resetta il gioco
    }


}
