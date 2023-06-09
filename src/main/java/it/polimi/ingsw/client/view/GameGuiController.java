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
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.layout.GridPane.getColumnIndex;

public class GameGuiController {

    private static final String ITEM_BASE_PATH = "graphics/item_tiles/";
    private static final int ITEM_SIZE = 40; // Adjust the size of each item image
    public static List<Coordinates> pickedItems = new ArrayList<>();
    public static Client client;
    public static GuiView view;
    public static Board boardModel;
    public static Bookshelf bookshelfModel = new Bookshelf();
    private final List<ImageView> itemImageViews = new ArrayList<>();
    private final List<Integer> indexList = new ArrayList<>();
    @FXML
    public GridPane bookshelfGrid;
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
    private VBox rearrangeArea;

    public GameGuiController() {
        client = MyShelfie.client;
        view = GuiView.gui;
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
                        selectedNode = getNodeByRowColumnIndex(boardGridPane, i, j);
                        if (selectedNode != null) {
                            selectedNode.setDisable(true);
                            selectedNode.setEffect(null);
                            ColorAdjust colorAdjust = new ColorAdjust();
                            colorAdjust.setSaturation(-1);
                            selectedNode.setEffect(colorAdjust);
                        }
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
        rearrangeArea.getChildren().clear();
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
                    disableItem(row, col);
                }
            }
        }
    }

    /**
     * Enables the item at the given row and column position.
     * Method called when the user clicks on the element.
     *
     * @param row
     * @param col
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
     * @param row
     * @param col
     */
    public void disableItem(int row, int col) {
        Node selectedNode = getNodeByRowColumnIndex(boardGridPane, row, col);
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
     * @param row
     * @param col
     */
    public void selectItem(int row, int col) {
        System.out.println("selected " + row + ", col" + col);

        if (boardModel.checkBorder(new Coordinates(row, col))) {

            pickedItems.add(new Coordinates(row, col));
            // synchronized (listLock) {
            if (pickedItems.size() == 1) {
                // pickedItems.add(new Coordinates(row, col));
                highlightPickableItems(row, col);
            } else {
                if (pickedItems.size() == 2) {
                    // removeItemsFromBoard(pickedItems);
                    disableAllItems();
                    System.out.println("picked items: " + pickedItems);
                    // addPickedItemsToRearrangeArea();
                    confirmSelection.setDisable(false);
                }
            }
        }
    }

    /**
     * Removes the chosen items from the board.
     *
     * @param pickedItems
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
                    client.sendMessage(new Message("insertMessage", "insert", getColumnIndex(node)));
                    rearrangeArea.getChildren().clear();
                    indexList.clear();
                });
            } else {
                node.setDisable(true);
                node.setEffect(null);
                node.setCursor(Cursor.DEFAULT);
            }
        }
    }

    public void disableBookshelf() {
        ObservableList<Node> children = bookshelfGrid.getChildren();
        for (Node node : children) {
            node.setDisable(true);
            // set node cursor as a default
            node.setCursor(Cursor.DEFAULT);
        }
    }

    @FXML
    void sendSelection(ActionEvent event) {
        List<Coordinates> pickedItemsCopy = new ArrayList<>(pickedItems);
        disableAllItems();
        client.sendMessage(new Message(pickedItemsCopy.get(0), pickedItemsCopy.get(1)));
        confirmSelection.setDisable(true);
        delete.setDisable(true);
        removeItemsFromBoard(pickedItems);
        pickedItems.clear();
    }

    // Board items loading

    // i=row (board)
    // j=column (board)
    public void updateBoard() {
        boardGridPane.getChildren().clear();
        for (int i = 0; i < boardModel.getBoardSize(); i++) {
            for (int j = 0; j < boardModel.getBoardSize(); j++) {
                if (boardModel.getItem(i, j) != null) {
                    // String fileName = boardModel.getItem(i, j).color().toString().toLowerCase().charAt(0) + String.valueOf(boardModel.getItem(i, j).number());
                    String fileName = boardModel.getItemFileName(i, j);
                    try {
                        // Image itemImage = new Image(getClass().getResource(ITEM_BASE_PATH + fileName + ".png").toExternalForm());

                        Image itemImage = new Image(getClass().getResource(ITEM_BASE_PATH + fileName).toExternalForm());

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

        boardModel = message.getBoard();
        updateBoard();
        System.out.println("game loaded");
        disableAllItems();
        initializeBookshelfGrid();
    }

    public void toggleHelp() {

    }

    private ImageView createImageView(String itemFileName) {
        ImageView item = new ImageView(new Image(getClass().getResource(itemFileName).toExternalForm()));
        item.setFitHeight(ITEM_SIZE);
        item.setFitWidth(ITEM_SIZE);
        item.setPreserveRatio(true);
        item.setSmooth(true);
        item.setCache(true);
        item.setCursor(Cursor.HAND); // Set the cursor to hand
        return item;
    }

    private void setupDragAndDrop(ImageView itemImageView, int itemIndex) {
        itemImageView.setOnDragDetected(event -> {
            Dragboard dragboard = itemImageView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(Integer.toString(itemIndex));
            dragboard.setContent(content);
            event.consume();
        });

        itemImageView.setOnDragOver(event -> {
            if (event.getGestureSource() != itemImageView && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        itemImageView.setOnDragEntered(event -> {
            if (event.getGestureSource() != itemImageView && event.getDragboard().hasString()) {
                itemImageView.setCursor(Cursor.HAND);
            }
            event.consume();
        });

        itemImageView.setOnDragExited(event -> {
            if (event.getGestureSource() != itemImageView && event.getDragboard().hasString()) {
                itemImageView.setCursor(Cursor.DEFAULT);
            }
            event.consume();
        });

        itemImageView.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;

            if (dragboard.hasString()) {
                int sourceIndex = Integer.parseInt(dragboard.getString());
                ImageView sourceImageView = itemImageViews.get(sourceIndex);
                ImageView targetImageView = (ImageView) event.getSource();

                rearrangeImageViews(sourceImageView, targetImageView);
                rearrangeItems(sourceIndex, itemIndex);
                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });

        itemImageView.setOnDragDone(event -> {
            if (event.getTransferMode() == TransferMode.MOVE) {
                // Perform any additional cleanup or actions after the drag-and-drop operation
            }
            event.consume();
        });
    }

    private void rearrangeImageViews(ImageView sourceImageView, ImageView targetImageView) {
        int sourceIndex = rearrangeArea.getChildren().indexOf(sourceImageView);
        int targetIndex = rearrangeArea.getChildren().indexOf(targetImageView);

        rearrangeArea.getChildren().removeAll(sourceImageView, targetImageView);
        rearrangeArea.getChildren().add(targetIndex, sourceImageView);
        rearrangeArea.getChildren().add(sourceIndex, targetImageView);

        // Swap the image views in the list
        itemImageViews.set(sourceIndex, targetImageView);
        itemImageViews.set(targetIndex, sourceImageView);

        // Swap the positions in the index list
        int sourcePosition = indexList.get(sourceIndex);
        int targetPosition = indexList.get(targetIndex);
        indexList.set(sourceIndex, targetPosition);
        indexList.set(targetIndex, sourcePosition);
    }

    private void rearrangeItems(int sourceIndex, int targetIndex) {
        int sourcePosition = indexList.get(sourceIndex);
        indexList.remove(sourceIndex);

        if (targetIndex > sourceIndex) {
            indexList.add(targetIndex - 1, sourcePosition);
        } else {
            indexList.add(targetIndex, sourcePosition);
        }
    }

    /**
     * Updates the bookshelf view, displaying the given bookshelf in the given grid.
     *
     * @param grid
     * @param bookshelf
     */
    public void updateBookshelf(GridPane grid, Bookshelf bookshelf) {
        grid.getChildren().clear();
        for (int i = 0; i < Bookshelf.getRows(); i++) {
            for (int j = 0; j < Bookshelf.getColumns(); j++) {
                if (bookshelf.getItemAt(i, j).isPresent()) {
                    String fileName = bookshelf.getItemFileName(i, j);
                    try {
                        Image itemImage = new Image(getClass().getResource(ITEM_BASE_PATH + fileName).toExternalForm());
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
                    ImageView itemImageView = new ImageView(getClass().getResource(ITEM_BASE_PATH + "null.png").toExternalForm());
                    itemImageView.setFitHeight(ITEM_SIZE);
                    itemImageView.setFitWidth(ITEM_SIZE);
                    itemImageView.setPreserveRatio(true);
                    itemImageView.setSmooth(true);
                    itemImageView.setCache(true);
                    // itemImageView.setCursor(Cursor.HAND); // Set the cursor to hand
                    // set image to be transparent
                    // itemImageView.setOpacity(0.0);
                    grid.add(itemImageView, j, Bookshelf.getRows() - i - 1);
                }
            }
        }
    }

    public void updateOtherBookshelves(Bookshelf bookshelf, String name) {

    }

    /**
     * Show a message on the messageLabel
     *
     * @param message
     */
    public void showMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }

    /**
     * Initialize the bookshelf grid with empty image views.
     */
    private void initializeBookshelfGrid() {
        for (int i = 0; i < Bookshelf.getRows(); i++) {
            for (int j = 0; j < Bookshelf.getColumns(); j++) {
                ImageView itemImageView = new ImageView(getClass().getResource(ITEM_BASE_PATH + "null.png").toExternalForm());
                itemImageView.setFitHeight(ITEM_SIZE);
                itemImageView.setFitWidth(ITEM_SIZE);
                itemImageView.setPreserveRatio(true);
                itemImageView.setSmooth(true);
                itemImageView.setCache(true);
                itemImageView.setCursor(Cursor.HAND); // Set the cursor to hand
                // set image to be transparent
                // itemImageView.setOpacity(0.0);
                bookshelfGrid.add(itemImageView, j, Bookshelf.getRows() - i - 1);
            }
        }
    }

    public void enableRearrange() {
        rearrangeArea.setDisable(false);
    }

    public void rearrange(List<Item> items) {
        rearrangeArea.getChildren().clear();
        for (Item item : items) {
            try {
                ImageView itemView = createImageView(ITEM_BASE_PATH + item.fileName());
                setupDragAndDrop(itemView, itemImageViews.size());
                rearrangeArea.getChildren().add(itemView);
                itemImageViews.add(itemView);
                indexList.add(items.indexOf(item));
                // removeItemsFromBoard(pickedItems);
            } catch (NullPointerException e) {
                System.err.println("Error on loading item image: " + item.fileName() + ", item not added to rearrange area");
            }
        }
    }
}