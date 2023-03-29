package it.polimi.ingsw;

import it.polimi.ingsw.Models.CommonGoalLayout.Diagonal;
import it.polimi.ingsw.Models.CommonGoalLayout.Layout;
import it.polimi.ingsw.Models.Goal.CommonGoal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonGoalTest {
    @Test
    void getScoringListCheck() {
        Layout s = new Diagonal(2, 1, 3);
        CommonGoal c = new CommonGoal(s);


        assertEquals(c.getScoringList().get(0), 8);
        assertEquals(c.getScoringList().get(1), 6);
        assertEquals(c.getScoringList().get(2), 4);


        CommonGoal c1 = new CommonGoal(s);
        assertEquals(c1.getScoringList().get(0), 8);
        assertEquals(c1.getScoringList().get(1), 4);

        CommonGoal c2 = new CommonGoal(s);
        assertEquals(c2.getScoringList().get(0), 8);
        assertEquals(c2.getScoringList().get(1), 6);
        assertEquals(c2.getScoringList().get(2), 4);
//        assertEquals(c.getScoringList().get(3),2);
    }

    @Test
    void getScoringCheck() throws IndexOutOfBoundsException {
        Layout s = new Diagonal(2, 1, 3);
        CommonGoal c = new CommonGoal(s);
        assertEquals(c.getScoring(), 8);
        assertEquals(c.getScoring(), 6);
        assertEquals(c.getScoring(), 4);
    }
}
