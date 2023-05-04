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
    void start() throws RemoteException, MalformedURLException;

    void stop() throws RemoteException, NotBoundException;
}
