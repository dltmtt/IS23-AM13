package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonGoalTest {

    @Test
    void check() {
        Layout s = new Stair(2, 3, 3, 'c');
        CommonGoal c = new CommonGoal(3, s);


        assertEquals(c.getScoringList().get(0), 8);
        assertEquals(c.getScoringList().get(1), 6);
        assertEquals(c.getScoringList().get(2), 4);


        CommonGoal c1 = new CommonGoal(2, s);
        assertEquals(c1.getScoringList().get(0), 8);
        assertEquals(c1.getScoringList().get(1), 4);

        CommonGoal c2 = new CommonGoal(4, s);
        assertEquals(c2.getScoringList().get(0), 8);
        assertEquals(c2.getScoringList().get(1), 6);
        assertEquals(c2.getScoringList().get(2), 4);
        // assertEquals(c.getScoringList().get(3),2);
    }
}
