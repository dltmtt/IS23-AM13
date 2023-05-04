package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculateGroupsTest {

    Bookshelf b;
    List<Item> items;

    @BeforeAll
    static void setupAll() {
        SettingLoader.loadBookshelfSettings();
    }

    @BeforeEach
    void setup() {
        b = new Bookshelf(Bookshelf.getRows(), Bookshelf.getColumns());
        items = new ArrayList<>();
    }

    // 1 group
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

        assertEquals(3, b.getPoints());
    }

    // 2 groups with different colors
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

        assertEquals(13, b.getPoints());
    }

    // 2 groups with different colors with another color in the middle
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

        assertEquals(10, b.getPoints());
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

        assertEquals(23, b.getPoints());
    }

    @Test
    void worstCaseScenario1() {
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

        assertEquals(10, b.getPoints());
    }

    @Test
    void worstScenario2HalfSnake() {
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

        assertEquals(12, b.getPoints());
    }

    @Test
    void worstScenario3Snake() {
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

        assertEquals(8, b.getPoints());
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

        assertEquals(13, b.getPoints());
    }

    @Test
    void zeroPoints() {
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

        assertEquals(0, b.getPoints());
    }

    @Test
    void donut() {
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

        assertEquals(19, b.getPoints());
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

        assertEquals(11, b.getPoints());
    }
}
