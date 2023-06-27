package it.polimi.ingsw.client;

import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.ServerCommunicationInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static it.polimi.ingsw.client.MyShelfie.HOSTNAME;
import static it.polimi.ingsw.server.ServerCommunicationInterface.PORT_RMI;

public class ClientRmi extends Client implements ClientCommunicationInterface {

    private Registry registry;
    private ServerCommunicationInterface server;

    /**
     * Starts the client.
     */
    public ClientRmi() throws RemoteException {
        super();
    }

    @Override
    public void sendMessage(Message message) {
        try {
            server.receiveMessage(message, this);
        } catch (RemoteException e) {
            // Don't do anything: if the server is down, the client will
            // notice itself and will exit.
        }
    }

    @Override
    public void connect() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(HOSTNAME, PORT_RMI);
        server = (ServerCommunicationInterface) registry.lookup("ServerCommunicationInterface");
    }

    // @Override
    // public void checkServerConnection() {
    //     new Thread(() -> {
    //         while (true) {
    //             try {
    //                 Thread.sleep(30000);
    //             } catch (InterruptedException e) {
    //                 e.printStackTrace();
    //             }
    //             if (!serverConnection) {
    //                 System.err.println("Server not responding. Please wait.");
    //                 try {
    //                     Thread.sleep(5000);
    //                     if (!serverConnection) {
    //                         System.err.println("Lost connection to server.");
    //                         stop();
    //                     }
    //                 } catch (InterruptedException e) {
    //                     throw new RuntimeException(e);
    //                 }
    //             }
    //             System.out.println("Server is responding.");
    //             serverConnection = false;
    //         }
    //     }).start();
    // }
}
