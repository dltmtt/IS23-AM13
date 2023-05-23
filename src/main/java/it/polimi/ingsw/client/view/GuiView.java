package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.Coordinates;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GuiView extends Application implements GameView {

    public Client client;
    // public GameGuiController controller;
    public Stage stage;

    private FXMLLoader loginScene, gameScene, waitingRoomScene, endGameScene;

    private LoginGuiController loginController;

    @Override
    public void startView(Client client) {
        // this.client = client;
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("My Shelfie");
        this.stage = stage;

        loginProcedure();
        // client.login();
    }

    @Override
    public void loginProcedure() {
        Scene login = null;
        loginScene = new FXMLLoader(GuiView.class.getResource("login.fxml"));
        loginScene.setController(new LoginGuiController(client));

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
    public void waitingRoom() {

    }

    @Override
    public void startGame() {

    }

    @Override
    public void waitForTurn() {

    }

    @Override
    public void myTurn() {

    }

    @Override
    public void endGame() {

    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }
}