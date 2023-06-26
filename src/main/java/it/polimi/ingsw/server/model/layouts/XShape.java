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

    /**
     *
     * @return the name of the layout.
     */
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
    /**
     * @return a string representation of the layout
     */
    public String getInfo() {
        return super.getInfo() + "X Shape";
    }
}
