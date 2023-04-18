package it.polimi.ingsw;

import it.polimi.ingsw.server.model.CommonGoal;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class GameTest {
    @Test
    public void GameTest() throws IOException, ParseException, IllegalAccessException {
        Game game = new Game(4);
        for (CommonGoal commonGoal : game.getCommonGoalDeck()) {
            System.out.println(commonGoal.getLayout().getInfo());
            System.out.println(commonGoal.getScoringList());
        }

    }
}
