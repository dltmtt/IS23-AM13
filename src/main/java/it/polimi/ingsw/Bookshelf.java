package it.polimi.ingsw;

import java.util.List;
import java.util.Optional;

class Item {
    Item() {
        System.out.println("This is an item.");
    }
}

/**
 * @author Matteo
 * @see Item
 * <p>
 * This class represents a bookshelf.
 * It has 5 rows and 6 columns.
 * Each cell can contain an item.
 */
public class Bookshelf {
    private final int rows = 5;
    private final int columns = 6;
    private final Optional<Item>[][] items;

    public Bookshelf() {
        this.items = new Optional[getRows()][getColumns()];

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
    private int getFreeCellsInColumn(int column) {
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
     * @throws IllegalArgumentException if the number of items to insert is greater than the number of free cells in the column
     */
    public void insert(int column, List<Item> items) {
        if (column < 0 || column > getColumns()) {
            throw new IllegalArgumentException("Column index must be between 0 and 5");
        }

        if (items.size() > getFreeCellsInColumn(column)) {
            throw new IllegalArgumentException("Not enough free cells in column " + column);
        }

        for (int i = 0; i < items.size(); i++) {
            this.items[i][column] = Optional.ofNullable(items.get(i));
        }
    }
}
