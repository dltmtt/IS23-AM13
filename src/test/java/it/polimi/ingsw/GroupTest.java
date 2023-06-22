package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.layouts.Layout;
import it.polimi.ingsw.utils.BookshelfUtilities;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
