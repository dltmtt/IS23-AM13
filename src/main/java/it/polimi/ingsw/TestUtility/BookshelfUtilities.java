package it.polimi.ingsw.TestUtility;

import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class BookshelfUtilities {
    // CORNER FUNCTIONS
    public void createCorner(Bookshelf b) {
        List<Item> soloItem = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        soloItem.add(new Item(Color.getRandomColor(), 0));
        b.insert(0, soloItem);
        b.insert(4, soloItem);
        for (int i = 0; i < 4; i++) {
            items.add(new Item(Color.getRandomColor(), 4));
        }
        b.insert(0, items);
        b.insert(4, items);
        b.insert(0, soloItem);
        b.insert(4, soloItem);
    }

    public void createFakeCorner(Bookshelf b, int choose) {
        switch (choose) {
            case 0 -> emptyBookshelf();
            case 1 -> oneDifferent(b);
            case 2 -> lastDifferent(b);
        }
    }

    public void emptyBookshelf() {
    }

    public void oneDifferent(Bookshelf b) {
        List<Item> soloItemEqual = new ArrayList<>();
        List<Item> soloItemDiff = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        soloItemDiff.add(new Item(Color.getRandomColor(), 4));
        soloItemEqual.add(new Item(Color.getRandomColor(), 3));
        while (soloItemDiff.get(0).equals(soloItemEqual.get(0))) {
            soloItemDiff.remove(0);
            soloItemDiff.add(new Item(Color.getRandomColor(), 4));
        }
        b.insert(0, soloItemEqual);
        b.insert(4, soloItemDiff);
        for (int i = 0; i < 4; i++) {
            items.add(new Item(Color.getRandomColor(), 3));
        }
        b.insert(0, items);
        b.insert(4, items);
        b.insert(0, soloItemEqual);
        b.insert(4, soloItemEqual);
    }

    public void lastDifferent(Bookshelf b) {
        List<Item> soloItemEqual = new ArrayList<>();
        List<Item> soloItemDiff = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        soloItemDiff.add(new Item(Color.getRandomColor(), 4));
        soloItemEqual.add(new Item(Color.getRandomColor(), 3));
        while (soloItemDiff.get(0).equals(soloItemEqual.get(0))) {
            soloItemDiff.remove(0);
            soloItemDiff.add(new Item(Color.getRandomColor(), 4));
        }
        b.insert(0, soloItemEqual);
        b.insert(4, soloItemEqual);
        for (int i = 0; i < 4; i++) {
            items.add(new Item(Color.getRandomColor(), 3));
        }
        b.insert(0, items);
        b.insert(4, items);
        b.insert(0, soloItemEqual);
        b.insert(4, soloItemDiff);
        b.cli_print();
    }

    public void createRandomElements(Bookshelf b, Color chosenColor, int number) {
        List<Item> itemList = new ArrayList<>();
        List<Color> colorsWithoutChosenColor;
        int randomNum;
        for (int i = 0; i < number; i++) {
            itemList.add(new Item(chosenColor, 0));
        }

        // To prevent the chosen color to be chosen again
        colorsWithoutChosenColor = Stream.of(Color.values()).filter(color -> color != chosenColor).toList();

        int cellRemaining = (b.getRows() * b.getColumns()) - itemList.size();

        // For some weird reason, the order of the operations in Java, makes weird things if I put directly the value of cellRemaining.
        for (int i = 0; i < cellRemaining; i++) {
            randomNum = ThreadLocalRandom.current().nextInt(0, colorsWithoutChosenColor.size());
            itemList.add(new Item(colorsWithoutChosenColor.get(randomNum), 0));
        }

        // Just random things
        for (int i = 0; i < 3; i++) {
            Collections.shuffle(itemList);
        }

        // Inserting the items in the bookshelf
        for (int i = 0; i < b.getRows(); i++) {
            for (int j = 0; j < b.getColumns(); j++) {
                b.insert(j, Collections.singletonList(itemList.get(i * b.getColumns() + j)));
            }
        }
    }

    public void createSingleRightDiagonal(Bookshelf b, int startingRow, int startingColumn, int dimension) {
        List<Item> itemList = new ArrayList<>();
        Color diagonalColor = Color.getRandomColor();

        for (int i = 0; i < startingRow; i++) {
            itemList.add(new Item(Color.getRandomColor(), 0));
        }

        for (int i = 0; i < b.getColumns(); i++) {
            if (itemList.size() > 0) {
                b.insert(i, itemList);
            }
        }

        for (int i = 0; i < dimension; i++) {
            itemList.clear();
            for (int j = 0; j < i; j++) {
                itemList.add(new Item(Color.getRandomColor(), 0));
            }
            itemList.add(new Item(diagonalColor, 0));
            b.insert(startingColumn + i, itemList);
        }
    }

    // In createLeftDiagonal, (startingRow, startingColumn) refers to the rightmost element of the diagonal

    public void createSingleLeftDiagonal(Bookshelf b, int startingRow, int startingColumn, int dimension) {
        List<Item> itemList = new ArrayList<>();
        Color diagonalcolor = Color.getRandomColor();

        for (int i = 0; i < dimension; i++) {
            itemList.clear();
            for (int j = 0; j < i; j++) {
                itemList.add(new Item(Color.getRandomColor(), 0));
            }
            itemList.add(new Item(diagonalcolor, 0));
            b.insert(startingColumn - i, itemList);
//            try {
//                b.insert(startingColumn - i, itemList);
//            } catch (IllegalArgumentException e) {
//                bookshelfPrint(b);
//            }
        }

    }

    public void createFakeDiagonal(Bookshelf b) {
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            itemList.add(new Item(Color.getRandomColor(), 0));
        }
        b.insert(4, itemList);
        itemList.remove(0);
        b.insert(3, itemList);
        itemList.remove(0);
        b.insert(2, itemList);
        itemList.remove(0);
        b.insert(1, itemList);
        // Dimension 4 diagonal
        for (int j = 0; j < b.getColumns() - 1; j++) {
            b.insert(j, itemList);
        }
        List<Item> items = new ArrayList<>();
        items.add(new Item(Color.getRandomColor(), 2));
        b.insert(4, items);
    }

    public void createFakeDiagonal(Bookshelf b, int type) {
        List<Item> equalItem = new ArrayList<>();
        equalItem.add(new Item(Color.getRandomColor(), 0));

        switch (type) {
            case 0 -> emptyBookshelf();
            case 1 -> onlyFirstCellRight(b, equalItem);
            case 2 -> Dimension2Right(b, equalItem);
            case 3 -> dimension3Right(b, equalItem);
            case 4 -> dimension4Right(b, equalItem);
            case 5 -> onlyFirstCellLeft(b, equalItem);
        }
    }

    public void onlyFirstCellRight(Bookshelf b, List<Item> items) {
        b.insert(0, items);
    }

    public void onlyFirstCellLeft(Bookshelf b, List<Item> items) {
        b.insert(4, items);
    }

    public void Dimension2Right(Bookshelf b, List<Item> items) {
        b.insert(0, items);
        List<Item> casual = new ArrayList<>();
        casual.add(new Item(Color.getRandomColor(), 0));

        b.insert(1, casual);
        b.insert(1, items);
    }

    public void dimension3Right(Bookshelf b, List<Item> items) {
        b.insert(0, items);
        List<Item> casual = new ArrayList<>();
        casual.add(new Item(Color.getRandomColor(), 0));
        b.insert(1, casual);
        b.insert(1, items);
        casual.add(new Item(Color.getRandomColor(), 0));
        b.insert(2, casual);
        b.insert(2, items);
    }

    public void dimension4Right(Bookshelf b, List<Item> items) {
        b.insert(0, items);
        List<Item> casual = new ArrayList<>();
        casual.add(new Item(Color.getRandomColor(), 0));
        b.insert(1, casual);
        b.insert(1, items);
        casual.add(new Item(Color.getRandomColor(), 0));
        b.insert(2, casual);
        b.insert(2, items);
        casual.add(new Item(Color.getRandomColor(), 0));
        b.insert(3, casual);
        b.insert(3, items);
    }

    public void createColumn(Bookshelf b, int different, int numberOfCol) {
        Color[] colors = Color.values();
        List<Item> itemList = new ArrayList<>();

        for (int col = 0; col < numberOfCol; col++) {
            itemList.clear();
            for (int i = 0; i < different; i++) {
                itemList.add(new Item(colors[i], 0));
            }

            for (int i = 0; i < b.getRows() - different; i++) {
                itemList.add(new Item(colors[0], 0));
            }

            b.insert(col, itemList);
        }

        // Fill the remaining columns with random colors
        for (int col = numberOfCol; col < b.getColumns(); col++) {
            itemList.clear();
            for (int i = 0; i < b.getRows(); i++) {
                itemList.add(new Item(Color.getRandomColor(), 0));
            }
            b.insert(col, itemList);
        }
    }

    public void createRow(Bookshelf b, int different, int numberOfRow) {
        Color[] colors = Color.values();
        List<Item> itemList = new ArrayList<>();
        List<Item> goalRow = new ArrayList<>();

        for (int i = 0; i < different; i++) {
            goalRow.add(new Item(colors[i], 0));
        }

        for (int i = 0; i < b.getColumns() - different; i++) {
            goalRow.add(new Item(colors[0], 0));
        }

        for (int number = 0; number < numberOfRow; number++) {
            for (int i = 0; i < b.getColumns(); i++) {
                b.insert(i, Collections.singletonList(goalRow.get(i)));
            }
        }

        for (int col = 0; col < b.getColumns(); col++) {
            itemList.clear();
            for (int i = 0; i < b.getFreeCellsInColumn(col); i++) {
                itemList.add(new Item(Color.getRandomColor(), 0));
            }
            b.insert(col, itemList);
        }
    }

    public void createLeftStair(Bookshelf b) {
        List<Item> itemList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            itemList.add(new Item(Color.getRandomColor(), 0));
        }

        b.insert(0, itemList);
        itemList.remove(0);
        b.insert(1, itemList);
        itemList.remove(0);
        b.insert(2, itemList);
        itemList.remove(0);
        b.insert(3, itemList);
        itemList.remove(0);
        b.insert(4, itemList);
        itemList.remove(0);
    }

    public void createRightStair(Bookshelf b) {
        List<Item> itemList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            itemList.add(new Item(Color.getRandomColor(), 0));
        }

        b.insert(4, itemList);
        itemList.remove(0);
        b.insert(3, itemList);
        itemList.remove(0);
        b.insert(2, itemList);
        itemList.remove(0);
        b.insert(1, itemList);
        itemList.remove(0);
        b.insert(0, itemList);
        itemList.remove(0);
    }
}
