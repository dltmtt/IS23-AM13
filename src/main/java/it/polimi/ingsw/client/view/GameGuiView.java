package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.Coordinates;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class GameGuiView extends Application implements GameView {

    public Client client;
    public GuiController controller;

    public Stage stage;

    public GameGuiView(Client client) {
        this.client = client;
    }

    @Override
    public void loginProcedure() {
        try {
            start(this.stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // public static void main(String[] args) {
    // launch();
    // }
    public void run() {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
       /*
        Parent demo=FXMLLoader.load(getClass().getResource("demo.fxml"));
       // scene.getStylesheets().add(String.valueOf(this.getClass().getResource("sample.css")));
        stage.setTitle("prova");
        stage.setScene(new Scene(demo));

        stage.show();

        */

        this.controller = new GuiController();
        //URL loginURL = this.getClass().getClassLoader().getResource("client/view/demo.fxml");
       // FXMLLoader loginLoader = new FXMLLoader(loginURL);
        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setLocation(this.getClass().getResource("demo.fxml"));
        Parent root = null;
        try{
             root = loginLoader.load();
        }catch(IOException e){
            System.exit(-1);
        } loginLoader.setController(controller);

      //  try {
      //      loginLoader.load();
       // } catch (IOException e) {
        //    throw new RuntimeException(e);
      //  }

        Scene scene= new Scene(root);
        this.stage.setScene(scene);
        this.stage.setTitle("prova");
        this.stage.setScene(new Scene(loginLoader.getRoot()));
        this.stage.show();
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
}
