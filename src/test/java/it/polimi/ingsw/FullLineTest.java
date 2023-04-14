package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.layouts.FullLine;
import it.polimi.ingsw.server.model.layouts.Layout;
import it.polimi.ingsw.utils.BookshelfUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FullLineTest {
    Bookshelf b;

    @BeforeAll
    static void setupAll() {
        BookshelfUtilities.loadSettings();
    }

    @BeforeEach
    void setup() {
        b = new Bookshelf(Bookshelf.getRows(), Bookshelf.getColumns());
    }

    @Test
    public void threeColumMax3DiffCol() {
        Layout fullLine = new FullLine(1, 3, 3, false);
        BookshelfUtilities.createColumn(b, 2, 3);
        if (!fullLine.check(b)) {
            b.cli_print();
        }
        assertTrue(fullLine.check(b));
    }

    @Test
    public void twoColumn6Diff() {
        Layout fullLine = new FullLine(6, 6, 2, false);
        BookshelfUtilities.createColumn(b, 6, 2);
        if (!fullLine.check(b)) {
            b.cli_print();
        }
        assertTrue(fullLine.check(b));
    }

    @Test
    public void fourRowsMax3DiffCol() {
        Layout fullLine = new FullLine(1, 3, 3, true);
        BookshelfUtilities.createRow(b, 2, 3);
        if (!fullLine.check(b)) {
            b.cli_print();
        }
        assertTrue(fullLine.check(b));
    }
}
