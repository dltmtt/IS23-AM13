package it.polimi.ingsw.model.commonGoalLayout;


import it.polimi.ingsw.model.game.Bookshelf;
import it.polimi.ingsw.model.item.Item;

import java.util.List;

/**
 * The disposition of the items in a common goal whose position has to be matched in the bookshelf.
 */
public abstract class Layout {
    private final int width;
    private final int height;
    // Minimum/maximum number of different colors in a layout
    private final int minDifferent;
    private final int maxDifferent;

    /**
     * This constructor creates a layout
     *
     * @param width        the width of the layout
     * @param height       the height of the layout
     * @param minDifferent the minimum number of different colors in the layout
     * @param maxDifferent the maximum number of different colors in the layout
     * @throws IllegalArgumentException if the parameters are invalid
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
     * Checks if the layout is valid for the bookshelf.
     *
     * @param b the bookshelf to check
     * @return true if the layout is valid for the bookshelf, false otherwise
     */
    public abstract boolean check(Bookshelf b) throws IllegalArgumentException;

    /**
     * Returns a string with the information about the layout.
     *
     * @return info about the layout
     */
    public String getInfo() {
        return "-width=" + width + " -height=" + height + "-minDifferent=" + minDifferent + "-maxDifferent=" + maxDifferent;
    }

    /**
     * Returns the width of the layout.
     *
     * @return the width of the layout
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the layout.
     *
     * @return the height of the layout
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the minimum number of different colors in a layout.
     *
     * @return the minimum number of different colors in a layout
     */
    public int getMinDifferent() {
        return minDifferent;
    }

    /**
     * Returns the maximum number of different colors in a layout.
     *
     * @return the maximum number of different colors in a layout
     */
    public int getMaxDifferent() {
        return maxDifferent;
    }

    public int getDifferentColors(List<Item> itemList) throws IllegalArgumentException {
        if (itemList == null) {
            throw new IllegalArgumentException("itemList is null");
        }
        return itemList.stream().map(Item::color).distinct().toList().size();
    }
}