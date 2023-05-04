package it.polimi.ingsw.server;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerRmi implements CommunicationInterface {
    private Registry registry;
    private CommunicationInterface server;
    private CommunicationInterface stub;

    public ServerRmi() throws RemoteException {
        super();
    }

    public void start() throws RemoteException, MalformedURLException {
        server = new ServerRmi();
        stub = (CommunicationInterface) UnicastRemoteObject.exportObject(server, 0);

        registry = LocateRegistry.createRegistry(PORT_RMI);
        registry.rebind("CommunicationInterface", stub);

        System.out.println("Server RMI started on port " + PORT_RMI + ".");
    }

    public void stop() throws RemoteException, NotBoundException {
        registry.unbind("CommunicationInterface");
        UnicastRemoteObject.unexportObject(server, true);
        System.out.println("RMI server stopped.");
    }
}
