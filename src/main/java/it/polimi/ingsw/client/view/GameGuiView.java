package it.polimi.ingsw.client.view;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public class GameGuiView extends GameView {

    @Override
    public String readUsername() {
        System.out.println("readUsername() not implemented yet in " + this.getClass().getName());
        return null;
    }

    @Override
    public void showLogin() {
        System.out.println("showLogin() not implemented yet in " + this.getClass().getName());
    }

    @Override
    public void showMessage(String message) {
        System.out.println("showMessage() not implemented yet in " + this.getClass().getName());
    }

    @Override
    public int readAge() {
        System.out.println("readAge() not implemented yet in " + this.getClass().getName());
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
    public void showPersonalCard(int card) {
        System.out.println("showPersonalCard() not implemented yet in " + this.getClass().getName());
    }

    @Override
    public void showCommonGoal(String card, int occurences, int size, boolean horizontal) throws IOException, ParseException {
        System.out.println("showCommonGoal() not implemented yet in " + this.getClass().getName());
    }

    @Override
    public List<Integer> showMove() {
        return null;
    }
}
