package it.polimi.ingsw.server;

import it.polimi.ingsw.commons.CommunicationInterface;
import it.polimi.ingsw.server.model.GameModel;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static it.polimi.ingsw.server.Server.PORT_RMI;

public class ServerRmi implements ServerInterface {
    private final GameModel model;
    private Registry registry;
    private CommunicationInterface server;
    private CommunicationInterface stub;

    public ServerRmi(GameModel model) throws RemoteException {
        this.model = model;
    }

    @Override
    public void start() throws RemoteException {
        server = new Server();
        stub = (CommunicationInterface) UnicastRemoteObject.exportObject(server, 0);

        registry = LocateRegistry.createRegistry(PORT_RMI);
        registry.rebind("CommunicationInterface", stub);

        System.out.println("RMI server configuration completed.");
    }

    @Override
    public void stop() throws RemoteException, NotBoundException {
        registry.unbind("CommunicationInterface");
        UnicastRemoteObject.unexportObject(server, true);
        System.out.println("RMI server stopped.");
    }

//    @Override
//    public String sendMessage(String clientMessage) throws RemoteException {
//
//    }
}
