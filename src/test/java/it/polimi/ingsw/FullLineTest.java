package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.layouts.FullLine;
import it.polimi.ingsw.server.model.layouts.Layout;
import it.polimi.ingsw.utils.BookshelfUtilities;
import it.polimi.ingsw.utils.CliUtilities;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FullLineTest {

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
    public void threeColumMax3DiffCol() {
        Layout fullLine = new FullLine(1, 3, 3, false);
        BookshelfUtilities.createColumn(b, 2, 3);
        assertTrue(fullLine.check(b));
    }

    @Test
    public void twoColumn6Diff() {
        Layout fullLine = new FullLine(6, 6, 2, false);
        BookshelfUtilities.createColumn(b, 6, 2);
        assertTrue(fullLine.check(b));
    }
    @Test
    public void twoRows6Diff(){
        Layout fullLine = new FullLine(5, 5, 2, true);
        BookshelfUtilities.createRow(b, 6, 2);
        CliUtilities.stringifyBookshelf(b.getItems()).stream().forEach(System.out::println);
        assertTrue(fullLine.check(b));
    }

    @Test
    public void fourRowsMax3DiffCol() {
        int counter = 0;
        do {
            Layout fullLine = new FullLine(1, 3, 4, true);
            BookshelfUtilities.createRow(b, 2, 4);
            if (!fullLine.check(b)) {
                assertTrue(fullLine.check(b));
            }

            b.clearBookshelf();
            b.clearBooleanMatrix();
            counter++;
        } while (counter < 10);
    }
}
