package it.polimi.ingsw;

import it.polimi.ingsw.model.commonGoalLayout.Layout;
import it.polimi.ingsw.model.game.Bookshelf;
import it.polimi.ingsw.utils.BookshelfUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroupTest {
    Bookshelf bookshelf;

    @BeforeAll
    static void setupAll() {
        BookshelfUtilities.loadSettings();
    }

    @BeforeEach
    void setup() {
        bookshelf = new Bookshelf(Bookshelf.getRows(), Bookshelf.getColumns());
    }

    @Test
    public void testGroup() {
        // Testing possible problematics dispositions of the bookshelves
        bookshelf.clearBooleanMatrix();
        bookshelf.clearBookshelf();
        int groupsize = 4;
        Layout layout;

        for (int config = 0; config < 12; config++) {
            layout = BookshelfUtilities.createProblematicLayouts(bookshelf, config);
            if (!layout.check(bookshelf)) {
                bookshelf.cli_print();
                System.out.println("Config: " + config);
            }
            assertTrue(layout.check(bookshelf));
        }
    }
}
