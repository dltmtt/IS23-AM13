package it.polimi.ingsw.Models.Game;

import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Item.Item;
import it.polimi.ingsw.Models.Utility.Coordinates;
import it.polimi.ingsw.Models.Utility.UsableCells;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private static final int boardSize = 9;
    private static final int numOfColorOccurrences = 22;
    private final Item[][] boardMatrix;
    private final List<Item> itemBag;
    private final UsableCells usableCells;
    private final int numOfPlayer;

    public Board(int numOfPlayer) {
        boardMatrix = new Item[boardSize][boardSize];
        usableCells = new UsableCells(numOfPlayer);
        itemBag = new ArrayList<>();
        this.numOfPlayer = numOfPlayer;

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
