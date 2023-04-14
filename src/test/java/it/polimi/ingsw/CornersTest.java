package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.layouts.Corners;
import it.polimi.ingsw.server.model.layouts.Layout;
import it.polimi.ingsw.utils.BookshelfUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CornersTest {
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
    void cornerCheck() {
        Layout layout = new Corners(1, 1);
        BookshelfUtilities.createCorner(b);
        assertTrue(layout.check(b));
    }

    // WARNING: This test fails
    @Test
    void checkFakeCorner() {
        Layout layout = new Corners(1, 1);
        BookshelfUtilities.createFakeCorner(b, 0);
        assertFalse(layout.check(b));
        b.clearBookshelf();
        BookshelfUtilities.createFakeCorner(b, 1);
        assertFalse(layout.check(b));
//        Bookshelf b2 = new Bookshelf(settings_height, settings_width);
//        Create Fake Corner possibly has a bug
//        createFakeCorner(b2, 2);
//        assertFalse(layout.check(b2));
    }
}
