package it.polimi.ingsw.utils;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.server.model.layouts.Group;
import it.polimi.ingsw.server.model.layouts.Layout;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public final class BookshelfUtilities {

    public static void createColumn(Bookshelf b, int different, int numberOfCol) {
        Color[] colors = Color.values();
        List<Item> itemList = new ArrayList<>();

        for (int col = 0; col < numberOfCol; col++) {
            itemList.clear();
            for (int i = 0; i < different; i++) {
                itemList.add(new Item(colors[i], 0));
            }

            for (int i = 0; i < Bookshelf.getRows() - different; i++) {
                itemList.add(new Item(colors[0], 0));
            }

            b.insert(col, itemList);
        }

        // Fill the remaining columns with random colors
        for (int col = numberOfCol; col < Bookshelf.getColumns(); col++) {
            itemList.clear();
            for (int i = 0; i < Bookshelf.getRows(); i++) {
                itemList.add(new Item(Color.getRandomColor(), 0));
            }
            b.insert(col, itemList);
        }
    }

    public static void createRow(Bookshelf b, int different, int numberOfRow) {
        Color[] colors = Color.values();
        List<Item> itemList = new ArrayList<>();
        List<Item> goalRow = new ArrayList<>();

        for (int i = 0; i < different; i++) {
            goalRow.add(new Item(colors[i], 0));
        }

        for (int i = 0; i < Bookshelf.getColumns() - different; i++) {
            goalRow.add(new Item(colors[0], 0));
        }

        for (int number = 0; number < numberOfRow; number++) {
            for (int i = 0; i < Bookshelf.getColumns(); i++) {
                b.insert(i, Collections.singletonList(goalRow.get(i)));
            }
        }

        for (int col = 0; col < Bookshelf.getColumns(); col++) {
            itemList.clear();
            for (int i = 0; i < b.getFreeCellsInColumn(col); i++) {
                itemList.add(new Item(Color.getRandomColor(), 0));
            }
            b.insert(col, itemList);
        }
    }

    // CORNER FUNCTIONS
    public static void createCorner(Bookshelf b) {
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

    public static void createFakeCorner(Bookshelf b, int choose) {
        switch (choose) {
            case 0 -> emptyBookshelf();
            case 1 -> oneDifferent(b);
            case 2 -> lastDifferent(b);
        }
    }

    public static void emptyBookshelf() {
    }

    public static void oneDifferent(Bookshelf b) {
        List<Item> soloItemEqual = new ArrayList<>();
        List<Item> soloItemDiff = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        
        List<Color> colorlist= new ArrayList<>(Arrays.stream(Color.values()).toList());
        //random index of colors
        int randomColor = new Random().nextInt(colorlist.size());
        soloItemDiff.add(new Item(colorlist.get(randomColor), 4));

        colorlist.remove(randomColor);
        randomColor= new Random().nextInt(colorlist.size());
        soloItemEqual.add(new Item(colorlist.get(randomColor), 3));

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

    public static void lastDifferent(Bookshelf b) {
        List<Item> soloItemEqual = new ArrayList<>();
        List<Item> soloItemDiff = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        List<Color> colorlist= new ArrayList<>(Arrays.stream(Color.values()).toList());
        //random index of colors
        int randomColor = new Random().nextInt(colorlist.size());
        soloItemDiff.add(new Item(colorlist.get(randomColor), 4));

        colorlist.remove(randomColor);
        randomColor= new Random().nextInt(colorlist.size());
        soloItemEqual.add(new Item(colorlist.get(randomColor), 3));

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

    // In createLeftDiagonal, (startingRow, startingColumn) refers to the rightmost element of the diagonal

    public static void createRandomElements(Bookshelf b, Color chosenColor, int number) {
        List<Item> itemList = new ArrayList<>();
        List<Color> colorsWithoutChosenColor;
        int randomNum;
        for (int i = 0; i < number; i++) {
            itemList.add(new Item(chosenColor, 0));
        }

        // To prevent the chosen color to be chosen again
        colorsWithoutChosenColor = Stream.of(Color.values()).filter(color -> color != chosenColor).toList();

        int cellRemaining = Bookshelf.getSize() - itemList.size();

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
        for (int i = 0; i < Bookshelf.getRows(); i++) {
            for (int j = 0; j < Bookshelf.getColumns(); j++) {
                b.insert(j, Collections.singletonList(itemList.get(i * Bookshelf.getColumns() + j)));
            }
        }
    }

    public static void createSingleRightDiagonal(Bookshelf b, int startingRow, int startingColumn, int dimension) {
        List<Item> itemList = new ArrayList<>();
        Color diagonalColor = Color.getRandomColor();

        for (int i = 0; i < startingRow; i++) {
            itemList.add(new Item(Color.getRandomColor(), 0));
        }

        for (int i = 0; i < Bookshelf.getColumns(); i++) {
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

    public static void createSingleLeftDiagonal(Bookshelf b, int startingRow, int startingColumn, int dimension) {
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

    public static void createFakeDiagonal(Bookshelf b) {
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
        for (int j = 0; j < Bookshelf.getColumns() - 1; j++) {
            b.insert(j, itemList);
        }
        List<Item> items = new ArrayList<>();
        items.add(new Item(Color.getRandomColor(), 2));
        b.insert(4, items);
    }

    public static void createFakeDiagonal(Bookshelf b, int type) {
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

    public static void onlyFirstCellRight(Bookshelf b, List<Item> items) {
        b.insert(0, items);
    }

    public static void onlyFirstCellLeft(Bookshelf b, List<Item> items) {
        b.insert(4, items);
    }

    public static void Dimension2Right(Bookshelf b, List<Item> items) {
        b.insert(0, items);
        List<Item> casual = new ArrayList<>();
        casual.add(new Item(Color.getRandomColor(), 0));

        b.insert(1, casual);
        b.insert(1, items);
    }

    public static void dimension3Right(Bookshelf b, List<Item> items) {
        b.insert(0, items);
        List<Item> casual = new ArrayList<>();
        casual.add(new Item(Color.getRandomColor(), 0));
        b.insert(1, casual);
        b.insert(1, items);
        casual.add(new Item(Color.getRandomColor(), 0));
        b.insert(2, casual);
        b.insert(2, items);
    }

    public static void dimension4Right(Bookshelf b, List<Item> items) {
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

    public static void createLeftStair(Bookshelf b) {
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

    public static void createRightStair(Bookshelf b) {
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

    /**
     * Creates problematic layouts in the bookshelf
     * Please refer to the documentation for more information about the specific layouts
     *
     * @param b       the bookshelf
     * @param variant the variant of the layout (between 0 and 11)
     * @return the correct layout goal to be checked
     */
    public static Layout createProblematicLayouts(Bookshelf b, int variant) {
        //creates different problematics layout in the bookshelf depending on the variant chosen
        //it is implemented with a switch statement
        List<Color> colorList = List.of(Color.values());
        List<Item> items = new ArrayList<>();
        b.clearBooleanMatrix();
        b.clearBookshelf();

        switch (variant) {

            case (0) -> {
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(2), 0));

                b.insert(0, items);

                items.clear();
                //second column
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(2), 0));

                b.insert(1, items);

                items.clear();
                //third column
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(2), 0));

                b.insert(2, items);

                items.clear();
                //fourth column
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));

                b.insert(3, items);

                items.clear();

                //fifth column
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(2), 0));

                b.insert(4, items);
                return new Group(1, 1, 4, 4);
            }

            case (1) -> {

                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(4), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));

                b.insert(0, items);

                items.clear();
                //second column
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(3), 0));

                b.insert(1, items);

                items.clear();
                //third column
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(3), 0));

                b.insert(2, items);

                items.clear();
                //fourth column
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(4), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(4), 0));

                b.insert(3, items);

                items.clear();

                //fifth column
                items.add(new Item(colorList.get(4), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(4), 0));

                b.insert(4, items);
                return new Group(1, 1, 4, 4);
            }

            case (2) -> {

                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(3), 0));

                b.insert(0, items);

                items.clear();
                //second column
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(3), 0));

                b.insert(1, items);

                items.clear();
                //third column
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(4), 0));
                items.add(new Item(colorList.get(4), 0));

                b.insert(2, items);

                items.clear();
                //fourth column
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(4), 0));
                items.add(new Item(colorList.get(4), 0));
                items.add(new Item(colorList.get(2), 0));

                b.insert(3, items);

                items.clear();

                //fifth column
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));

                b.insert(4, items);
                return new Group(1, 1, 6, 4);
            }

            case (3) -> {

                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                //one empty space

                b.insert(0, items);

                items.clear();
                //second column
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(0), 0));
                //two empty spaces

                b.insert(1, items);

                items.clear();
                //third column
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                //two empty spaces

                b.insert(2, items);

                items.clear();
                //fourth column
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                //two empty spaces

                b.insert(3, items);

                items.clear();

                //fifth column
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(3), 0));

                b.insert(4, items);
                return new Group(1, 1, 4, 4);
            }

            case (4) -> {

                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                //three empty spaces


                b.insert(0, items);

                items.clear();
                //second column
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(1), 0));

                b.insert(1, items);

                items.clear();
                //third column
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));

                b.insert(2, items);

                items.clear();
                //fourth column
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(0), 0));
                //one empty space

                b.insert(3, items);

                items.clear();

                //fifth column
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(0), 0));
                //one empty space

                b.insert(4, items);
                return new Group(1, 1, 5, 4);
            }

            case (5) -> {

                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));


                b.insert(0, items);
                b.insert(2, items);


                items.clear();
                //second column
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));

                b.insert(1, items);
                b.insert(3, items);

                items.clear();
                //last column
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));

                b.insert(4, items);

                return new Group(1, 1, 7, 4);
            }

            case (6) -> {

                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(3), 0));

                b.insert(0, items);

                items.clear();
                //second column
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(3), 0));

                b.insert(1, items);

                items.clear();
                //third column
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(3), 0));

                b.insert(2, items);

                items.clear();
                //fourth column
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(2), 0));

                b.insert(3, items);

                items.clear();

                //fifth column
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(3), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));

                b.insert(4, items);
                return new Group(1, 1, 7, 4);
            }

            case (7) -> {

                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(0), 0));

                b.insert(0, items);

                items.clear();
                //second column
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));

                b.insert(1, items);

                items.clear();
                //third column
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(1), 0));

                b.insert(2, items);

                items.clear();
                //fourth column
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));

                b.insert(3, items);

                items.clear();

                //fifth column
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(2), 0));

                b.insert(4, items);
                return new Group(1, 1, 7, 4);
            }

            case (8) -> {

                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(0), 0));

                b.insert(0, items);

                items.clear();
                //second column
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(0), 0));

                b.insert(1, items);

                items.clear();
                //third column
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));

                b.insert(2, items);

                items.clear();
                //fourth column
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(2), 0));

                b.insert(3, items);

                items.clear();

                //fifth column
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));

                b.insert(4, items);
                return new Group(1, 1, 6, 4);
            }

            case (9) -> {

                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));

                b.insert(0, items);
                b.insert(1, items);

                items.clear();

                //third column
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(0), 0));
                //one empty space

                b.insert(2, items);

                items.clear();
                //fourth column
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(2), 0));
                items.add(new Item(colorList.get(2), 0));
                //one empty space

                b.insert(3, items);

                items.clear();

                //fifth column
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(0), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));
                items.add(new Item(colorList.get(1), 0));

                b.insert(4, items);
                return new Group(1, 1, 6, 4);
            }


            case (10) -> {
                int groupsize = 4;
                //correctly identifying a column of 4 items of the same color
                for (int i = 0; i < Bookshelf.getColumns(); i++) {
                    items.clear();
                    for (int index = 0; index < groupsize; index++) {
                        items.add(new Item(colorList.get(i % (colorList.size() - 1)), 0));
                    }

                    int numrem = Bookshelf.getRows() - items.size();
                    for (int remaining = 0; remaining < numrem; remaining++) {
                        items.add(new Item(colorList.get(colorList.size() - 1), 0));
                    }
                    b.insert(i, items);
                }
                return new Group(1, 1, Bookshelf.getColumns(), groupsize);
            }

            case (11) -> {
                //correctly identifying a row of 4 items of the same color
                int groupsize = 4;

                for (int i = 0; i < groupsize; i++) {
                    items.add(new Item(colorList.get(i % (colorList.size() - 1)), 0));
                }
                for (int i = 0; i < Bookshelf.getRows() - items.size(); i++) {
                    items.add(new Item(colorList.get(colorList.size() - 1), 0));
                }
                for (int i = 0; i < groupsize; i++) {
                    b.insert(i, items);
                }

                items.clear();
                for (int i = 0; i < Bookshelf.getColumns(); i++) {
                    items.add(new Item(colorList.get(colorList.size() - 1), 0));
                }


                for (int i = groupsize; i < Bookshelf.getColumns(); i++) {
                    b.insert(i, items);
                }
                return new Group(1, 1, Bookshelf.getColumns(), groupsize);
            }

            default -> throw new IllegalStateException("Unexpected value: " + variant);
        }
    }
}
