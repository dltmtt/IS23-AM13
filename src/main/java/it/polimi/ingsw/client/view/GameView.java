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


/**
 * This is the interface used for the view (cli or gui)
 *
 * @see GameCliView
 * @see GuiView
 */
public interface GameView {
    String insertError = "The index must be between 0 and " + (Bookshelf.getColumns() - 1) + ": "; // TODO: make sure to load settings before this
    String insertUsernamePrompt = "Please, insert your username: ";
    String firstGameQuestion = "Is this the first time you play this game?";
    String insertUsernameAgainPrompt = "Please, insert your username again: ";
    String welcomeMessage = "Welcome to...\n";
    String insertNumberOfPlayersPrompt = "Please, insert the total number of players: ";

    /**
     * This method is used to starts the login procedure, it's different for cli and gui
     */
    void loginProcedure();

    /**
     * Parameter is useless in CLI but needed in GUI
     */

    void startView();

    /**
     * This method is used to show a general message
     *
     * @param message is the message to show
     */
    void showMessage(String message);

    /**
     * Allows the pick of the items from the board and sends the message to the server
     */
    void showPick();

    /**
     * This method is used to show the board
     *
     * @param board is the board to show
     */
    void showBoard(Board board);

    /**
     * This method is used to show the bookshelf
     *
     * @param bookshelf is the bookshelf to show
     */
    void showBookshelf(Bookshelf bookshelf);

    /**
     * This method is used for show the rearrange question
     *
     * @param items are the items to rearrange
     */
    boolean showRearrange(List<Item> items) throws IOException;

    /**
     * This method is used to show the insert question
     */
    void promptInsert();

    /**
     * This method is used to show the end game
     *
     * @param winners are the winners
     * @param losers  are the losers
     */
    void showEndGame(HashMap<String, Integer> winners, HashMap<String, Integer> losers);

    /**
     * This method is read the new sort from read line
     *
     * @param items are the items to sort
     */
    List<Integer> rearrange(List<Item> items) throws IOException;

    /**
     * This method is used to show the current score
     *
     * @param score is the current score
     */
    void showCurrentScore(int score);

    /**
     * This method is used to show the waiting room
     */
    void waitingRoom();

    /**
     * This method is used to show the game
     *
     * @param myGame is the game to show
     */
    void startGame(Message myGame);

    /**
     * This method is used to show the client's bookshelf
     *
     * @param bookshelves are the bookshelves
     */
    default void pickMyBookshelf(HashMap<String, Bookshelf> bookshelves) {
        String name = getClient().getUsername();
        showBookshelf(bookshelves.get(name));
    }

    /**
     * This method is used to show the other bookshelf
     *
     * @param bookshelves are the bookshelves
     */
    default void pickOtherBookshelf(HashMap<String, Bookshelf> bookshelves) {
        Set<String> otherBookshelves = bookshelves.keySet();
        otherBookshelves.remove(getClient().getUsername());
        for (String name : otherBookshelves) {
            showOtherBookshelf(bookshelves.get(name), name);
        }
    }

    /**
     * Getter for the client
     */

    Client getClient();

    /**
     * Setter for the client
     */
    void setClient(Client client);

    /**
     * This method is used to show the  bookshelf of a general client
     *
     * @param bookshelf is the bookshelf to show
     * @param name      is the name of the player that owns the bookshelf
     */
    void showOtherBookshelf(Bookshelf bookshelf, String name);


    /**
     * This method is used to show the username error message
     */
    void usernameError();

    /**
     * This method is used to show the login error message
     */
    void completeLoginError();

    /**
     * This method is used to show the number of players error message
     */
    void playerNumberError();

    /**
     * This method is used to show the number of players choice ì
     */
    void playerChoice();

    /**
     * This method is used to show the last round message
     */
    void showLastRound();

    /**
     * This method is used to show the game already started message
     */
    void showGameAlreadyStarted();

    /**
     * This method is used to show the remove player message
     */
    void showRemovePlayer();

    /**
     * This method is used to start the reaarrange procedure
     */
    void rearrangeProcedure(List<Item> items);

    /**
     * This method is used to update the score
     */
    void updateScore(List<Integer> topOfScoringList, List<Integer> score);

    /**
     * This method is used to load the language
     */
    void loadLanguage();

    /**
     * This method is used to set the only one boolean
     */
    void setTheOnlyOne(boolean b);

    /**
     * This method is used to show the waiting message
     */
    void showWaiting();

    /**
     * This method is used to disable the game in case of one player remaining
     */
    void disableGame();

    /**
     * This method is used to enable the game in case of one player reconnecting
     */
    void enableGame(Boolean currentTurn);

    /**
     * This method is used to show the initial connection message
     */
    void initiateConnection();

    /**
     * This method is used to show the connection success message
     */
    void connectionSuccess();

    /**
     * This method is used to show the connection error message
     */
    void connectionError();

    void endAlone();
}
