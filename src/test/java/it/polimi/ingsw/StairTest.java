package it.polimi.ingsw;

import it.polimi.ingsw.Models.CommonGoalLayout.Layout;
import it.polimi.ingsw.Models.CommonGoalLayout.Stair;
import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Item.Item;
import it.polimi.ingsw.TestUtility.BookshelfUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StairTest extends BookshelfUtilities {
    Bookshelf b;
    Layout stair;

    @BeforeEach
    void setup() {
        b = new Bookshelf();
        stair = new Stair(b.getColumns());
    }

    @Test
    void StairCheckRowZero() {
        createLeftStair(b);
        assertTrue(stair.check(b));
        b = new Bookshelf();
        createRightStair(b);
//        b.print();
        assertTrue(stair.check(b));
    }

    @Test
    void StairCheckRowOne() {
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < b.getColumns(); i++) {
            itemList.clear();
            itemList.add(new Item(Color.getRandomColor(), 0));
            b.insert(i, itemList);
        }
        createLeftStair(b);
        b.cli_print();
        assertTrue(stair.check(b));
        b = new Bookshelf();
        for (int i = 0; i < b.getColumns(); i++) {
            itemList.clear();
            itemList.add(new Item(Color.getRandomColor(), 0));
            b.insert(i, itemList);
        }
        createRightStair(b);
        assertTrue(stair.check(b));
    }
}
