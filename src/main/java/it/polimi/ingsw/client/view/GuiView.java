package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.MyShelfie;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class is the GUI view, it extends Application and implements GameView.
 * It contains all the methods to load the scenes and to show them.
 * @see GameView
 */
public class GuiView extends Application implements GameView {
    /**
     * This is the static reference to the client, to be placed in the controller constructors (used to send messages).
     */
    public static Client client; // Static reference to the client, in order to use the sendMessage() function

    /**
     * This is the static reference to the gui, to be placed in the controller constructors (used to show scenes).
     */
    public static GuiView gui;

    /**
     * This is the static reference to the loaded scenes.
     */
    public static Scene loginScene, playerNumberScene, waitingRoomScene, gameScene, endGameScene, refusedScene; // Loaded scenes

    /**
     * This is the Main stage
     */
    public static Stage stage;
    /**
     * This is the static reference to the game controller.
     * @see GameGuiController
     */
    public static GameGuiController gameController;
    private static LoginGuiController loginController;
    private static EndGameGuiController endGameController;
    /**
     * This is the resource bundle for the language
     */
    public ResourceBundle bundle;
    private boolean theOnlyOne = false;

    /**
     * Launches the GUI.
     */
    @Override
    public void startView() {
        // CAUTION: if any attributes need to be set, do so in the startView method,
        // because startView has the launch() command, which creates another instance of GuiView.
        launch();
        // Instruction executed after closing GUI window
        System.exit(0);
    }

    /**
     * This is the starting method for GUI, loads all scene loader and loads all the scenes.
     * It calls method <code>loginProcedure()</code>.
     *
     * @param stage internal parameter, set by <code>launch()</code>.
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("My Shelfie");
        try {
            stage.getIcons().add(new Image(Objects.requireNonNull(GuiView.class.getResource("graphics/publisher_material/icon.png")).openStream()));
        } catch (IOException e) {
            System.err.println("Icon not found");
        }
        GuiView.gui = this;
        GuiView.stage = stage;
        setClient(MyShelfie.client);

        // Scene loader
        FXMLLoader loginSceneLoader = new FXMLLoader(GuiView.class.getResource("newLogin2.fxml"));
        loginController = new LoginGuiController();
        loginSceneLoader.setController(loginController);
        try {
            loginScene = new Scene(loginSceneLoader.load());
        } catch (IOException e) {
            System.err.println("Failed to load newLogin2.fxml");
            throw new RuntimeException(e);
        }

        loginProcedure();
    }

    /**
     * Sets the main stage to the gameScene.
     *
     * @param message the message containing the game information.
     */
    @Override
    public void startGame(Message message) {
        Platform.runLater(() -> {
            gameController.showGame(message);
            stage.setScene(gameScene);
            stage.show();
        });
    }

    /**
     * Sets the waiting room scene.
     */
    @Override
    public void waitingRoom() {
        Platform.runLater(() -> {
            stage.setScene(waitingRoomScene);
            stage.show();
        });
    }

    /**
     * Sets the login scene.
     */
    @Override
    public void loginProcedure() {
        Platform.runLater(() -> {
            loginController.hideWaiting();
            loginController.setSettings();
            stage.setScene(loginScene);
            stage.show();
        });
    }

    /**
     * Shows a message in a label inside the game scene.
     *
     * @param message the message to be shown.
     */
    @Override
    public void showMessage(String message) {
        Platform.runLater(() -> gameController.showMessage(message));
    }

    /**
     * Enables the items with one free side, allowing the player to pick them.
     */
    @Override
    public void showPick() {
        Platform.runLater(gameController::enableItemsWithOneFreeSide);
    }

    /**
     * Updates the board model in the GUI interface, then it updates the GUI accordingly.
     *
     * @param board the board to be shown.
     */
    @Override
    public void showBoard(Board board) {
        Platform.runLater(() -> {
            GameGuiController.boardModel = board;
            gameController.updateBoard();
        });
    }

    /**
     * This particular implementation of showRearrange always returns true,
     * because the GUI doesn't really need to ask the user if they want to rearrange
     * the items.
     *
     * @param items the items to be rearranged.
     * @return true (always).
     */
    @Override
    public boolean showRearrange(List<Item> items) {
        return true;
    }

