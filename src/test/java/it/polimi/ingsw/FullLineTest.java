package it.polimi.ingsw;

import it.polimi.ingsw.Models.CommonGoalLayout.FullLine;
import it.polimi.ingsw.Models.CommonGoalLayout.Layout;
import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.TestUtility.BookshelfUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FullLineTest extends BookshelfUtilities {
    Bookshelf b;

    @BeforeEach
    void setup() {
        b = new Bookshelf();
    }

    @Test
    public void threeColumMax3DiffCol() {
        Layout fullLine = new FullLine(1, 3, 3, false);
        createColumn(b, 2, 3);
        if (!fullLine.check(b)) {
            b.cli_print();
        }
        assertTrue(fullLine.check(b));
    }

    @Test
    public void twoColumn6Diff() {
        Layout fullLine = new FullLine(6, 6, 2, false);
        createColumn(b, 6, 2);
        if (!fullLine.check(b)) {
            b.cli_print();
        }
        assertTrue(fullLine.check(b));
    }

    @Test
    public void fourRowsMax3DiffCol() {
        Layout fullLine = new FullLine(1, 3, 3, true);
        createRow(b, 2, 3);
        if (!fullLine.check(b)) {
            b.cli_print();
        }
        assertTrue(fullLine.check(b));
    }
}
