package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Coordinates;
import it.polimi.ingsw.utils.UsableCells;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private static final int boardSize = 9;
    private static final int numOfColorOccurrences = 22;
    private final Item[][] boardMatrix;
    private final List<Item> itemBag;
    private final UsableCells usableCells;

    /**
     * Creates a new square board of size <code>boardSize</code>,
     * fills the bag of items with every possible item and defines
     * the usable cells in this board based on the number of players.
     *
     * @param numOfPlayers the number of players in the game
     */
    public Board(int numOfPlayers) {
        boardMatrix = new Item[boardSize][boardSize];
        usableCells = new UsableCells(numOfPlayers);
        itemBag = new ArrayList<>();

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
                } else if (usableCells.getList().contains(new Coordinates(row, column))) {
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
