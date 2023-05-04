package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.server.model.layouts.Layout;
import it.polimi.ingsw.server.model.layouts.Stair;
import it.polimi.ingsw.utils.BookshelfUtilities;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static it.polimi.ingsw.utils.SettingLoader.BASE_PATH;
import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StairTest {

    Bookshelf b;
    Layout stair;

    @BeforeEach
    void setup() {
        // Read the settings from the properties file
        int rowsSetting;
        int colsSetting;

        Properties prop = new Properties();
        // In case the file is not found, the default values will be used
        try (InputStream input = new FileInputStream(BASE_PATH + "settings.properties")) {
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
        stair = new Stair(1, 6, Bookshelf.getColumns());
    }

    @Test
    void StairCheckRowZero() {
        BookshelfUtilities.createLeftStair(b);
        assertTrue(stair.check(b));
        b.clearBookshelf();
        BookshelfUtilities.createRightStair(b);
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
        BookshelfUtilities.createLeftStair(b);
        //        b.cli_print();
        assertTrue(stair.check(b));
        b.clearBookshelf();
        for (int i = 0; i < Bookshelf.getColumns(); i++) {
            itemList.clear();
            itemList.add(new Item(Color.getRandomColor(), 0));
            b.insert(i, itemList);
        }
        BookshelfUtilities.createRightStair(b);
        assertTrue(stair.check(b));
    }
}
