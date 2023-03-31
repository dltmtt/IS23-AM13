package it.polimi.ingsw;

import it.polimi.ingsw.Models.CommonGoalLayout.Layout;
import it.polimi.ingsw.Models.CommonGoalLayout.Stair;
import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Item.Item;
import it.polimi.ingsw.TestUtility.BookshelfUtilities;
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

public class StairTest extends BookshelfUtilities {
    Bookshelf b;
    Layout stair;

    @BeforeEach
    void setup() {
        // Read the settings from the properties file
        int rowsSetting;
        int colsSetting;

        Properties prop = new Properties();
        // In case the file is not found, the default values will be used
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
        stair = new Stair(Bookshelf.getColumns());
    }

    @Test
    void StairCheckRowZero() {
        createLeftStair(b);
        assertTrue(stair.check(b));
        b.clearBookshelf();
        createRightStair(b);
//        b.print();
        assertTrue(stair.check(b));
    }

    @Test
    void StairCheckRowOne() {
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < Bookshelf.getColumns(); i++) {
            itemList.clear();
            itemList.add(new Item(Color.getRandomColor(), 0));
            b.insert(i, itemList);
        }
        createLeftStair(b);
        b.cli_print();
        assertTrue(stair.check(b));
        b.clearBookshelf();
        for (int i = 0; i < Bookshelf.getColumns(); i++) {
            itemList.clear();
            itemList.add(new Item(Color.getRandomColor(), 0));
            b.insert(i, itemList);
        }
        createRightStair(b);
        assertTrue(stair.check(b));
    }
}
