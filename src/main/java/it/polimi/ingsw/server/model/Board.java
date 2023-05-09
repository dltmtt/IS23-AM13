package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Coordinates;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static it.polimi.ingsw.utils.SettingLoader.BASE_PATH;

public class Board {

    private static final int boardSize = 9;
    private static final int numOfColorOccurrences = 22;
    private final Item[][] boardMatrix;
    private final List<Item> itemBag;
    private final List<Coordinates> usableCells;

    /**
     * Creates a new square board of size <code>boardSize</code>,
     * fills the bag of items with every possible item and defines
     * the usable cells in this board based on the number of players.
     *
     * @param numOfPlayers the number of players in the game
     */
    public Board(int numOfPlayers) {
        boardMatrix = new Item[boardSize][boardSize];
        itemBag = new ArrayList<>();
        usableCells = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONObject personalGoalJson = null;
        try {
            personalGoalJson = (JSONObject) parser.parse(new FileReader(BASE_PATH + "usable_cells.json"));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        JSONArray usableCellsArray = (JSONArray) personalGoalJson.get("usable_cells");

        for (int i = 0; i <= numOfPlayers - 2; i++) {
            JSONObject elem = (JSONObject) usableCellsArray.get(i);
            JSONArray current_usable = (JSONArray) elem.get("current_usable");
            for (Object o : current_usable) {
                JSONObject cell = (JSONObject) o;
                usableCells.add(new Coordinates(Math.toIntExact((Long) cell.get("x")), Math.toIntExact((Long) cell.get("y"))));
            }
        }
        // Initialize the bag of items. For each color, there are 22 occurrences with 3 different images for a total of 132 items.
        for (Color color : Color.values()) {
            for (int i = 0; i < numOfColorOccurrences; i++) {
                itemBag.add(new Item(color, i % 3));
            }
        }
    }

    public Board() {
        boardMatrix = new Item[boardSize][boardSize];
        itemBag = new ArrayList<>();
        usableCells = new ArrayList<>();
    }

    public void setItem(int row, int column, Item item) {
        boardMatrix[row][column] = item;
    }

    public List<Item> getItemBag() {
        return itemBag;
    }

    public Item getItem(int row, int column) {
        return boardMatrix[row][column];
    }

    public Item[][] getBoardMatrix() {
        return boardMatrix;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void fill() {
        Random randNumberGenerator = new Random();
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if (itemBag.isEmpty()) {
                    System.err.println("cannot fll the board, itemBag is empty");
                } else if (usableCells.contains(new Coordinates(row, column))) {
                    int indexRandom = randNumberGenerator.nextInt(itemBag.size());
                    boardMatrix[row][column] = itemBag.get(indexRandom);
                    itemBag.remove(indexRandom);
                }
            }
        }
    }

    //x is row
    //y is column

    public List<Item> pickFromBoard(List<Coordinates> pickedFromTo) throws IllegalAccessException {
        List<Item> itemsPicked = new ArrayList<>();
        if (Objects.equals(pickedFromTo.get(0).x(), pickedFromTo.get(1).x())) {
            for (int i = pickedFromTo.get(0).y(); i <= pickedFromTo.get(1).y(); i++) {
                itemsPicked.add(boardMatrix[pickedFromTo.get(0).x()][i]);
                boardMatrix[pickedFromTo.get(0).x()][i] = null;
            }
        } else {
            for (int i = pickedFromTo.get(0).y(); i <= pickedFromTo.get(1).y(); i++) {
                itemsPicked.add(boardMatrix[i][pickedFromTo.get(0).y()]);
                boardMatrix[i][pickedFromTo.get(0).y()] = null;
            }
        }
        return itemsPicked;
    }

    public boolean isValidMove(List<Coordinates> list) {
        return true;
    }

    public List<Item> selectFromBoard(List<Coordinates> selectedFromTo) throws IllegalAccessException {
        List<Item> itemsSelected = new ArrayList<>();
        for (Coordinates coordinates : selectedFromTo) {
            itemsSelected.add(boardMatrix[coordinates.x()][coordinates.y()]);
        }
        return itemsSelected;
    }
}
