package it.polimi.ingsw;

import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.PersonalGoal;
import it.polimi.ingsw.server.model.Player;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameTest {
    @Test
    public void GoalLoading() throws IOException, ParseException, IllegalAccessException {
        List<Player> players = new ArrayList<>(4);
        Game game = new Game(players);
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
        List<Player> players = new ArrayList<>(4);


        game = new Game(players);
        game.start();
//        game.getLivingRoom().cli_print();


    }
}
