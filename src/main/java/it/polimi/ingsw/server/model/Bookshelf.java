package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utils.CliUtilities;
import it.polimi.ingsw.utils.Color;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.utils.SettingLoader.BASE_PATH;

/**
 * A bookshelf, where the player can collect items.
 * It has <code>rows</code> rows (6 by default) and <code>columns</code> (5 by default) columns.
 * Each cell can contain an item.
 *
 * @see Item
 */
public class Bookshelf implements AbleToGetPoints {

    private static int rows;
    private static int columns;
    private final Optional<Item>[][] items;
    // This matrix is used to check if a cell can be visited (and it has not been visited yet).
    // booleanMatrix[i][j] = true means that the cell (i, j) can be visited.
    private final boolean[][] booleanMatrix;

    /**
     * Creates a new bookshelf where all cells are empty.
     * <p>
     * <code>booleanMatrix</code> is used to check if a cell can be visited (and it has not been visited yet).
     * It is initialized to <code>true</code>.
     *
     * @param rows    the number of rows of the bookshelf
     * @param columns the number of columns of the bookshelf
     * @throws IllegalArgumentException if either <code>rows</code> or <code>columns</code> is less than 1
     */
    public Bookshelf(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            String errorMessage = "Bookshelf Constructor: Invalid parameters ";
            if (rows < 1) {
                errorMessage += "rows set to an invalid value (" + rows + "), 1 or more required";
            }
            if (columns < 1) {
                errorMessage += "columns set to an invalid value (" + columns + "), 1 or more required";
            }
            throw new IllegalArgumentException(errorMessage);
        }

        Bookshelf.rows = rows;
        Bookshelf.columns = columns;

