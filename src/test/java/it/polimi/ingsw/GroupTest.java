package it.polimi.ingsw;

import it.polimi.ingsw.model.commongoallayout.Group;
import it.polimi.ingsw.model.commongoallayout.Layout;
import it.polimi.ingsw.model.game.Bookshelf;
import it.polimi.ingsw.model.item.Color;
import it.polimi.ingsw.model.item.Item;
import it.polimi.ingsw.utils.BookshelfUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroupTest {
    Bookshelf bookshelf;
    List<Item> items;

    @BeforeAll
    static void setupAll() {
        BookshelfUtilities.loadSettings();
    }

    @BeforeEach
    void setup() {
        bookshelf = new Bookshelf(Bookshelf.getRows(), Bookshelf.getColumns());
        items = new ArrayList<>();
    }

    @Test
    public void testGroup() {
        // Testing possible problematics dispositions of the bookshelves
        bookshelf.clearBooleanMatrix();
        bookshelf.clearBookshelf();
        List<Color> colorList = List.of(Color.values());
        int groupsize = 4;
        Layout layout = new Group(1, 1, Bookshelf.getColumns(), groupsize);
        //correctly identifying a column of 4 items of the same color
        for (int i = 0; i < Bookshelf.getColumns(); i++) {
            items.clear();
            for (int index = 0; index < groupsize; index++) {
                items.add(new Item(colorList.get(i % (colorList.size() - 1)), 0));
            }

            int numrem = Bookshelf.getRows() - items.size();
            for (int remaining = 0; remaining < numrem; remaining++) {
                items.add(new Item(colorList.get(colorList.size() - 1), 0));
            }
            bookshelf.insert(i, items);
        }
        bookshelf.cli_print();
        assertTrue(layout.check(bookshelf));

        //correctly identifying a row of 4 items of the same color
        setup();
        items.clear();
        for (int i = 0; i < groupsize; i++) {
            items.add(new Item(colorList.get(i % (colorList.size() - 1)), 0));
        }
        for (int i = 0; i < Bookshelf.getRows() - items.size(); i++) {
            items.add(new Item(colorList.get(colorList.size() - 1), 0));
        }
        for (int i = 0; i < groupsize; i++) {
            bookshelf.insert(i, items);
        }

        items.clear();
        for (int i = 0; i < Bookshelf.getColumns(); i++) {
            items.add(new Item(colorList.get(colorList.size() - 1), 0));
        }


        for (int i = groupsize; i < Bookshelf.getColumns(); i++) {
            bookshelf.insert(i, items);
        }
        bookshelf.cli_print();
        assertTrue(layout.check(bookshelf));

        setup();
        items.clear();
        //weird disposition of items
        //WORKS ONLY ON SPECIFIC DIMENSIONS and LAYOUT
        layout = new Group(1, 1, 4, 4);
        int columns = 5;
        int rows = 6;

        items.clear();

        //first column
        items.add(new Item(colorList.get(0), 0));
        items.add(new Item(colorList.get(0), 0));
        items.add(new Item(colorList.get(2), 0));
        items.add(new Item(colorList.get(2), 0));
        items.add(new Item(colorList.get(3), 0));
        items.add(new Item(colorList.get(2), 0));

        bookshelf.insert(0, items);

        items.clear();
        //second column
        items.add(new Item(colorList.get(0), 0));
        items.add(new Item(colorList.get(2), 0));
        items.add(new Item(colorList.get(2), 0));
        items.add(new Item(colorList.get(3), 0));
        items.add(new Item(colorList.get(1), 0));
        items.add(new Item(colorList.get(2), 0));

        bookshelf.insert(1, items);

        items.clear();
        //third column
        items.add(new Item(colorList.get(0), 0));
        items.add(new Item(colorList.get(3), 0));
        items.add(new Item(colorList.get(3), 0));
        items.add(new Item(colorList.get(2), 0));
        items.add(new Item(colorList.get(1), 0));
        items.add(new Item(colorList.get(2), 0));

        bookshelf.insert(2, items);

        items.clear();
        //fourth column
        items.add(new Item(colorList.get(1), 0));
        items.add(new Item(colorList.get(2), 0));
        items.add(new Item(colorList.get(2), 0));
        items.add(new Item(colorList.get(2), 0));
        items.add(new Item(colorList.get(1), 0));
        items.add(new Item(colorList.get(1), 0));

        bookshelf.insert(3, items);

        items.clear();

        //fifth column
        items.add(new Item(colorList.get(1), 0));
        items.add(new Item(colorList.get(1), 0));
        items.add(new Item(colorList.get(1), 0));
        items.add(new Item(colorList.get(1), 0));
        items.add(new Item(colorList.get(1), 0));
        items.add(new Item(colorList.get(2), 0));

        bookshelf.insert(4, items);
        bookshelf.cli_print();
        assertTrue(layout.check(bookshelf));
    }
}
