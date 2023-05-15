package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.Coordinates;

import java.util.List;

public class GameGuiView extends GameView {

    @Override
    public String readUsername() {
        System.out.println("readUsername() not implemented yet in " + this.getClass().getName());
        return null;
    }

    @Override
    public String showLogin() {
        System.out.println("showLogin() not implemented yet in " + this.getClass().getName());
        return null;
    }

    @Override
    public void showMessage(String message) {
        System.out.println("showMessage() not implemented yet in " + this.getClass().getName());
    }

    @Override
    public int readNumber() {
        System.out.println("readNumber() not implemented yet in " + this.getClass().getName());
        return 0;
    }

    @Override
    public int promptAge() {
        System.out.println("showAge() not implemented yet in " + this.getClass().getName());
        return 0;
    }

    @Override
    public boolean promptFirstGame() {
        return true;
    }

    @Override
    public int promptNumberOfPlayers() {
        return 0;
    }

    @Override
    public void showPersonalGoal(int card) {
        System.out.println("showPersonalGoal() not implemented yet in " + this.getClass().getName());
    }

    @Override
    public void showCommonGoal(String card, int occurrences, int size, boolean horizontal) {
        System.out.println("showCommonGoal() not implemented yet in " + this.getClass().getName());
    }

    @Override
    public List<Coordinates> showPick() {
        return null;
    }

    @Override
    public void showBoard(Board board) {

    }

    @Override
    public void showBookshelf(Bookshelf bookshelf) {

    }

    @Override
    public void showStartGame() {

    }

    @Override
    public void showEndGame(List<String> winners) {

    }

    @Override
    public boolean showRearrange(List<Item> items) {
        return false;
    }

    @Override
    public List<Integer> rearrange(List<Item> items) {
        return null;
    }

    @Override
    public void showCurrentScore(int score) {

    }

    @Override
    public void showDisconnection() {

    }

    @Override
    public int promptInsert() {
        return 0;
    }
}
