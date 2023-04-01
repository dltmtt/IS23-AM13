package it.polimi.ingsw;

import it.polimi.ingsw.model.commongoallayout.ItemsPerColor;
import it.polimi.ingsw.model.commongoallayout.Layout;
import it.polimi.ingsw.model.game.Bookshelf;
import it.polimi.ingsw.model.item.Color;
import it.polimi.ingsw.utils.BookshelfUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemsPerColorTest {
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
    public void eightSameColorItems() {
        Layout L = new ItemsPerColor(8, 8);
        for (Color color : Color.values()) {
            b.clearBookshelf();
            BookshelfUtilities.createRandomElements(b, color, 8);
            assert L.check(b);
        }

        int goalNum = 6;
        // And the other way round
        for (Color color : Color.values()) {
            for (int numOfElements = 1; numOfElements < Bookshelf.getSize(); numOfElements++) {
                b.clearBookshelf();
                L = new ItemsPerColor(goalNum, goalNum);
                BookshelfUtilities.createRandomElements(b, color, numOfElements);
                if (goalNum == numOfElements) {
                    if (!L.check(b)) {
                        b.cli_print();
                        System.out.println(L.getInfo());
                    }
                    assertTrue(L.check(b));
                } else {
                    assert true;
                }
            }
        }
    }
}
