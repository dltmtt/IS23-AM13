package it.polimi.ingsw.server.model;

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
    private final int roomId;

    public Room(int roomId) {
        this.roomId = roomId;
    }

    public Room(int roomId, List<Player> players) {
        this.roomId = roomId;
        this.players.addAll(players);
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public void setNumberOfPlayers(int maxPlayers) {
        Room.maxPlayers = maxPlayers;
        Room.minPlayers = 2;
    }

    public boolean full() {
        return players.size() == maxPlayers;
    }

    public int getRoomId() {
        return roomId;
    }

    public void addPlayer(Player newPlayer) throws FullRoomException {
        //        if (players.size() < maxPlayers) {
        //            players.add(newPlayer);
        //        } else {
        //            String errorMessage = "The selected room (" + roomId + ") is full, the following users are connected:  " + players;
        //            throw new FullRoomException(errorMessage);
        //        }
        players.add(newPlayer);
    }

    public List<Player> getListOfPlayers() {
        return players;
    }

    public GameModel startGame() {
        return new GameModel(players);
    }
}
