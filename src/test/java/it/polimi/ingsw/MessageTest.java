package it.polimi.ingsw;

import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.PersonalGoal;
import it.polimi.ingsw.server.model.layouts.Group;
import it.polimi.ingsw.utils.SettingLoader;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class MessageTest {

    @Test
    void categoryTest() {
        Message message = new Message("username", "Valeria", 0, false, 0);
        assert message.getCategory().equals("username");
    }

    @Test
    void argumentTest() {
        Message message = new Message("username", "Valeria", 0, false, 0);
        assert message.getUsername().equals("Valeria");
    }

    @Test
    void ageTest() {
        Message message = new Message("age", "Valeria", 22, false, 0);
        assert message.getAge() == 22;
    }

    @Test
    void firstGameTest() {
        Message message = new Message("firstGame", "Valeria", 0, true, 0);
        assert message.getFirstGame();
    }

    @Test
    void commonGoalMessageTest() throws IOException, ParseException {
        PersonalGoal personalGoal = SettingLoader.loadSpecificPersonalGoal(1);
        CommonGoal commonGoal1 = new CommonGoal(new Group(1, 1, 3, 4), 3);
        List<CommonGoal> list = List.of(commonGoal1);
        Message message = new Message(1, list, new Bookshelf(), new Board(3));
    }
}
