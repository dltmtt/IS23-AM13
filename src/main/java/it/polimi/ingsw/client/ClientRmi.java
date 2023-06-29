package it.polimi.ingsw.client;

import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.ServerCommunicationInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static it.polimi.ingsw.client.MyShelfie.HOSTNAME;
import static it.polimi.ingsw.server.ServerCommunicationInterface.PORT_RMI;

/**
 * This class is the RMI implementation of the client.
 * It extends the abstract class Client and implements the ClientCommunicationInterface.
 */
public class ClientRmi extends Client implements ClientCommunicationInterface {

    private Registry registry;
    private ServerCommunicationInterface server;

    /**
     * Starts the client.
     */
    public ClientRmi() throws RemoteException {
        super();
    }

    /**
     * Used to send a message.
     * @param message the message to send.
     */
    @Override
    public void sendMessage(Message message) {
        try {
            server.receiveMessage(message, this);
        } catch (RemoteException e) {
            // Don't do anything: if the server is down, the client will
            // notice itself and will exit.
        }
    }

    /**
     * Used to connect to the server.
     * @throws RemoteException if the connection fails.
     * @throws NotBoundException if the connection fails.
     */
    @Override
    public void connect() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(HOSTNAME, PORT_RMI);
        server = (ServerCommunicationInterface) registry.lookup("ServerCommunicationInterface");
    }
}
