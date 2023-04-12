package it.polimi.ingsw;

import it.polimi.ingsw.model.commonGoalLayout.Layout;
import it.polimi.ingsw.model.commonGoalLayout.XShape;
import it.polimi.ingsw.model.game.Bookshelf;
import it.polimi.ingsw.model.item.Color;
import it.polimi.ingsw.model.item.Item;
import it.polimi.ingsw.utils.BookshelfUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class XShapeTest {
    private final Layout XShape = new XShape(3, 1, 1);
    private Bookshelf b;

    @BeforeAll
    static void setupAll() {
        BookshelfUtilities.loadSettings();
    }

    @BeforeEach
    void setup() {
        b = new Bookshelf(Bookshelf.getRows(), Bookshelf.getColumns());
    }

    @Test
    void base_IsolatedX() {
        // Chooses the color to paint the X
        Color Xcolor = Color.getRandomColor();

        // Left and right border of the X
        List<Item> XBorder = new ArrayList<>();
        XBorder.add(new Item(Xcolor, 0));
        XBorder.add(new Item(Color.getRandomColor(), 0));
        XBorder.add(new Item(Xcolor, 0));

        // Central column of the X
        List<Item> XCenter = new ArrayList<>();
        XCenter.add(new Item(Color.getRandomColor(), 0));
        XCenter.add(new Item(Xcolor, 0));
        XCenter.add(new Item(Color.getRandomColor(), 0));

        List<Item> itemList = new ArrayList<>();

        // This test is composed by various batches, because of the nature of random generated colors…
        // for normal testing I think 100 would be enough.
        int testNumber = 100;

        for (int test = 0; test < testNumber; test++) {
            // Select every possible starting row for the X shape
            for (int startingRow = 0; startingRow < Bookshelf.getRows() - XShape.getHeight() + 1; startingRow++) {
                // Select every possible starting column for the X shape
                for (int startingCol = 0; startingCol < Bookshelf.getColumns() - XShape.getWidth() + 1; startingCol++) {
                    b.clearBookshelf();

                    // Creation of the base
                    for (int col = 0; col < Bookshelf.getColumns(); col++) {
                        itemList.clear();
                        for (int baseRow = 0; baseRow < startingRow; baseRow++) {
                            itemList.add(new Item(Color.getRandomColor(), 0));
                        }
                        if (!itemList.isEmpty()) {
                            b.insert(col, itemList);
                        }
                    }

                    // Insertion of the X shape
                    b.insert(startingCol, XBorder);
                    b.insert(startingCol + 1, XCenter);
                    b.insert(startingCol + 2, XBorder);

                    // Filling the remaining cells
                    for (int col = 0; col < Bookshelf.getColumns(); col++) {
                        itemList.clear();
                        if (b.getFreeCellsInColumn(col) != 0) {
                            for (int i = 0; i < b.getFreeCellsInColumn(col); i++) {
                                itemList.add(new Item(Color.getRandomColor(), 0));
                            }
                            b.insert(col, itemList);
                        }
                    }

//                    if (!XShape.check(b)) {
//                        b.print();
//                    }
                    assertTrue(XShape.check(b));
                }
            }
        }
    }
}
