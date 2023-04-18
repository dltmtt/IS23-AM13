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
import java.util.Random;

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
    public Board(int numOfPlayers) throws IOException, ParseException {
        boardMatrix = new Item[boardSize][boardSize];
        itemBag = new ArrayList<>();
        usableCells = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONObject personalGoalJson = (JSONObject) parser.parse(new FileReader("src/main/resources/usable_cells.json"));
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

    public List<Item> getItemBag() {
        return itemBag;
    }

    public Item getItem(int row, int column) {
        return boardMatrix[row][column];
    }

    public void fill() throws IllegalAccessException {
        Random randNumberGenerator = new Random();
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if (itemBag.isEmpty()) {
                    throw new IllegalAccessException("the list is empty");
                } else if (usableCells.contains(new Coordinates(row, column))) {
                    int indexRandom = randNumberGenerator.nextInt(itemBag.size());
                    boardMatrix[row][column] = itemBag.get(indexRandom);
                    itemBag.remove(indexRandom);
                }
            }
        }
    }

    public List<Item> pickFromBoard(List<Coordinates> pickedFromB) throws IllegalAccessException {
        List<Item> itemsPicked = new ArrayList<>();
        for (Coordinates coordinates : pickedFromB) {
            itemsPicked.add(boardMatrix[coordinates.x()][coordinates.y()]);
            boardMatrix[coordinates.x()][coordinates.y()] = null;
        }
        return itemsPicked;
    }
}
