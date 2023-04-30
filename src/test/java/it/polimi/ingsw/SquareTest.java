package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.server.model.layouts.Layout;
import it.polimi.ingsw.server.model.layouts.Square;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SquareTest {
    @BeforeAll
    static void setupAll() {
        SettingLoader.loadBookshelfSettings();
    }

    @Test
    void FirstTest() {
        Layout square = new Square(1, 1, 2, 2);

        Bookshelf b = new Bookshelf();
        List<Item> items = new ArrayList<>();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 2));
        items.add(new Item(Color.PINK, 1));
        b.insert(0, items);
        items.clear();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(1, items);
        items.clear();

        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        b.insert(2, items);
        items.clear();

        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        b.insert(3, items);

        System.out.println("FirstTest");
        b.cli_print();

        assertTrue(square.check(b));


    }

    @Test
    void FalseTest() {
        Layout square = new Square(1, 1, 2, 2);

        Bookshelf b = new Bookshelf();
        List<Item> items = new ArrayList<>();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.PINK, 1));
        b.insert(0, items);
        items.clear();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(1, items);
        items.clear();

        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(2, items);
        items.clear();

        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        b.insert(3, items);

        System.out.println("FalseTest");
        b.cli_print();

        assertFalse(square.check(b));
    }

    @Test
    void FalseTest2() {
        Layout square = new Square(1, 1, 2, 2);

        Bookshelf b = new Bookshelf();
        List<Item> items = new ArrayList<>();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(0, items);
        items.clear();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(1, items);
        items.clear();

        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(2, items);
        items.clear();

        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        b.insert(3, items);

        System.out.println("FalseTest2");
        b.cli_print();

        assertFalse(square.check(b));
    }

    @Test
    void TrueTest() {
        Layout square = new Square(1, 1, 2, 2);

        Bookshelf b = new Bookshelf();
        List<Item> items = new ArrayList<>();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(0, items);
        items.clear();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        b.insert(1, items);
        items.clear();

        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        b.insert(2, items);
        items.clear();

        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        b.insert(3, items);

        System.out.println("TrueTest");
        b.cli_print();

        assertTrue(square.check(b));
    }

    @Test
    void TrueTest2() {
        Layout square = new Square(1, 1, 2, 2);

        Bookshelf b = new Bookshelf();
        List<Item> items = new ArrayList<>();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(0, items);
        items.clear();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(1, items);
        items.clear();

        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        b.insert(2, items);
        items.clear();

        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        b.insert(3, items);
        items.clear();

        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        b.insert(4, items);

        System.out.println("TrueTest2");
        b.cli_print();
        assertTrue(square.check(b));
    }

    @Test
    void TrueTest3() {
        Layout square = new Square(1, 1, 2, 2);

        Bookshelf b = new Bookshelf();
        List<Item> items = new ArrayList<>();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        b.insert(0, items);
        items.clear();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        b.insert(1, items);
        items.clear();

        items.add(new Item(Color.PINK, 1));
        b.insert(2, items);
        items.clear();

        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        b.insert(3, items);
        items.clear();

        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        b.insert(4, items);

        System.out.println("TrueTest3");
        b.cli_print();

        assertTrue(square.check(b));
    }
}
