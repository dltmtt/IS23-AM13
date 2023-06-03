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
import java.util.HashMap;
import java.util.List;

public class GuiView extends Application implements GameView {

    public static GameGuiController gameController;

    // Static reference to the client, in order to use the sendMessage() function
    public static Client client;
    public static List<Client> clients;
    public static GuiView gui;
    // loaded scenes
    public static Scene loginScene, playerNumberScene, waitingRoomScene, gameScene, endGameScene;
    // main stage
    public static Stage stage;
    private static LoginGuiController loginController;

    /**
     * launch GUI
     * CAUTION: if any attributes need to be set do it in the <code>startView</code> method, because startView has the <code>launch()</code> command, which creates another instance of GuiView
     *
     * @param client
     */
    @Override
    public void startView(Client client) {
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
            stage.getIcons().add(new Image(GuiView.class.getResource("graphics/publisher_material/icon.png").openStream()));
        } catch (IOException e) {
            System.err.println("Icon not found");
        }
        GuiView.gui = this;
        GuiView.stage = stage;
        setClient(MyShelfie.client);

        // scene loader
        FXMLLoader loginSceneLoader = new FXMLLoader(GuiView.class.getResource("login.fxml"));
        loginController = new LoginGuiController(client, this);
        loginSceneLoader.setController(loginController);
        try {
            loginScene = new Scene(loginSceneLoader.load());
        } catch (IOException e) {
            System.err.println("Failed to load login.fxml");
            throw new RuntimeException(e);
        }

        FXMLLoader playerNumberSceneLoader = new FXMLLoader(GuiView.class.getResource("playerSelection.fxml"));
        // Controller is already set in the fxml file
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
     * @param message
     */
    @Override
    public void startGame(Message message) {
        Platform.runLater(() -> {
            stage.setScene(gameScene);
            gameController.showGame(message);
            stage.show();
        });
    }

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

    @Override
    public void showMessage(String message) {
        // System.out.println(message);
    }

    @Override
    public void showPick() {
        Platform.runLater(gameController::enableAllItems);
    }

    @Override
    public void showBoard(Board board) {

    }

    @Override
    public void showBookshelf(Bookshelf bookshelf) {

    }

    @Override
    public void showStartGame() {

    }

    @Override
    public boolean showRearrange(List<Item> items) {
        return false;
    }

    @Override
    public int promptInsert() {
        return 0;
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

    @Override
    public void showDisconnection() {

    }

    @Override
    public void endGame() {
        stage.setScene(endGameScene);
        stage.show();
    }

    @Override
    public void setClient(Client client) {
        GuiView.client = client;
    }

    @Override
    public void pickMyBookshelf(HashMap<Bookshelf, String> bookshelves) {

    }

    @Override
    public void pickOtherBookshelf(HashMap<Bookshelf, String> bookshelves) {

    }

    @Override
    public void showOtherBookshelf(Bookshelf bookshelf, String name) {

    }

    @Override
    public void usernameError() {
        loginController.usernameAlreadyTaken();
    }

    @Override
    public void completeLoginError() {
        // GUI-wise there's no difference
        usernameError();
    }

    @Override
    public void playerNumberError() {
        // since the number of players is set by a slider, which minimum and maximum are valid, there is no prob
    }

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
}