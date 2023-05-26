package it.polimi.ingsw.server;

import it.polimi.ingsw.commons.Message;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerRmi implements ServerCommunicationInterface, ServerInterface {

    private Registry registry;
    private ServerCommunicationInterface server;

    public ServerRmi() throws RemoteException {
        super();
    }

    @Override
    public void start() {
        try {
            server = new ServerRmi();
            ServerCommunicationInterface stub = (ServerCommunicationInterface) UnicastRemoteObject.exportObject(server, 0);

            registry = LocateRegistry.createRegistry(PORT_RMI);
            registry.rebind("ServerCommunicationInterface", stub);
        } catch (RemoteException e) {
            // e.printStackTrace();
            System.err.println("Unable to start the RMI server.");
        }

        System.out.println("RMI server started on port " + PORT_RMI + ".");
    }

    @Override
    public void stop() {
        try {
            registry.unbind("ServerCommunicationInterface");
            UnicastRemoteObject.unexportObject(server, true);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            System.err.println("Unable to stop the RMI server.");
        }
        System.out.println("RMI server stopped.");
    }

    @Override
    public void receiveMessageTcp(Message message, SocketClientHandler client) {
        // Not used
    }
}
