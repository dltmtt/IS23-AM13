package it.polimi.ingsw;

import it.polimi.ingsw.Models.CommonGoalLayout.ItemsPerColor;
import it.polimi.ingsw.Models.CommonGoalLayout.Layout;
import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.TestUtility.BookshelfUtilities;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemsPerColorTest extends BookshelfUtilities {
    @Test
    public void EightSameColorItems() {
        Bookshelf b = new Bookshelf();
        Layout L = new ItemsPerColor(8, 8);
        for (Color color : Color.values()) {
            b = new Bookshelf();
            createRandomElements(b, color, 8);
            assert L.check(b);
        }

        int goalnum = 6;
        //and the other way round
        for (Color color : Color.values()) {
            for (int numofelements = 1; numofelements < b.getSize(); numofelements++) {
                b = new Bookshelf();
                L = new ItemsPerColor(goalnum, goalnum);
                createRandomElements(b, color, numofelements);
                if (goalnum == numofelements) {
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
