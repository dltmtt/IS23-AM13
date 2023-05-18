package it.polimi.ingsw;

import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.PersonalGoal;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GameTest {

    @Test
    public void GoalLoading() {
        List<Player> players = new ArrayList<>(4);
        GameModel gameModel = new GameModel(players);

        for (CommonGoal commonGoal : gameModel.getCommonGoalDeck()) {
            System.out.println(commonGoal.getLayout().getInfo());
            System.out.println(commonGoal.getScoringList());
        }

        for (PersonalGoal personalGoal : gameModel.getPersonalGoalDeck()) {
            System.out.println(personalGoal.getPersonalGoal().toString());
        }
    }

    @Test
    public void BoardFilling() {
        GameModel gameModel;
        List<Player> players = new ArrayList<>(4);

        gameModel = new GameModel(players);
        gameModel.start();
        //        gameModel.getLivingRoom().cli_print();
    }
}
