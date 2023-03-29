package it.polimi.ingsw;

import it.polimi.ingsw.Models.CommonGoalLayout.Corners;
import it.polimi.ingsw.Models.CommonGoalLayout.Layout;
import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.TestUtility.BookshelfUtilities;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CornersTest extends BookshelfUtilities {

    @Test
    void cornerCheck() {
        Bookshelf b = new Bookshelf();
        Layout layout = new Corners(1, 1);
        createCorner(b);
        assertTrue(layout.check(b));
    }

    @Test
    void checkFakeCorner() {
        Bookshelf b = new Bookshelf();
        Layout layout = new Corners(1, 1);
        createFakeCorner(b, 0);
        assertFalse(layout.check(b));
        Bookshelf b1 = new Bookshelf();
        createFakeCorner(b, 1);
        assertFalse(layout.check(b1));
        Bookshelf b2 = new Bookshelf();
        createFakeCorner(b2, 2);
        assertFalse(layout.check(b2));
    }
}
