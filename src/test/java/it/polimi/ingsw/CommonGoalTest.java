package it.polimi.ingsw;

import it.polimi.ingsw.model.commongoallayout.Diagonal;
import it.polimi.ingsw.model.commongoallayout.Layout;
import it.polimi.ingsw.model.goal.CommonGoal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonGoalTest {
    @Test
    void getScoringListCheck() {
        Layout s = new Diagonal(2, 1, 3);

        CommonGoal c2 = new CommonGoal(s);
        c2.setScoringList(2);
        assertEquals(4, c2.getScoringList().get(1));
        assertEquals(8, c2.getScoringList().get(0));

        CommonGoal c3 = new CommonGoal(s);
        c3.setScoringList(3);
        assertEquals(4, c3.getScoringList().get(2));
        assertEquals(6, c3.getScoringList().get(1));
        assertEquals(8, c3.getScoringList().get(0));

        CommonGoal c4 = new CommonGoal(s);
        c4.setScoringList(4);
        assertEquals(2, c4.getScoringList().get(3));
        assertEquals(4, c4.getScoringList().get(2));
        assertEquals(6, c4.getScoringList().get(1));
        assertEquals(8, c4.getScoringList().get(0));
    }

    @Test
    void getScoringCheck() throws IndexOutOfBoundsException {
        Layout s = new Diagonal(2, 1, 3);

        CommonGoal c4 = new CommonGoal(s);
        c4.setScoringList(4);
        assertEquals(8, c4.getScoring());
        assertEquals(6, c4.getScoring());
        assertEquals(4, c4.getScoring());
        assertEquals(2, c4.getScoring());
    }
}
