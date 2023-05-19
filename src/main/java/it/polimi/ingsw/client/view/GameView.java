package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.Coordinates;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public interface GameView {

    String insertError = "The index must be between 0 and " + (Bookshelf.getColumns() - 1) + ": ";
    String insertUsernamePrompt = "Please, insert your username: ";
    String insertAgePrompt = "Please, insert your age: ";
    String firstGameQuestion = "Is this the first time you play this game?";
    String insertUsernameAgainPrompt = "Please, insert your username again: ";
    String welcomeMessage = "Welcome to...\n";
    String insertNumberOfPlayersPrompt = "Please, insert the total number of players: ";

    static void cleanScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    void loginProcedure();

    void run();

    String readUsername();

    int readNumber();

    String showLogin();

    void showMessage(String message);

    int promptAge();

    boolean promptFirstGame();

    int promptNumberOfPlayers();

    void showPersonalGoal(int card) throws IOException, ParseException;

    void showCommonGoal(String card, int occurrences, int size, boolean horizontal) throws IOException, ParseException;

    List<Coordinates> showPick();

    void showBoard(Board board);

    void showBookshelf(Bookshelf bookshelf);

    void showStartGame();

    boolean showRearrange(List<Item> items) throws IOException;

    int promptInsert();

    void showEndGame(List<String> winners);

    List<Integer> rearrange(List<Item> items) throws IOException;

    void showCurrentScore(int score);

    void showDisconnection();

    void waitingRoom();

    void startGame();

    void waitForTurn();

    void myTurn();

    void endGame();
}
