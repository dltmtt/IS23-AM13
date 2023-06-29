package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A room is a collection of players that are playing together.
 * It has a minimum and a maximum number of players.
 */
public class Room {

    private static int maxPlayers;
    private final List<Player> players = new ArrayList<>();
    private final int roomId;

    /**
     * Creates a new room with the given ID.
     *
     * @param roomId the ID of the room
     */
    public Room(int roomId) {
        this.roomId = roomId;
    }

    /**
     * Creates a new room with the given ID and players.
     *
     * @param roomId  the ID of the room
     * @param players the list of players
     */
    public Room(int roomId, List<Player> players) {
        this.roomId = roomId;
        this.players.addAll(players);
        maxPlayers = players.size();
    }

    /**
     * Sets the minimum and maximum number of players.
     *
     * @param maxPlayers the maximum number of players
     */
    public void setNumberOfPlayers(int maxPlayers) {
        Room.maxPlayers = maxPlayers;
    }

    /**
     * Tells whether the room is full or not.
     *
     * @return true if the room is full, false otherwise
     */
    public boolean full() {
        return players.size() == maxPlayers;
    }

    /**
     * Tells whether the maximum number of players is set or not.
     *
     * @return true if the maximum number of players is set, false otherwise
     */
    public boolean isMaxPlayersSet() {
        return maxPlayers != 0;
    }

    /**
     * Tells whether the room has too many players or not.
     *
     * @return true if the room has too many players, false otherwise
     */
    public boolean tooManyPlayers() {
        System.out.println(players.size() + " " + maxPlayers);
        return players.size() > maxPlayers;
    }

    /**
     * Adds a player to the room.
     *
     * @param newPlayer the player to be added
     */
    public void addPlayer(Player newPlayer) {
        players.add(newPlayer);
    }

    /**
     * Starts a new game.
     *
     * @return the game model
     */
    public GameModel startGame() {
        return new GameModel(players);
    }
}
