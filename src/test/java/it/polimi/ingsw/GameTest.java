package it.polimi.ingsw;

import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.PersonalGoal;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class GameTest {
    @Test
    public void GoalLoading() throws IOException, ParseException, IllegalAccessException {
        Game game = new Game(4);
        for (CommonGoal commonGoal : game.getCommonGoalDeck()) {
            System.out.println(commonGoal.getLayout().getInfo());
            System.out.println(commonGoal.getScoringList());
        }
        for (PersonalGoal personalGoal : game.getPersonalGoalDeck()) {
            System.out.println(personalGoal.getPersonalGoal().toString());
        }

    }

    @Test
    public void BoardFilling() {
        Game game;
        try {
            game = new Game(4);
        } catch (IllegalAccessException | IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        game.start();
        game.getLivingRoom().cli_print();


    }
}
