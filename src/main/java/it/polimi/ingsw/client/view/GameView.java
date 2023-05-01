package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.GameController;

public abstract class GameView {
    protected final String insertUsernamePrompt = "Please, insert your username: ";
    protected final String insertAgePrompt = "Please, insert your age: ";
    protected final String firstGamePrompt = "Is this the first time you play this game? ";
    protected final String insertUsernameAgainPrompt = "Please, insert your username again: ";
    protected final String welcomeMessage = "Welcome to My Shelfie!\n";
    GameController gameController;

    public abstract String readUsername();

    public void setController(GameController gameController) {
        this.gameController = gameController;
    }

    public abstract void showLogin();

    public abstract void showMessage(String message);

    public abstract int readAge();

    public abstract int promptAge();

    public abstract boolean promptFirstGame();
}
