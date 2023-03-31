package it.polimi.ingsw;

import it.polimi.ingsw.Models.CommonGoalLayout.Layout;
import it.polimi.ingsw.Models.CommonGoalLayout.XShape;
import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class XShapeTest {
    private final Layout XShape = new XShape(3, 1, 1);
    private Bookshelf b;

    @BeforeEach
    void setup() {
        // Read the settings from the properties file
        int rowsSetting;
        int colsSetting;

        Properties prop = new Properties();
        //In case the file is not found, the default values will be used
        try (InputStream input = new FileInputStream("settings/settings.properties")) {

            // Load a properties file
            prop.load(input);
            rowsSetting = parseInt(prop.getProperty("bookshelf.rows"));
            colsSetting = parseInt(prop.getProperty("bookshelf.columns"));
        } catch (IOException ex) {
            ex.printStackTrace();
            // If there is an error, use the default values
            rowsSetting = 5;
            colsSetting = 6;
        }

        b = new Bookshelf(rowsSetting, colsSetting);
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
            for (int startingRow = 0; startingRow < b.getRows() - XShape.getHeight() + 1; startingRow++) {
                // Select every possible starting column for the X shape
                for (int startingCol = 0; startingCol < b.getColumns() - XShape.getWidth() + 1; startingCol++) {
                    b.clearBookshelf();

                    // Creation of the base
                    for (int col = 0; col < b.getColumns(); col++) {
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
                    for (int col = 0; col < b.getColumns(); col++) {
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
