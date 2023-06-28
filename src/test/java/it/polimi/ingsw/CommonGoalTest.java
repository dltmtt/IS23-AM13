package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.layouts.Diagonal;
import it.polimi.ingsw.server.model.layouts.Layout;
import it.polimi.ingsw.server.model.layouts.XShape;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonGoalTest {

    @Test
    void getScoringListCheck() {
        Layout s = new Diagonal(1, 3, 2);

        CommonGoal c2 = new CommonGoal(s, 2);
        assertEquals(4, c2.getScoringList().get(1));
        assertEquals(8, c2.getScoringList().get(0));

        CommonGoal c3 = new CommonGoal(s, 3);
        assertEquals(4, c3.getScoringList().get(2));
        assertEquals(6, c3.getScoringList().get(1));
        assertEquals(8, c3.getScoringList().get(0));

        CommonGoal c4 = new CommonGoal(s, 4);
        assertEquals(2, c4.getScoringList().get(3));
        assertEquals(4, c4.getScoringList().get(2));
        assertEquals(6, c4.getScoringList().get(1));
        assertEquals(8, c4.getScoringList().get(0));
    }

    @Test
    void getScoringCheck() throws IndexOutOfBoundsException {
        Layout s = new Diagonal(1, 3, 2);

        CommonGoal c4 = new CommonGoal(s, 4);
        assertEquals(8, c4.getScoring());
        assertEquals(6, c4.getScoring());
        assertEquals(4, c4.getScoring());
        assertEquals(2, c4.getScoring());
    }

    @Test
    void commonGoalWithMove() {
        SettingLoader.loadBookshelfSettings();
        Layout s = new XShape(1, 1, 5);
        Bookshelf b = new Bookshelf();
        List<Item> items = new ArrayList<>();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(0, items);

        items.clear();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        b.insert(1, items);

        items.clear();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));

        Player p = new Player("test", 0, false, true, false);
        p.setBookshelf(b);

        List<CommonGoal> commonGoals = new ArrayList<>();
        commonGoals.add(new CommonGoal(s, 2));
        Player.setCommonGoal(commonGoals);
        p.move(items, 3);
        p.move(items, 2);
    }
}
