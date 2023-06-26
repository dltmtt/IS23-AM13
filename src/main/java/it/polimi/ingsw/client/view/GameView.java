package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface GameView {

    String insertError = "The index must be between 0 and " + (Bookshelf.getColumns() - 1) + ": "; // TODO: make sure to load settings before this
    String insertUsernamePrompt = "Please, insert your username: ";
    String firstGameQuestion = "Is this the first time you play this game?";
    String insertUsernameAgainPrompt = "Please, insert your username again: ";
    String welcomeMessage = "Welcome to...\n";
    String insertNumberOfPlayersPrompt = "Please, insert the total number of players: ";

    void loginProcedure();

    // Parameter is useless in CLI but needed in GUI
    void startView(Client client);

    void showMessage(String message);

    /**
     * Allows the pick of the items from the board and sends the message to the server
     */
    void showPick();

    void showBoard(Board board);

    void showBookshelf(Bookshelf bookshelf);

    boolean showRearrange(List<Item> items) throws IOException;

    void promptInsert();

    // void showEndGame(List<String> winners, List<Integer> Wscores, List<String> otherPlayers, List<Integer> otherScores);

    void showEndGame(HashMap<String, Integer> winners, HashMap<String, Integer> losers);

    List<Integer> rearrange(List<Item> items) throws IOException;

    void showCurrentScore(int score);

    void waitingRoom();

    void startGame(Message myGame);

    default void pickMyBookshelf(HashMap<String, Bookshelf> bookshelves) {
        String name = getClient().getUsername();
        showBookshelf(bookshelves.get(name));
    }

    default void pickOtherBookshelf(HashMap<String, Bookshelf> bookshelves) {
        Set<String> otherBookshelves = bookshelves.keySet();
        otherBookshelves.remove(getClient().getUsername());
        for (String name : otherBookshelves) {
            showOtherBookshelf(bookshelves.get(name), name);
        }
    }

    Client getClient();

    void setClient(Client client);

    void showOtherBookshelf(Bookshelf bookshelf, String name);

    void usernameError();

    void completeLoginError();

    void playerNumberError();

    void playerChoice();

    void showLastRound();

    void showGameAlreadyStarted();

    void showRemovePlayer();

    void showDisconnection(List<String> disconnectedPlayers);

    void rearrangeProcedure(List<Item> items);

    void updateScore(List<Integer> topOfScoringList, List<Integer> score);

    void loadLanguage();

    void setTheOnlyOne(boolean b);

    void showWaiting();
}
