package it.polimi.ingsw;

import org.jetbrains.annotations.NotNull;

/**
 * @author Simone
 * This class implements the methods for a Square common goal.
 * @implNote This class is not yet finished, it is just a draft.
 */
public class Square extends Layout {

    /**
     * The number of ideal occurrences of the layout
     */
    private final int occurrences;

    /**
     * This constructor creates a square layout
     *
     * @param dimension    the dimension of the square
     * @param minDifferent the minimum number of different colors in the layout
     * @param maxDifferent the maximum number of different colors in the layout
     * @param occurrences  the number of occurrences of the layout
     * @throws IllegalArgumentException if the parameters are invalid
     */
    public Square(int dimension, int minDifferent, int maxDifferent, int occurrences) throws IllegalArgumentException {
        super(dimension, dimension, minDifferent, maxDifferent);

        if (occurrences < 0) {
            throw new IllegalArgumentException("Invalid number of occurrences");
        }

        this.occurrences = occurrences;
    }


    /**
     * This function checks if the Square layout is valid in the bookshelf
     *
     * @param b the bookshelf to check
     * @return true if the layout is valid, false otherwise
     */
    public boolean check(Bookshelf b) {
        return getCurrent(b) == getOccurrences();
    }

    /**
     * This function returns the goal of occurrences of the layout
     *
     * @return the number of occurrences of the layout
     */
    public int getOccurrences() {
        return occurrences;
    }

    private int bottomUpCheck(@NotNull Bookshelf b) {
        return 0;

    }

    private int topDownCheck(@NotNull Bookshelf b) {
        return 0;
    }

    public int getCurrent(@NotNull Bookshelf b) {
        int counter = 0;
        int validcells = 0;

        //validity matrix
        boolean[][] valid = new boolean[b.getColumns()][b.getRows()];

        //reset validity
        for (int i = 0; i < b.getRows(); i++) {
            for (int j = 0; j < b.getColumns(); j++) {
                valid[i][j] = true;
            }
        }


        for (int col = 0; col < b.getColumns() - getWidth(); col++) {
            for (int row = 0; row < b.getRows() - getHeight(); row++) {
                if (b.getRows() - b.getFreeCellsInColumn(col) - row >= getHeight())
                    for (int width = col; width < col + getWidth(); width++) {
                        for (int height = row; height < row + getHeight(); height++) {
                            //if(b.get)
                        }
                    }
            }
        }
        return 1;
    }

    public String getInfo() {
        return super.getInfo() + "-occurrences=" + occurrences + " -type=rectangle";
    }
}
