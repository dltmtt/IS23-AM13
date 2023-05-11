package it.polimi.ingsw.server;

import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.Item;

import java.util.List;

public class ServerParser {

    public Message sendStartGame(int personalGoalCard, List<CommonGoal> commonGoalcard, Bookshelf bookshelf, Board livingroom) {
        return new Message(personalGoalCard, commonGoalcard, bookshelf, livingroom);
    }

    public Message sendMessage(String message) {
        return new Message(message);
    }

    public Message sendMessage1(String category, String type, int n) {
        return new Message(category, type, n);
    }

    public Message sendPosition(int position) {
        return new Message(position);
    }

    public Message sendTurn(int turn) {
        return new Message(turn);
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
            case "turn" -> "turn";
            case "pick" -> "pick";
            case "update" -> "update";
            case "insert" -> "insert";
            case "board" -> "board";
            case "sort" -> "sort";
            default -> null;
        };
    }

    public Message sendUpdate(String category, Bookshelf bookshelf, Board livingroom, int score) {
        return new Message(category, bookshelf, livingroom, score);
    }

    public Message sendBoard(String category, Board livingroom) {
        return new Message(category, livingroom);
    }

    public Message sendPicked(List<Item> picked) {
        return new Message(picked);
    }

    public List<Integer> getSort(Message message) {
        return message.getSort();
    }

    public List<Integer> getPick(Message message) {
        return message.getPick();
    }

    public int getPosition(Message message) {
        return message.getPosition();
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

    public int getInsert(Message message) {
        return message.getInsert();
    }
}
