package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.Coordinates;
import it.polimi.ingsw.utils.SettingLoader;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public abstract class GameView {

    public final String insertError = "The index must be between 0 and " + (Bookshelf.getColumns() - 1) + ": ";
    protected final String insertUsernamePrompt = "Please, insert your username: ";
    protected final String insertAgePrompt = "Please, insert your age: ";
    protected final String firstGameQuestion = "Is this the first time you play this game?";
    protected final String insertUsernameAgainPrompt = "Please, insert your username again: ";
    protected final String welcomeMessage = "Welcome to...\n";
    protected final String insertNumberOfPlayersPrompt = "Please, insert the total number of players: ";

    public GameView() {
        SettingLoader.loadBookshelfSettings();
    }

    public static void cleanScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public abstract String readUsername();

    public abstract int readNumber();

    public abstract String showLogin();

    public abstract void showMessage(String message);

    public abstract int promptAge();

    public abstract boolean promptFirstGame();

    public abstract int promptNumberOfPlayers();

    public abstract void showPersonalGoal(int card) throws IOException, ParseException;

    public abstract void showCommonGoal(String card, int occurrences, int size, boolean horizontal) throws IOException, ParseException;

    public abstract List<Coordinates> showPick();

    public abstract void showBoard(Board board);

    public abstract void showBookshelf(Bookshelf bookshelf);

    public abstract void showStartGame();

    public abstract boolean showRearrange(List<Item> items) throws IOException;

    public abstract int promptInsert();

    public abstract void showEndGame(List<String> winners);

    public abstract List<Integer> rearrange(List<Item> items) throws IOException;

    public abstract void showCurrentScore(int score);

    public abstract void showDisconnection();
}
