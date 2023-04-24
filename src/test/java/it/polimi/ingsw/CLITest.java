package it.polimi.ingsw;

import it.polimi.ingsw.client.view.PersonalGoalview;
import it.polimi.ingsw.server.model.PersonalGoal;
import it.polimi.ingsw.server.model.Player;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CLITest {
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
}
