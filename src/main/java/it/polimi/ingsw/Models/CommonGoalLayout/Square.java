package it.polimi.ingsw.Models.CommonGoalLayout;

import it.polimi.ingsw.Models.Game.Bookshelf;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

// Not finished yet, just a draft

/**
 * The common goal layout with a square of cells of the same type.
 */
public class Square extends Layout {
    // The number of ideal occurrences of the layout
    private final int occurrences;

    /**
     * Creates a square layout.
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
     * Checks if the Square layout is valid in the bookshelf.
     *
     * @param b the bookshelf to check
     * @return true if the layout is valid, false otherwise
     */
    public boolean check(Bookshelf b) {
        return getCurrent(b) == getOccurrences();
    }

    /**
     * Returns the goal of occurrences of the layout.
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
        int validCells = 0;

        // Validity matrix
        boolean[][] valid = new boolean[Bookshelf.getColumns()][Bookshelf.getRows()];

        // Reset validity
        for (int i = 0; i < Bookshelf.getRows(); i++) {
            Arrays.fill(valid[i], true);
        }

        for (int col = 0; col < Bookshelf.getColumns() - getWidth(); col++) {
            for (int row = 0; row < Bookshelf.getRows() - getHeight(); row++) {
                if (Bookshelf.getRows() - b.getFreeCellsInColumn(col) - row >= getHeight())
                    for (int width = col; width < col + getWidth(); width++) {
                        for (int height = row; height < row + getHeight(); height++) {
//                            if(b.get)
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
