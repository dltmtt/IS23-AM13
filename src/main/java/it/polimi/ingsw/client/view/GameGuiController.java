package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.utils.Coordinates;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameGuiController {

    private static final String BASE_PATH = "graphics/item_tiles/";
    private static final int ITEM_SIZE = 40; // Adjust the size of each item image
    private static final int ROW_SPACING = 10; // Adjust the spacing between rows
    private static final int COLUMN_SPACING = 10; // Adjust the spacing between columns
    public static List<Coordinates> pickedItems = new ArrayList<>();
    public static Client client;
    public static GuiView view;
    public final Object listLock = new Object();
    @FXML
    private GridPane boardGridPane;
    @FXML
    private GridPane bookshelfGrid;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private ImageView board;
    @FXML
    private ImageView bookshelf;
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
        client = GuiView.client;
        view = GuiView.gui;
    }

    /**
     * set as enabled every ImageView in boardGridPane that is on the same row or same column of the given parameters
     *
     * @param row
     * @param col
     */
    private void highlightPickableItems(int row, int col) {
        Node selectedNode;
        for (int i = 0; i < boardGridPane.getRowCount(); i++) {
            for (int j = 0; j < boardGridPane.getColumnCount(); j++) {
                if (i == row || j == col) {
                    selectedNode = getNodeByRowColumnIndex(i, j);
                    if (selectedNode != null) {
                        selectedNode.setDisable(false);
                        selectedNode.setEffect(new DropShadow(20, Color.ORANGE));
                    }
                } else {
                    selectedNode = getNodeByRowColumnIndex(i, j);
                    if (selectedNode != null) {
                        selectedNode.setDisable(true);
                        selectedNode.setEffect(null);
                        ColorAdjust colorAdjust = new ColorAdjust();
                        colorAdjust.setSaturation(-1);
                        selectedNode.setEffect(colorAdjust);
                    }
                }
            }
        }
    }

    /**
     * Returns the Node at the given row and column position.
     * Please note that the row and column index are referred to the Board System, not to the GridPane.
     * Therefore, the cell (0,0) is the bottom left corner of the board.
     *
     * @param row    the row index of the node (Board-wise)
     * @param column the column index of the node (Board-wise)
     * @return the Node at the given row and column position
     */

    public Node getNodeByRowColumnIndex(final int row, final int column) {
        Node result = null;
        if (row < 0 || column < 0 || row >= boardGridPane.getRowCount() || column >= boardGridPane.getColumnCount()) {
            return null;
        } else {
            ObservableList<Node> children = boardGridPane.getChildren();
            for (Node node : children) {
                if (GridPane.getRowIndex(node) == boardGridPane.getRowCount() - row - 1 && GridPane.getColumnIndex(node) == column) {
                    result = node;
                    break;
                }
            }
            return result;
        }
    }

    public void enableAllItems() {
        pickedItems = new ArrayList<>();
        ObservableList<Node> children = this.boardGridPane.getChildren();
        for (Node node : children) {
            node.setDisable(false);
            node.setEffect(new DropShadow(20, Color.ORANGE));
            // set node cursor as a hand
            node.setCursor(Cursor.HAND);
        }
        // System.out.println("enabled all items");
    }

    public void selectItem(int row, int col) {
        System.out.println("selected " + row + ", col" + col);

        pickedItems.add(new Coordinates(row, col));
        synchronized (listLock) {
            if (pickedItems.size() == 1) {
                // pickedItems.add(new Coordinates(row, col));
                highlightPickableItems(row, col);
            } else {
                if (pickedItems.size() == 2) {
                    // pickedItems.add(new Coordinates(row, col));
                    System.out.println("picked items: " + pickedItems);
                    listLock.notify();
                }
            }
        }
    }

    public List<Coordinates> getPickedItems() {
        return pickedItems;
    }

    public Object getListLock() {
        return listLock;
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
        try {
            cg1.setImage(new Image(getClass().getResource("graphics/common_goal_cards/" + commonGoalFiles.get(0) + ".jpg").toExternalForm()));
        } catch (NullPointerException e) {
            System.out.println("Error on loading commonGoal: " + commonGoalFiles.get(0));
        }
        try {
            if (message.getCardType().size() == 1) {
                cg2.setImage(new Image(getClass().getResource("graphics/common_goal_cards/back.jpg").toExternalForm()));
            } else {
                cg2.setImage(new Image(getClass().getResource("graphics/common_goal_cards/" + commonGoalFiles.get(1) + ".jpg").toExternalForm()));
            }
        } catch (NullPointerException e) {
            System.out.println("Error on loading commonGoal2:" + commonGoalFiles.get(1));
        }

        // Board items loading

        // i=row (board)
        // j=column (board)

        Board board = message.getBoard();
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (board.getItem(i, j) != null) {
                    String fileName = board.getItem(i, j).color().toString().toLowerCase().charAt(0) + String.valueOf(board.getItem(i, j).number());
                    try {
                        Image itemImage = new Image(getClass().getResource(BASE_PATH + fileName + ".png").toExternalForm());
                        ImageView itemImageView = new ImageView(itemImage);
                        itemImageView.setFitHeight(ITEM_SIZE);
                        itemImageView.setFitWidth(ITEM_SIZE);

                        // set the onClicked action as itemSelected() on itemImageView
                        int finalI = i;
                        int finalJ = j;
                        itemImageView.setOnMouseClicked(mouseEvent -> selectItem(finalI, finalJ));

                        boardGridPane.add(itemImageView, j, board.getBoardSize() - i - 1);
                        // initially every item is not enabled
                        itemImageView.setDisable(true);
                    } catch (NullPointerException e) {
                        System.out.println("Error loading on loading item image: " + fileName);
                    }
                }
            }
        }
        System.out.println("game loaded");
        synchronized (this.listLock) {
            listLock.notify();
        }
    }
}