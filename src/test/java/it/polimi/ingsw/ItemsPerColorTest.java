package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.server.model.layouts.ItemsPerColor;
import it.polimi.ingsw.server.model.layouts.Layout;
import it.polimi.ingsw.utils.BookshelfUtilities;
import it.polimi.ingsw.utils.CliUtilities;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemsPerColorTest {

    Bookshelf b;

    @BeforeAll
    static void setupAll() {
        SettingLoader.loadBookshelfSettings();
    }

    @BeforeEach
    void setup() {
        b = new Bookshelf(Bookshelf.getRows(), Bookshelf.getColumns());
    }

    @Test
    public void eightSameColorItems() {
        Layout L = new ItemsPerColor(8, 8);
        for (Color color : Color.values()) {
            b.clearBookshelf();
            BookshelfUtilities.createRandomElements(b, color, 8);
            CliUtilities.stringifyBookshelf(b.getItems()).forEach(System.out::println);
            assert L.check(b);
        }

        int goalNum = 8;
        // And the other way round
        for (Color color : Color.values()) {
            for (int numOfElements = 1; numOfElements < Bookshelf.getSize(); numOfElements++) {
                b.clearBookshelf();
                L = new ItemsPerColor(goalNum, goalNum);
                BookshelfUtilities.createRandomElements(b, color, numOfElements);
                if (goalNum == numOfElements) {
                    if (!L.check(b)) {
                        System.out.println(L.getInfo());
                    }
                    CliUtilities.stringifyBookshelf(b.getItems()).forEach(System.out::println);
                    assertTrue(L.check(b));
                } else {
                    CliUtilities.stringifyBookshelf(b.getItems()).forEach(System.out::println);
                    assert true;
                }
            }
        }
    }

    @Test
    void getName() {
        Layout L = new ItemsPerColor(1, 1);
        assert L.getName().equals("itemsPerColor");
    }

    @Test
    void ItemPerColorFake() {
        Bookshelf b = new Bookshelf();
        Layout L = new ItemsPerColor(1, 1);
        List<Item> items = new ArrayList<>();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(0, items);
        items.clear();

        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.PINK, 1));
        b.insert(1, items);

        assertFalse(L.check(b));
    }

    @Test
    void okTest() {
        Bookshelf b = new Bookshelf();
        Layout L = new ItemsPerColor(1, 1);
        List<Item> items = new ArrayList<>();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(0, items);
        items.clear();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.PINK, 1));
        b.insert(1, items);

        assertTrue(L.check(b));
    }

    @Test
    void ciaoTest() {
        Bookshelf b = new Bookshelf();
        Layout L = new ItemsPerColor(1, 1);
        List<Item> items = new ArrayList<>();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(0, items);
        items.clear();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));

        b.insert(1, items);
        items.clear();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        b.insert(2, items);

        CliUtilities.stringifyBookshelf(b.getItems()).forEach(System.out::println);
        assertFalse(L.check(b));
    }
}
