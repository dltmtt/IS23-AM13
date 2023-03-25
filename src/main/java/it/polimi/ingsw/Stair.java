package it.polimi.ingsw;

/**
 * This class implements the methods for a Stair common goal.
 */
public class Stair extends Layout {

    /**
     * Constructor
     *
     * @param dimension width and height of the stair
     */
    public Stair(int dimension) throws IllegalArgumentException {
        super(dimension, dimension, 1, 6);
    }

    /**
     * Method to check if there's a stair towards left
     *
     * @param b bookshelf to check
     * @return true if there's a stair towards left
     */
    private boolean check_left(Bookshelf b) {
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
     */
    private boolean check_right(Bookshelf b) {
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

    public boolean check(Bookshelf b) {
        return check_right(b) || check_left(b);
    }

    public String getInfo() {
        return super.getInfo();
    }
}

