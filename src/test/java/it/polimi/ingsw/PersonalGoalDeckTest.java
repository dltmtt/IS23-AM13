package it.polimi.ingsw;

import it.polimi.ingsw.model.game.Bookshelf;
import it.polimi.ingsw.model.goal.PersonalGoal;
import it.polimi.ingsw.model.item.Color;
import it.polimi.ingsw.model.utility.Coordinates;
import it.polimi.ingsw.utils.BookshelfUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
