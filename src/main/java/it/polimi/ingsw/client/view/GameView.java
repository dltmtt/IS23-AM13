package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.GameController;

public abstract class GameView {
    protected final String insertUsernamePrompt = "Please, insert your username: ";
    protected final String insertUsernameAgainPrompt = "Please, insert your username again: ";
    protected final String welcomeMessage = "Welcome to My Shelfie!\n";
    GameController gameController;

    public abstract String readUsername();

    public void setController(GameController gameController) {
        this.gameController = gameController;
    }

    public abstract void showLogin();

    public abstract void showMessage(String message);
}
