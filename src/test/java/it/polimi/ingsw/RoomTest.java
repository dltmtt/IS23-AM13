package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Room;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class RoomTest {

    @Test
    public void isFull() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("pippo", 0, false, false, false));
        players.add(new Player("pluto", 0, false, false, false));
        Room newRoom = new Room(1);
        newRoom.setNumberOfPlayers(2);
        newRoom.addPlayer(players.get(0));
        newRoom.addPlayer(players.get(1));

        assert (newRoom.full());
    }

    @Test
    void isMaxPlayersSet() {
        Room newRoom = new Room(1);
        newRoom.setNumberOfPlayers(2);

        assert (newRoom.isMaxPlayersSet());
    }

    @Test
    void startGame() {
        Room newRoom = new Room(1);
        newRoom.setNumberOfPlayers(2);
        List<Player> players = new ArrayList<>();
        players.add(new Player("pippo", 0, false, false, false));
        players.add(new Player("pluto", 0, false, false, false));
        newRoom.addPlayer(players.get(0));
        newRoom.addPlayer(players.get(1));

        assert (newRoom.startGame() != null);
    }

    @Test
    void tooManyPlayers() {
        Room newRoom = new Room(1);
        newRoom.setNumberOfPlayers(2);
        List<Player> players = new ArrayList<>();
        players.add(new Player("pippo", 0, false, false, false));
        players.add(new Player("pluto", 0, false, false, false));
        players.add(new Player("paperino", 0, false, false, false));
        newRoom.addPlayer(players.get(0));
        newRoom.addPlayer(players.get(1));
        newRoom.addPlayer(players.get(2));

        assert (newRoom.tooManyPlayers());
    }

    @Test
    void constructor2() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("pippo", 0, false, false, false));
        players.add(new Player("pluto", 0, false, false, false));
        Room newRoom = new Room(1, players);


        assert (newRoom.full());
    }
}
