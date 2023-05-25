package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.MyShelfie;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.Coordinates;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GuiView extends Application implements GameView {

    // Static reference to the client, in order to use the sendMessage() function
    public static Client client;
    public static List<Client> clients;
    // public GameGuiController controller;

    // main stage
    public static Stage stage;

    // loaded scenes
    public Scene loginScene, waitingRoomScene, gameScene, endGameScene;

    // scene loader
    private FXMLLoader loginSceneLoader, waitingRoomSceneLoader, gameSceneLoader, endGameSceneLoader;

    private LoginGuiController loginController;

    @Override
    public void waitingRoom() {

        // scenes already loaded

        // Scene waitingRoom = null;
        // waitingRoomScene = new FXMLLoader(GuiView.class.getResource("waitingRoom.fxml"));
        // // waitingRoomScene.setController(new waitingRoomController(client));
        //
        // try {
        //     waitingRoom = new Scene(waitingRoomScene.load());
        // } catch (IOException e) {
        //     throw new RuntimeException(e);
        // }
        // stage.setScene(waitingRoom);
        // stage.show();
        //
        // String response = client.sendMessage(new Message("ready", "", 0, false, 0)).getCategory();
        // while (response == null) {
        //     try {
        //         Thread.sleep(5000);
        //     } catch (InterruptedException e) {
        //         throw new RuntimeException(e);
        //     }
        //     response = client.sendMessage(new Message("ready", "", 0, false, 0)).getCategory();
        // }
        //
        // try {
        //     client.startGame();
        // } catch (FullRoomException | IOException | ParseException | IllegalAccessException e) {
        //     System.err.println("An error occurred while starting the game.");
        //     e.printStackTrace();
        //     System.exit(1);
        // }
    }

    /**
     * sets the main stage to the gameScene
     *
     * @param message
     */
    @Override
    public void startGame(Message message) {
        stage.setScene(gameScene);
        stage.show();
    }

    /**
     * launch GUI
     * CAUTION: if any attributes need to be set do it in the <code>startView</code> method, because startView has the <code>launch()</code> command, which creates another instance of GuiView
     *
     * @param client
     */
    @Override
    public void startView(Client client) {
        // GuiView.client = client;
        launch();
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
        GuiView.stage = stage;
        setClient(MyShelfie.client);

        loginSceneLoader = new FXMLLoader(GuiView.class.getResource("login.fxml"));
        loginSceneLoader.setController(new LoginGuiController(client, this));
        try {
            loginScene = new Scene(loginSceneLoader.load());
        } catch (IOException e) {
            System.err.println("Failed to load login.fxml");
            throw new RuntimeException(e);
        }

        waitingRoomSceneLoader = new FXMLLoader(GuiView.class.getResource("waitingRoom.fxml"));
        waitingRoomSceneLoader.setController(new WaitingRoomController());
        try {
            waitingRoomScene = new Scene(waitingRoomSceneLoader.load());
        } catch (IOException e) {
            System.err.println("Failed to load waitingRoom.fxml");
            throw new RuntimeException(e);
        }

        gameSceneLoader = new FXMLLoader(GuiView.class.getResource("game.fxml"));
        gameSceneLoader.setController(new GameGuiController(MyShelfie.client, this));
        try {
            gameScene = new Scene(gameSceneLoader.load());
        } catch (IOException e) {
            System.err.println("Failed to load game.fxml");
            throw new RuntimeException(e);
        }

        endGameSceneLoader = new FXMLLoader(this.getClass().getResource("endGame.fxml"));
        endGameSceneLoader.setController(new EndGameGuiController(clients, this));

        try {
            endGameScene = new Scene(endGameSceneLoader.load());
        } catch (IOException e) {
            System.err.println("Failed to load endGame.fxml");
            throw new RuntimeException(e);
        }

        // startGame(new Message(0));
        loginProcedure();
        // client.login();
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

    }

    /*
        @Override
        public boolean promptFirstGame() {
            return false;
        }

        @Override
        public int promptNumberOfPlayers() {
            return 0;
        }
    */
    @Override
    public void showPersonalGoal(int card) {

    }

    @Override
    public void showCommonGoal(String card, int occurrences, int size, boolean horizontal) {

    }

    @Override
    public List<Coordinates> showPick() {
        return null;
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

    }
}