package it.polimi.ingsw;

import it.polimi.ingsw.Model.Game.Bookshelf;
import it.polimi.ingsw.Model.Items.Color;
import it.polimi.ingsw.Model.Items.Item;
import it.polimi.ingsw.Model.commonGoalLayout.Layout;
import it.polimi.ingsw.Model.commonGoalLayout.Stair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StairTest {
    void createLeftStair(Bookshelf b) {
        List<Item> itemList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            itemList.add(new Item(Color.randomColor(), 0));
        }

        b.insert(0, itemList);
        itemList.remove(0);
        b.insert(1, itemList);
        itemList.remove(0);
        b.insert(2, itemList);
        itemList.remove(0);
        b.insert(3, itemList);
        itemList.remove(0);
        b.insert(4, itemList);
        itemList.remove(0);
    }

    void createRightStair(Bookshelf b) {
        List<Item> itemList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            itemList.add(new Item(Color.randomColor(), 0));
        }

        b.insert(4, itemList);
        itemList.remove(0);
        b.insert(3, itemList);
        itemList.remove(0);
        b.insert(2, itemList);
        itemList.remove(0);
        b.insert(1, itemList);
        itemList.remove(0);
        b.insert(0, itemList);
        itemList.remove(0);
    }

    @Test
    void StairCheckRowZero() {
        Bookshelf b = new Bookshelf();
        Layout stair = new Stair(b.getColumns());
        createLeftStair(b);
        assertTrue(stair.check(b));
        b = new Bookshelf();
        createRightStair(b);
//        b.print();
        assertTrue(stair.check(b));
    }

    @Test
    void StairCheckRowOne() {
        Bookshelf b = new Bookshelf();
        Layout stair = new Stair(b.getColumns());
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < b.getColumns(); i++) {
            itemList.clear();
            itemList.add(new Item(Color.randomColor(), 0));
            b.insert(i, itemList);
        }
        createLeftStair(b);
        b.cli_print();
        assertTrue(stair.check(b));
        b = new Bookshelf();
        for (int i = 0; i < b.getColumns(); i++) {
            itemList.clear();
            itemList.add(new Item(Color.randomColor(), 0));
            b.insert(i, itemList);
        }
        createRightStair(b);
        assertTrue(stair.check(b));
    }
}
