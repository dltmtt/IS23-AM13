package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.layouts.Diagonal;
import it.polimi.ingsw.server.model.layouts.Layout;
import it.polimi.ingsw.server.model.layouts.Stair;
import it.polimi.ingsw.utils.BookshelfUtilities;
import it.polimi.ingsw.utils.Color;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void rearrange() {
        Player player = new Player("test", true, true, true);
        List<Item> coordinates = new ArrayList<>();
        coordinates.add(new Item(Color.BLUE, 1));
        coordinates.add(new Item(Color.BLUE, 2));
        System.out.println(coordinates);
        List<Integer> sort = new ArrayList<>();
        sort.add(1);
        sort.add(0);
        player.rearrangePickedItems(coordinates, sort);
        System.out.println(coordinates);
    }

    @Test
    void getCommonGoalNames() {
        List<Boolean> commons = new ArrayList<>();
        List<CommonGoal> commonGoal = new ArrayList<>();
        commons.add(true);
        commons.add(false);

        Player player = new Player("test", true, true, true, commons);
        Layout s = new Diagonal(1, 3, 2);
        Layout s2 = new Stair(1, 6, Bookshelf.getColumns());
        commonGoal.add(new CommonGoal(s, 2));
        commonGoal.add(new CommonGoal(s2, 2));
        Player.setCommonGoal(commonGoal);

        assertEquals("diagonal", player.getCommonNames().get(0));
        assertEquals("stair", player.getCommonNames().get(1));
    }

    @Test
    void setPlayer() {
        Player p = new Player("Ciao", true, true, true);
        p.setIsFirstPlayer(true);
        assertTrue(p.isFirstPlayer());
        assertEquals("Ciao", p.getUsername());
        p.setHasEndGameCard(true);
    }

    @Test
    void set_get_points() throws IOException, ParseException {
        Player p = new Player("Ciao", true, true, true);
        List<CommonGoal> commonGoals = new ArrayList<>();
        commonGoals.add(new CommonGoal(new Diagonal(1, 3, 2), 2));
        commonGoals.add(new CommonGoal(new Stair(1, 6, Bookshelf.getColumns()), 2));

        Player.setCommonGoal(commonGoals);
        List<Integer> points = new ArrayList<>();
        points.add(4);
        points.add(8);
        p.setCommonGoalPoints(points);
        assertEquals(12, p.getCommonGoalPoints());
        p.setPersonalGoal(SettingLoader.loadSpecificPersonalGoal(1));
        Bookshelf b = new Bookshelf(6, 5);
        p.setBookshelf(b);
        assertEquals(0, p.getPersonalGoalPoints());
        assertEquals(13, p.calculateScore());
        assert (p.getBookshelf() != null);
        assert (p.getCommonGoalScoreList().get(0) == 4);
    }

    @Test
    void commonGoalCompleted() {
        Player p = new Player("Ciao", true, true, false);
        List<CommonGoal> commonGoals = new ArrayList<>();
        commonGoals.add(new CommonGoal(new Diagonal(1, 1, 5), 2));
        Player.setCommonGoal(commonGoals);
        Bookshelf b = new Bookshelf(6, 5);
        p.setBookshelf(b);
        BookshelfUtilities.createSingleRightDiagonal(b, 0, 0, 5);
        List<Item> items = new ArrayList<>();
        items.add(new Item(Color.BLUE, 1));
        p.move(items, 0);
        assert (p.getCommonGoalCompleted().get(0));
    }

    @Test
    void checkExceptions() {
        Player p = new Player("Ciao", true, true, false);
        List<Integer> sort = new ArrayList<>();
        sort.add(1);
        sort.add(0);

        List<Item> items = new ArrayList<>();
        items.add(new Item(Color.BLUE, 1));

        assertThrows(IllegalArgumentException.class, () -> p.rearrangePickedItems(items, sort));
        sort.clear();
        sort.add(2);

        assertThrows(IndexOutOfBoundsException.class, () -> p.rearrangePickedItems(items, sort));
    }
}
