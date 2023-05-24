package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class GameGuiController {

    private static final String BASE_PATH = "/graphics/item_tiles/";
    private static final int ITEM_SIZE = 50; // Adjust the size of each item image
    private static final int ROW_SPACING = 10; // Adjust the spacing between rows
    private static final int COLUMN_SPACING = 10; // Adjust the spacing between columns

    public Client client;
    public GuiView view;
    @FXML
    private GridPane boardGridPane;
    @FXML
    private ImageView bookshelf, bookshelf_g1, bookshelf_g2, bookshelf_g3;
    @FXML
    private ImageView board;
    @FXML
    private Canvas boardCanvas, bookshelfCanvas;

    public GameGuiController(Client client, GuiView view) {
        this.client = client;
        this.view = view;
    }

    public static void drawBookshelf(Canvas bookshelfCanvas, Bookshelf bookshelf) {
        int rows = Bookshelf.getRows();
        int columns = Bookshelf.getColumns();

        // Calculate the total width and height of the bookshelf
        double width = columns * (ITEM_SIZE + COLUMN_SPACING) - COLUMN_SPACING;
        double height = rows * (ITEM_SIZE + ROW_SPACING) - ROW_SPACING;

        // Create a new GraphicsContext to draw on the canvas
        GraphicsContext graphics = bookshelfCanvas.getGraphicsContext2D();

        // Clear the canvas to make it transparent
        graphics.clearRect(0, 0, bookshelfCanvas.getWidth(), bookshelfCanvas.getHeight());

        // Draw each item on the bookshelf
        Optional<Item>[][] items = bookshelf.getItems();
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                Optional<Item> item = items[row][column];
                if (item.isPresent()) {
                    // Calculate the position of the item on the canvas
                    double x = column * (ITEM_SIZE + COLUMN_SPACING);
                    double y = row * (ITEM_SIZE + ROW_SPACING);

                    // Load the item image
                    String imagePath = BASE_PATH + item.get().color().toString().charAt(0) + item.get().number() + ".png";
                    Image image = new Image(imagePath, ITEM_SIZE, ITEM_SIZE, true, true);

                    // Draw the item on the canvas
                    graphics.drawImage(image, x, y);
                }
            }
        }
    }

    public void drawItems(Item[][] items) {
        boardGridPane.getChildren().clear();

        for (int row = 0; row < items.length; row++) {
            for (int column = 0; column < items[row].length; column++) {
                Item item = items[row][column];
                Button button = new Button();
                button.setPrefSize(ITEM_SIZE, ITEM_SIZE);
                button.setBackground(null); // Remove default button background
                String imagePath;
                if (item != null) {
                    imagePath = BASE_PATH + item.color().toString().toLowerCase().charAt(0) + item.number() + ".png";
                } else {
                    imagePath = BASE_PATH + "null.png";
                }
                Image image = new Image(getClass().getResourceAsStream(imagePath));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(ITEM_SIZE);
                imageView.setFitHeight(ITEM_SIZE);
                button.setGraphic(imageView);
                boardGridPane.add(button, column, row);
            }
        }

        boardGridPane.setHgap(COLUMN_SPACING);
        boardGridPane.setVgap(ROW_SPACING);
        boardGridPane.setPadding(new Insets(10));
    }
}