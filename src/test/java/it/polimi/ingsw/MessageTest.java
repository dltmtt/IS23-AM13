package it.polimi.ingsw;

import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.PersonalGoal;
import it.polimi.ingsw.server.model.layouts.Group;
import it.polimi.ingsw.server.model.layouts.XShape;
import it.polimi.ingsw.utils.SettingLoader;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static it.polimi.ingsw.utils.BookshelfUtilities.randomFill;

public class MessageTest {

    @BeforeAll
    static void setup() {
        SettingLoader.loadBookshelfSettings();
    }

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
        Bookshelf bookshelf = new Bookshelf();
        randomFill(bookshelf);
        Message message = new Message(1, list, bookshelf, new Board(3));
        System.out.println(message.getJson());
    }

    @Test
    void BookshelfMessageTest() throws IOException, ParseException, IllegalAccessException {
        Bookshelf bookshelf = new Bookshelf();
        randomFill(bookshelf);
        List<CommonGoal> list = List.of(new CommonGoal(new Group(1, 1, 3, 4), 3));
        Message message = new Message(1, list, bookshelf, new Board(3));
        //        Bookshelf receivedBookshelf = new Bookshelf(message.getBookshelf());

        //        assert Arrays.deepEquals(bookshelf.getItems(), receivedBookshelf.getItems());
        //        bookshelf.cli_print();
        //        receivedBookshelf.cli_print();
    }

    @Test
    void emptyBookshelfMessage() throws IOException, ParseException {
        Bookshelf bookshelf = new Bookshelf();
        int numberOfPlayers = 3;
        List<CommonGoal> list = List.of(new CommonGoal(new Group(1, 1, 3, 4), numberOfPlayers), new CommonGoal(new XShape(1, 1, 3), numberOfPlayers));
        Message message = new Message(1, list, bookshelf, new Board(numberOfPlayers));
        //        Bookshelf receivedBookshelf = new Bookshelf(message.getBookshelf());
        //
        //        assert Arrays.deepEquals(bookshelf.getItems(), receivedBookshelf.getItems());
        //        bookshelf.cli_print();
        //        receivedBookshelf.cli_print();
    }
}
