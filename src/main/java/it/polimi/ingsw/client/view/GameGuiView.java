package it.polimi.ingsw.client.view;

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
}
