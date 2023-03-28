package it.polimi.ingsw.Model.commonGoalLayout;

import it.polimi.ingsw.Model.Game.Bookshelf;
import it.polimi.ingsw.Model.Items.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the methods for an X Shape common goal.
 * The X Shape is a 3x3 layout with the following cells filled:
 * - bottom left
 * - bottom right
 * - central
 * - upper left
 * - upper right
 * The number of different colors in the layout must be between the minimum and the maximum.
 * The width and height of the layout are set to 3 because the number of rows and columns is taken directly from the examined bookshelf.
 *
 * @author Simone
 */
public class XShape extends Layout {
    /**
     * Creates a new X Shape layout with the given parameters.
     * The width and height of the layout are set to 3 because the number of rows and columns is taken directly from the examined bookshelf.
     *
     * @param dimension    the width and height of the layout
     * @param minDifferent the minimum number of different colors in a layout
     * @param maxDifferent the maximum number of different colors in a layout
     * @author Simone
     */
    public XShape(int dimension, int minDifferent, int maxDifferent) {
        //for now dimension parameter (width and height) are ignored
        super(3, 3, minDifferent, maxDifferent);
    }


    /**
     * This method checks
     *
     * @param b the bookshelf to be checked
     * @return true if the bookshelf has no free cells in the first and last column and if the number of distinct colors in the four corners is between the minimum and the maximum, false otherwise
     * @throws IllegalArgumentException if the bookshelf is null
     * @author Simone
     */
    public boolean check(Bookshelf b) throws IllegalArgumentException {
        if (b == null) throw new IllegalArgumentException("The bookshelf cannot be null");

        boolean check_iteration;
        List<Color> colorList = new ArrayList<>();
        int col;
        int row;

        for (row = 0; row < b.getRows() - getHeight() + 1; row++) {
            for (col = 0; col < b.getColumns() - getWidth() + 1; col++) {
                if (b.getItemAt(row, col).isPresent()) {
                    //The first check to make sure there are the minimum number of item to begin the check
                    //in particular, if the check starts on (0,0) then the following cells must be filled: (2, 0), (2, 1), (2, 2)
                    //so in general, given the parameters row and column, the check can proceed if and only if, cell from (row+getHeight()-1, col) to (row+getHeight-1, col+getWidth()-1) are filled
                    check_iteration = true;
                    for (int index = col; index < col + getWidth() - 1; index++) {
                        check_iteration = check_iteration && b.getItemAt(row + getHeight() - 1, index).isPresent();
                    }

                    if (check_iteration) {

                        //bottom left
                        colorList.clear();
                        colorList.add(b.getItemAt(row, col).get().color());

                        //bottom right
                        Color color = b.getItemAt(row, col + 2).get().color();
                        if (!colorList.contains(color)) {
                            colorList.add(color);
                        }

                        //central
                        color = b.getItemAt(row + 1, col + 1).get().color();
                        if (!colorList.contains(color)) {
                            colorList.add(color);
                        }

                        //upper left
                        color = b.getItemAt(row + 2, col).get().color();
                        if (!colorList.contains(color)) {
                            colorList.add(color);
                        }

                        //upper right
                        color = b.getItemAt(row + 2, col).get().color();
                        if (!colorList.contains(color)) {
                            colorList.add(color);
                        }


                        //the final check is all about the number of different colors found
                        if (colorList.size() >= getMinDifferent() && colorList.size() <= getMaxDifferent()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public String getInfo() {
        return super.getInfo() + "X Shape";
    }
}
