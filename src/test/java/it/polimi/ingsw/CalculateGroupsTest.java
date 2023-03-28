package it.polimi.ingsw;

import it.polimi.ingsw.Model.Game.Bookshelf;
import it.polimi.ingsw.Model.Items.Color;
import it.polimi.ingsw.Model.Items.Item;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculateGroupsTest {
    /**
     * 1 group
     */
    @Test
    void calculateBlueGroups() {
        Bookshelf b = new Bookshelf();
        List<Item> items = new ArrayList<>();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(0, items);
        List<Item> items1 = new ArrayList<>();
        items1.add(new Item(Color.YELLOW, 1));
        items1.add(new Item(Color.BLUE, 1));
        items1.add(new Item(Color.BLUE, 1));
        b.insert(1, items1);


        assertEquals(b.getPoints(), 3);
    }

    /**
     * 2 groups different color
     */
    @Test
    void calculateBlueGroups2() {
        Bookshelf b = new Bookshelf();

        List<Item> items = new ArrayList<>();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(0, items);
        List<Item> items1 = new ArrayList<>();
        items1.add(new Item(Color.YELLOW, 1));
        items1.add(new Item(Color.BLUE, 1));
        items1.add(new Item(Color.BLUE, 1));
        b.insert(1, items1);

        List<Item> items2 = new ArrayList<>();
        items2.add(new Item(Color.YELLOW, 1));
        items2.add(new Item(Color.BLUE, 1));
        b.insert(2, items2);

        List<Item> items3 = new ArrayList<>();
        items3.add(new Item(Color.YELLOW, 1));
        items3.add(new Item(Color.BLUE, 1));
        items3.add(new Item(Color.BLUE, 1));
        b.insert(3, items3);


        List<Item> items4 = new ArrayList<>();
        items4.add(new Item(Color.YELLOW, 1));
        items4.add(new Item(Color.YELLOW, 1));
        b.insert(4, items4);

        assertEquals(b.getPoints(), 13);
    }

    /**
     * 2 groups different color with another color in the middle
     */
    @Test
    void calculateBlueGroups3() {
        Bookshelf b = new Bookshelf();

        List<Item> items = new ArrayList<>();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(0, items);
        List<Item> items1 = new ArrayList<>();
        items1.add(new Item(Color.YELLOW, 1));
        items1.add(new Item(Color.BLUE, 1));
        items1.add(new Item(Color.BLUE, 1));
        b.insert(1, items1);

        List<Item> items2 = new ArrayList<>();
        items2.add(new Item(Color.GREEN, 1));
        items2.add(new Item(Color.BLUE, 1));
        b.insert(2, items2);

        List<Item> items3 = new ArrayList<>();
        items3.add(new Item(Color.YELLOW, 1));
        items3.add(new Item(Color.BLUE, 1));
        items3.add(new Item(Color.BLUE, 1));
        b.insert(3, items3);


        List<Item> items4 = new ArrayList<>();
        items4.add(new Item(Color.YELLOW, 1));
        items4.add(new Item(Color.YELLOW, 1));
        b.insert(4, items4);

        assertEquals(b.getPoints(), 10);
    }


    @Test
    void calculateGroups4() {
        Bookshelf b = new Bookshelf();

        List<Item> items = new ArrayList<>();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(0, items);


        List<Item> items1 = new ArrayList<>();
        items1.add(new Item(Color.YELLOW, 1));
        items1.add(new Item(Color.BLUE, 1));
        items1.add(new Item(Color.BLUE, 1));
        items1.add(new Item(Color.WHITE, 1));
        items1.add(new Item(Color.BLUE, 1));
        items1.add(new Item(Color.BLUE, 1));
        b.insert(1, items1);

        List<Item> items2 = new ArrayList<>();
        items2.add(new Item(Color.GREEN, 1));
        items2.add(new Item(Color.BLUE, 1));
        items2.add(new Item(Color.GREEN, 1));
        items2.add(new Item(Color.PINK, 1));
        items2.add(new Item(Color.PINK, 1));
        items2.add(new Item(Color.PINK, 1));
        b.insert(2, items2);

        List<Item> items3 = new ArrayList<>();
        items3.add(new Item(Color.YELLOW, 1));
        items3.add(new Item(Color.BLUE, 1));
        items3.add(new Item(Color.BLUE, 1));
        items3.add(new Item(Color.PINK, 1));
        items3.add(new Item(Color.BLUE, 1));
        items3.add(new Item(Color.PINK, 1));
        b.insert(3, items3);

        List<Item> items4 = new ArrayList<>();
        items4.add(new Item(Color.YELLOW, 1));
        items4.add(new Item(Color.YELLOW, 1));
        items3.add(new Item(Color.WHITE, 1));
        items4.add(new Item(Color.PINK, 1));
        items4.add(new Item(Color.PINK, 1));
        items4.add(new Item(Color.PINK, 1));
        b.insert(4, items4);

        assertEquals(b.getPoints(), 23);
    }

}
