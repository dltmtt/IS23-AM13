package it.polimi.ingsw.client;

import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

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

    Message sendTurn(String category, int posix) {
        return new Message(category, posix);
    }

    Message sendInsert(int column) {
        return new Message("insert", "insert", column);
    }

    Message sendPick(int fromRow, int fromCol, int toRow, int toCol) {
        return new Message(fromRow, fromCol, toRow, toCol);
    }

    Message sendUpdate() {
        return new Message("update");
    }

    List<Item> getPicked(Message message) {
        return message.getPicked();
    }

    int getTurn(Message message) {
        return message.getTurn();
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

    public Bookshelf getBookshelf(Message message) {
        return message.getBookshelf();
    }

    public Board getBoard(Message message) throws IOException, ParseException {
        return message.getBoard();
    }

    public List<String> getCardstype(Message message) {
        return message.getCardType();
    }

    public List<Integer> getCardOccurences(Message message) {
        return message.getCardOccurences();
    }

    public List<Integer> getCardSize(Message message) {
        return message.getCardSize();
    }

    public List<Boolean> getCardHorizontal(Message message) {
        return message.getCardHorizotal();
    }

    public Message sendMessage(String message) {
        return new Message(message);
    }

    public int getPickedSize(Message message) {
        return message.getPickedSize();
    }

    public Message sendRearrange(List<Integer> sorted) {
        return new Message("sort", sorted);
    }
}
