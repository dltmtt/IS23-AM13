package it.polimi.ingsw.model.commonGoalLayout;

import it.polimi.ingsw.model.game.Bookshelf;


/**
 * The common goal layout with five cards of the same type in a diagonal.
 */
public class Diagonal extends Layout {
    /**
     * Creates a diagonal layout.
     *
     * @param dimension    the dimension of the layout
     * @param minDifferent the minimum number of different colors in the layout
     * @param maxDifferent the maximum number of different colors in the layout
     * @throws IllegalArgumentException if the parameters are invalid
     */
    public Diagonal(int dimension, int minDifferent, int maxDifferent) throws IllegalArgumentException {
        super(dimension, dimension, minDifferent, maxDifferent);
    }

    /**
     * Checks if there are 5 cards of the same color in a diagonal.
     *
     * @param b the bookshelf to check
     * @return true if there are 5 cards of the same color in a diagonal, false otherwise
     * @throws IllegalArgumentException if the bookshelf is null
     */
    public boolean check(Bookshelf b) throws IllegalArgumentException {
        // Check if the bookshelf is null
        if (b == null) throw new IllegalArgumentException("The bookshelf cannot be null");

        if (checkRight(b, 0, 0) || checkRight(b, 1, 0)) {
            return true;
        }
        return checkLeft(b, 0, Bookshelf.getColumns() - 1) || checkLeft(b, 1, Bookshelf.getColumns() - 1); //|| checkRight(b, 0, 0);
    }

    /**
     * Checks if there are 5 cards of the same color in a diagonal from the top right to the bottom left.
     *
     * @param b      the bookshelf to check
     * @param row    the row of the first card
     * @param column the column of the first card
     * @return true if there are 5 cards of the same color in a diagonal from the top right to the bottom left, false otherwise
     */
    public boolean checkRight(Bookshelf b, int row, int column) throws IllegalArgumentException {
        if (b == null) {
            throw new IllegalArgumentException("The bookshelf cannot be null");
        }

        // Check if the row and the column are valid
        if (row < 0 || row >= Bookshelf.getRows() || column < 0 || column >= Bookshelf.getColumns()) {
            String message = "";
            if (row < 0 || row >= Bookshelf.getRows())
                message += "The row must be valid (0-" + (Bookshelf.getRows() - 1) + "), received " + row + " ";
            if (column < 0 || column >= Bookshelf.getColumns())
                message += "The column must be valid (0-" + (Bookshelf.getColumns() - 1) + "), received " + column + " ";
            throw new IllegalArgumentException(message);
        }

        int counter = 1;
        // Check if there are 5 items of the same color in a diagonal from the top right to the bottom left
        while (row < Bookshelf.getRows() - 1 && column < Bookshelf.getColumns() - 1) {
            if (b.getItemAt(row, column).isPresent() && b.getItemAt(row + 1, column + 1).isPresent()) {
                if (!b.getItemAt(row, column).get().color().equals(b.getItemAt(row + 1, column + 1).get().color())) {
                    break;
                } else {
                    counter++;
                    row++;
                    column++;
                }
            } else {
                break;
            }
        }
        return counter == 5;
    }

    /**
     * This method checks if there are 5 cards of the same color in a diagonal from the top left to the bottom right
     *
     * @param b      the bookshelf to check
     * @param row    the row of the first card
     * @param column the column of the first card
     * @return true if there are 5 cards of the same color in a diagonal from the top left to the bottom right, false otherwise
     */
    public boolean checkLeft(Bookshelf b, int row, int column) throws IllegalArgumentException {
        if (b == null) {
            throw new IllegalArgumentException("The bookshelf cannot be null");
        }

        // Check if the row and the column are valid
        if (row < 0 || row >= Bookshelf.getRows() || column < 0 || column >= Bookshelf.getColumns()) {
            String message = "";
            if (row < 0 || row >= Bookshelf.getRows())
                message += "The row must be valid (0-" + (Bookshelf.getRows() - 1) + "), received " + row + " ";
            if (column < 0 || column >= Bookshelf.getColumns())
                message += "The column must be valid (0-" + (Bookshelf.getColumns() - 1) + "), received " + column + " ";
            throw new IllegalArgumentException(message);
        }
        int counter = 1;
        while (row < Bookshelf.getRows() - 1 && column > 0) {
            if (b.getItemAt(row, column).isPresent() && b.getItemAt(row + 1, column - 1).isPresent()) {
                if (!b.getItemAt(row, column).get().color().equals(b.getItemAt(row + 1, column - 1).get().color())) {
                    break;
                } else {
                    counter++;
                    row++;
                    column--;
                }
            } else {
                break;
            }
        }
        return counter == 5;
    }

    public String getInfo() {
        return super.getInfo() + "-type=diagonal";
    }
}
