package it.polimi.ingsw.client;

import it.polimi.ingsw.commons.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This class defines the methods that a client must implement independently of the
 * communication protocol used. These methods are implemented by the
 * {@link Client} class, which is extended by the classes that implement
 * the communication protocols.
 */
public interface ClientCommunicationInterface extends Remote {

    /**
     * This method parses the received message and calls the appropriate method.
     *
     * @param message the message to parse.
     * @throws RemoteException if the connection is lost.
     */
    void callBackSendMessage(Message message) throws RemoteException;

    /**
     * This method gets the Username.
     * @return the username.
     * @throws RemoteException if the connection is lost.
     */
    String getUsername() throws RemoteException;

    /**
     * This method sets the username.
     * @param username the username to set.
     * @throws RemoteException if the connection is lost.
     */
    void setUsername(String username) throws RemoteException;
}
