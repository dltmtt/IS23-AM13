package it.polimi.ingsw;

import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculateGroupsTest {
    Bookshelf b;
    List<Item> items;

    @BeforeEach
    void setUp() {
        b = new Bookshelf();
        items = new ArrayList<>();
    }

    /**
     * 1 group
     */
    @Test
    void calculateBlueGroups() {
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

    @Test
    void WorstCaseScenario1() {
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(0, items);

        items.clear();
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(1, items);

        items.clear();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        b.insert(2, items);

        items.clear();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.YELLOW, 1));
        b.insert(3, items);

        items.clear();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.PINK, 1));
        b.insert(4, items);

        assertEquals(b.getPoints(), 10);
    }

    @Test
    void WorstScenario2_HalfSnake() {
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(0, items);

        items.clear();
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.WHITE, 1));
        b.insert(1, items);

        items.clear();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        b.insert(2, items);

        items.clear();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.YELLOW, 1));
        b.insert(3, items);

        items.clear();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.PINK, 1));
        b.insert(4, items);

        assertEquals(b.getPoints(), 12);
    }

    @Test
    void WorstScenario3_Snake() {
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(0, items);

        items.clear();
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.WHITE, 1));
        b.insert(1, items);

        items.clear();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        b.insert(2, items);

        items.clear();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.YELLOW, 1));
        b.insert(3, items);

        items.clear();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.PINK, 1));
        b.insert(4, items);

        assertEquals(b.getPoints(), 8);
    }

    @Test
    void WorstCaseScenario_snake_and_2_others() {
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(0, items);

        items.clear();
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        b.insert(1, items);

        items.clear();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        b.insert(2, items);

        items.clear();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        b.insert(3, items);

        items.clear();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        b.insert(4, items);

        assertEquals(b.getPoints(), 13);
    }

    @Test
    void ZeroPoints() {
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        b.insert(0, items);

        items.clear();
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(1, items);

        items.clear();
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(2, items);

        items.clear();
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        b.insert(3, items);

        items.clear();
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        b.insert(4, items);

        assertEquals(b.getPoints(), 0);
    }

    @Test
    void Donut() {
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        b.insert(0, items);

        items.clear();
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.PINK, 1));
        b.insert(1, items);

        items.clear();
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        b.insert(2, items);

        items.clear();
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.WHITE, 1));
        b.insert(3, items);

        items.clear();
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(4, items);

        assertEquals(b.getPoints(), 19);
    }

    @Test
    void spiral() {
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        b.insert(0, items);

        items.clear();
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.PINK, 1));
        b.insert(1, items);

        items.clear();
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.PINK, 1));
        b.insert(2, items);

        items.clear();
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.PINK, 1));
        b.insert(3, items);

        items.clear();
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        b.insert(4, items);

        assertEquals(b.getPoints(), 11);
    }
}
