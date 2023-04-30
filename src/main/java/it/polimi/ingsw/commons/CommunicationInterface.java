package it.polimi.ingsw.commons;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Here go the methods that the client can call on the server.
 *
 * @see CommunicationImplementation
 */
public interface CommunicationInterface extends Remote {
    String sendMessage(String clientMessage) throws RemoteException;
}
