package it.polimi.ingsw;

import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Goal.PersonalGoal;
import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Utility.Coordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalGoalDeckTest {
    PersonalGoal personalGoal;

    // Tests if the calculation of the points for the number of color reached in personal goal
    @Test
    void calculatePoints() {
        personalGoal = new PersonalGoal(1);
        Bookshelf b = new Bookshelf();
        assertEquals(personalGoal.getPoints(b), 4);
    }

    // Tests the right setup of personal goal
    @Test
    void zeroPoints() {
        personalGoal = new PersonalGoal(3);
        assertEquals(personalGoal.getPoints(new Bookshelf()), 0);
    }

    // Tests what happens if it is reached less than 3 color of the personal goal
    @Test
    void lessThan3Points() {
        personalGoal = new PersonalGoal(2);
        personalGoal.colorReached();
        personalGoal.colorReached();
        assertEquals(personalGoal.getPoints(new Bookshelf()), 2);
    }

    // Tests the method getColor of personalGoal
    @Test
    void getRightColor() {
        personalGoal = new PersonalGoal(0);
        assertEquals(personalGoal.getColor(new Coordinates(3, 0)), Color.YELLOW);
    }
}
