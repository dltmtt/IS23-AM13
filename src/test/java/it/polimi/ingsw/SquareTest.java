package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.server.model.layouts.FullLine;
import it.polimi.ingsw.server.model.layouts.Layout;
import it.polimi.ingsw.server.model.layouts.Square;
import it.polimi.ingsw.utils.CliUtilities;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquareTest {

    @BeforeAll
    static void setupAll() {
        SettingLoader.loadBookshelfSettings();
    }

    @Test
    void getName() {
        Layout square = new Square(1, 1, 2, 2);
        assertEquals("square", square.getName());
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

        assertTrue(square.check(b));
    }

    @Test
    void TrueTest4() {
        Layout square = new Square(1, 1, 2, 2);
        Layout fullLine = new FullLine(1, 1, 1, false);

        Bookshelf b = new Bookshelf();
        List<Item> items = new ArrayList<>();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        b.insert(0, items);
        items.clear();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        b.insert(1, items);
        items.clear();

        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        b.insert(2, items);
        items.clear();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        b.insert(3, items);
        items.clear();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        b.insert(4, items);

        System.out.println("TrueTest4");

        assertTrue(square.check(b));
        assertTrue(fullLine.check(b));
    }

    @Test
    void checkFromGui2() {
        Layout square = new Square(1, 1, 2, 2);

        Bookshelf b = new Bookshelf();
        List<Item> items = new ArrayList<>();
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));

        b.insert(4, items);

        items.clear();
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));

        b.insert(3, items);

        items.clear();
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.WHITE, 1));
        b.insert(2, items);

        items.clear();
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(1, items);

        CliUtilities.stringifyBookshelf(b.getItems()).forEach(System.out::println);
        assertFalse(square.check(b));
    }

    @Test
    void checkFromGui() {
        Layout square = new Square(1, 1, 2, 2);

        Bookshelf b = new Bookshelf();
        List<Item> items = new ArrayList<>();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));

        b.insert(0, items);
        items.clear();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));

        b.insert(1, items);
        items.clear();

        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));

        b.insert(3, items);
        items.clear();
        square.check(b);
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.PINK, 1));
        b.insert(4, items);
        items.clear();
        assertTrue(square.check(b));
    }
}
