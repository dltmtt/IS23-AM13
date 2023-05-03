package it.polimi.ingsw.client;

import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.Item;

import java.io.Serializable;
import java.util.Optional;

public class ClientParser implements Serializable {

    Message sendUsername(String username) {
        return new Message("username", username, 0, false, 0);
    }

    Message sendAge(int age) {
        return new Message("age", "", age, false, 0);
    }

    Message sendFirstGame(boolean firstGame) {
        return new Message("firstGame", "", 0, firstGame, 0);
    }

    Message sendNumPlayer(int numPlayer) {
        return new Message("numPlayer", "", 0, false, numPlayer);
    }

    String getMessage(Message message) {
        return message.getCategory();
    }

    Message sendPosix(int posix) {
        return new Message(posix);
    }

    int getPosix(Message message) {
        return message.getPosix();
    }

    public Message sendReady() {
        return new Message("ready", "", 0, false, 0);
    }

    public Message game() {
        return new Message("startGame");
    }

    public int getPersonalGoal(Message message) {
        return message.getPersonalGoal();
    }

    public String getCommonGoalLayout(Message message, int i) {
        return message.getCommonGoalLayout(i);
    }

    public Optional<Item>[][] getBookshelf(Message message) {
        return message.getBookshelf();
    }

    public Item[][] getBoard(Message message) {
        return message.getBoard();
    }


}
