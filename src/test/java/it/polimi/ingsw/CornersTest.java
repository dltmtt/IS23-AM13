package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.layouts.Corners;
import it.polimi.ingsw.server.model.layouts.Layout;
import it.polimi.ingsw.utils.BookshelfUtilities;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CornersTest {
    Bookshelf b;

    @BeforeAll
    static void setupAll() {
        SettingLoader.loadBookshelfSettings();
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

    @Test
    void checkFakeCorner() {
        for(int i=0; i<100; i++) {
            Layout layout = new Corners(1, 1);
            BookshelfUtilities.createFakeCorner(b, 0);
            assertFalse(layout.check(b));
            b.clearBookshelf();
            b.clearBooleanMatrix();
            BookshelfUtilities.createFakeCorner(b, 1);
            assertFalse(layout.check(b));
            b.clearBookshelf();
            b.clearBooleanMatrix();
            BookshelfUtilities.createFakeCorner(b, 2);
            assertFalse(layout.check(b));
        }
    }
}
