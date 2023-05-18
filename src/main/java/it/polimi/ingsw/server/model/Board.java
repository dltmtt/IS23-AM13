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

    public static final int boardSize = 9;
    private static final int numOfColorOccurrences = 22;
    private final Item[][] boardMatrix;
    private final List<Item> itemBag;
    private final List<Coordinates> usableCells;
    private List<Item> extractedItems;

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
        JSONObject personalGoalJson;
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
        extractedItems = new ArrayList<>();
    }

    public Board() {
        boardMatrix = new Item[boardSize][boardSize];
        itemBag = new ArrayList<>();
        usableCells = new ArrayList<>();
        extractedItems = new ArrayList<>();
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
                    if (boardMatrix[row][column] == null)
                        boardMatrix[row][column] = itemBag.get(indexRandom);
                    itemBag.remove(indexRandom);
                }
            }
        }
    }

    public List<Item> pickFromBoard(List<Coordinates> pickedFromTo) throws IllegalAccessException {
        List<Item> itemsPicked = new ArrayList<>();
        if (pickedFromTo.size() == 1) {
            itemsPicked.add(boardMatrix[pickedFromTo.get(0).x()][pickedFromTo.get(0).y()]);
            boardMatrix[pickedFromTo.get(0).x()][pickedFromTo.get(0).y()] = null;
            return itemsPicked;
        }
        // same row
        if (Objects.equals(pickedFromTo.get(0).x(), pickedFromTo.get(1).x())) {
            int minCoord = Math.min(pickedFromTo.get(0).y(), pickedFromTo.get(1).y());
            int maxCoord = Math.max(pickedFromTo.get(0).y(), pickedFromTo.get(1).y());

            for (int i = minCoord; i <= maxCoord; i++) {
                itemsPicked.add(boardMatrix[pickedFromTo.get(0).x()][i]);
                boardMatrix[pickedFromTo.get(0).x()][i] = null;
            }
            // same column
        } else {
            int minCoord = Math.min(pickedFromTo.get(0).x(), pickedFromTo.get(1).x());
            int maxCoord = Math.max(pickedFromTo.get(0).x(), pickedFromTo.get(1).x());

            for (int i = minCoord; i <= maxCoord; i++) {
                itemsPicked.add(boardMatrix[i][pickedFromTo.get(0).y()]);
                boardMatrix[i][pickedFromTo.get(0).y()] = null;
            }
        }
        extractedItems = itemsPicked;
        return itemsPicked;
    }

    /**
     * checks if all the tiles chosen have at least one free adjacent cell
     *
     * @return true if at least one cell is free, false otherwise
     */
    public boolean AtLeastOneFree(List<Coordinates> cells) {
        for (Coordinates cell : cells) {
            if (!checkBorder(cell)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkBorder(Coordinates cell) {
        if (cell.x() > 0 && boardMatrix[cell.x() - 1][cell.y()] == null) {
            return true;
        }
        if (cell.x() < boardSize - 1 && boardMatrix[cell.x() + 1][cell.y()] == null) {
            return true;
        }
        if (cell.y() > 0 && boardMatrix[cell.x()][cell.y() - 1] == null) {
            return true;
        }
        return cell.y() < boardSize - 1 && boardMatrix[cell.x()][cell.y() + 1] == null;
    }

    // it returns the number of tiles left on the board
    public int numLeft() {
        int count = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (boardMatrix[i][j] != null) {
                    count++;
                }
            }
        }
        return count;
    }

    // method to see if the tile has nothing adjacent to it
    public boolean isAlone(int i, int j) {
        if (i == 0 && j == 0) {
            return boardMatrix[i + 1][j] == null && boardMatrix[i][j + 1] == null;
        } else if (i == 0 && j == boardSize - 1) {
            return boardMatrix[i + 1][j] == null && boardMatrix[i][j - 1] == null;
        } else if (i == boardSize - 1 && j == 0) {
            return boardMatrix[i - 1][j] == null && boardMatrix[i][j + 1] == null;
        } else if (i == boardSize - 1 && j == boardSize - 1) {
            return boardMatrix[i - 1][j] == null && boardMatrix[i][j - 1] == null;
        } else if (i == 0) {
            return boardMatrix[i + 1][j] == null && boardMatrix[i][j + 1] == null && boardMatrix[i][j - 1] == null;
        } else if (i == boardSize - 1) {
            return boardMatrix[i - 1][j] == null && boardMatrix[i][j + 1] == null && boardMatrix[i][j - 1] == null;
        } else if (j == 0) {
            return boardMatrix[i + 1][j] == null && boardMatrix[i - 1][j] == null && boardMatrix[i][j + 1] == null;
        } else if (j == boardSize - 1) {
            return boardMatrix[i + 1][j] == null && boardMatrix[i - 1][j] == null && boardMatrix[i][j - 1] == null;
        } else {
            return boardMatrix[i + 1][j] == null && boardMatrix[i - 1][j] == null && boardMatrix[i][j + 1] == null && boardMatrix[i][j - 1] == null;
        }
    }

    // method that checks if all the tiles in the board have no tiles adjacent to them
    // in this case: refill of the board
    public boolean allIsolated() {
        int count = numLeft();
        if (count == 0) {
            return true;
        }
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (boardMatrix[i][j] != null) {
                    if (isAlone(i, j))
                        count--;
                }
            }
        }
        return count == 0;
    }

    public boolean allNotNull(List<Coordinates> cells) {
        for (Coordinates cell : cells) {
            if (boardMatrix[cell.x()][cell.y()] == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * checks if the distance between the tiles chosen is at most 3 and if the order is correct (ascending)
     *
     * @param cells the list of coordinates of the tiles chosen
     * @return true if the order is correct and the distance is at most 3, false otherwise
     */
    public boolean OrderAndMaxOf3(List<Coordinates> cells) {
        if (Objects.equals(cells.get(0).x(), cells.get(1).x())) {
            return cells.get(1).y() - cells.get(0).y() <= 2;
        }
        return cells.get(1).x() - cells.get(0).x() <= 2;
    }

    public List<Item> selectFromBoard(List<Coordinates> selectedFromTo) {
        List<Item> itemsSelected = new ArrayList<>();
        for (Coordinates coordinates : selectedFromTo) {
            itemsSelected.add(boardMatrix[coordinates.x()][coordinates.y()]);
        }
        return itemsSelected;
    }
}
