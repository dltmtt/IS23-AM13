package it.polimi.ingsw;

import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Goal.PersonalGoal;
import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Utility.Coordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalGoalDeckTest {
    PersonalGoal personalGoal;
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

    // Tests the right setup of personal goal
    @Test
    void zeroPoints() {
        personalGoal = new PersonalGoal(3);
        assertEquals(0, personalGoal.getPoints(b));
    }

    // Tests what happens if it is reached less than 3 color of the personal goal
    @Test
    void lessThan3Points() {
        personalGoal = new PersonalGoal(2);
        personalGoal.colorReached();
        personalGoal.colorReached();
        assertEquals(2, personalGoal.getPoints(b));
    }

    // WARNING: this test fails (but I think it is a WIP).
    @Test
    void getRightColor() {
        personalGoal = new PersonalGoal(0);
        assertEquals(Color.YELLOW, personalGoal.getColor(new Coordinates(3, 0)));
    }
}
