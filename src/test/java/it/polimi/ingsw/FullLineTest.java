package it.polimi.ingsw;

import it.polimi.ingsw.Models.CommonGoalLayout.FullLine;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FullLineTest extends BookshelfUtilities {
    Bookshelf b;

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
    public void threeColumMax3DiffCol() {
        Layout fullLine = new FullLine(1, 3, 3, false);
        createColumn(b, 2, 3);
        if (!fullLine.check(b)) {
            b.cli_print();
        }
        assertTrue(fullLine.check(b));
    }

    @Test
    public void twoColumn6Diff() {
        Layout fullLine = new FullLine(6, 6, 2, false);
        createColumn(b, 6, 2);
        if (!fullLine.check(b)) {
            b.cli_print();
        }
        assertTrue(fullLine.check(b));
    }

    @Test
    public void fourRowsMax3DiffCol() {
        Layout fullLine = new FullLine(1, 3, 3, true);
        createRow(b, 2, 3);
        if (!fullLine.check(b)) {
            b.cli_print();
        }
        assertTrue(fullLine.check(b));
    }
}
