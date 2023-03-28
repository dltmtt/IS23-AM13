package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class XShapeTest {
    private final Layout XShape = new XShape(3, 1, 1);
    private Bookshelf b = new Bookshelf();

    @Test
    void base_IsolatedX() {
        // Chooses the color to paint the X
        Color Xcolor = Color.randomColor();

        // Left and right border of the X
        List<Item> XBorder = new ArrayList<>();
        XBorder.add(new Item(Xcolor, 0));
        XBorder.add(new Item(Color.randomColor(), 0));
        XBorder.add(new Item(Xcolor, 0));

        // Central column of the X
        List<Item> XCenter = new ArrayList<>();
        XCenter.add(new Item(Color.randomColor(), 0));
        XCenter.add(new Item(Xcolor, 0));
        XCenter.add(new Item(Color.randomColor(), 0));

        List<Item> itemList = new ArrayList<>();


        // This test is composed by various batches, because of the nature of random generated colors...
        // for normal testing I think 100 would be enough.
        int testNumber = 100;

        for (int test = 0; test < testNumber; test++) {
            // For cycle to select every possible starting row for the X shape
            for (int startingRow = 0; startingRow < b.getRows() - XShape.getHeight() + 1; startingRow++) {
                // For cycle to select every possible starting column for the X shape
                for (int startingCol = 0; startingCol < b.getColumns() - XShape.getWidth() + 1; startingCol++) {
                    b = new Bookshelf();

                    // Creation of the base
                    for (int col = 0; col < b.getColumns(); col++) {
                        itemList.clear();
                        for (int baseRow = 0; baseRow < startingRow; baseRow++) {
                            itemList.add(new Item(Color.randomColor(), 0));
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
                    for (int col = 0; col < b.getColumns(); col++) {
                        itemList.clear();
                        if (b.getFreeCellsInColumn(col) != 0) {
                            for (int i = 0; i < b.getFreeCellsInColumn(col); i++) {
                                itemList.add(new Item(Color.randomColor(), 0));
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
