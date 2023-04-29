package it.polimi.ingsw.server.rmi;


import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utils.AnswerMessage;
import it.polimi.ingsw.utils.Coordinates;
import it.polimi.ingsw.utils.RequestMessage;

import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIServer implements RMIInterface {
    static int port = 50573;
    List<Player> players = new ArrayList<>();

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

    public void login(String username, int age, Boolean FirstGame, boolean isFirstPlayer) throws RemoteException {
        //check if the username and the password are correct
        //if they are correct, call the method insertNewClient
        if (players.stream().map(Player::getNickname).noneMatch(username::equals)) {
            players.add(new Player(username, age, FirstGame, false, false));
            System.out.println("login successful");
        } else
            System.out.println("login failed");
    }

    public void move(List<Coordinates> picked) throws RemoteException {
        //check if the username and the password are correct
        //if they are correct, call the method insertNewClient

    }

    @Override
    public void sendRequestMessage(RequestMessage requestMessage) throws RemoteException {

    }

    @Override
    public void sendAnswerMessage(AnswerMessage answerMessage) throws RemoteException {

    }

    @Override
    public void ping(String username) throws RemoteException {
        System.out.println(username + "is logging...");
        pong();
    }

    @Override
    public boolean pong() throws RemoteException {
        return true;
    }

    @Override
    public void receiveMessage(RequestMessage message) throws RemoteException {
        analyzeMessage(message);
    }

    public void analyzeMessage(RequestMessage message) throws RemoteException {
        //analyze the message and call the right method
        String type = message.getType();
        if (type.equals("login")) {
            login(message.getUsername(), message.getAge(), message.getIsFirstGame(), message.getIsFirstPlayer());
        } else if (type.equals("move")) {
            move(message.getMove());
        }

    }


}
