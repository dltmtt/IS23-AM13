package it.polimi.ingsw.server.model.layouts;

import it.polimi.ingsw.server.model.Bookshelf;

/**
 * The common goal layout with columns of increasing or decreasing height.
 * Starting from the first column on the left or on the right,
 * each next column must be made of exactly one more tile.
 * Tiles can be of any type.
 */
public class Stair extends Layout {

    /**
     * Creates a stair layout.
     *
     * @param dimension    the dimension of the stair (number of columns)
     * @param minDifferent the minimum number of different colors
     * @param maxDifferent the maximum number of different colors
     * @throws IllegalArgumentException if the parameters are invalid
     */
    public Stair(int minDifferent, int maxDifferent, int dimension) throws IllegalArgumentException {
        // Stair layout ony checks only for the number of items in a column, so the min and max number of different colors is 1 and 6
        super(dimension, dimension, minDifferent, maxDifferent);
    }

    /**
     * Checks if there's a stair towards left.
     *
     * @param b bookshelf to check
     * @return true if there's a stair towards left
     * @throws IllegalArgumentException if the bookshelf is null
     */
    private boolean check_left(Bookshelf b) throws IllegalArgumentException {
        if (b == null) {
            throw new IllegalArgumentException("The bookshelf cannot be null");
        }

        int previousCol = b.getCellsInColumn(0) + 1;
        for (int i = 0; i < Bookshelf.getColumns(); i++) {
            int currentCol = b.getCellsInColumn(i);
            if (currentCol != previousCol - 1) {
                return false;
            }
            previousCol = currentCol;
        }
        return true;
    }

    /**
     * Checks if there's a stair towards right.
     *
     * @param b bookshelf to check
     * @return true if there's a stair towards right
     * @throws IllegalArgumentException if the bookshelf is null
     */
    private boolean check_right(Bookshelf b) throws IllegalArgumentException {
        if (b == null) {
            throw new IllegalArgumentException("The bookshelf cannot be null");
        }

        int previousCol = b.getCellsInColumn(0) - 1;
        for (int i = 0; i < Bookshelf.getColumns(); i++) {
            int currentCol = b.getCellsInColumn(i);
            if (currentCol != previousCol + 1) {
                return false;
            }
            previousCol = currentCol;
        }
        return true;
    }

    @Override
    public String getName() {
        return "stair";
    }

    /**
     * Checks if there's a stair.
     *
     * @param b bookshelf to check
     * @return true if there's a stair
     * @throws IllegalArgumentException if the bookshelf is null
     */
    public boolean check(Bookshelf b) throws IllegalArgumentException {
        if (b == null) {
            throw new IllegalArgumentException("The bookshelf cannot be null");
        }

        return check_right(b) || check_left(b);
    }

    public String getInfo() {
        return super.getInfo() + " -type: stair";
    }
}
