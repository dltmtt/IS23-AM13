package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculateGroupsTest {


    @Test
    void calculateBluGropus() {
        Bookshelf b = new Bookshelf();
        List<Item> items = new ArrayList<>();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(0, items);
        List<Item> items1 = new ArrayList<>();
        items1.add(new Item(Color.BLUE, 1));
        items1.add(new Item(Color.BLUE, 1));
        items1.add(new Item(Color.BLUE, 1));
        b.insert(1, items1);
        ClasseTemporanea T = new ClasseTemporanea();
        assertEquals(T.calculateGroups(b, 0, 0, Color.BLUE), 3);

    }
}
