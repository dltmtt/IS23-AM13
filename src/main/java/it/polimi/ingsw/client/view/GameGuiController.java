package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.MyShelfie;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.Coordinates;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static javafx.scene.layout.GridPane.getColumnIndex;

public class GameGuiController {

    private static final String ITEM_BASE_PATH = "graphics/item_tiles/";
    private static final int ITEM_SIZE = 40; // Adjust the size of each item image
    private static final List<GridPane> playersBookshelves = new ArrayList<>();
    public static List<Coordinates> pickedItems = new ArrayList<>();
    public static Client client;
    public static GuiView view;
    public static Board boardModel;
    public static Bookshelf bookshelfModel = new Bookshelf();
    private static List<Label> playersLabels;
    private final List<ImageView> itemImageViews = new ArrayList<>();
    private final List<Integer> indexList = new ArrayList<>();
    private final List<Integer> newOrder = new ArrayList<>();
    public HashMap<String, Integer> playersBookshelf = new HashMap<>();
    public int playerCounter = 1;
    @FXML
    public GridPane bookshelfGrid;
    @FXML
    public GridPane grid;
    @FXML
    private Button rearrange;
    @FXML
    private ImageView board;
    @FXML
    private GridPane boardGridPane;
    @FXML
    private ImageView bookshelf;
    @FXML
    private ImageView cg1;
    @FXML
    private ImageView cg2;
    @FXML
    private Button confirmSelection;
    @FXML
    private Button delete;
    @FXML
    private Button help;
    @FXML
    private ImageView pg;
    @FXML
    private Label messageLabel;
    @FXML
    private Label player1;
    @FXML
    private Label player2;
    @FXML
    private Label player3;
    @FXML
    private Label promptInsert;
    @FXML
    private Label promptRearrange;

    @FXML
    private ImageView topOfScoring1;

    @FXML
    private ImageView topOfScoring2;

    @FXML
    private Label adjacentGroupsLabel;

    @FXML
    private Label commonGoalLabel;

    @FXML
    private Label personalGoalLabel;

    @FXML
    private Label totalPointsLabel;

    public GameGuiController() {
        client = MyShelfie.client;
        view = GuiView.gui;
        playersLabels = new ArrayList<>();
        playersLabels.add(player1);
        playersLabels.add(player2);
        playersLabels.add(player3);
    }

