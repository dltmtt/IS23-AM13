package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.GameView;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.GameModel;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

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

    public void showPersonalGoal(int card) throws IOException, ParseException {
        gameView.showPersonalCard(card);
    }

    public void showCommonGoal(List<String> cards, List<Integer> occurences, List<Integer> size, List<Boolean> horizontal) {
        for (int i = 0; i < cards.size(); i++) {
            try {
                gameView.showCommonGoal(cards.get(i), occurences.get(i), size.get(i), horizontal.get(i));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Integer> shoeMoveScreen() {
        return gameView.showMove();
    }

    public void showBoard(Board board) {
        gameView.showBoard(board);
    }

    public void showBookshelf(Bookshelf bookshelf) {
        gameView.showBookshelf(bookshelf);
    }
}
