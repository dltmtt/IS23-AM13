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

    public static Client client;
    public static List<Client> clients;
    // public GameGuiController controller;
    public static Stage stage;
    private static FXMLLoader waitingRoomScene;
    private FXMLLoader loginScene;
    private FXMLLoader gameScene;
    private FXMLLoader endGameScene;

    private LoginGuiController loginController;

    @Override
    public void waitingRoom() {
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

    @Override
    public void startGame(Message message) {
        Scene gameS = null;
        gameScene = new FXMLLoader(GuiView.class.getResource("game.fxml"));
        gameScene.setController(new GameGuiController(client, this));

        try {
            gameS = new Scene(gameScene.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(gameS);
        stage.show();
    }

    @Override
    public void startView(Client client) {
        // GuiView.client = client;
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("My Shelfie");
        GuiView.stage = stage;
        setClient(MyShelfie.client);

        // startGame(new Message(0));
        loginProcedure();
        // client.login();
    }

    @Override
    public void loginProcedure() {
        Scene login = null;
        loginScene = new FXMLLoader(GuiView.class.getResource("login.fxml"));
        loginScene.setController(new LoginGuiController(client, this));

        try {
            login = new Scene(loginScene.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(login);
        stage.show();
    }

    @Override
    public String readUsername() {
        return null;
    }

    @Override
    public int readNumber() {
        return 0;
    }

    @Override
    public String showLogin() {
        return null;
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public int promptAge() {
        return 0;
    }

    @Override
    public boolean promptFirstGame() {
        return false;
    }

    @Override
    public int promptNumberOfPlayers() {
        return 0;
    }

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
        Scene endGame = null;
        endGameScene = new FXMLLoader(this.getClass().getResource("endGame.fxml"));
        endGameScene.setController(new EndGameController(clients, this));

        try {
            endGame = new Scene(endGameScene.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(endGame);
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
}