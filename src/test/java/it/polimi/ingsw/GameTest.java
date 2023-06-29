package it.polimi.ingsw;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.layouts.Group;
import it.polimi.ingsw.server.model.layouts.XShape;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        //        gameModel.getBoard().cli_print();
    }

    @Test
    public void GameModelTest() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("pippo", false, true, false));
        players.add(new Player("pluto", false, false, false));
        Bookshelf bookshelf = new Bookshelf(6, 5);
        players.get(0).setBookshelf(bookshelf);
        Board board = new Board(players.size());
        List<CommonGoal> commonGoalList = new ArrayList<>();
        commonGoalList.add(new CommonGoal(new XShape(1, 1, 3), 2));
        List<Integer> scoringList = new ArrayList<>();
        assertEquals(8, commonGoalList.get(0).getScoring());
        scoringList.add(4);
        commonGoalList.get(0).setScoringList(scoringList);

        Player.setCommonGoal(commonGoalList);
        GameModel gameModel = new GameModel(players, board, commonGoalList);
        assertEquals(gameModel.getPlayers(), players);
        players.add(new Player("paperino", false, false, false));
        gameModel.setPlayers(players);
        assertEquals(gameModel.getPlayers(), players);
        commonGoalList.add(new CommonGoal(new Group(1, 1, 2, 2), 2));
        gameModel.setGame(commonGoalList);
        // assertEquals(gameModel.getCommonGoalDeck(), commonGoalList);
        gameModel.setHasGameEnded(true);
        assertTrue(gameModel.isHasGameEnded());
        assertEquals(gameModel.getBoard(), board);
        gameModel.getTopScoringPoints();

        List<Item> items = new ArrayList<>();
        items.add(new Item(Color.BLUE, 1));

        gameModel.setCurrentPlayer(players.get(0));
        gameModel.move(items, 0);
        assertEquals(gameModel.getCurrentPlayer(), players.get(0));

        CommonGoal commonGoal = new CommonGoal(new XShape(1, 1, 3), 0);
        assertEquals(0, commonGoal.getScoring());
    }
}
