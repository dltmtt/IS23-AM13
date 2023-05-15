package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.GameView;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.Coordinates;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public class GameController {

    private final GameModel gameModel;
    private final GameView gameView;

    public GameController(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        gameView.setController(this);
    }

    public void startGame() {
        gameView.showStartGame();
    }

    public String showLoginScreen() {
        return gameView.showLogin();
    }

    public int showAgeScreen() {
        return gameView.promptAge();
    }

    public boolean showFirstGameScreen() {
        return gameView.promptFirstGame();
    }

    public int showNumberOfPlayersScreen() {
        return gameView.promptNumberOfPlayers();
    }

    public void showPersonalGoal(int card) throws IOException, ParseException {
        gameView.showPersonalGoal(card);
    }

    public void showCommonGoal(List<String> cards, List<Integer> occurrences, List<Integer> size, List<Boolean> horizontal) {
        for (int i = 0; i < cards.size(); i++) {
            try {
                gameView.showCommonGoal(cards.get(i), occurrences.get(i), size.get(i), horizontal.get(i));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Coordinates> showPickScreen() {
        return gameView.showPick();
    }

    public boolean showRearrangeScreen(List<Item> items) throws IOException {
        return gameView.showRearrange(items);
    }

    public List<Integer> rearrangeScreen(List<Item> items) throws IOException {
        return gameView.rearrange(items);
    }

    public void showBoard(Board board) {
        GameView.cleanScreen();
        gameView.showBoard(board);
    }

    public void showBookshelf(Bookshelf bookshelf) {
        gameView.showBookshelf(bookshelf);
    }

    public boolean isValidMove(Board board, List<Coordinates> pickedFromTo) {
        return true;
    }

    public int showInsertScreen() {
        return gameView.promptInsert();
    }

    public void showScore(int score) {
        gameView.showCurrentScore(score);
    }

    public void showEndGame(List<String> winners) {
        gameView.showEndGame(winners);
    }

    public void showDisconnection() {
        gameView.showDisconnection();
    }
}