    /**
     * set as enabled every ImageView in boardGridPane that is on the same row or same column of the given parameters
     *
     * @param row the row of the selected ImageView
     * @param col the column of the selected ImageView
     */
    private void highlightPickableItems(int row, int col) {
        Node selectedNode;
        for (int i = 0; i < boardGridPane.getRowCount(); i++) {
            for (int j = 0; j < boardGridPane.getColumnCount(); j++) {
                if ((i == row || j == col) && (Math.abs(i - row) <= 2 && Math.abs(j - col) <= 2) && boardModel.checkBorder(new Coordinates(i, j))) {
                    if ((i == row - 2 && !boardModel.checkBorder(new Coordinates(i + 1, j))) || (i == row + 2 && !boardModel.checkBorder(new Coordinates(i - 1, j))) || (j == col - 2 && !boardModel.checkBorder(new Coordinates(i, j + 1))) || (j == col + 2 && !boardModel.checkBorder(new Coordinates(i, j - 1)))) {
                        disableItem(boardGridPane, i, j);
                    } else {
                        selectedNode = getNodeByRowColumnIndex(boardGridPane, i, j);
                        if (selectedNode != null) {
                            selectedNode.setDisable(false);
                            selectedNode.setEffect(new DropShadow(20, Color.ORANGE));
                        }
                    }
                } else {
                    selectedNode = getNodeByRowColumnIndex(boardGridPane, i, j);
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
     * Method called when the user clicks on the delete button.
     */
    public void deleteCurrentSelection() {
        pickedItems.clear();
        grid.getChildren().clear();
        enableItemsWithOneFreeSide();
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
    public Node getNodeByRowColumnIndex(GridPane selectedGrid, final int row, final int column) {
        Node result = null;
        if (row < 0 || column < 0 || row >= selectedGrid.getRowCount() || column >= selectedGrid.getColumnCount()) {
            return null;
        } else {
            ObservableList<Node> children = selectedGrid.getChildren();
            for (Node node : children) {
                if (GridPane.getRowIndex(node) == selectedGrid.getRowCount() - row - 1 && getColumnIndex(node) == column) {
                    result = node;
                    break;
                }
            }
            return result;
        }
    }

    /**
     * Enables the items that have at least one free side.
     */
    public void enableItemsWithOneFreeSide() {
        // ObservableList<Node> children = this.boardGridPane.getChildren();
        for (int row = 0; row < boardGridPane.getRowCount(); row++) {
            for (int col = 0; col < boardGridPane.getColumnCount(); col++) {
                if (boardModel.checkBorder(new Coordinates(row, col))) {
                    enableItem(row, col);
                } else {
                    disableItem(boardGridPane, row, col);
                }
            }
        }
    }

    /**
     * Enables the item at the given row and column position.
     * Method called when the user clicks on the element.
     *
     * @param row the row index of the node (Model-wise)
     * @param col the column index of the node (Model-wise)
     */
    public void enableItem(int row, int col) {
        Node selectedNode = getNodeByRowColumnIndex(boardGridPane, row, col);
        if (selectedNode != null) {
            selectedNode.setDisable(false);
            selectedNode.setEffect(new DropShadow(20, Color.ORANGE));
            // set node cursor as a hand
            selectedNode.setCursor(Cursor.HAND);
        }
    }

    /**
     * Disables the item at the given row and column position.
     *
     * @param row the row index of the node (Model-wise)
     * @param col the column index of the node (Model-wise)
     */
    public void disableItem(GridPane grid, int row, int col) {
        Node selectedNode = getNodeByRowColumnIndex(grid, row, col);
        if (selectedNode != null) {
            selectedNode.setDisable(true);
            selectedNode.setEffect(null);
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setSaturation(-1);
            selectedNode.setEffect(colorAdjust);
            // set node cursor as a default
            selectedNode.setCursor(Cursor.DEFAULT);
        }
    }

    /**
     * Enables all the items in the <code>boardGridPane</code>.
     */
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

    /**
     * Disables all the items in the <code>boardGridPane</code>.
     */
    public void disableAllItems() {
        ObservableList<Node> children = this.boardGridPane.getChildren();
        for (Node node : children) {
            node.setDisable(true);
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setSaturation(-1);
            node.setEffect(colorAdjust);
            // set node cursor as a default
            node.setCursor(Cursor.DEFAULT);
        }
    }

    /**
     * Method that handles the selection of an item.
     * If it's the first element selected then it highlights the pickable items, otherwise if the second element is selected it removes the items from the board and adds them to the rearrange area.
     *
     * @param row the row index of the node (Model-wise)
     * @param col the column index of the node (Model-wise)
     */
    public void selectItem(int row, int col) {
        System.out.println("selected " + row + ", col" + col);

        if (boardModel.checkBorder(new Coordinates(row, col))) {

            pickedItems.add(new Coordinates(row, col));
            // synchronized (listLock) {
            if (pickedItems.size() == 1) {
                // pickedItems.add(new Coordinates(row, col));
                highlightPickableItems(row, col);
                highlightItem(row, col, Color.GREEN);
                confirmSelection.setDisable(false);
                delete.setDisable(false);
            } else {
                if (pickedItems.size() == 2) {
                    // removeItemsFromBoard(pickedItems);
                    disableAllItems();
                    System.out.println("picked items: " + pickedItems);
                    // addPickedItemsToRearrangeArea();

                    highlightSelection(pickedItems);
                }
            }
        }
    }

    public void highlightItem(int row, int col, Color color) {
        Node selectedNode = getNodeByRowColumnIndex(boardGridPane, row, col);
        if (selectedNode != null) {
            selectedNode.setEffect(new DropShadow(20, color));
        }
    }

    public void highlightSelection(List<Coordinates> pickedItems) {
        int startX = Math.min(pickedItems.get(0).x(), pickedItems.get(1).x());
        int startY = Math.min(pickedItems.get(0).y(), pickedItems.get(1).y());
        int endX = Math.max(pickedItems.get(0).x(), pickedItems.get(1).x());
        int endY = Math.max(pickedItems.get(0).y(), pickedItems.get(1).y());
        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                highlightItem(i, j, Color.GREEN);
            }
        }
    }

    /**
     * Removes the chosen items from the board.
     *
     * @param pickedItems the list of the items to remove
     */
    public void removeItemsFromBoard(List<Coordinates> pickedItems) {
        int startX = Math.min(pickedItems.get(0).x(), pickedItems.get(1).x());
        int startY = Math.min(pickedItems.get(0).y(), pickedItems.get(1).y());
        int endX = Math.max(pickedItems.get(0).x(), pickedItems.get(1).x());
        int endY = Math.max(pickedItems.get(0).y(), pickedItems.get(1).y());
        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                getNodeByRowColumnIndex(boardGridPane, i, j).setVisible(false);
                getNodeByRowColumnIndex(boardGridPane, i, j).setDisable(true);
                // boardGridPane.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == finalJ && GridPane.getRowIndex(node) == finalI);
            }
        }
    }

    /**
     * Enables the columns of the bookshelfGrid that have enough space to insert the pickedItems
     */
    public void enableInsert() {
        ObservableList<Node> children = bookshelfGrid.getChildren();

        for (Node node : children) {
            if (bookshelfModel.getFreeCellsInColumn(getColumnIndex(node)) >= indexList.size()) {
                node.setDisable(false);
                node.setEffect(new DropShadow(20, Color.ORANGE));
                // set node cursor as a hand
                node.setCursor(Cursor.HAND);
                node.setOnMouseClicked(mouseEvent -> {
                    disableAllItems();
                    disableBookshelf();
                    confirmSelection.setDisable(true);
                    delete.setDisable(true);
                    pickedItems.clear();
                    client.sendMessage(new Message("sort", indexList));
                    client.sendMessage(new Message("insertMessage", "insert", getColumnIndex(node)));
                    grid.getChildren().clear();
                    indexList.clear();
                });
            } else {
                node.setDisable(true);
                node.setEffect(null);
                node.setCursor(Cursor.DEFAULT);
            }
        }
    }

    /**
     * Disable all items in the bookshelf, to avoid the user to select a column when it's not his turn or not the right moment.
     */
    public void disableBookshelf() {
        ObservableList<Node> children = bookshelfGrid.getChildren();
        for (Node node : children) {
            node.setDisable(true);
            // set node cursor as a default
            node.setCursor(Cursor.DEFAULT);
        }
    }

    /**
     * Method that is called when the "Send Selection" button is pressed, sending the List of Coordinates to the server, disabling the remaining items on the board and removing the selected items from the board.
     *
     * @param event the event that triggered the method
     */
    @FXML
    void sendSelection(ActionEvent event) {
        disableAllItems();
        if (pickedItems.size() == 2) {
            client.sendMessage(new Message(pickedItems.get(0), pickedItems.get(1)));
        } else {
            client.sendMessage(new Message(pickedItems.get(0), pickedItems.get(0)));
        }
        confirmSelection.setDisable(true);
        delete.setDisable(true);
        removeItemsFromBoard(pickedItems);
        pickedItems.clear();
    }

    /**
     * Method that updates the boardGridPane with the items in the boardModel.
     * It's called when the board is updated by the server.
     */
    public void updateBoard() {
        boardGridPane.getChildren().clear();

        // Board items loading

        // i=row (board)
        // j=column (board)
        for (int i = 0; i < boardModel.getBoardSize(); i++) {
            for (int j = 0; j < boardModel.getBoardSize(); j++) {
                if (boardModel.getItem(i, j) != null) {
                    // String fileName = boardModel.getItem(i, j).color().toString().toLowerCase().charAt(0) + String.valueOf(boardModel.getItem(i, j).number());
                    String fileName = boardModel.getItemFileName(i, j);
                    try {
                        // Image itemImage = new Image(getClass().getResource(ITEM_BASE_PATH + fileName + ".png").toExternalForm());

                        Image itemImage = new Image(Objects.requireNonNull(getClass().getResource(ITEM_BASE_PATH + fileName)).toExternalForm());

                        ImageView itemImageView = new ImageView(itemImage);
                        itemImageView.setFitHeight(ITEM_SIZE);
                        itemImageView.setFitWidth(ITEM_SIZE);

                        // set the onClicked action as itemSelected() on itemImageView
                        int finalI = i;
                        int finalJ = j;
                        itemImageView.setOnMouseClicked(mouseEvent -> selectItem(finalI, finalJ));

                        boardGridPane.add(itemImageView, j, boardModel.getBoardSize() - i - 1);
                        // initially every item is not enabled
                        itemImageView.setDisable(true);
                    } catch (NullPointerException e) {
                        System.out.println("Error on loading item image: " + fileName + " at (" + i + "," + j + "), the item is: " + boardModel.getItem(i, j));
                    }
                }
            }
        }
    }

    /**
     * Method called when the complete game is sent from the server.
     *
     * @param message the message containing the complete game
     */
    public void showGame(Message message) {
        // Personal Goal image loading
        int personalGoalIndex = message.getPersonalGoal();
        String imagePath = "graphics/personal_goal_cards/pg_" + personalGoalIndex + ".png";
        Image image = new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());
        pg.setImage(image);

        // Common Goal image loading
        List<String> commonGoalFiles = new ArrayList<>();
        for (int i = 0; i < message.getCardOccurrences().size(); i++) {
            String fileName = message.getCardType().get(i) + "-" + message.getCardSize().get(i).toString() + "-" + message.getCardOccurrences().get(i).toString() + "-" + message.getCardHorizontal().get(i).toString();
            commonGoalFiles.add(fileName);
        }
        try {
            cg1.setImage(new Image(Objects.requireNonNull(getClass().getResource("graphics/common_goal_cards/" + commonGoalFiles.get(0) + ".jpg")).toExternalForm()));
        } catch (NullPointerException e) {
            System.out.println("Error on loading commonGoal: " + commonGoalFiles.get(0));
        }
        try {
            if (message.getCardType().size() == 1) {
                cg2.setImage(new Image(Objects.requireNonNull(getClass().getResource("graphics/common_goal_cards/back.jpg")).toExternalForm()));
            } else {
                cg2.setImage(new Image(Objects.requireNonNull(getClass().getResource("graphics/common_goal_cards/" + commonGoalFiles.get(1) + ".jpg")).toExternalForm()));
            }
        } catch (NullPointerException e) {
            System.out.println("Error on loading commonGoal2:" + commonGoalFiles.get(1));
        }

        boardModel = message.getBoard();
        updateBoard();

        updateScoring(message.getTopOfScoringList());

        System.out.println("game loaded");
        disableAllItems();
        initializeBookshelfGrid();
    }

