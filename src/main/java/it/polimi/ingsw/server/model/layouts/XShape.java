package it.polimi.ingsw.server.model.layouts;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.utils.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * The common goal layout with tiles of the same type forming an X shape.
 * The X Shape is a 3×3 layout with the following cells filled:
 * <ul>
 *  <li>bottom left
 *  <li>bottom right
 *  <li>central
 *  <li>upper left
 *  <li>upper right
 * </ul>
 * The number of different colors in the layout must be between the minimum and the maximum.
 * The width and height of the layout are set to 3 because the number of rows and columns is taken directly from the examined bookshelf.
 */
public class XShape extends Layout {
    private final List<Color> colorsList = new ArrayList<>();

    /**
     * Creates a new X Shape layout with the given parameters.
     * The width and height of the layout are set to 3 because the number of rows and columns is taken directly from the examined bookshelf.
     *
     * @param dimension    the width and height of the layout
     * @param minDifferent the minimum number of different colors in a layout
     * @param maxDifferent the maximum number of different colors in a layout
     */
    public XShape(int minDifferent, int maxDifferent, int dimension) {
        // For now dimension parameter (width and height) are ignored
        super(3, 3, minDifferent, maxDifferent);
    }

    @Override
    public String getName() {
        return "xShape";
    }

    /**
     * This method checks if the bookshelf has no free cells in the first
     * and last column and if the number of distinct colors in the four
     * corners is between the minimum and the maximum.
     *
     * @param b the bookshelf to check
     * @return true if the bookshelf has no free cells in the first and
     * last column and if the number of distinct colors in the four corners is
     * between the minimum and the maximum, false otherwise
     * @throws IllegalArgumentException if the bookshelf is null
     */

    public boolean check(Bookshelf b) throws IllegalArgumentException{
        if(b==null)
            throw new IllegalArgumentException("The bookshelf cannot be null");

        Color color;
        boolean check;

        for(int row =0; row<Bookshelf.getRows(); row++){
            for(int col=0; col<Bookshelf.getColumns();col++) {
                colorsList.clear();
                check = true;
                if (b.getItemAt(row, col).isPresent()) {

                    //element in the same row on the right
                    if (col + 2 < Bookshelf.getColumns() && b.getItemAt(row, col + 2).isPresent()) {
                        colorsList.add(b.getItemAt(row, col + 2).get().color());
                    } else
                        continue;
                    //element in the same column on the bottom
                    if (row - 2 >= 0 && b.getItemAt(row - 2, col).isPresent()) {
                        colorsList.add(b.getItemAt(row - 2, col).get().color());
                    } else
                        continue;
                    //element in the row below and next column
                    if (col + 1 < Bookshelf.getColumns() && b.getItemAt(row - 1, col + 1).isPresent()) {
                        colorsList.add(b.getItemAt(row - 1, col + 1).get().color());
                    } else
                        continue;
                    // element on the bottom and on the left
                    if (col + 2 < Bookshelf.getColumns() && b.getItemAt(row - 2, col + 2).isPresent()) {
                        colorsList.add(b.getItemAt(row - 2, col + 2).get().color());
                    } else
                        continue;

                    color = b.getItemAt(row, col).get().color();
                    if (colorsList.size() < 4)
                        check = false;
                    else {
                        for (Color c : colorsList) {
                            if (c != color) {
                                check = false;
                                break;
                            }
                        }
                    }
                    if (check)
                        return true;
                }
            }
        }
        return false;
    }
    /*
    public boolean check(Bookshelf b) throws IllegalArgumentException {
        if (b == null)
            throw new IllegalArgumentException("The bookshelf cannot be null");

        boolean check_iteration;
        List<Color> colorList = new ArrayList<>();
        int col;
        int row;

        for (row = 0; row < Bookshelf.getRows() - getHeight() + 1; row++) {
            for (col = 0; col < Bookshelf.getColumns() - getWidth() + 1; col++) {
                if (b.getItemAt(row, col).isPresent()) {
                    // The first check to make sure there are the minimum number of item to begin the check
                    // in particular, if the check starts on (0,0) then the following cells must be filled: (2, 0), (2, 1), (2, 2)
                    // so in general, given the parameters row and column, the check can proceed if and only if, cell from (row+getHeight()-1, col) to (row+getHeight-1, col+getWidth()-1) are filled
                    check_iteration = true;
                    for (int index = col; index < col + getWidth() - 1; index++) {
                        check_iteration = check_iteration && b.getItemAt(row + getHeight() - 1, index).isPresent();
                    }

                    if (check_iteration) {
                        // Bottom left
                        colorList.clear();
                        colorList.add(b.getItemAt(row, col).get().color());

                        // Bottom right
                        if (b.getItemAt(row, col + 2).isEmpty()) {
                            continue;
                        }
                        Color color = b.getItemAt(row, col + 2).get().color();
                        if (!colorList.contains(color)) {
                            colorList.add(color);
                        }

                        if (b.getItemAt(row + 1, col + 1).isEmpty()) {
                            continue;
                        }

                        // Central
                        color = b.getItemAt(row + 1, col + 1).get().color();
                        if (!colorList.contains(color)) {
                            colorList.add(color);
                        }
                        if (b.getItemAt(row + 2, col).isEmpty()) {
                            continue;
                        }

                        // Upper left
                        color = b.getItemAt(row + 2, col).get().color();
                        if (!colorList.contains(color)) {
                            colorList.add(color);
                        }

                        // Upper right
                        color = b.getItemAt(row + 2, col).get().color();
                        if (!colorList.contains(color)) {
                            colorList.add(color);
                        }

                        // The final check is all about the number of different colors found
                        if (colorList.size() >= getMinDifferent() && colorList.size() <= getMaxDifferent()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    */

    /**
     * @return a string representation of the layout
     */
    public String getInfo() {
        return super.getInfo() + "X Shape";
    }
}