        // noinspection unchecked
        items = (Optional<Item>[][]) new Optional[Bookshelf.rows][Bookshelf.columns];
        booleanMatrix = new boolean[Bookshelf.rows][Bookshelf.columns];
        clearBookshelf();
        clearBooleanMatrix();
    }

    public Bookshelf() {
        // noinspection unchecked
        items = (Optional<Item>[][]) new Optional[Bookshelf.rows][Bookshelf.columns];
        booleanMatrix = new boolean[Bookshelf.rows][Bookshelf.columns];
        clearBookshelf();
        clearBooleanMatrix();
    }

    public Bookshelf(Optional<Item>[][] items) {
        this.items = items;
        booleanMatrix = new boolean[Bookshelf.rows][Bookshelf.columns];
        clearBooleanMatrix();
    }

    /**
     * @return the number of rows in a bookshelf
     */
    public static int getRows() {
        return rows;
    }

    public static void setRows(int rows) {
        Bookshelf.rows = rows;
    }

    /**
     * @return the number of columns in a bookshelf
     */
    public static int getColumns() {
        return columns;
    }

    public static void setColumns(int columns) {
        Bookshelf.columns = columns;
    }

    /**
     * @return the number of cells in a bookshelf
     */
    public static int getSize() {
        return rows * columns;
    }

    /**
     * @return the number of rows or columns, whichever is smaller
     */
    public static int getDimension() {
        return Math.min(rows, columns);
    }

    /**
     * @param column the column index
     * @return the number of free cells in the column with index <code>column</code>
     */
    public int getFreeCellsInColumn(int column) {
        int freeCells = 0;
        for (int i = 0; i < rows; i++) {
            if (this.items[i][column].isEmpty()) {
                freeCells++;
            }
        }
        return freeCells;
    }

    /**
     * @param column the column index
     * @return the number of used cells in the column with index <code>column</code>
     */
    public int getCellsInColumn(int column) {
        int usedCells = 0;
        for (int i = 0; i < rows; i++) {
            if (this.items[i][column].isPresent()) {
                usedCells++;
            }
        }
        return usedCells;
    }

    // Clears the boolean matrix
    public void clearBooleanMatrix() {
        for (boolean[] row : booleanMatrix) {
            Arrays.fill(row, true);
        }
    }

    public void clearBookshelf() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.items[i][j] = Optional.empty();
            }
        }
        clearBooleanMatrix();
    }

    public boolean isBookshelfFull() {
        for (int i = 0; i < columns; i++) {
            if (!isColumnFull(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Inserts a list of items in a column.
     *
     * @param column the column index
     * @param items  the list of items to insert
     * @throws IllegalArgumentException if the column index is not between 0 and the number of columns
     * @throws IllegalArgumentException if the list of items is empty
     * @throws IllegalArgumentException if there are more items to insert than free cells in the column
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
     * @param row    the row index (starting from 0)
     * @param column the column index (starting from 0)
     * @return the item in the position (<code>row</code>, <code>column</code>)
     */
    public Optional<Item> getItemAt(int row, int column) throws ArrayIndexOutOfBoundsException {
        if (row >= rows || column >= columns || row < 0 || column < 0) {
            throw new ArrayIndexOutOfBoundsException("Invalid row or column for the method getItemAt -row:" + row + " -column: " + column);
        }

        return items[row][column];
    }

    /**
     * @param row the index of the row to check
     * @return true if the row is completely full, false otherwise
     */
    public boolean isRowFull(int row) throws IllegalArgumentException {
        if (row >= rows || row < 0) {
            throw new IllegalArgumentException("Invalid row for the method isRowFull -row:" + row);
        }
        for (int col = 0; col < columns; col++) {
            if (getItemAt(row, col).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param col the index of the column to check
     * @return true if the column is completely full, false otherwise
     * @throws IllegalArgumentException if the column index is not between 0 (inclusive) and the number of columns (exclusive)
     */
    public boolean isColumnFull(int col) throws IllegalArgumentException {
        if (col >= columns || col < 0) {
            throw new IllegalArgumentException("Invalid column for the method isColumnFull -column:" + col);
        }
        return getItemAt(rows - 1, col).isPresent();
    }

    /**
     * Calculates the points given by adjacent item tiles in this bookshelf.
     * Groups of adjacent item tiles of the same type grant points depending
     * on how many tiles are connected (with one side touching).
     * Items with the same background color are considered of the same type.
     *
     * @return the total score given by adjacent groups of items in this bookshelf
     * @see #adjacentGroups(Color, int, int)
     * @see #calculateGroupPoints(int)
     */
    public int getPoints() {
        int points = 0;
        clearBooleanMatrix();
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                if (items[i][j].isPresent()) {
                    // noinspection OptionalGetWithoutIsPresent
                    points += calculateGroupPoints(adjacentGroups(items[i][j].get().color(), i, j));
                }
            }
        }
        return points;
    }

    // TODO: clear the matrix after the method is called

    /**
     * Counts the size of the group of adjacent items of the same colors, starting from the <code>(row, column)</code> cell.
     *
     * @param color  the color to match
     * @param row    the starting row
     * @param column the starting column
     * @return the size of the group
     */
    public int adjacentGroups(Color color, int row, int column) {
        int matches;

        if (row >= rows || column >= columns || row < 0 || column < 0) {
            return 0;
        }

        if (items[row][column].isEmpty()) {
            return 0;
        }

        // noinspection OptionalGetWithoutIsPresent
        if (items[row][column].get().color() != color) {
            return 0;
        }

        if (!booleanMatrix[row][column]) {
            matches = adjacentGroups(color, row + 1, column) + adjacentGroups(color, row, column + 1);
        } else {
            booleanMatrix[row][column] = false;
            matches = 1 + adjacentGroups(color, row + 1, column) + adjacentGroups(color, row, column + 1) + adjacentGroups(color, row - 1, column) + adjacentGroups(color, row, column - 1);
        }
        return matches;
    }

    /**
     * Calculates the points given by a group of adjacent items of the same type.
     * Points are calculated as follows:
     * <ul>
     *  <li>2 points for 3 adjacent tiles of the same type
     *  <li>3 points for 4 adjacent tiles of the same type
     *  <li>5 points for 5 adjacent tiles of the same type
     *  <li>8 points for 6 or more adjacent tiles of the same type
     * </ul>
     *
     * @param matches the number of adjacent items of the same type
     * @return the points given by a group of items
     */
    public int calculateGroupPoints(int matches) {
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

    /**
     * Gets the content of a column as a list of items. Empty cells are not included.
     *
     * @param col the column index
     * @return the items in the column
     * @throws IllegalArgumentException if the column index is not between 0 and the number of columns
     */
    public List<Item> getColumnContent(int col) throws IllegalArgumentException {
        if (col < 0 || col > columns) {
            throw new IllegalArgumentException("Invalid number of column (" + col + ")");
        }
        List<Item> content = new ArrayList<>();
        Optional<Item> item;
        for (int i = 0; i < getCellsInColumn(col); i++) {
            item = getItemAt(i, col);
            item.ifPresent(content::add);
        }
        return content;
    }

    /**
     * Gets the content of a row as a list of items. Empty cells are not included.
     *
     * @param row the row index
     * @return the items in the row
     * @throws IllegalArgumentException if the row index is not between 0 and the number of rows
     */
    public List<Item> getRowContent(int row) throws IllegalArgumentException {
        if (row < 0 || row > getRows()) {
            throw new IllegalArgumentException("Invalid number of row (" + row + ")");
        }
        List<Item> content = new ArrayList<>();
        Optional<Item> item;
        for (int i = 0; i < getColumns(); i++) {
            item = getItemAt(row, i);
            item.ifPresent(content::add);
        }
        return content;
    }

    /**
     * Prints the board on the console.
     * TODO: move this method to the view
     */
    public void print() {
        // Without JPanel images would be added to JFrame on top of each other.
        // That way only last image would be visible.
        int left_offset = 11;
        int vertical_space = 11;
        int bottom_offset = 11;
        int col_width = 90;
        int col_height = 90;

        JPanel panel = new JPanel();

        ImageIcon BackgroundImage = new ImageIcon(BASE_PATH + "graphics/our_variants/back.jpg");

        panel.setLayout(null);
        panel.add(new JLabel(BackgroundImage));
        //        panel.setAlignmentX(1);
        Dimension bookshelfSize = new Dimension(BackgroundImage.getIconWidth(), BackgroundImage.getIconHeight());

        JFrame frame = new JFrame("Bookshelf Display V1.0");
        List<Item> itemList;
        JLabel item;
        ImageIcon itemImage;

        for (int col = 0; col < getColumns(); col++) {
            itemList = getColumnContent(col);
            for (int i = 0; i < itemList.size(); i++) {
                //                System.out.println(BASE_PATH + "graphics/our_variants/"+itemList.get(i).getColor().toString().toLowerCase()+".png");
                itemImage = new ImageIcon(BASE_PATH + "graphics/our_variants/" + itemList.get(i).color().toString().toLowerCase() + ".png");
                item = new JLabel(itemImage);
                item.setLayout(null);
                item.setLocation(left_offset + col_width * col, bookshelfSize.height - bottom_offset - (i + 1) * col_height);
                item.setVisible(true);
                item.getWidth();
                panel.add(item);
            }
        }
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
        //        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Optional<Item>[][] getItems() {
        return items;
    }

    public List<Item> getItemsAsList() {
        List<Item> itemList = new ArrayList<>();
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getColumns(); col++) {
                itemList.add(items[row][col].orElse(null));
            }
        }
        return itemList;
    }

    // TODO: move this to the view
    public void cli_print() {
        CliUtilities.stringifyBookshelf(items).forEach(System.out::println);
    }

    public void setItem(int row, int col, Optional<Item> item) {
        items[row][col] = item;
    }
}
