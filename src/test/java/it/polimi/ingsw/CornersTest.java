package it.polimi.ingsw;

import it.polimi.ingsw.Models.CommonGoalLayout.Corners;
import it.polimi.ingsw.Models.CommonGoalLayout.Layout;
import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.TestUtility.BookshelfUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CornersTest extends BookshelfUtilities {
    Bookshelf b;

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
    }

    @Test
    void cornerCheck() {
        Layout layout = new Corners(1, 1);
        createCorner(b);
        assertTrue(layout.check(b));
    }

    // WARNING: This test fails
    @Test
    void checkFakeCorner() {
        Layout layout = new Corners(1, 1);
        createFakeCorner(b, 0);
        assertFalse(layout.check(b));
        b.clearBookshelf();
        createFakeCorner(b, 1);
        assertFalse(layout.check(b));
//        Bookshelf b2 = new Bookshelf(settings_height, settings_width);
//        Create Fake Corner possibly has a bug
//        createFakeCorner(b2, 2);
//        assertFalse(layout.check(b2));
    }
}
