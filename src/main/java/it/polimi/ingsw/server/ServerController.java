package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Room;
import it.polimi.ingsw.utils.FullRoomException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServerController {
    private final List<Player> players = new ArrayList<>();
    private GameModel gameModel = null;
    private Room room = null;

    public boolean checkUsername(String username) {
        for (Player player : players) {
            if (player.getNickname().equals(username)) {
                return false;
            }
        }
        return true;
    }

    public void addPlayerByUsername(String username) {
        players.add(new Player(username, 0, false, false, false));
    }

    public void addPlayerAge(int age) {
        players.get(players.size() - 1).setAge(age);
    }

    public void addPlayerFirstGame(boolean firstGame) throws FullRoomException {
        players.get(players.size() - 1).setFirstGame(firstGame);
    }

    public String startRoom() throws FullRoomException {
        if (room == null) {
            Random random = new Random();
            int idRoom = random.nextInt(1000);
            room = new Room(idRoom);
            players.get(players.size() - 1).setIsFirstPlayer(true);
            room.addPlayer(players.get(players.size() - 1));
            return "FirstPlayer";
        } else if (!room.full()) {
            players.get(players.size() - 1).setIsFirstPlayer(false);
            room.addPlayer(players.get(players.size() - 1));
        } else {
            //TODO: gestire l'eccezione
            throw new FullRoomException("Room is full");
        }
        return null;
    }

    public String checkRoom() {
        if (room.full()) {
            gameModel = new GameModel(players);
            gameModel.start();
            return "Game started";
        } else if (room.getNumberOfPlayers() >= 2) {
            return "first player can start the game";
        }
        return "waiting for other players to join";
    }


}
