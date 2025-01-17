package it.polimi.ingsw.server.model.layouts;

import it.polimi.ingsw.server.model.Bookshelf;

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
    public Diagonal(int minDifferent, int maxDifferent, int dimension) throws IllegalArgumentException {
        super(dimension, dimension, minDifferent, maxDifferent);
    }

    /**
     *
     * @return the name of the layout
     */
    @Override
    public String getName() {
        return "diagonal";
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
        if (b == null)
            throw new IllegalArgumentException("The bookshelf cannot be null");

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
        assert b != null;
        assert row >= 0 && row < Bookshelf.getRows();
        assert column >= 0 && column < Bookshelf.getColumns();

        int counter = 1;
        // Check if there are 5 items of the same color in a diagonal from the top right to the bottom left
        while (row < Bookshelf.getRows() - 1 && column < Bookshelf.getColumns() - 1) {
            if (b.getItemAt(row, column).isPresent() && b.getItemAt(row + 1, column + 1).isPresent()) {
                if (b.getItemAt(row, column).get().color() != b.getItemAt(row + 1, column + 1).get().color()) {
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
        assert b != null;
        assert row >= 0 && row < Bookshelf.getRows();
        assert column >= 0 && column < Bookshelf.getColumns();

        int counter = 1;
        while (row < Bookshelf.getRows() - 1 && column > 0) {
            if (b.getItemAt(row, column).isPresent() && b.getItemAt(row + 1, column - 1).isPresent()) {
                if (b.getItemAt(row, column).get().color() != b.getItemAt(row + 1, column - 1).get().color()) {
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

    /**
     *
     * @return the info of the layout
     */
    public String getInfo() {
        return super.getInfo() + "-type=diagonal";
    }
}
