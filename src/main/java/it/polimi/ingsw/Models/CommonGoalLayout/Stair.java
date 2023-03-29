package it.polimi.ingsw.Models.CommonGoalLayout;

import it.polimi.ingsw.Models.Game.Bookshelf;

/**
 * This class implements the methods for a Stair common goal.
 */
public class Stair extends Layout {
    /**
     * This constructor creates a stair layout
     *
     * @param dimension the dimension of the stair
     * @throws IllegalArgumentException if the parameters are invalid
     */
    public Stair(int dimension) throws IllegalArgumentException {
        //stair layout ony checks only for the number of items in a column, so the min and max number of different colors is 1 and 6
        super(dimension, dimension, 1, 6);
    }

    /**
     * Method to check if there's a stair towards left
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
        for (int i = 0; i < b.getColumns(); i++) {
            int currentCol = b.getCellsInColumn(i);
            if (currentCol != previousCol - 1) {
                return false;
            }
            previousCol = currentCol;
        }
        return true;
    }

    /**
     * Method to check if there's a stair towards right
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
        for (int i = 0; i < b.getColumns(); i++) {
            int currentCol = b.getCellsInColumn(i);
            if (currentCol != previousCol + 1) {
                return false;
            }
            previousCol = currentCol;
        }
        return true;
    }

    /**
     * Method to check if there's a stair
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

