package it.polimi.ingsw;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.layouts.FullLine;
import it.polimi.ingsw.server.model.layouts.Group;
import it.polimi.ingsw.server.model.layouts.XShape;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void GameModelTest() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("pippo", 0, false, true, false));
        players.add(new Player("pluto", 1, false, false, false));
        Board board = new Board(players.size());
        List<CommonGoal> commonGoalList = new ArrayList<>();
        commonGoalList.add(new CommonGoal(new XShape(1, 1, 3), 2));
        commonGoalList.add(new CommonGoal(new FullLine(1, 1, 2, true), 2));

        GameModel gameModel = new GameModel(players, board, commonGoalList);
        assertEquals(gameModel.getPlayers(), players);
        players.add(new Player("paperino", 2, false, false, false));
        gameModel.setPlayers(players);
        assertEquals(gameModel.getPlayers(), players);
        commonGoalList.add(new CommonGoal(new Group(1, 1, 2, 2), 2));
        gameModel.setGame(commonGoalList);
        //assertEquals(gameModel.getCommonGoalDeck(), commonGoalList);
        gameModel.setTheGameEnded(true);
        assertEquals(gameModel.isTheGameEnded(), true);
        assertEquals(gameModel.getLivingRoom(), board);
        gameModel.getTopScoringPoints();

    }

}
