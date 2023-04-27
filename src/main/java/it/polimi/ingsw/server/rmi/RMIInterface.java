package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utils.AnswerMessage;
import it.polimi.ingsw.utils.RequestMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

//this interface is used to send all the data of the items from the client
public interface RMIInterface extends Remote {

    //inserisco un nuovo client
    void insertNewClient(Player player) throws RemoteException;

    int getAge(Player player) throws RemoteException;

    String getUsername(Player player) throws RemoteException;

    //azzero il gioco prima di iniziarne un altro
    void reset() throws RemoteException;

    void sendRequestMessage(RequestMessage requestMessage) throws RemoteException;

    void sendAnswerMessage(AnswerMessage answerMessage) throws RemoteException;

    void ping(String username) throws RemoteException;

    boolean pong() throws RemoteException;

    void receiveMessage(RequestMessage message) throws RemoteException;

}
