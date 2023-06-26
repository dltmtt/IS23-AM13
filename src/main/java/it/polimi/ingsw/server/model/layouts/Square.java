package it.polimi.ingsw.server.model.layouts;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.utils.Color;

import java.util.Arrays;

/**
 * The common goal layout with a square of cells of the same type.
 */
public class Square extends Layout {

    /**
     * The number of ideal occurrences of the layout.
     */
    private final int occurrences;

    /**
     * The size of the square.
     */
    private final int size;

    /**
     * The matrix of booleans that represents the square.
     */
    boolean[][] booleanMatrix = new boolean[Bookshelf.getRows()][Bookshelf.getColumns()];

    /**
     * Creates a square layout.
     *
     * @param size         the dimension of the square
     * @param minDifferent the minimum number of different colors in the layout
     * @param maxDifferent the maximum number of different colors in the layout
     * @param occurrences  the number of occurrences of the layout
     * @throws IllegalArgumentException if the parameters are invalid
     */
    public Square(int minDifferent, int maxDifferent, int occurrences, int size) throws IllegalArgumentException {
        super(size, size, minDifferent, maxDifferent);

        if (occurrences < 0) {
            throw new IllegalArgumentException("Invalid number of occurrences");
        }

        this.occurrences = occurrences;
        this.size = size;
        for (int i = 0; i < Bookshelf.getRows(); i++) {
            Arrays.fill(booleanMatrix[i], true);
        }
    }

    /**
     *
     * @return the name of the layout.
     */
    @Override
    public String getName() {
        return "square";
    }

    /**
     * This method checks if the Square layout is valid in the bookshelf.
     *
     * @param b the bookshelf to check
     * @return true if the layout is valid, false otherwise
     */
    public boolean check(Bookshelf b) {
        for (int i = 0; i < Bookshelf.getRows(); i++) {
            Arrays.fill(booleanMatrix[i], true);
        }
        int found = 0;
        for (int j = 0; j < Bookshelf.getColumns(); j++) {
            for (int i = 0; i < Bookshelf.getRows(); i++) {
                if (b.getItemAt(i, j).isPresent()) {
                    found += searchSquare(b, b.getItemAt(i, j).get().color(), i, j);
                }
                if (found == occurrences)
                    return true;
            }
        }
        return false;
    }

    /**
     * This method searches for a square of the same color in the bookshelf.
     *
     * @param b      the bookshelf to check
     * @param color  the color of the square
     * @param row    the row of the first cell of the square
     * @param column the column of the first cell of the square
     * @return 1 if the square is found, 0 otherwise
     */
    public int searchSquare(Bookshelf b, Color color, int row, int column) {
        int current = 1;
        if (row >= Bookshelf.getRows() - 1 || column >= Bookshelf.getColumns() - 1) {
            return 0;
        }
        if (!booleanMatrix[row][column]) {
            return 0;
        }

        for (int i = 1; i < size; i++) {
            if (booleanMatrix[row][column + i] && b.getItemAt(row, column + i).isPresent() && b.getItemAt(row, column + i).get().color().equals(color)) {
                current++;
            }
            if (booleanMatrix[row + i][column] && b.getItemAt(row + i, column).isPresent() && b.getItemAt(row + i, column).get().color().equals(color)) {
                current++;
            }
            if (booleanMatrix[row + i][column + i] && b.getItemAt(row + i, column + i).isPresent() && b.getItemAt(row + i, column + i).get().color().equals(color)) {
                current++;
            }
        }

        if (current == size * size) {
            booleanMatrix[row][column] = false;
            for (int i = 1; i < size; i++) {
                booleanMatrix[row][column + i] = false;
                booleanMatrix[row + i][column] = false;
                booleanMatrix[row + i][column + i] = false;
            }
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * @return the info of the layout
     */
    public String getInfo() {
        return super.getInfo() + "-occurrences=" + occurrences + "-size= " + size + " -type=square";
    }
}
