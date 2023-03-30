package it.polimi.ingsw;

import it.polimi.ingsw.Models.Goal.PersonalGoal;
import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Utility.Coordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalGoalDeckTest {
    PersonalGoal personalGoal;

    @Test
    /**
     * "calculatePoints" tests if the calculation of the points for the number of color reached in personal goal.
     */
    void calculatePoints() {
        personalGoal = new PersonalGoal(1);
        personalGoal.ColorReached();
        personalGoal.ColorReached();
        personalGoal.ColorReached();
        assertEquals(personalGoal.getPoints(), 4);
    }

    /**
     * "zeroPoints" tests the right setup of personal goal.
     */
    @Test
    void ZeroPoints() {
        personalGoal = new PersonalGoal(3);
        assertEquals(personalGoal.getPoints(), 0);
    }

    /**
     * "LessThan3Points" tests what happens if it is reached less than 3 color of the personal goal.
     */
    @Test
    void LessThan3Points() {
        personalGoal = new PersonalGoal(2);
        personalGoal.ColorReached();
        personalGoal.ColorReached();
        assertEquals(personalGoal.getPoints(), 2);
    }

    /**
     * "getRightColor" tests the method "getColor" of personal c.
     */
    @Test
    void getRightColor() {
        personalGoal = new PersonalGoal(0);
        assertEquals(personalGoal.getColor(new Coordinates(3, 0)), Color.YELLOW);
    }
}
