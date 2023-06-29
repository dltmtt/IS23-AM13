package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.server.model.layouts.Layout;
import it.polimi.ingsw.server.model.layouts.XShape;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class XShapeTest {

    private final Layout XShape = new XShape(1, 1, 3);
    private Bookshelf b;

    @BeforeAll
    static void setupAll() {
        SettingLoader.loadBookshelfSettings();
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

        //right column of the X
        List<Item> XRight = new ArrayList<>();
        XRight.add(new Item(Xcolor, 0));
        XRight.add(new Item(Color.getRandomColor(), 0));
        XRight.add(new Item(Xcolor, 0));

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
                    b.insert(startingCol, XRight);
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

    @Test
    void XShapeTestFromGui() {
        Bookshelf b = new Bookshelf();
        List<Item> itemList = new ArrayList<>();
        Layout XShape = new XShape(1, 1, 5);
        CommonGoal commonGoal = new CommonGoal(XShape, 2);

        itemList.add(new Item(Color.GREEN, 0));
        itemList.add(new Item(Color.LIGHTBLUE, 0));
        itemList.add(new Item(Color.GREEN, 0));
        b.insert(1, itemList);

        itemList.clear();

        itemList.add(new Item(Color.GREEN, 0));
        itemList.add(new Item(Color.GREEN, 0));
        b.insert(2, itemList);

        itemList.clear();

        itemList.add(new Item(Color.GREEN, 0));
        itemList.add(new Item(Color.BLUE, 0));
        itemList.add(new Item(Color.GREEN, 0));
        b.insert(3, itemList);

        itemList.clear();

        assertTrue(commonGoal.check(b));
    }

    @Test
    void XShape2() {
        Bookshelf b = new Bookshelf();

        List<Item> itemList = new ArrayList<>();
        Layout XShape = new XShape(1, 1, 5);
        CommonGoal commonGoal = new CommonGoal(XShape, 2);

        itemList.add(new Item(Color.BLUE, 0));
        itemList.add(new Item(Color.GREEN, 0));
        itemList.add(new Item(Color.BLUE, 0));
        b.insert(2, itemList);

        itemList.clear();
        itemList.add(new Item(Color.WHITE, 0));
        itemList.add(new Item(Color.BLUE, 0));
        itemList.add(new Item(Color.BLUE, 0));
        b.insert(3, itemList);

        itemList.clear();
        itemList.add(new Item(Color.BLUE, 0));
        itemList.add(new Item(Color.PINK, 0));
        itemList.add(new Item(Color.BLUE, 0));
        b.insert(4, itemList);

        itemList.clear();
        assertTrue(commonGoal.check(b));

    }
}
