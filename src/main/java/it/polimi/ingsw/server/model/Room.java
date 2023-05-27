package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utils.FullRoomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A room is a collection of players that are playing together.
 * It has a minimum and a maximum number of players.
 */
public class Room {

    private static int minPlayers;
    private static int maxPlayers;
    private final List<Player> players = new ArrayList<>();
    private final Map<Integer, Room> rooms = new HashMap<>();
    private final int roomId;

    /**
     * Creates a new room with the given id.
     *
     * @param roomId the id of the room
     */
    public Room(int roomId) {
        this.roomId = roomId;
    }

    /**
     * Creates a new room with the given id and players.
     *
     * @param roomId  the id of the room
     * @param players the list of players
     */
    public Room(int roomId, List<Player> players) {
        this.roomId = roomId;
        this.players.addAll(players);
    }

    /**
     * @return the number of players
     */
    public int getNumberOfPlayers() {
        return players.size();
    }

    /**
     * Sets the minimum and maximum number of players.
     *
     * @param maxPlayers the maximum number of players
     */
    public void setNumberOfPlayers(int maxPlayers) {
        Room.maxPlayers = maxPlayers;
        Room.minPlayers = 2;
    }

    /**
     * @return true if the room is full, false otherwise
     */
    public boolean full() {
        return players.size() == maxPlayers;
    }

    public boolean isNumSetted() {
        return maxPlayers != 0;
    }

    public boolean tooMuchPlayers() {
        System.out.println(players.size() + " " + maxPlayers);
        return players.size() > maxPlayers;
    }

    /**
     * @return the id of the room
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * This method adds a player to the room.
     *
     * @param newPlayer the player to be added
     * @throws FullRoomException if the room is full
     */
    public void addPlayer(Player newPlayer) throws FullRoomException {
        //        if (players.size() < maxPlayers) {
        //            players.add(newPlayer);
        //        } else {
        //            String errorMessage = "The selected room (" + roomId + ") is full, the following users are connected:  " + players;
        //            throw new FullRoomException(errorMessage);
        //        }
        players.add(newPlayer);
    }

    /**
     * @return the list of players
     */
    public List<Player> getListOfPlayers() {
        return players;
    }

    /**
     * This method starts a new game.
     *
     * @return the game model
     */
    public GameModel startGame() {
        return new GameModel(players);
    }
}
