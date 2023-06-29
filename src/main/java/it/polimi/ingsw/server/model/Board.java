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

/**
 * This class represents the board of the game as a matrix of items. It also contains:
 * <ul>
 *   <li>itemBag: the bag of items</li>
 *   <li>usableCells: the list of coordinates of the cells that can be used</li>
 *   <li>extractedItems: the list of items extracted from the bag</li>
 * </ul>
 */
public class Board {

    /**
     * The size of the board.
     */
    public static final int boardSize = 9;

    /**
     * The number of occurrences of each color in the bag.
     */
    private static final int numOfColorOccurrences = 22;
    /**
     * The bag of items.
     */
    private final List<Item> itemBag;
    /**
     * The list of coordinates of the cells that can be used. It is
     * determined by the number of players.
     */
    private final List<Coordinates> usableCells;
    /**
     * The matrix of items that represents the board.
     */
    private final Item[][] boardMatrix;

    /**
     * This method:
     * <ul>
     *   <li>creates a new square board of size <code>boardSize</code>;</li>
     *   <li>fills the bag of items with every possible item;</li>
     *   <li>defines the usable cells in this board based on the number of players.</li>
     * </ul>
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
    }

    /**
     * This method creates a new square board of size <code>boardSize</code> and initializes:
     * <ul>
     *   <li>the bag of items;</li>
     *   <li>the list of usable cells;</li>
     *   <li>the list of extractedItems.</li>
     * </ul>
     */
    public Board() {
        boardMatrix = new Item[boardSize][boardSize];
        itemBag = new ArrayList<>();
        usableCells = new ArrayList<>();
    }

    public Board(Item[][] boardMatrix, List<Item> itemBag, int numOfPlayers) {
        this.boardMatrix = boardMatrix;
        this.itemBag = itemBag;
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
    }

    /**
     * This method sets the Item.
     *
     * @param row    the row of the board
     * @param column the column of the board
     * @param item   the item to be set
     */
    public void setItem(int row, int column, Item item) {
        boardMatrix[row][column] = item;
    }

    /**
     * This method returns the item bag
     *
     * @return the list of items contained in the bag
     */
    public List<Item> getItemBag() {
        return itemBag;
    }

    /**
     * This method enables to get an Item from the board.
     *
     * @param row    the row of the board
     * @param column the column of the board
     * @return the Item in the specified position
     */
    public Item getItem(int row, int column) {
        return boardMatrix[row][column];
    }

    /**
     * Given a row and a column, this method returns the name of the
     * image file that represents the Item in that position.
     *
     * @param row    the row of the board
     * @param column the column of the board
     * @return the name of the file that represents the Item in that position
     */
    public String getItemFileName(int row, int column) {
        Item item = boardMatrix[row][column];
        return item.color().toString().toLowerCase().charAt(0) + String.valueOf(item.number()) + ".png";
    }

    /**
     * This method the matrix
     *
     * @return the board as a matrix of Items.
     */
    public Item[][] getBoardMatrix() {
        return boardMatrix;
    }

    /**
     * This method returns the size of the board.
     *
     * @return the size of the board
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * This method puts all the Items in the usable cells of the board.
     * It is used at the beginning of the game and when the board needs to be refilled.
     */
    public void fill() {
        Random randNumberGenerator = new Random();
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if (itemBag.isEmpty()) {
                    return;
                } else if (usableCells.contains(new Coordinates(row, column))) {
                    int indexRandom = randNumberGenerator.nextInt(itemBag.size());
                    if (boardMatrix[row][column] == null)
                        boardMatrix[row][column] = itemBag.get(indexRandom);
                    itemBag.remove(indexRandom);
                }
            }
        }
    }

    /**
     * This method is used to extract a list of Items from the board.
     *
     * @param pickedFromTo the list of Coordinates of the Items to be extracted
     * @return the list of Items extracted
     */
    public List<Item> pickFromBoard(List<Coordinates> pickedFromTo) {
        List<Item> itemsPicked = new ArrayList<>();
        if (pickedFromTo.size() == 1) {
            itemsPicked.add(boardMatrix[pickedFromTo.get(0).x()][pickedFromTo.get(0).y()]);
            boardMatrix[pickedFromTo.get(0).x()][pickedFromTo.get(0).y()] = null;
            return itemsPicked;
        }
        // same row
        if (Objects.equals(pickedFromTo.get(0).x(), pickedFromTo.get(1).x())) {
            int minCoordinate = Math.min(pickedFromTo.get(0).y(), pickedFromTo.get(1).y());
            int maxCoordinate = Math.max(pickedFromTo.get(0).y(), pickedFromTo.get(1).y());

            for (int i = minCoordinate; i <= maxCoordinate; i++) {
                itemsPicked.add(boardMatrix[pickedFromTo.get(0).x()][i]);
                boardMatrix[pickedFromTo.get(0).x()][i] = null;
            }
            // same column
        } else {
            int minCoordinate = Math.min(pickedFromTo.get(0).x(), pickedFromTo.get(1).x());
            int maxCoordinate = Math.max(pickedFromTo.get(0).x(), pickedFromTo.get(1).x());

            for (int i = minCoordinate; i <= maxCoordinate; i++) {
                itemsPicked.add(boardMatrix[i][pickedFromTo.get(0).y()]);
                boardMatrix[i][pickedFromTo.get(0).y()] = null;
            }
        }
        return itemsPicked;
    }

    /**
     * This method checks if all the tiles chosen have at least one free adjacent cell
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

    /**
     * This method checks if the cell has a free side.
     *
     * @param cell contains the coordinates of the cell to be checked
     * @return true if the cell has at least one side free, false otherwise
     */
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

    /**
     * This method calculates the number of tiles left on the board.
     *
     * @return the number of tiles left on the board
     */
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

    /**
     * This method checks if the tile is not surrounded by other tiles.
     *
     * @param i corresponds to the row of the tile
     * @param j corresponds to the column of the tile
     * @return true if the tile is not surrounded by other tiles, false otherwise
     */
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

    /**
     * This method checks if all the tiles left in the board have no tiles adjacent to them.
     * It is used to see if the board needs to be refilled.
     *
     * @return true if all the tiles left in the board have no tiles adjacent to them, false otherwise
     */
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

    /**
     * This method checks if the start and end items are not null.
     *
     * @param cells the list of coordinates of the tiles chosen
     * @return true if all the tiles chosen are not null, false otherwise
     */
    public boolean startEndNotNull(List<Coordinates> cells) {
        for (Coordinates cell : cells) {
            if (boardMatrix[cell.x()][cell.y()] == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method checks if the distance between the tiles chosen is at most 3 and if the order is correct (ascending).
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
}
