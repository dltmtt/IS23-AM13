package it.polimi.ingsw.server;

import it.polimi.ingsw.model.game.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private final List<Player> players = new ArrayList<>();
    private final Map<Integer, Room> rooms = new HashMap<>();
    private final int ID_Room;
    private int numPlayers;

    public Room(int numPlayers, int ID_Room) {
        this.numPlayers = numPlayers;
        this.ID_Room = ID_Room;
    }

    public int getNumberOfPlayers() {
        return numPlayers;
    }

    public boolean full() {
        return players.size() >= 2;
    }

    public int getID_Room() {
        return ID_Room;
    }

    public void addPlayer(Player newPlayer) {
        if (players.size() < 2) {
            players.add(newPlayer);
            numPlayers++;
        }

    }

    public List getListOfPlayers() {
        return players;
    }
}