    /**
     * Enables the available columns, allowing the player to insert the item they picked.
     */
    @Override
    public void promptInsert() {
        Platform.runLater(gameController::enableInsert);
    }


    /**
     * Shows the end game scene.
     * @param winners the winners of the game.
     * @param losers the losers of the game.
     */
    @Override
    public void showEndGame(HashMap<String, Integer> winners, HashMap<String, Integer> losers) {
        Platform.runLater(() -> {
            endGameController.setWinner(winners, losers);
            stage.setScene(endGameScene);
            stage.show();
        });
    }

    /**
     * This is the rearrange method, not used in the GUI.
     * @param items the items to be rearranged.
     * @return null, because the GUI handles the rearrangement itself, so this method shouldn't be called.
     * @see GameGuiController#rearrange(List).
     */
    @Override
    public List<Integer> rearrange(List<Item> items) {
        return null;
    }


    /**
     * Shows the current score of the player, not used in the GUI as it's handled by the GUI itself
     *
     * @param score the current score of the player
     * @see GameGuiController#updatePlayerPoints(List)
     */
    @Override
    public void showCurrentScore(int score) {

    }

    /**
     * Shows the other player's bookshelf
     *
     * @param bookshelf the bookshelf to be shown
     */
    @Override
    public void showOtherBookshelf(Bookshelf bookshelf, String name) {
        Platform.runLater(() -> {
            gameController.updateOtherBookshelves(bookshelf, name);
        });
    }

    /**
     * Shows the player's bookshelf
     *
     * @param bookshelf the bookshelf to be shown
     */
    @Override
    public void showBookshelf(Bookshelf bookshelf) {
        Platform.runLater(() -> {
            GameGuiController.bookshelfModel = bookshelf;
            gameController.updateBookshelf(gameController.bookshelfGrid, bookshelf, true, GameGuiController.MAIN_ITEM_SIZE);
        });
    }

    /**
     * Sets an error message in the login scene
     */
    @Override
    public void usernameError() {
        Platform.runLater(() -> loginController.usernameAlreadyTaken());
        // loginController.usernameAlreadyTaken();
    }

    /**
     * Sets an error message in the login scene.
     *
     * @see #usernameError() (GUI-wise there's no difference).
     */
    @Override
    public void completeLoginError() {
        // GUI-wise there's no difference
        usernameError();
    }

    /**
     * Sets an error message in the player number scene, but since the number of players is set by a slider,
     * which minimum and maximum are valid, this method is never called and does nothing.
     */
    @Override
    public void playerNumberError() {
        // since the number of players is set by a slider, which minimum and maximum are valid, there is no prob
    }

    /**
     * Sets the player number scene.
     */
    @Override
    public void playerChoice() {
        Platform.runLater(() -> {
            stage.setScene(playerNumberScene);
            stage.show();
        });
    }

    /**
     * Changes the game scene to display the last round.
     */
    @Override
    public void showLastRound() {
        Platform.runLater(() -> {
            gameController.showLastRound();
        });
    }

    /**
     * Redirects a player to the closure scene, as it has denied the access to the game.
     * @see #showRemovePlayer().
     */
    @Override
    public void showGameAlreadyStarted() {
        showRemovePlayer();
    }

    /**
     * Scene change to the refused scene, as the player has been removed from the game.
     */
    @Override
    public void showRemovePlayer() {
        Platform.runLater(() -> {
            stage.setScene(refusedScene);
            stage.show();
        });
    }

    /**
     * Enables the rearrange area in the GUI, allowing the player to rearrange the items.
     */
    @Override
    public void rearrangeProcedure(List<Item> items) {
        Platform.runLater(() -> {
            gameController.enableRearrange();
            gameController.rearrange(items);
        });
    }

    /**
     * It forwards the score update message to GameGuiController, to update the points of the player in GUI.
     * @param topOfScoringList the top of the scoring list for the current common goals.
     * @param score the scores of the player.
     * @see GameGuiController#updateScore(List, List).
     */
    @Override
    public void updateScore(List<Integer> topOfScoringList, List<Integer> score) {
        Platform.runLater(() -> {
            gameController.updateScore(topOfScoringList, score);
        });
    }

