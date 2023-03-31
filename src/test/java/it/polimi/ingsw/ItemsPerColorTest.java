package it.polimi.ingsw;

import it.polimi.ingsw.Models.CommonGoalLayout.ItemsPerColor;
import it.polimi.ingsw.Models.CommonGoalLayout.Layout;
import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.TestUtility.BookshelfUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemsPerColorTest extends BookshelfUtilities {
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
    public void eightSameColorItems() {
        Layout L = new ItemsPerColor(8, 8);
        for (Color color : Color.values()) {
            b.clearBookshelf();
            createRandomElements(b, color, 8);
            assert L.check(b);
        }

        int goalNum = 6;
        // And the other way round
        for (Color color : Color.values()) {
            for (int numOfElements = 1; numOfElements < b.getSize(); numOfElements++) {
                b.clearBookshelf();
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
