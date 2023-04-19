package it.polimi.ingsw;

import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.layouts.Corners;
import it.polimi.ingsw.server.model.layouts.Diagonal;
import it.polimi.ingsw.server.model.layouts.FullLine;
import it.polimi.ingsw.server.model.layouts.XShape;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Coordinates;
import it.polimi.ingsw.utils.SettingLoader;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetPointsTest {
    // player1 is the first to reach both common goals and 5/6 items of the personal goal,
    // so he should get 9 points from the personal goal and 16 points from the common goals
    // plus 3 points for one group of 4 adjacent items, for a total of 28 points.
    @Test
    void generalTest() throws IllegalAccessException, IOException, ParseException {
        List<Player> players = new ArrayList<>();
        players.add(new Player("test1", 20, true, true, false));
        players.add(new Player("test2", 20, true, true, false));
        players.add(new Player("test3", 20, true, true, false));


        Game game = new Game(players);
        game.start();

        List<CommonGoal> commonGoals = new ArrayList<>();
        commonGoals.add(new CommonGoal(new Corners(1, 1), players.size()));
        commonGoals.add(new CommonGoal(new Diagonal(1, 1, 5), players.size()));
        Player.setCommonGoal(commonGoals);

        players.get(0).setPersonalGoal(SettingLoader.loadSpecificPersonalGoal(1));
        players.get(1).setPersonalGoal(SettingLoader.loadSpecificPersonalGoal(2));
        players.get(2).setPersonalGoal(SettingLoader.loadSpecificPersonalGoal(3));

        List<Item> items = new ArrayList<>();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.BLUE, 1));
        players.get(0).getBookshelf().insert(0, items);
        items.clear();

        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.GREEN, 1));
        players.get(0).getBookshelf().insert(1, items);
        items.clear();

        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.BLUE, 1));
        players.get(0).getBookshelf().insert(2, items);
        items.clear();

        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.WHITE, 1));
        players.get(0).getBookshelf().insert(3, items);
        items.clear();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        players.get(0).getBookshelf().insert(4, items);
        items.clear();

        players.get(0).move(new Coordinates(2, 2), new Coordinates(2, 3), 2);

        int score1 = players.get(0).calculateScore();

        assertEquals(28, score1);
    }

    //player1: reaches 4/6 cells of personal goal(4 points), full line common goal as first(8 points), x-shape common goal as second (4 points)  and 13 points from adjacent items.
    //Total score: 29 points.
    //player2: reaches 6/6 cells of personal goal(12 points),x-shape common goal as first (8 points)  and 7 points from adjacent items.
    //Total score: 27 points.
    @Test
    void TwoPlayerMatch_Player1Wins() throws IllegalAccessException, IOException, ParseException {

        List<Player> players = new ArrayList<>();
        players.add(new Player("Valeria", 21, false, true, false));
        players.add(new Player("Arianna", 20, false, false, false));

        Game game = new Game(players);
        game.start();

        players.get(0).setPersonalGoal(SettingLoader.loadSpecificPersonalGoal(9));
        players.get(1).setPersonalGoal(SettingLoader.loadSpecificPersonalGoal(8));

        List<CommonGoal> commonGoals = new ArrayList<>();
        commonGoals.add(new CommonGoal(new FullLine(1, 3, 3, false), players.size())); //vertical fullline
        commonGoals.add(new CommonGoal(new XShape(1, 1, 3), players.size()));
        Player.setCommonGoal(commonGoals);

        //player1 set-up
        List<Item> items = new ArrayList<>();

        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        players.get(0).getBookshelf().insert(1, items);
        items.clear();

        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.YELLOW, 1));
        players.get(0).getBookshelf().insert(3, items);
        items.clear();

        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        players.get(0).getBookshelf().insert(4, items);
        items.clear();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.PINK, 1));
        players.get(0).getBookshelf().insert(0, items);
        items.clear();

        //player1 reach common goal full line as first
        players.get(0).move(new Coordinates(1, 4), new Coordinates(1, 5), 0);

        //player2 set-up
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        players.get(1).getBookshelf().insert(0, items);
        items.clear();

        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.GREEN, 1));
        players.get(1).getBookshelf().insert(1, items);
        items.clear();

        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.WHITE, 1));
        players.get(1).getBookshelf().insert(2, items);
        items.clear();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        players.get(1).getBookshelf().insert(3, items);
        items.clear();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        players.get(1).getBookshelf().insert(4, items);
        items.clear();

        //player2 reach common goal x-shape as first
        players.get(1).move(new Coordinates(2, 1), new Coordinates(2, 2), 0);

        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        players.get(0).getBookshelf().insert(2, items);
        items.clear();

        //player1 reach common goal x-shape as second
        int i = 4;
        int j = 1;
        while (game.getLivingRoom().getItem(i, j).color() == Color.GREEN || game.getLivingRoom().getItem(i, j).color() == Color.YELLOW) {
            i++;
            j++;
        }
        players.get(0).move(new Coordinates(i, j), new Coordinates(i, j + 1), 2);

//        assertEquals(29, player1.calculateScore());
        assertEquals(27, players.get(1).calculateScore());

    }

    @Test
    void TwoPlayerMatch_tie() throws IllegalAccessException, IOException, ParseException {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Valeria", 21, false, true, false));
        players.add(new Player("Arianna", 20, false, false, false));
        Game game = new Game(players);
        game.start();

        players.get(0).setPersonalGoal(SettingLoader.loadSpecificPersonalGoal(9));
        players.get(1).setPersonalGoal(SettingLoader.loadSpecificPersonalGoal(8));


        List<CommonGoal> commonGoals = new ArrayList<>();
        commonGoals.add(new CommonGoal(new Corners(1, 1), players.size()));
        commonGoals.add(new CommonGoal(new Diagonal(1, 1, 5), players.size()));
        Player.setCommonGoal(commonGoals);

        List<Item> items = new ArrayList<>();
        //player1 set-up
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        players.get(0).getBookshelf().insert(0, items);
        items.clear();

        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.GREEN, 1));
        players.get(0).getBookshelf().insert(1, items);
        items.clear();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        players.get(0).getBookshelf().insert(2, items);
        items.clear();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        players.get(0).getBookshelf().insert(3, items);
        items.clear();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.WHITE, 1));
        players.get(0).getBookshelf().insert(4, items);
        items.clear();

        game.move(new Coordinates(3, 4), new Coordinates(3, 5), 0);
//        assertEquals(35, player1.calculateScore());

        //player2 set-up
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.GREEN, 1));
        players.get(1).getBookshelf().insert(0, items);
        items.clear();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.YELLOW, 1));
        players.get(1).getBookshelf().insert(1, items);
        items.clear();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.WHITE, 1));
        players.get(1).getBookshelf().insert(2, items);
        items.clear();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        players.get(1).getBookshelf().insert(3, items);
        items.clear();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        players.get(1).getBookshelf().insert(4, items);
        items.clear();

        game.move(new Coordinates(3, 5), new Coordinates(3, 6), 0);
//        player2.getBookshelf().cli_print();
//        assertEquals(35, player2.calculateScore());


    }
}
