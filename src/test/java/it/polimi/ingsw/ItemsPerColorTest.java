package it.polimi.ingsw;

import it.polimi.ingsw.Models.CommonGoalLayout.ItemsPerColor;
import it.polimi.ingsw.Models.CommonGoalLayout.Layout;
import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.TestUtility.BookshelfUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemsPerColorTest extends BookshelfUtilities {
    Bookshelf b;

    @BeforeEach
    void setUp() {
        b = new Bookshelf();
    }

    @Test
    public void EightSameColorItems() {
        Layout L = new ItemsPerColor(8, 8);
        for (Color color : Color.values()) {
            b = new Bookshelf();
            createRandomElements(b, color, 8);
            assert L.check(b);
        }

        int goalNum = 6;
        // And the other way round
        for (Color color : Color.values()) {
            for (int numOfElements = 1; numOfElements < b.getSize(); numOfElements++) {
                b = new Bookshelf();
                L = new ItemsPerColor(goalNum, goalNum);
                createRandomElements(b, color, numOfElements);
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
