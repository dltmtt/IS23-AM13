package it.polimi.ingsw;

import it.polimi.ingsw.commons.Message;
import org.junit.jupiter.api.Test;

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
}
