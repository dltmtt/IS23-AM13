package it.polimi.ingsw.client;

import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.CommunicationInterface;
import it.polimi.ingsw.utils.FullRoomException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static it.polimi.ingsw.server.CommunicationInterface.HOSTNAME;
import static it.polimi.ingsw.server.CommunicationInterface.PORT_RMI;

public class ClientRmi extends Client {

    private Registry registry;
    private CommunicationInterface server;

    /**
     * Starts the client
     */
    public ClientRmi() {
        super();
    }

    @Override
    public Message sendMessage(Message message) {
        Message response;
        try {
            response = server.sendMessage(message);
        } catch (IOException | FullRoomException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public void sendMe() {
        server.sendClient(this);
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void connect() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(HOSTNAME, PORT_RMI);
        server = (CommunicationInterface) registry.lookup("CommunicationInterface");
    }
}
