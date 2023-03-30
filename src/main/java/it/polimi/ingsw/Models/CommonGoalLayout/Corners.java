package it.polimi.ingsw.Models.CommonGoalLayout;

import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Item.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This class implements the methods for a Corners common goal.
 *
 * @author Simone
 */
public class Corners extends Layout {

    /**
     * Creates a new Corner layout with the given parameters.
     * The width and height of the layout are set to 0 because the number of rows and columns is taken directly from the examined bookshelf.
     *
     * @param minDifferent the minimum number of different colors in a layout
     * @param maxDifferent the maximum number of different colors in a layout
     * @author Simone
     */
    public Corners(int minDifferent, int maxDifferent) {
        // Set width and height to 0 because it takes the number of rows and columns directly from the examined bookshelf
        super(0, 0, minDifferent, maxDifferent);
    }

    /**
     * This method checks if the bookshelf has no free cells in the first and last column and if the number of distinct colors in the four corners is between the minimum and the maximum.
     *
     * @param b the bookshelf to be checked
     * @return true if the bookshelf has no free cells in the first and last column and if the number of distinct colors in the four corners is between the minimum and the maximum, false otherwise
     * @throws IllegalArgumentException if the bookshelf is null
     * @author Simone
     */
    @Override
    public boolean check(Bookshelf b) throws IllegalArgumentException {
        // Check if the bookshelf is null
        if (b == null) throw new IllegalArgumentException("The bookshelf cannot be null");


        // Local list of distinct colors
        List<Color> colorList = new ArrayList<>();


        // Local color variable
        Color color;


        // Check if the bookshelf has no free cells in the first and last column
        if (b.getFreeCellsInColumn(0) != 0 || b.getFreeCellsInColumn(b.getColumns() - 1) != 0) {
            return false;
        } else {
            // Add the colors of the four corners to the list

            // Bottom left
            color = b.getItemAt(0, 0).get().color();
            colorList.add(color);

            // Top left
            color = b.getItemAt(b.getRows() - 1, 0).get().color();
            colorList.add(color);

            // Top right
            color = b.getItemAt(b.getRows() - 1, b.getColumns() - 1).get().color();
            colorList.add(color);

            color = b.getItemAt(0, b.getColumns() - 1).get().color();
            colorList.add(color);

            // Remove duplicates
            colorList = colorList.stream().distinct().collect(Collectors.toList());

            // Check if the number of distinct colors is between the minimum and the maximum
            boolean result = colorList.size() >= getMinDifferent() && colorList.size() <= getMaxDifferent();

            return result;
        }
    }

    @Override
    public String getInfo() {
        return super.getInfo();
    }
}
