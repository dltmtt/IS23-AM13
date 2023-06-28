package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.layouts.Diagonal;
import it.polimi.ingsw.server.model.layouts.Layout;
import it.polimi.ingsw.utils.BookshelfUtilities;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DiagonalTest {

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
    void checkSingleRightDiagonal() {
        Layout layout = new Diagonal(1, 1, 5);
        assertEquals("diagonal", layout.getName());
        BookshelfUtilities.createSingleRightDiagonal(b, 0, 0, 5);
        assertTrue(layout.check(b));
        b.clearBookshelf();
        BookshelfUtilities.createSingleRightDiagonal(b, 1, 0, 5);
        assertTrue(layout.check(b));
    }

    @Test
    void checkSingleLeftDiagonal() {
        Layout layout = new Diagonal(1, 1, 5);
        BookshelfUtilities.createSingleLeftDiagonal(b, 0, 4, 5);
        boolean result = layout.check(b);
        assertTrue(result);
        b.clearBookshelf();
        BookshelfUtilities.createSingleLeftDiagonal(b, 1, 4, 5);
        assertTrue(layout.check(b));
    }

    @Test
    public void provaRight() {
        for (int dimension = 1; dimension < Math.min(Bookshelf.getRows(), Bookshelf.getColumns()) + 1; dimension++) {
            for (int row = 0; row < Bookshelf.getRows() - dimension + 1; row++) {
                for (int col = 0; col < Bookshelf.getColumns() - dimension + 1; col++) {
                    b.clearBookshelf();
                    BookshelfUtilities.createSingleRightDiagonal(b, row, col, dimension);
                    System.out.println("row: " + row + ", column: " + col + " dimension: " + dimension);
                }
            }
        }
    }

    @Test
    void checkFakeRightDiagonal() {
        Layout layout = new Diagonal(1, 1, 5);

        for (int i = 0; i < 5; i++) {
            b.clearBookshelf();
            BookshelfUtilities.createFakeDiagonal(b, i);
            assertFalse(layout.check(b));
        }
    }
}
