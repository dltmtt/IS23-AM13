package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

//this interface is used to send all the data of the items from the client
public interface RMIServerInterface extends Remote {

    //inserisco un nuovo client
    ArrayList insertNewClient(Player player) throws RemoteException;

    //azzero il gioco prima di iniziarne un altro
    void reset() throws RemoteException;
}
