package it.polimi.ingsw.server;


import it.polimi.ingsw.server.model.Player;

import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer implements RMIInterface {
    static int port = 50573;
    /*
    instead:
    if(argc==2){
        String host = args[0];
        int port = Integer.parseInt(args[1]);
    }
     */

    public RMIServer() throws RemoteException {
        super();
    }

    public static void main(String[] args) throws RemoteException {

        Registry registry = null;
        RMIServer obj = new RMIServer();
        RMIInterface stub = null;

        try {
            stub = (RMIInterface) UnicastRemoteObject.exportObject(obj, port);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }


        try {
            //creation of the registry
            registry = LocateRegistry.createRegistry(port);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("RMI registry already exists");
        }
        if (registry == null) {
            throw new ServerException("RMI not initializable");
        }
        try {
            registry.bind("server", stub);
        } catch (Exception e) {
            throw new ServerException("error", e);
        }
        System.out.println("Server is ready");
    }

    @Override
    public String getUsername(Player player) throws RemoteException {
        return player.getNickname();
    }

    @Override
    public int getAge(Player player) throws RemoteException {
        return player.getAge();
    }

    @Override
    public void insertNewClient(Player player) throws RemoteException {
        //
    }

    @Override
    public void reset() throws RemoteException {
        //reset of the game?
    }
}
