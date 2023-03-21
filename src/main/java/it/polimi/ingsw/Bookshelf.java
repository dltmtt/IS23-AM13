package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Matteo
 * @see Item
 * <p>
 * This class represents a bookshelf.
 * It has 5 rows and 6 columns.
 * Each cell can contain an item.
 */
public class Bookshelf {
    private final int rows = 6;
    private final int columns = 5;

    @SuppressWarnings("unchecked")
    private final Optional<Item>[][] items = (Optional<Item>[][]) new Optional[rows][columns];

    /**
     * Creates a new bookshelf. All cells are empty.
     */
    public Bookshelf() {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                this.items[i][j] = Optional.empty();
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
        if (column < 0 || column > getColumns()) {
            throw new IllegalArgumentException("Column index must be between 0 and " + getColumns());
        }

        if (items.size() > getFreeCellsInColumn(column)) {
            throw new IllegalArgumentException("Not enough free cells in column " + column);
        }

        if (items.size() == 0) {
            throw new IllegalArgumentException("No items to insert");
        }
        int freeCells = getFreeCellsInColumn(column);
        for (int i = 0; i < items.size(); i++) {
            this.items[getRows() - freeCells + i][column] = Optional.ofNullable(items.get(i));
        }
    }

    /**
     * This function returns
     *
     * @param row    row (first row is 0)
     * @param column column (first column is 0)
     * @author Simone
     */
    public Optional<Item> getItemAt(int row, int column) throws ArrayIndexOutOfBoundsException {
        if (row > getRows() || column > getColumns() || row < 0 || column < 0) {
            throw new ArrayIndexOutOfBoundsException("invalid input getItemAt");
        }
        return items[row][column];
    }

    /**
     * this function return whether a row is full
     *
     * @return true if the row is full
     */
    public boolean isRowFull(int row) {
        for (int col = 0; col < getColumns(); col++) {
            if (getItemAt(row, col).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public List<Item> getColumnContent(int col) {
        List<Item> content = new ArrayList<Item>();
        Optional<Item> it;
        for (int i = 0; i < getRows(); i++) {
            it = getItemAt(i, col);
            if (it.isPresent()) {
                content.add(it.get());
            }
        }
        return content;
    }

}
