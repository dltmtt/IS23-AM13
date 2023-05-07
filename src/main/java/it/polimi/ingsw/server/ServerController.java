package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utils.Coordinates;
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

    public int startRoom() throws FullRoomException {
        if (room == null) {
            Random random = new Random();
            int idRoom = random.nextInt(1000);
            room = new Room(idRoom);
            players.get(players.size() - 1).setIsFirstPlayer(true);
            room.addPlayer(players.get(players.size() - 1));
        } else if (!room.full()) {
            players.get(players.size() - 1).setIsFirstPlayer(false);
            room.addPlayer(players.get(players.size() - 1));
        } else {
            //TODO: gestire l'eccezione
            throw new FullRoomException("Room is full");
        }
        return room.getListOfPlayers().size();
    }

    public String checkRoom() throws IllegalAccessException {
        if (room.full()) {
            gameModel = new GameModel(players);
            gameModel.start();
            return "Game started";
        }
        return null;
    }

    public String checkNumPlayer(int numPlayer) {
        if (numPlayer > 4) {
            return "retry";
        }
        room.setNumberOfPlayers(numPlayer);
        return "ok";
    }

    public int getPersonalGoalCard(int index) {
        return room.getListOfPlayers().get(index - 1).getPersonalGoal().getIndex();
    }

    public List<CommonGoal> getCommonGoals() {
        return Player.getCommonGoals();
    }

    public Bookshelf getBookshelf(int index) {
        return room.getListOfPlayers().get(index - 1).getBookshelf();
    }

    public Bookshelf getCurrentePlayerBookshelf() {
        return gameModel.getCurrentPlayer().getBookshelf();
    }

    public Board getBoard() {
        return gameModel.getLivingRoom();
    }

    public boolean yourTurn(int index) {
        return gameModel.getCurrentPlayer().equals(room.getListOfPlayers().get(index - 1));
    }

    public String move(List<Integer> move) {
        return checkMove(move);
    }

    public String checkMove(List<Integer> move) {
        if (move.get(0) == move.get(2) || move.get(1) == move.get(3) || (move.get(4) >= 0 && move.get(4) <= 4)) {
            gameModel.move(new Coordinates(move.get(0), move.get(1)), new Coordinates(move.get(2), move.get(3)), move.get(4));
            return "ok";
        }
        return "no";
    }
}
