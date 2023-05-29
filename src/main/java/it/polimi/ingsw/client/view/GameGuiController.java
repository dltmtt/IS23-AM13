package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.Board;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameGuiController {

    private static final String BASE_PATH = "/graphics/item_tiles/";
    private static final int ITEM_SIZE = 50; // Adjust the size of each item image
    private static final int ROW_SPACING = 10; // Adjust the spacing between rows
    private static final int COLUMN_SPACING = 10; // Adjust the spacing between columns

    public Client client;
    public GuiView view;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView board;

    @FXML
    private GridPane boardGridPane;

    @FXML
    private ImageView bookshelf;

    @FXML
    private GridPane bookshelfGrid;

    @FXML
    private Canvas canvasPlayer1;

    @FXML
    private Canvas canvasPlayer2;

    @FXML
    private Canvas canvasPlayer3;

    @FXML
    private ImageView cg1;

    @FXML
    private ImageView cg2;

    @FXML
    private ImageView pg;

    @FXML
    private Label player1;

    @FXML
    private Label player2;

    @FXML
    private Label player3;

    public GameGuiController() {
        this.client = GuiView.client;
        this.view = GuiView.gui;
    }

    public void showGame(Message message) {

        // Personal Goal image loading
        int personalGoalIndex = message.getPersonalGoal();
        String imagePath = "graphics/personal_goal_cards/pg_" + personalGoalIndex + ".png";
        Image image = new javafx.scene.image.Image(getClass().getResource(imagePath).toExternalForm());
        pg.setImage(image);

        // Common Goal image loading
        List<String> commonGoalFiles = new ArrayList<>();
        for (int i = 0; i < message.getCardOccurrences().size(); i++) {
            String fileName = message.getCardType().get(i) + "-" + message.getCardSize().get(i).toString() + "-" + message.getCardOccurrences().get(i).toString() + "-" + message.getCardHorizontal().get(i).toString();
            commonGoalFiles.add(fileName);
        }
        cg1.setImage(new Image(getClass().getResource("graphics/common_goal_cards/" + commonGoalFiles.get(0) + ".jpg").toExternalForm()));
        if (message.getCardType().size() == 1) {
            cg2.setImage(new Image(getClass().getResource("graphics/common_goal_cards/back.jpg").toExternalForm()));
        } else {
            cg2.setImage(new Image(getClass().getResource("graphics/common_goal_cards/" + commonGoalFiles.get(1) + ".jpg").toExternalForm()));
        }

        // Board items loading
        Board board = message.getBoard();
    }
}