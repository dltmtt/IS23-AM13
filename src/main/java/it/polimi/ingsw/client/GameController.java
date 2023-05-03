package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.GameView;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Coordinates;

import java.util.HashMap;

public class GameController {
    private final GameModel gameModel;
    private final GameView gameView;
    private final Client client;

    public GameController(GameModel gameModel, GameView gameView, Client client) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.client = client;
        gameView.setController(this);
    }

    public void startGame() {
        gameModel.start();
    }

    public void login(String username) throws Exception {
        client.login(username);
    }

    public void showLoginScreen() {
        gameView.showLogin();
    }

    public int showAgeScreen() {
        return gameView.promptAge();
    }

    public boolean showFirstGamescreen() {
        return gameView.promptFirstGame();
    }

    public int showNumberOfPlayersScreen() {
        return gameView.promptNumberOfPlayers();
    }

    public void showPersonalGoal(HashMap<Coordinates, Color> card) {
        gameView.showPersonalCard(card);
    }
}
