package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.GameController;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.utils.SettingLoader;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public abstract class GameView {

    protected final String insertUsernamePrompt = "Please, insert your username: ";
    protected final String insertAgePrompt = "Please, insert your age: ";
    protected final String firstGamePrompt = "Is this the first time you play this game? ";
    protected final String insertUsernameAgainPrompt = "Please, insert your username again: ";
    protected final String welcomeMessage = "Welcome to My Shelfie!\n";
    protected final String insertNumberOfPlayersPrompt = "Please, insert the number of players you want to play with: ";
    GameController gameController;

    public GameView() {
        SettingLoader.loadBookshelfSettings();
    }

    public abstract String readUsername();

    public void setController(GameController gameController) {
        this.gameController = gameController;
    }

    public abstract void showLogin();

    public abstract void showMessage(String message);

    public abstract int readAge();

    public abstract int promptAge();

    public abstract boolean promptFirstGame();

    public abstract int promptNumberOfPlayers();

    public abstract void showPersonalCard(int card) throws IOException, ParseException;

    public abstract void showCommonGoal(String card, int occurences, int size, boolean horizontal) throws IOException, ParseException;

    public abstract List<Integer> showMove();

    public abstract void showBoard(Board board);

    public abstract void showBookshelf(Bookshelf bookshelf);
}
