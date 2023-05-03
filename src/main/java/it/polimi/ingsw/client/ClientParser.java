package it.polimi.ingsw.client;

import it.polimi.ingsw.commons.Message;

public class ClientParser {

    Message sendUsername(String username) {
        return new Message("username", username, 0, false, 0);
    }

    Message senAge(int age) {
        return new Message("age", "", age, false, 0);
    }

    Message sendfirstGame(boolean firstGame) {
        return new Message("firstGame", "", 0, firstGame, 0);
    }

    Message sendNumPlayer(int numPlayer) {
        return new Message("numPlayer", "", 0, false, numPlayer);
    }

    String getMessage(Message message) {
        return message.getMessage();
    }


}
