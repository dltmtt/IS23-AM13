package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.PersonalGoal;
import it.polimi.ingsw.utils.BookshelfUtilities;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Coordinates;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalGoalDeckTest {
    PersonalGoal personalGoal;
    Bookshelf b;

    @BeforeAll
    static void setupAll() {
        BookshelfUtilities.loadSettings();
    }

    @BeforeEach
    void setup() {
        b = new Bookshelf(Bookshelf.getRows(), Bookshelf.getColumns());
    }

    // Tests the right setup of personal goal
    @Test
    void zeroPoints() throws IOException, ParseException {
        personalGoal = new PersonalGoal(3);
        assertEquals(0, personalGoal.getPoints(b));
    }

    // Tests what happens if it is reached less than 3 color of the personal goal
    @Test
    void lessThan3Points() throws IOException, ParseException {
        personalGoal = new PersonalGoal(2);
        personalGoal.colorReached();
        personalGoal.colorReached();
        assertEquals(2, personalGoal.getPoints(b));
    }

    // WARNING: this test fails (but I think it is a WIP).
    @Test
    void getRightColor() throws IOException, ParseException {
        personalGoal = new PersonalGoal(0);
        assertEquals(Color.YELLOW, personalGoal.getColor(new Coordinates(0, 3)));
    }
}
