package it.polimi.ingsw;

import it.polimi.ingsw.Controllers.Game;
import it.polimi.ingsw.Models.CommonGoalLayout.Corners;
import it.polimi.ingsw.Models.CommonGoalLayout.Diagonal;
import it.polimi.ingsw.Models.Game.Board;
import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Game.Player;
import it.polimi.ingsw.Models.Goal.CommonGoal;
import it.polimi.ingsw.Models.Goal.PersonalGoal;
import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Item.Item;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetPointsTest {
    @Test
    void generalTest() throws IllegalAccessException {
        Player player1 = new Player("test1", 20, true, true, true, new Bookshelf(), new Board(3));
        Player player2 = new Player("test2", 20, true, true, true, new Bookshelf(), new Board(3));
        Player player3 = new Player("test3", 20, true, true, true, new Bookshelf(), new Board(3));
        Game game = new Game(3);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        List<CommonGoal> commonGoals = new ArrayList<>();
        commonGoals.add(new CommonGoal(new Corners(1, 1)));
        commonGoals.add(new CommonGoal(new Diagonal(5, 1, 1)));
        Player.setCommonGoal(commonGoals);
        player1.setPersonalGoal(new PersonalGoal(1));
        player2.setPersonalGoal(new PersonalGoal(2));
        player3.setPersonalGoal(new PersonalGoal(3));
        List<Item> items = new ArrayList<>();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.BLUE, 1));
        player1.getBookshelf().insert(0, items);
        items.clear();
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.GREEN, 1));
        player1.getBookshelf().insert(1, items);
        items.clear();
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.WHITE, 1));
        player1.getBookshelf().insert(2, items);
        items.clear();
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.WHITE, 1));
        player1.getBookshelf().insert(3, items);
        items.clear();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        player1.getBookshelf().insert(4, items);
        int score1 = player1.calculateScore();
        player1.getBookshelf().cli_print();
        assertEquals(30, score1);


    }
}