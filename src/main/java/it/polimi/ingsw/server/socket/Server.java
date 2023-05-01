/*
package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.server.model.Room;
import it.polimi.ingsw.server.model.Player;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Map;

public class Server {
    public static int minNumPlayers = 2;
    public static int maxNumPlayers = 4;
    public static int port;
    ServerSocket serverSocket;
    private Map<String, Player> players;

    public Server() throws IOException {
        //  Map<Player, Room> turns = new HashMap<>();
        ArrayList<Room> rooms = new ArrayList<>();  //list of the rooms created
        serverSocket = new ServerSocket();
    }

    public static void main(String[] args) {
        int len = args.length;
        if (len >= 2) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(-1);
            }
        } else {
            port = 1234;
        }
    }

    public void login(String username, Player player) throws LoginException {
        if (username.length() > 0 && !players.containsKey(username)) {
            //setto il nome del giocatore a username
            System.out.println("Player" + username + "connected");
        } else if (username.length() == 0) {
            System.out.println("The username is not valid");
        } else {
            System.out.println("The player" + username + "has already logged in");
            throw new LoginException();
        }
    }

    //this method returns the player with username "name"
    public Player getPlayer(String name) {
        return players.get(name);
    }
}
*/
