package it.polimi.ingsw.server;

import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.CommonGoal;

import java.util.List;

public class ServerParser {


    public Message sendStartGame(int personalGoalCard, List<CommonGoal> commonGoalcard, Bookshelf bookshelf, Board livingroom) {
        return new Message(personalGoalCard, commonGoalcard, bookshelf, livingroom);
    }


    public Message sendMessage(String message) {
        return new Message(message);
    }

    public Message sendPosix(int posix) {
        return new Message(posix);
    }

    public String getMessageCategory(Message message) {
        String category = message.getCategory();
        return switch (category) {
            case "username" -> "username";
            case "age" -> "age";
            case "firstGame" -> "firstGame";
            case "numPlayer" -> "numPlayer";
            case "ready" -> "ready";
            case "startGame" -> "startGame";
            case "index" -> "index";
            default -> null;
        };
    }

    public int getPosix(Message message) {
        return message.getPosix();
    }

    public String getUsername(Message message) {
        return message.getUsername();
    }

    public int getAge(Message message) {
        return message.getAge();
    }

    public boolean getFirstGame(Message message) {
        return message.getFirstGame();
    }

    public int getNumPlayer(Message message) {
        return message.getNumPlayer();
    }
}
