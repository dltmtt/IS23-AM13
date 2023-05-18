package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.utils.BookshelfUtilities;
import it.polimi.ingsw.utils.CliUtilities;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PrintsNicerTest {

    @BeforeAll
    public static void setUp() {
        SettingLoader.loadBookshelfSettings();
    }

    @Test
    void emojiTest() {
        for (Color color : Color.values()) {
            for (int i = 1; i <= 3; i++) {
                System.out.println(CliUtilities.emoji(color, i));
            }

            System.out.println("→" + CliUtilities.emoji(color, 4));
        }
    }

    @Test
    void borderTest() {
        System.out.print(CliUtilities.topLeftBookshelfBorder);
        System.out.print(CliUtilities.topCenterBookshelfBorder);
        System.out.println(CliUtilities.topRightBookshelfBorder);
        System.out.print(CliUtilities.bookshelfBorder);
        System.out.print(CliUtilities.emptySpace);
        System.out.print(CliUtilities.bookshelfBorder);
        System.out.print(CliUtilities.emptySpace);
        System.out.print(CliUtilities.bookshelfBorder);
        System.out.print(CliUtilities.emptySpace);
        System.out.println(CliUtilities.bookshelfBorder);
        System.out.print(CliUtilities.middleLeftBookshelfBorder);
        System.out.print(CliUtilities.middleCenterBookshelfBorder);
        System.out.println(CliUtilities.middleRightBookshelfBorder);
        System.out.print(CliUtilities.bookshelfBorder);
        System.out.print(CliUtilities.emptySpace);
        System.out.print(CliUtilities.bookshelfBorder);
        System.out.print(CliUtilities.emptySpace);
        System.out.print(CliUtilities.bookshelfBorder);
        System.out.print(CliUtilities.emptySpace);
        System.out.println(CliUtilities.bookshelfBorder);
        System.out.print(CliUtilities.bottomLeftBookshelfBorder);
        System.out.print(CliUtilities.bottomCenterBookshelfBorder);
        System.out.println(CliUtilities.bottomRightBookshelfBorder);
    }

    @Test
    void fullBookshelfTest() {
        Bookshelf b = new Bookshelf();
        BookshelfUtilities.randomFill(b);
    }

    @Test
    void emptyBookshelfTest() {
        Bookshelf b = new Bookshelf();
        b = new Bookshelf(1, 1);
        b = new Bookshelf(1, 3);
        b = new Bookshelf(3, 1);
        b = new Bookshelf(3, 13);
    }
}
