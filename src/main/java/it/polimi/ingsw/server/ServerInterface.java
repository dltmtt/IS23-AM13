package it.polimi.ingsw.server;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Defines the methods that a server must implement independently of the
 * communication protocol used.
 * <p>
 * An interface is used instead of an abstract class so that the implementation
 * can extend {@link java.rmi.server.UnicastRemoteObject} and be exported
 * as a remote object. This is not necessary with the current implementation,
 * but it might be useful in the future.
 */
public interface ServerInterface {

    // Note: the actual implementation of the methods doesn't have
    // to throw all the exceptions listed here

    /**
     * Starts the server.
     *
     * @throws RemoteException       if a connection error occurs
     * @throws MalformedURLException if the url of the server is not valid
     */
    void start() throws RemoteException, MalformedURLException;

    /**
     * Stops the server.
     *
     * @throws RemoteException   if a connection error occurs
     * @throws NotBoundException if the server is not bound
     */
    void stop() throws RemoteException, NotBoundException;
}
