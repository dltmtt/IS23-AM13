package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.MyShelfie;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GuiView extends Application implements GameView {

    public static GameGuiController gameController;

    public static Client client; // Static reference to the client, in order to use the sendMessage() function
    public static List<Client> clients;
    public static GuiView gui;
    public static Scene loginScene, playerNumberScene, waitingRoomScene, gameScene, endGameScene; // Loaded scenes
    public static Stage stage; // Main stage
    private static LoginGuiController loginController;

    /**
     * Launches the GUI.
     *
     * @param client the client reference
     */
    @Override
    public void startView(Client client) {
        // CAUTION: if any attributes need to be set, do so in the startView method,
        // because startView has the launch() command, which creates another instance of GuiView.
        launch();
        // Instruction executed after closing GUI window
    }

    /**
     * Starting method for GUI, loads all scene loader and loads all the scenes.
     * calls method <code>loginProcedure()</code>
     *
     * @param stage internal parameter, set by <code>launch()</code>
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
        loginController = new LoginGuiController(client, this);
        loginSceneLoader.setController(loginController);
        try {
            loginScene = new Scene(loginSceneLoader.load());
        } catch (IOException e) {
            System.err.println("Failed to load newLogin2.fxml");
            throw new RuntimeException(e);
        }

        FXMLLoader playerNumberSceneLoader = new FXMLLoader(GuiView.class.getResource("playerSelection.fxml"));

        // Controller is already set in the FXML file
        try {
            playerNumberScene = new Scene(playerNumberSceneLoader.load());
        } catch (IOException e) {
            System.err.println("Failed to load playerSelection.fxml");
            throw new RuntimeException(e);
        }

        FXMLLoader waitingRoomSceneLoader = new FXMLLoader(GuiView.class.getResource("waitingRoom.fxml"));
        waitingRoomSceneLoader.setController(new WaitingRoomController());
        try {
            waitingRoomScene = new Scene(waitingRoomSceneLoader.load());
        } catch (IOException e) {
            System.err.println("Failed to load waitingRoom.fxml");
            throw new RuntimeException(e);
        }

        FXMLLoader gameSceneLoader = new FXMLLoader(GuiView.class.getResource("game.fxml"));
        gameController = new GameGuiController();
        gameSceneLoader.setController(gameController);
        try {
            gameScene = new Scene(gameSceneLoader.load());
        } catch (IOException e) {
            System.err.println("Failed to load game.fxml");
            throw new RuntimeException(e);
        }

        FXMLLoader endGameSceneLoader = new FXMLLoader(this.getClass().getResource("endGame.fxml"));
        endGameSceneLoader.setController(new EndGameGuiController(clients, this));

        try {
            endGameScene = new Scene(endGameSceneLoader.load());
        } catch (IOException e) {
            System.err.println("Failed to load endGame.fxml");
            throw new RuntimeException(e);
        }

        loginProcedure();
    }

    /**
     * sets the main stage to the gameScene
     *
     * @param message the message containing the game information
     */
    @Override
    public void startGame(Message message) {
        Platform.runLater(() -> {
            stage.setScene(gameScene);
            gameController.showGame(message);
            stage.show();
        });
    }

    /**
     * Sets the waiting room scene
     */
    @Override
    public void waitingRoom() {
        Platform.runLater(() -> {
            stage.setScene(waitingRoomScene);
            stage.show();
        });
    }

    /**
     * Sets the login scene
     */
    @Override
    public void loginProcedure() {
        stage.setScene(loginScene);
        stage.show();
    }

    /**
     * Shows a message in a label inside the game scene
     *
     * @param message the message to be shown
     */
    @Override
    public void showMessage(String message) {
        Platform.runLater(() -> gameController.showMessage(message));
    }

    /**
     * enables the items with one free side, allowing the player to pick them
     */
    @Override
    public void showPick() {
        Platform.runLater(gameController::enableItemsWithOneFreeSide);
    }

    /**
     * updates the board model in the GUI interface, then it updates the GUI accordingly
     *
     * @param board the board to be shown
     */
    @Override
    public void showBoard(Board board) {
        Platform.runLater(() -> {
            GameGuiController.boardModel = board;
            gameController.updateBoard();
        });
    }

    /**
     * this particular implementation of showRearrange always returns true, because the GUI doesn't really need to ask the user if he wants to rearrange the items
     *
     * @param items the items to be rearranged
     * @return true (always)
     */
    @Override
    public boolean showRearrange(List<Item> items) {
        return true;
    }

    /**
     * enables the available columns, allowing the player to insert the item they picked
     */
    @Override
    public void promptInsert() {
        Platform.runLater(gameController::enableInsert);
    }

    @Override
    public void showEndGame(List<String> winners) {

    }

    @Override
    public List<Integer> rearrange(List<Item> items) {
        return null;
    }

    @Override
    public void showCurrentScore(int score) {

    }

    /**
     * shows the end game scene
     */
    @Override
    public void endGame() {
        stage.setScene(endGameScene);
        stage.show();
    }

    /**
     * shows the other player's bookshelf
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
     * shows the player's bookshelf
     *
     * @param bookshelf the bookshelf to be shown
     */
    @Override
    public void showBookshelf(Bookshelf bookshelf) {
        Platform.runLater(() -> {
            GameGuiController.bookshelfModel = bookshelf;
            gameController.updateBookshelf(gameController.bookshelfGrid, bookshelf);
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
     * Sets an error message in the login scene
     *
     * @see #usernameError() (GUI-wise there's no difference)
     */
    @Override
    public void completeLoginError() {
        // GUI-wise there's no difference
        usernameError();
    }

    /**
     * sets an error message in the player number scene, but since the number of players is set by a slider, which minimum and maximum are valid, this method is never called and does nothing
     */
    @Override
    public void playerNumberError() {
        // since the number of players is set by a slider, which minimum and maximum are valid, there is no prob
    }

    /**
     * sets the player number scene
     */
    @Override
    public void playerChoice() {
        Platform.runLater(() -> {
            stage.setScene(playerNumberScene);
            stage.show();
        });
    }

    @Override
    public void showLastRound() {
        // TODO: implement
    }

    @Override
    public void showGameAlreadyStarted() {

    }

    @Override
    public void showRemovePlayer() {

    }

    @Override
    public void showDisconnection(List<String> disconnectedPlayers) {

    }

    /**
     * enables the rearrange area in the GUI, allowing the player to rearrange the items
     */
    @Override
    public void rearrangeProcedure(List<Item> items) {
        Platform.runLater(() -> {
            gameController.enableRearrange();
            gameController.rearrange(items);
        });
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public void setClient(Client client) {
        GuiView.client = client;
    }
}
