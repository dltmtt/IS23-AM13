package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Matteo
 * @test CalculateGroupsTest
 *
 * <p>
 * This class represents a bookshelf.
 * It has 5 rows and 6 columns.
 * Each cell can contain an item.
 * @see Item
 */
public class Bookshelf implements AbleToGetPoints {
    private final int rows = 6;
    private final int columns = 5;
    private final boolean[][] boolMatrix;

    @SuppressWarnings("unchecked")
    private final Optional<Item>[][] items = (Optional<Item>[][]) new Optional[rows][columns];

    /**
     * Creates a new bookshelf. All cells are empty.
     * <p>
     * boolMatrix is a matrix of booleans that is used to check if a cell can be visited (and it has not been visited yet).
     * It is initialized to true.
     */
    public Bookshelf() {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                this.items[i][j] = Optional.empty();
            }
        }
        boolMatrix = new boolean[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                boolMatrix[i][j] = true;
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    /**
     * Returns the number of free cells in a column.
     *
     * @param column the column index
     * @return the number of free cells in the column
     */
    public int getFreeCellsInColumn(int column) {
        int freeCells = 0;
        for (int i = 0; i < getRows(); i++) {
            if (this.items[i][column].isEmpty()) {
                freeCells++;
            }
        }
        return freeCells;
    }

    public int getCellsInColumn(int column) {
        int usedCells = 0;
        for (int i = 0; i < getRows(); i++) {
            if (this.items[i][column].isPresent()) {
                usedCells++;
            }
        }
        return usedCells;
    }

    /**
     * Inserts a list of items in a column.
     *
     * @param column the column index
     * @param items  the list of items to insert
     * @throws IllegalArgumentException if the column index is not between 0 and the number of columns
     * @throws IllegalArgumentException if the list of items is empty
     * @throws IllegalArgumentException if the number of items to insert is greater than the number of free cells in the column
     */
    public void insert(int column, List<Item> items) {
        if (column < 0 || column > columns) {
            throw new IllegalArgumentException("Column index must be between 0 and " + columns);
        }

        if (items.size() > getFreeCellsInColumn(column)) {
            throw new IllegalArgumentException("Not enough free cells in column " + column + " available: " + getFreeCellsInColumn(column) + " required: " + items.size());
        }

        if (items.size() == 0) {
            throw new IllegalArgumentException("No items to insert");
        }

        int freeCells = getFreeCellsInColumn(column);
        for (int i = 0; i < items.size(); i++) {
            this.items[(rows - freeCells) + i][column] = Optional.ofNullable(items.get(i));
        }
    }

    /**
     * This function returns the item in the position (row, column).
     *
     * @param row    row (starting from 0)
     * @param column column (starting from 0)
     * @return the item in the position (row, column)
     * @author Simone
     */
    public Optional<Item> getItemAt(int row, int column) throws ArrayIndexOutOfBoundsException {
        if (row >= rows || column >= columns || row < 0 || column < 0) {
            throw new ArrayIndexOutOfBoundsException("Invalid row or column for the method getItemAt -row:" + row + " -column: " + column);
        }
        return items[row][column];
    }

    /**
     * This function tells whether a row is full or not.
     *
     * @param row row to check for completion
     * @return true if the row is completely full, false otherwise
     */
    public boolean isRowFull(int row) {
        for (int col = 0; col < columns; col++) {
            if (getItemAt(row, col).isEmpty()) {
                return false;
            }
        }
        return true;
    }


    /**
     * This method calculates the points of adjacent groups of items of the bookshelf.
     *
     * @return the total score of adjacent groups of items of the given bookshelf
     * @author Valeria
     */
    public int getPoints() {
        int points = 0;
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                if (items[i][j].isPresent()) {
                    points += calculatePoints(adjacentGroups(items[i][j].get().getColor(), i, j));
                }
            }
        }
        return points;
    }

    /**
     * Counts the number of items, in a given bookshelf, that match the given color
     *
     * @param color  the color to match
     * @param row    the starting row
     * @param column the starting column
     * @return the number of items that match the given color
     * @author Valeria
     */
    public int adjacentGroups(Color color, int row, int column) {
        int matches;

        if (row >= rows || column >= columns) {
            return 0;
        }

        if (items[row][column].isEmpty()) {
            return 0;
        }

        if (!items[row][column].get().getColor().equals(color)) {
            return 0;
        }

        if (!boolMatrix[row][column]) {
            matches = adjacentGroups(color, row + 1, column) + adjacentGroups(color, row, column + 1);
        } else {
            boolMatrix[row][column] = false;
            matches = 1 + adjacentGroups(color, row + 1, column) + adjacentGroups(color, row, column + 1);
        }
        return matches;
    }

    /**
     * Calculates the points for a given number of matches
     *
     * @param matches the number of matches
     * @return the points for the given number of matches
     */
    public int calculatePoints(int matches) {
        if (matches < 3)
            return 0;
        else if (matches == 3)
            return 2;
        else if (matches == 4)
            return 3;
        else if (matches == 5)
            return 5;
        else
            return 8;
    }

    public List<Item> getColumnContent(int col) {
        List<Item> content = new ArrayList<>();
        Optional<Item> item;
        for (int i = 0; i < rows; i++) {
            item = getItemAt(i, col);
            item.ifPresent(content::add);
        }
        return content;
    }

    public void print() {
        String singleRow;
        List<String> stringBook = new ArrayList<>();
        for (int row = 0; row < getRows(); row++) {
            singleRow = row + "\t";
            for (int col = 0; col < getColumns(); col++) {
                singleRow += "\t" +
                        (getItemAt(row, col).isPresent() ? getItemAt(row, col).get().getColor().ordinal() : " ");
            }
            stringBook.add(singleRow);
        }

        for (int i = 0; i < getRows(); i++) {
            System.out.println(stringBook.get(stringBook.size() - i - 1));
        }

        String colNum = " ";
        for (int i = 0; i < getColumns(); i++) {
            colNum += "\t" + i;
        }
        System.out.println(" \t" + colNum);
    }
}
