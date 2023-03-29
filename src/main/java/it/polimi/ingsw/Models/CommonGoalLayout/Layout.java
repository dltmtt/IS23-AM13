package it.polimi.ingsw.Models.CommonGoalLayout;


import it.polimi.ingsw.Models.Games.Bookshelf;
import it.polimi.ingsw.Models.Item.Item;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Simone
 * This class implements the methods for a common goal.
 */
public abstract class Layout {
    /**
     * The width of the layout
     */
    private final int width;
    /**
     * The height of the layout
     */
    private final int height;
    /**
     * the minimum number of different colors in a layout
     */
    private final int minDifferent;
    /**
     * the maximum number of different colors in a layout
     */
    private final int maxDifferent;


    /**
     * This constructor creates a layout
     *
     * @param width        the width of the layout
     * @param height       the height of the layout
     * @param minDifferent the minimum number of different colors in the layout
     * @param maxDifferent the maximum number of different colors in the layout
     * @throws IllegalArgumentException if the parameters are invalid
     * @author Simone
     */
    public Layout(int width, int height, int minDifferent, int maxDifferent) throws IllegalArgumentException {
        if (width < 0 || height < 0 || minDifferent > maxDifferent || minDifferent < 0) {
            String ErrorMessage = "Invalid Parameters: ";
            if (width < 0) {
                ErrorMessage += "width set to an invalid value (" + width + "), 0 only used for FullLine layouts";
            }

            if (height < 0) {
                ErrorMessage += "height set to an invalid value (" + height + "), 0 only used for FullLine layouts";
            }

            if (minDifferent < 0) {
                ErrorMessage += "minDifferent set to a negative value (" + minDifferent + ") ";
            }

            if (maxDifferent < 0) {
                ErrorMessage += "maxDifferent set to a negative value (" + minDifferent + ") ";
            }

            if (minDifferent > maxDifferent) {
                ErrorMessage += "minDifferent is greater than maxDifferent(" + minDifferent + ">" + maxDifferent + ") ";
            }
            throw new IllegalArgumentException(ErrorMessage);
        }
        this.width = width;
        this.height = height;
        this.minDifferent = minDifferent;
        this.maxDifferent = maxDifferent;
    }

    /**
     * This method checks if the layout is valid for the bookshelf
     *
     * @param b the bookshelf to check
     * @return true if the layout is valid for the bookshelf, false otherwise
     * @author Simone
     */
    public abstract boolean check(Bookshelf b) throws IllegalArgumentException;


    /**
     * This method returns a string with the information about the layout
     *
     * @return info about the layout
     * @author Simone
     */
    public String getInfo() {
        return "-width=" + width + " -height=" + height + "-minDifferent=" + minDifferent + "-maxDifferent=" + maxDifferent;
    }

    /**
     * This method returns the width of the layout
     *
     * @return the width of the layout
     * @author Simone
     */
    public int getWidth() {
        return width;
    }

    /**
     * This method returns the height of the layout
     *
     * @return the height of the layout
     * @author Simone
     */
    public int getHeight() {
        return height;
    }

    /**
     * This method returns the minimum number of different colors in a layout
     *
     * @return the minimum number of different colors in a layout
     * @author Simone
     */
    public int getMinDifferent() {
        return minDifferent;
    }

    /**
     * This method returns the maximum number of different colors in a layout
     *
     * @return the maximum number of different colors in a layout
     * @author Simone
     */

    public int getMaxDifferent() {
        return maxDifferent;
    }


    public int getDifferentColors(List<Item> itemList) {
        return itemList.stream().map(it -> it.color()).distinct().collect(Collectors.toList()).size();
    }
}