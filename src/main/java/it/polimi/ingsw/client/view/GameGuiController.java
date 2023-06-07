package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.MyShelfie;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.Board;
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
import java.util.Objects;

public class GameGuiController {

    private static final String ITEM_BASE_PATH = "graphics/item_tiles/";
    private static final int ITEM_SIZE = 40; // Adjust the size of each item image
    private static final int ROW_SPACING = 10; // Adjust the spacing between rows
    private static final int COLUMN_SPACING = 10; // Adjust the spacing between columns
    public static List<Coordinates> pickedItems = new ArrayList<>();
    public static Client client;
    public static GuiView view;
    private final List<ImageView> itemImageViews = new ArrayList<>();
    private final List<Integer> indexList = new ArrayList<>();
    public Board boardModel;
    @FXML
    private ImageView board;

    @FXML
    private GridPane boardGridPane;

    @FXML
    private ImageView bookshelf;

    @FXML
    private GridPane bookshelfGrid;

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
     * @param row
     * @param col
     */

    // un po' rietitivo, se possibile da sistemare
    private void highlightPickableItems(int row, int col) {
        Node selectedNode;
        for (int i = 0; i < boardGridPane.getRowCount(); i++) {
            for (int j = 0; j < boardGridPane.getColumnCount(); j++) {
                if ((i == row || j == col) && (Math.abs(i - row) <= 2 && Math.abs(j - col) <= 2) && boardModel.checkBorder(new Coordinates(i, j))) {
                    if ((i == row - 2 && !boardModel.checkBorder(new Coordinates(i + 1, j))) || (i == row + 2 && !boardModel.checkBorder(new Coordinates(i - 1, j))) || (j == col - 2 && !boardModel.checkBorder(new Coordinates(i, j + 1))) || (j == col + 2 && !boardModel.checkBorder(new Coordinates(i, j - 1)))) {
                        selectedNode = getNodeByRowColumnIndex(i, j);
                        if (selectedNode != null) {
                            selectedNode.setDisable(true);
                            selectedNode.setEffect(null);
                            ColorAdjust colorAdjust = new ColorAdjust();
                            colorAdjust.setSaturation(-1);
                            selectedNode.setEffect(colorAdjust);
                        }
                    } else {
                        selectedNode = getNodeByRowColumnIndex(i, j);
                        if (selectedNode != null) {
                            selectedNode.setDisable(false);
                            selectedNode.setEffect(new DropShadow(20, Color.ORANGE));
                        }
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

    public void enableItem(int row, int col) {
        Node selectedNode = getNodeByRowColumnIndex(row, col);
        if (selectedNode != null) {
            selectedNode.setDisable(false);
            selectedNode.setEffect(new DropShadow(20, Color.ORANGE));
            // set node cursor as a hand
            selectedNode.setCursor(Cursor.HAND);
        }
    }

    public void disableItem(int row, int col) {
        Node selectedNode = getNodeByRowColumnIndex(row, col);
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

    public void disableAllItems() {
        ObservableList<Node> children = this.boardGridPane.getChildren();
        for (Node node : children) {
            node.setDisable(true);
            node.setEffect(null);
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setSaturation(-1);
            node.setEffect(colorAdjust);
            // set node cursor as a default
            node.setCursor(Cursor.DEFAULT);
        }
        // System.out.println("disabled all items");
    }

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
                    // pickedItems.add(new Coordinates(row, col));
                    System.out.println("picked items: " + pickedItems);
                    addPickedItemsToRearrangeArea();
                    confirmSelection.setDisable(false);
                }
            }
        }
    }

    public void removeItemsFromBoard(List<Coordinates> pickedItems){
        for(int i=0; i<boardModel.getBoardSize(); i++){
            for(int j=0; j<boardModel.getBoardSize();j++){
                for(Coordinates c:pickedItems){
                    if(c.x().equals(boardModel.getBoardSize() - i -1) && c.y().equals(j)){
                        int finalJ = j;
                        int finalI =i;
                        boardGridPane.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == finalJ && GridPane.getRowIndex(node) == finalI);
                    }
                }
            }
        }
    }
    @FXML
    void sendSelection(ActionEvent event) {
        List<Coordinates> pickedItemsCopy = new ArrayList<>(pickedItems);
        pickedItems.clear();
        disableAllItems();
        client.sendMessage(new Message(pickedItemsCopy.get(0), pickedItemsCopy.get(1)));
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

        boardModel = message.getBoard();
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
        System.out.println("game loaded");
    }

    public void toggleHelp() {

    }

    public void addPickedItemsToRearrangeArea() {
        if (Objects.equals(pickedItems.get(0).x(), pickedItems.get(1).x())) {
            for (int i = Math.min(pickedItems.get(0).y(), pickedItems.get(1).y()); i <= Math.max(pickedItems.get(0).y(), pickedItems.get(1).y()); i++) {
                String itemFileName = ITEM_BASE_PATH + boardModel.getItemFileName(pickedItems.get(0).x(), i);
                try {
                    ImageView item = createImageView(itemFileName);
                    setupDragAndDrop(item, itemImageViews.size());
                    rearrangeArea.getChildren().add(item);
                    itemImageViews.add(item);
                    indexList.add(itemImageViews.size() - 1);
                    removeItemsFromBoard(pickedItems);
                } catch (NullPointerException e) {
                    System.err.println("Error on loading item image: " + itemFileName + " at (" + pickedItems.get(0).x() + "," + i + "), item not added to rearrange area");
                }
            }
        } else {
            for (int i = Math.min(pickedItems.get(0).x(), pickedItems.get(1).x()); i <= Math.max(pickedItems.get(0).x(), pickedItems.get(1).x()); i++) {
                String itemFileName = ITEM_BASE_PATH + boardModel.getItemFileName(i, pickedItems.get(0).y());
                try {
                    ImageView item = createImageView(itemFileName);
                    setupDragAndDrop(item, itemImageViews.size());
                    rearrangeArea.getChildren().add(item);
                    itemImageViews.add(item);
                    indexList.add(itemImageViews.size() - 1);
                    removeItemsFromBoard(pickedItems);
                } catch (NullPointerException e) {
                    System.err.println("Error on loading item image: " + itemFileName + " at (" + pickedItems.get(0).x() + "," + i + "), item not added to rearrange area");
                }
            }
        }
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
}