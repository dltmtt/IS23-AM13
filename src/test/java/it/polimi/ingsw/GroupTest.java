package it.polimi.ingsw;

import it.polimi.ingsw.client.view.BookshelfView;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.server.model.layouts.Group;
import it.polimi.ingsw.server.model.layouts.Layout;
import it.polimi.ingsw.utils.BookshelfUtilities;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroupTest {

    Bookshelf bookshelf;

    @BeforeAll
    static void setupAll() {
        SettingLoader.loadBookshelfSettings();
    }

    @BeforeEach
    void setup() {
        bookshelf = new Bookshelf(Bookshelf.getRows(), Bookshelf.getColumns());
    }

    @Test
    public void testGroup() {
        // Testing possible problematics dispositions of the bookshelves
        int groupSize = 4;
        Layout layout;

        for (int config = 0; config < 12; config++) {
            layout = BookshelfUtilities.createProblematicLayouts(bookshelf, config);
            if (!layout.check(bookshelf)) {
                System.out.println("Config: " + config);
            }
            assertTrue(layout.check(bookshelf));
        }
    }

    @Test
    void GroupFromGui() {
        SettingLoader.loadBookshelfSettings();
        Bookshelf bookshelf = new Bookshelf();
        Layout group = new Group(1, 1, 6, 2);
        CommonGoal commonGoal = new CommonGoal(group, 2);
        List<Item> items = new ArrayList<>();

        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        bookshelf.insert(0, items);
        items.clear();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        bookshelf.insert(1, items);
        items.clear();

        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.GREEN, 1));
        bookshelf.insert(2, items);
        items.clear();

        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.LIGHTBLUE, 1));
        bookshelf.insert(3, items);
        items.clear();
        BookshelfView bookshelfView = new BookshelfView(bookshelf);
        bookshelfView.printBookshelf();
        assertTrue(!group.check(bookshelf));
    }
}
