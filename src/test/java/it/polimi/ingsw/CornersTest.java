package it.polimi.ingsw;

import it.polimi.ingsw.Models.CommonGoalLayout.Corners;
import it.polimi.ingsw.Models.CommonGoalLayout.Layout;
import it.polimi.ingsw.Models.Games.Bookshelf;
import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Item.Item;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CornersTest {
    void createCorner(Bookshelf b) {
        List<Item> soloItem = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        soloItem.add(new Item(Color.randomColor(), 0));
        b.insert(0, soloItem);
        b.insert(4, soloItem);
        for (int i = 0; i < 4; i++) {
            items.add(new Item(Color.randomColor(), 4));
        }
        b.insert(0, items);
        b.insert(4, items);
        b.insert(0, soloItem);
        b.insert(4, soloItem);
    }

    void createFakeCorner(Bookshelf b, int choose) {
        switch (choose) {
            case 0 -> EmptyBookshelf();
            case 1 -> OneDifferent(b);
            case 2 -> LastDifferent(b);
        }
    }

    void EmptyBookshelf() {
    }

    void OneDifferent(Bookshelf b) {
        List<Item> soloItemEqual = new ArrayList<>();
        List<Item> soloItemDiff = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        soloItemDiff.add(new Item(Color.randomColor(), 4));
        soloItemEqual.add(new Item(Color.randomColor(), 3));
        while (soloItemDiff.get(0).equals(soloItemEqual.get(0))) {
            soloItemDiff.remove(0);
            soloItemDiff.add(new Item(Color.randomColor(), 4));
        }
        b.insert(0, soloItemEqual);
        b.insert(4, soloItemDiff);
        for (int i = 0; i < 4; i++) {
            items.add(new Item(Color.randomColor(), 3));
        }
        b.insert(0, items);
        b.insert(4, items);
        b.insert(0, soloItemEqual);
        b.insert(4, soloItemEqual);
    }

    void LastDifferent(Bookshelf b) {
        List<Item> soloItemEqual = new ArrayList<>();
        List<Item> soloItemDiff = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        soloItemDiff.add(new Item(Color.randomColor(), 4));
        soloItemEqual.add(new Item(Color.randomColor(), 3));
        while (soloItemDiff.get(0).equals(soloItemEqual.get(0))) {
            soloItemDiff.remove(0);
            soloItemDiff.add(new Item(Color.randomColor(), 4));
        }
        b.insert(0, soloItemEqual);
        b.insert(4, soloItemEqual);
        for (int i = 0; i < 4; i++) {
            items.add(new Item(Color.randomColor(), 3));
        }
        b.insert(0, items);
        b.insert(4, items);
        b.insert(0, soloItemEqual);
        b.insert(4, soloItemDiff);
        b.cli_print();
    }

    @Test
    void cornerCheck() {
        Bookshelf b = new Bookshelf();
        Layout layout = new Corners(1, 1);
        createCorner(b);
        assertTrue(layout.check(b));
    }

    @Test
    void checkFakeCorner() {
        Bookshelf b = new Bookshelf();
        Layout layout = new Corners(1, 1);
        createFakeCorner(b, 0);
        assertFalse(layout.check(b));
        Bookshelf b1 = new Bookshelf();
        createFakeCorner(b, 1);
        assertFalse(layout.check(b1));
        Bookshelf b2 = new Bookshelf();
        createFakeCorner(b2, 2);
        assertFalse(layout.check(b2));
    }
}