    private void updateScoring(List<Integer> topScoring) {
        try {
            topOfScoring1.setImage(new Image(Objects.requireNonNull(getClass().getResource("graphics/scoring_tokens/" + topScoring.get(0) + ".png")).toExternalForm()));
            topOfScoring1.setVisible(true);
        } catch (NullPointerException e) {
            System.out.println("Error on loading scoring token: " + topScoring.get(0));
        }
        if (topScoring.size() == 2) {
            try {
                topOfScoring2.setImage(new Image(Objects.requireNonNull(getClass().getResource("graphics/scoring_tokens/" + topScoring.get(1) + ".png")).toExternalForm()));
                topOfScoring2.setVisible(true);
            } catch (NullPointerException e) {
                System.out.println("Error on loading scoring token: " + topScoring.get(1));
            }
        }
    }

    public void toggleHelp() {

    }

    /**
     * Method that creates an ImageView for the item with the given fileName.
     *
     * @param itemFileName the name of the file of the item
     * @return the ImageView of the item
     */
    private ImageView createImageView(String itemFileName) {
        try {
            ImageView item = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(itemFileName)).toExternalForm()));
            item.setFitHeight(ITEM_SIZE);
            item.setFitWidth(ITEM_SIZE);
            item.setPreserveRatio(true);
            item.setSmooth(true);
            item.setCache(true);
            item.setCursor(Cursor.HAND); // Set the cursor to hand
            return item;
        } catch (NullPointerException e) {
            System.out.println("Error on loading item image: " + itemFileName);
            return null;
        }
    }

    public void updateOtherBookshelves(Bookshelf bookshelf, String name) {
        if (!playersBookshelf.containsKey(name)) {
            playersBookshelf.put(name, playerCounter);
        }
    }

    /**
     * Show a message on the messageLabel
     *
     * @param message the message to be shown
     */
    public void showMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }

    /**
     * Method used to swap the items in the indexList
     *
     * @param sourceIndex the index of the item to be moved
     * @param targetIndex the index of the item to be swapped with
     */
    private void rearrangeItems(int sourceIndex, int targetIndex) {
        int sourcePosition = indexList.get(sourceIndex);
        indexList.remove(sourceIndex);

        if (targetIndex > sourceIndex) {
            indexList.add(targetIndex - 1, sourcePosition);
        } else {
            indexList.add(targetIndex, sourcePosition);
        }
    }

    public void selectInRearrangeArea(GridPane grid) {
        rearrange.setDisable(false);
        newOrder.clear();
        if (grid.getChildren().size() == 2) {
            Node node1 = grid.getChildren().get(1);
            grid.getChildren().remove(1);
            Node node0 = grid.getChildren().get(0);
            grid.getChildren().remove(0);
            grid.add(node1, 0, 0);
            grid.add(node0, 0, 1);

            //  rearrangeItems(0,1);
            //  rearrangeItems(1,0);
            newOrder.addAll(indexList);
            indexList.clear();
            indexList.add(newOrder.get(1));
            indexList.add(newOrder.get(0));
        } else {   // size==3
            Node node2 = grid.getChildren().get(2);
            grid.getChildren().remove(2);
            Node node1 = grid.getChildren().get(1);
            grid.getChildren().remove(1);
            Node node0 = grid.getChildren().get(0);
            grid.getChildren().remove(0);
            grid.add(node2, 0, 0);
            grid.add(node0, 0, 1);
            grid.add(node1, 0, 2);

            newOrder.addAll(indexList);
            indexList.clear();
            indexList.add(newOrder.get(2));
            indexList.add(newOrder.get(0));
            indexList.add(newOrder.get(1));
            //  rearrangeItems(0,1);
            //  rearrangeItems(1,2);
            //  rearrangeItems(2,0);
        }
    }

    /**
     * Initialize the bookshelf grid with empty image views.
     */
    private void initializeBookshelfGrid() {
        for (int i = 0; i < Bookshelf.getRows(); i++) {
            for (int j = 0; j < Bookshelf.getColumns(); j++) {
                try {
                    ImageView itemImageView = new ImageView(Objects.requireNonNull(getClass().getResource(ITEM_BASE_PATH + "null.png")).toExternalForm());
                    itemImageView.setFitHeight(ITEM_SIZE);
                    itemImageView.setFitWidth(ITEM_SIZE);
                    itemImageView.setPreserveRatio(true);
                    itemImageView.setSmooth(true);
                    itemImageView.setCache(true);
                    itemImageView.setCursor(Cursor.HAND); // Set the cursor to hand
                    // set image to be transparent
                    // itemImageView.setOpacity(0.0);
                    bookshelfGrid.add(itemImageView, j, Bookshelf.getRows() - i - 1);
                } catch (NullPointerException e) {
                    System.out.println("Error on loading item image: null.png");
                }
            }
        }
    }

    public void enableRearrange() {
        grid.setDisable(false);
    }

    /**
     * Updates the bookshelf view, displaying the given bookshelf in the given grid.
     *
     * @param grid      the grid to be updated
     * @param bookshelf the bookshelf to be displayed
     */
    public void updateBookshelf(GridPane grid, Bookshelf bookshelf) {
        grid.getChildren().clear();
        for (int i = 0; i < Bookshelf.getRows(); i++) {
            for (int j = 0; j < Bookshelf.getColumns(); j++) {
                if (bookshelf.getItemAt(i, j).isPresent()) {
                    String fileName = bookshelf.getItemFileName(i, j);
                    try {
                        Image itemImage = new Image(Objects.requireNonNull(getClass().getResource(ITEM_BASE_PATH + fileName)).toExternalForm());
                        ImageView itemImageView = new ImageView(itemImage);
                        itemImageView.setFitHeight(ITEM_SIZE);
                        itemImageView.setFitWidth(ITEM_SIZE);
                        itemImageView.setImage(itemImage);
                        grid.add(itemImageView, j, Bookshelf.getRows() - i - 1);
                        itemImageView.setVisible(true);
                    } catch (NullPointerException e) {
                        System.out.println("Error on loading item image: " + fileName + " at (" + i + "," + j + "), the item is: " + bookshelf.getItemAt(i, j));
                    }
                } else {
                    try {
                        ImageView itemImageView = new ImageView(Objects.requireNonNull(getClass().getResource(ITEM_BASE_PATH + "null.png")).toExternalForm());
                        itemImageView.setFitHeight(ITEM_SIZE);
                        itemImageView.setFitWidth(ITEM_SIZE);
                        itemImageView.setPreserveRatio(true);
                        itemImageView.setSmooth(true);
                        itemImageView.setCache(true);
                        // itemImageView.setCursor(Cursor.HAND); // Set the cursor to hand
                        // set image to be transparent
                        // itemImageView.setOpacity(0.0);
                        grid.add(itemImageView, j, Bookshelf.getRows() - i - 1);
                    } catch (NullPointerException e) {
                        System.out.println("Error on loading item image: null.png");
                    }
                }
            }
        }
    }

    public void rearrange(List<Item> items) {
        int i = 0;
        grid.getChildren().clear();
        for (int j = 0; j < items.size(); j++) {
            try {
                int finalI = items.size() - i - 1;
                ImageView itemView = createImageView(ITEM_BASE_PATH + items.get(j).fileName());
                if (itemView != null) {
                    itemView.setFitHeight(ITEM_SIZE);
                    itemView.setFitWidth(ITEM_SIZE);
                    grid.add(itemView, 0, finalI);
                    System.out.println("itemView is not null");
                    itemView.setDisable(false);
                    indexList.add(items.indexOf(items.get(j)));
                }
            } catch (NullPointerException e) {
                System.err.println("Error on loading item image: " + items.get(j).fileName() + ", item not added to rearrange area");
            }
            i++;
        }
        System.out.println(grid.getChildren().size());
        if (grid.getChildren().size() == 1) {
            rearrange.setDisable(true);
        } else {
            rearrange.setOnMouseClicked(mouseEvent -> selectInRearrangeArea(grid));
        }
    }

    public void updateScore(List<Integer> topOfScoringList, List<Integer> score) {
        updateScoring(topOfScoringList);
        updatePoints(score);
    }

    private void updatePoints(List<Integer> score) {
        personalGoalLabel.setText(score.get(0).toString());
        commonGoalLabel.setText(score.get(1).toString());
        adjacentGroupsLabel.setText(score.get(2).toString());
        totalPointsLabel.setText(score.get(3).toString());
    }
}