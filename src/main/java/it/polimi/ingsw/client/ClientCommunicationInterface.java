package it.polimi.ingsw.client;

import it.polimi.ingsw.commons.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCommunicationInterface extends Remote {

    void callBackSendMessage(Message message) throws RemoteException;
}
