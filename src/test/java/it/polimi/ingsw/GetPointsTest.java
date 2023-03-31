package it.polimi.ingsw;

import it.polimi.ingsw.Controllers.Game;
import it.polimi.ingsw.Models.CommonGoalLayout.Corners;
import it.polimi.ingsw.Models.CommonGoalLayout.Diagonal;
import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Game.Player;
import it.polimi.ingsw.Models.Goal.CommonGoal;
import it.polimi.ingsw.Models.Goal.PersonalGoal;
import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Item.Item;
import it.polimi.ingsw.Models.Utility.Coordinates;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetPointsTest {

    //player1 is the first to reach both common goals and 5/6 items of the personal goal
    //so he should get 9 points from the personal goal and 16 points from the common goals
    //plus 3 points for one group of 4 adjacent items, for a total of 28 points
    @Test
    void generalTest() throws IllegalAccessException {

        Game game = new Game(3);
        Player player1 = new Player("test1", 20, true, true, false, new Bookshelf(), game.getLivingRoom());
        Player player2 = new Player("test2", 20, true, true, false, new Bookshelf(), game.getLivingRoom());
        Player player3 = new Player("test3", 20, true, true, false, new Bookshelf(), game.getLivingRoom());
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.initialize(3);

        List<CommonGoal> commonGoals = new ArrayList<>();
        commonGoals.add(new CommonGoal(new Corners(1, 1)));
        commonGoals.add(new CommonGoal(new Diagonal(5, 1, 1)));
        commonGoals.get(0).setScoringList(3);
        commonGoals.get(1).setScoringList(3);
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
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.BLUE, 1));
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
        items.clear();

        player1.move(new Coordinates(2, 2), new Coordinates(2, 3), 2);

        int score1 = player1.calculateScore();

        assertEquals(28, score1);

    }
}