package it.polimi.ingsw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalGoalDeckTest {

    @Test
    /**
     * "calculatePoints" tests if the calculation of the points for the number of color reached in personal goal
     */

    void calculatePoints() {
        PersonalGoal personalGoal=new PersonalGoal(1);
        personalGoal.ColorReached();
        personalGoal.ColorReached();
        personalGoal.ColorReached();
        assertEquals(personalGoal.getPoints(), 4);

    }

    /**
     * "zeroPoints" tests the right setup of personal goal
     */
    @Test
    void ZeroPoints() {
        PersonalGoal personalGoal=new PersonalGoal(3);
        assertEquals(personalGoal.getPoints(), 0);
    }

    /**
     * "LessThan3Points" tests what happens if it is reached less than 3 color of the personal goal
     */
    @Test
    void LessThan3Points() {
        PersonalGoal personalGoal=new PersonalGoal(2);
        personalGoal.ColorReached();
        personalGoal.ColorReached();
        assertEquals(personalGoal.getPoints(),2);
    }

    /**
     * "getRightColor" tests the method "getcolor" of personal c
     */

    @Test
    void getRightColor() {
        PersonalGoal personalGoal=new PersonalGoal(0);

        assertEquals(personalGoal.getColor(new IntegerPair(3,0)),Color.YELLOW);
    }
}