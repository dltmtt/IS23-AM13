package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.Coordinates;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class GameGuiView extends Application implements GameView {

    public static void main(String[] args) {
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
        GuiController controller = new GuiController();
        URL url = this.getClass().getResource("demo.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(controller);
        // loader.setRoot(controller);
        loader.load();
        stage.setTitle("prova");
        stage.setScene(new Scene(loader.getRoot()));
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
    public void showPersonalGoal(int card) throws IOException, ParseException {

    }

    @Override
    public void showCommonGoal(String card, int occurrences, int size, boolean horizontal) throws IOException, ParseException {

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
    public boolean showRearrange(List<Item> items) throws IOException {
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
    public List<Integer> rearrange(List<Item> items) throws IOException {
        return null;
    }

    @Override
    public void showCurrentScore(int score) {

    }

    @Override
    public void showDisconnection() {

    }
}