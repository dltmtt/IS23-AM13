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


    // Tests if the calculation of the points for the number of color reached in personal goal
    @Test
    void calculatePoints() {
        personalGoal = new PersonalGoal(1);
        assertEquals(personalGoal.getPoints(b), 4);
    }

    // Tests the right setup of personal goal
    @Test
    void zeroPoints() {
        personalGoal = new PersonalGoal(3);
        assertEquals(personalGoal.getPoints(b), 0);
    }

    // Tests what happens if it is reached less than 3 color of the personal goal
    @Test
    void lessThan3Points() {
        personalGoal = new PersonalGoal(2);
        personalGoal.colorReached();
        personalGoal.colorReached();
        assertEquals(personalGoal.getPoints(b), 2);
    }

    // Tests the method getColor of personalGoal
    @Test
    void getRightColor() {
        personalGoal = new PersonalGoal(0);
        assertEquals(personalGoal.getColor(new Coordinates(3, 0)), Color.YELLOW);
    }
}
