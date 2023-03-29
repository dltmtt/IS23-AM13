package it.polimi.ingsw.Models.Game;

import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Item.Item;
import it.polimi.ingsw.Models.Utility.Coordinates;
import it.polimi.ingsw.Models.Utility.UsableCells;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    // Matrix 9x9
    private final Item[][] boardMatrix;
    private final List<Item> itemBag;
    private final UsableCells usableCells;

    private final int numOfPlayer;


    public Board(int numOfPlayer) {
        boardMatrix = new Item[9][9];
        usableCells = new UsableCells(numOfPlayer);
        itemBag = new ArrayList<>();
        this.numOfPlayer = numOfPlayer;

        /*
         * Bag of Item initialized
         * i % 3 -> for the 3 possible images for each color, it is only used for the view.
         * i < 22 -> 'cause each color has 22 occurrences
         * At the end we have a list with 132 elements (Item)
         */
        for (int i = 0; i < 22; i++) {
            itemBag.add(new Item(Color.GREEN, i % 3));
        }

        for (int i = 0; i < 22; i++) {
            itemBag.add(new Item(Color.PINK, i % 3));
        }
        for (int i = 0; i < 22; i++) {
            itemBag.add(new Item(Color.WHITE, i % 3));
        }
        for (int i = 0; i < 22; i++) {
            itemBag.add(new Item(Color.YELLOW, i % 3));
        }
        for (int i = 0; i < 22; i++) {
            itemBag.add(new Item(Color.BLUE, i % 3));
        }
        for (int i = 0; i < 22; i++) {
            itemBag.add(new Item(Color.LIGHTBLUE, i % 3));
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
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
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
