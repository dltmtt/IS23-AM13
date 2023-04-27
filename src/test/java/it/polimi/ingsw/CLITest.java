package it.polimi.ingsw;

import it.polimi.ingsw.client.view.CommonGoalView;
import it.polimi.ingsw.client.view.PersonalGoalview;
import it.polimi.ingsw.server.model.PersonalGoal;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utils.SettingLoader;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CLITest {

    @BeforeEach
    void setUp() {
        SettingLoader.loadBookshelfSettings();
    }

    @Test
    void PersonalGoal() throws IOException, ParseException {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player1", 20, true, true, false));
        players.add(new Player("Player2", 20, false, false, false));
        players.add(new Player("Player3", 20, false, false, false));
        Game game = new Game(players);
        game.start();
        PersonalGoal personalGoal =
                game.getCurrentPlayer().getPersonalGoal();
        PersonalGoalview personalGoalview = new PersonalGoalview(personalGoal.getPersonalGoalCard());
        personalGoalview.printLayout();
    }

    @Test
    void CommonGoalAll() {
        CommonGoalView.itemsPerColorPrintLayout();
        CommonGoalView.squarePrintLayout();
        CommonGoalView.cornersPrintLayout();
        CommonGoalView.diagonalPrintLayout();
        CommonGoalView.fullLinePrintLayout(2, true);
        CommonGoalView.fullLinePrintLayout(4, true);
        CommonGoalView.fullLinePrintLayout(2, false);
        CommonGoalView.fullLinePrintLayout(3, false);
        CommonGoalView.groupPrintLayout(6, 2);
        CommonGoalView.groupPrintLayout(4, 4);
        CommonGoalView.stairPrintLayout();
        CommonGoalView.xShapePrintLayout();

    }

    @Test
    void CommonGoalSelected() {
        CommonGoalView.print("Stair", 0, 0, false);
        CommonGoalView.print("Corners", 0, 0, false);
        CommonGoalView.print("Diagonal", 0, 0, false);
        CommonGoalView.print("FullLine", 2, 0, true);
        CommonGoalView.print("Group", 6, 2, false);
        CommonGoalView.print("XShape", 0, 0, false);
        CommonGoalView.print("ItemsPerColor", 0, 0, false);
        CommonGoalView.print("Square", 0, 0, false);
        CommonGoalView.print("FullLine", 4, 0, true);
        CommonGoalView.print("FullLine", 2, 0, false);
        CommonGoalView.print("FullLine", 3, 0, false);
    }
}