    /**
     * Loads the language of the GUI, by loading the FXML files with the correct language.
     * @see Client#locale
     */
    @Override
    public void loadLanguage() {

        bundle = ResourceBundle.getBundle("game", client.locale);

        FXMLLoader playerNumberSceneLoader = new FXMLLoader(GuiView.class.getResource("playerSelection.fxml"), bundle);

        // Controller is already set in the FXML file
        try {
            playerNumberScene = new Scene(playerNumberSceneLoader.load());
        } catch (IOException e) {
            System.err.println("Failed to load playerSelection.fxml");
            throw new RuntimeException(e);
        }

        FXMLLoader waitingRoomSceneLoader = new FXMLLoader(GuiView.class.getResource("waitingRoom.fxml"), bundle);
        waitingRoomSceneLoader.setController(new WaitingRoomController());
        try {
            waitingRoomScene = new Scene(waitingRoomSceneLoader.load());
        } catch (IOException e) {
            System.err.println("Failed to load waitingRoom.fxml");
            throw new RuntimeException(e);
        }

        FXMLLoader endGameSceneLoader = new FXMLLoader(this.getClass().getResource("endGame.fxml"), bundle);
        endGameController = new EndGameGuiController(this, client);
        endGameSceneLoader.setController(endGameController);

        try {
            endGameScene = new Scene(endGameSceneLoader.load());
        } catch (IOException e) {
            System.err.println("Failed to load endGame.fxml");
            throw new RuntimeException(e);
        }

        FXMLLoader refusedSceneLoader = new FXMLLoader(this.getClass().getResource("refused.fxml"), bundle);
        refusedSceneLoader.setController(new RefusedGuiController(this));

        try {
            refusedScene = new Scene(refusedSceneLoader.load());
        } catch (IOException e) {
            System.err.println("Failed to load refused.fxml");
            throw new RuntimeException(e);
        }

        FXMLLoader gameSceneLoader = new FXMLLoader(GuiView.class.getResource("game.fxml"), bundle);
        gameController = new GameGuiController();
        gameSceneLoader.setController(gameController);
        try {
            gameScene = new Scene(gameSceneLoader.load());
        } catch (IOException e) {
            System.err.println("Failed to load game.fxml");
            throw new RuntimeException(e);
        }
    }

    /**
     *  Sets the parameter as it's the only player connected.
     * @param b the boolean to set.
     */
    @Override
    public void setTheOnlyOne(boolean b) {
        theOnlyOne = b;
    }

    /**
     * Shows the waiting graphics in the login scene.
     * @see LoginGuiController#showWaiting()
     */
    @Override
    public void showWaiting() {
        Platform.runLater(() -> {
            loginController.showWaiting();
        });
    }

    /**
     * Disables the game scene, called when the player is the only one connected.
     * @see GameGuiController#freezeGame().
     */
    @Override
    public void disableGame() {
        Platform.runLater(() -> {
            gameController.freezeGame();
        });
    }

    /**
     * Enables the game scene, called when the player is not the only one connected.
     * @param currentTurn set as true if it's the player's turn, false otherwise.
     * @see GameGuiController#unfreezeGame(Boolean).
     */
    @Override
    public void enableGame(Boolean currentTurn) {
        Platform.runLater(() -> {
            gameController.unfreezeGame(currentTurn);
        });
    }

    /**
     * Sets the connection status in the login scene, handling the start of the connection to the server.
     * @see LoginGuiController#initiateConnection()
     */
    @Override
    public void initiateConnection() {
        Platform.runLater(() -> {
            loginController.initiateConnection();
        });
    }

    /**
     * Sets the connection status as successful in the login scene.
     * @see LoginGuiController#connectionSuccess().
     */
    @Override
    public void connectionSuccess() {
        Platform.runLater(() -> {
            loginController.connectionSuccess();
        });
    }

    /**
     * Sets the connection status as failed in the login scene.
     * @see LoginGuiController#connectionError()
     */
    @Override
    public void connectionError() {
        Platform.runLater(() -> {
            loginController.connectionError();
        });
    }

    @Override
    public void endAlone() {
        Platform.runLater(() -> {
            stage.setScene(endGameScene);
            stage.show();
            endGameController.endAlone();
        });
    }

    /**
     * Returns the set client.
     * @return client referred to the view.
     */
    @Override
    public Client getClient() {
        return client;
    }

    /**
     * Sets the client.
     * @param client the client to set.
     */
    @Override
    public void setClient(Client client) {
        GuiView.client = client;
    }


}
