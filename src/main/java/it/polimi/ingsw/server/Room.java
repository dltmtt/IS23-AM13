package it.polimi.ingsw.server;

import it.polimi.ingsw.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utils.FullRoomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private static int minPlayers;
    private static int maxPlayers;
    private final List<Player> players = new ArrayList<>();
    private final Map<Integer, Room> rooms = new HashMap<>();
    private final int ID_Room;

    public Room(int ID_Room) {
        this.ID_Room = ID_Room;
    }

    public Room(int ID_Room, List<Player> players) {
        this.ID_Room = ID_Room;
        this.players.addAll(players);
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public boolean full() {
        return players.size() >= minPlayers;
    }

    public int getID_Room() {
        return ID_Room;
    }

    public void addPlayer(Player newPlayer) throws FullRoomException {
        if (players.size() < maxPlayers) {
            players.add(newPlayer);
        } else {
            String errorMessage = "The selected room (" + ID_Room + ") is full, the following users are connected:  " + players;
            throw new FullRoomException(errorMessage);
        }

    }

    public List<Player> getListOfPlayers() {
        return players;
    }

    public Game startGame() {
        return new Game(players);
    }
}
