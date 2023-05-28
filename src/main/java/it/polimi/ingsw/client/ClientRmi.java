package it.polimi.ingsw.client;

import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.ServerCommunicationInterface;
import it.polimi.ingsw.utils.FullRoomException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static it.polimi.ingsw.server.ServerCommunicationInterface.HOSTNAME;
import static it.polimi.ingsw.server.ServerCommunicationInterface.PORT_RMI;

public class ClientRmi extends Client implements ClientCommunicationInterface {

    private Registry registry;
    private ServerCommunicationInterface server;

    /**
     * Starts the client
     */
    public ClientRmi() throws RemoteException {
        super();
    }

    @Override
    public void sendMessage(Message message) {
        try {
            server.receiveMessage(message, this);
        } catch (FullRoomException | Exception e) {
            // throw new RuntimeException(e);
        }
    }

    public void checkServerConnection() {
        if (!serverConnected) {
            System.err.println("\nServer disconnected.");
            System.exit(0);
        }
        serverConnected = false;
    }

    @Override
    public void connect() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(HOSTNAME, PORT_RMI);
        server = (ServerCommunicationInterface) registry.lookup("ServerCommunicationInterface");
    }
}
