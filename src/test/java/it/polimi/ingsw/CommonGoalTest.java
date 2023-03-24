package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonGoalTest {

    @Test
    //void check() {
    void getScoringListcheck() {
        Layout s = new Diagonal(2, 1, 3, false);
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

    @Test
    void getScoringcheck() throws IndexOutOfBoundsException {
        Layout s = new Diagonal(2, 1, 3, false);
        CommonGoal c = new CommonGoal(3, s);
        assertEquals(c.getScoring(), 8);
        assertEquals(c.getScoring(), 6);
        assertEquals(c.getScoring(), 4);
    }
}
