package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.utils.BookshelfUtilities;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PrintsNicer;
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
                System.out.println(PrintsNicer.emoji(color, i));
            }

            System.out.println("→" + PrintsNicer.emoji(color, 4));
        }
    }

    @Test
    void borderTest() {
        System.out.print(PrintsNicer.topLeftBookshelfBorder);
        System.out.print(PrintsNicer.topCenterBookshelfBorder);
        System.out.println(PrintsNicer.topRightBookshelfBorder);
        System.out.print(PrintsNicer.bookshelfBorder);
        System.out.print(PrintsNicer.emptySpace);
        System.out.print(PrintsNicer.bookshelfBorder);
        System.out.print(PrintsNicer.emptySpace);
        System.out.print(PrintsNicer.bookshelfBorder);
        System.out.print(PrintsNicer.emptySpace);
        System.out.println(PrintsNicer.bookshelfBorder);
        System.out.print(PrintsNicer.middleLeftBookshelfBorder);
        System.out.print(PrintsNicer.middleCenterBookshelfBorder);
        System.out.println(PrintsNicer.middleRightBookshelfBorder);
        System.out.print(PrintsNicer.bookshelfBorder);
        System.out.print(PrintsNicer.emptySpace);
        System.out.print(PrintsNicer.bookshelfBorder);
        System.out.print(PrintsNicer.emptySpace);
        System.out.print(PrintsNicer.bookshelfBorder);
        System.out.print(PrintsNicer.emptySpace);
        System.out.println(PrintsNicer.bookshelfBorder);
        System.out.print(PrintsNicer.bottomLeftBookshelfBorder);
        System.out.print(PrintsNicer.bottomCenterBookshelfBorder);
        System.out.println(PrintsNicer.bottomRightBookshelfBorder);


    }


    @Test
    void fullBookshelfTest() {
        Bookshelf b = new Bookshelf();
        BookshelfUtilities.randomFill(b);
//        b.cli_print_2();
//        b.cli_print();
    }

    @Test
    void emptyBookshelfTest() {
        Bookshelf b = new Bookshelf();
//        b.cli_print_2();
        b = new Bookshelf(1, 1);
//        b.cli_print_2();
        b = new Bookshelf(1, 3);
//        b.cli_print_2();
        b = new Bookshelf(3, 1);
//        b.cli_print_2();
        b = new Bookshelf(3, 13);
//        b.cli_print_2();

    }

}
