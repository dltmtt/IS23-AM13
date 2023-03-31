package it.polimi.ingsw.Models.CommonGoalLayout;

import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Item.Item;

import java.util.List;

/**
 * The common goal layout with a row or a column with every cell filled.
 */
public class FullLine extends Layout {
    private final int occurrences; // number of occurrences of the layout
    private final boolean horizontal; // true if the layout is a row, false if it a column

    /**
     * Creates a full line layout, either horizontal or vertical.
     * The minimum and maximum number of different types of items in the layout are given by <code>minDifferent</code> and <code>maxDifferent</code>.
     * An item type is defined by its color.
     *
     * @param minDifferent the minimum number of different types of items in this layout
     * @param maxDifferent the maximum number of different types of items in this layout
     * @param occurrences  the number of occurrences of this layout required to fulfill the common goal
     * @param horizontal   true if this layout is horizontal (a row), false if it is vertical (a column)
     */
    public FullLine(int minDifferent, int maxDifferent, int occurrences, boolean horizontal) {
        super(0, 0, minDifferent, maxDifferent);
        this.occurrences = occurrences;
        this.horizontal = horizontal;

    }

    /**
     * Checks if the layout is satisfied in the bookshelf
     *
     * @param b the bookshelf to check
     * @return true if the layout is satisfied, false otherwise
     */
    public boolean check(Bookshelf b) {
        // Check if the bookshelf is valid
        if (b == null) {
            throw new IllegalArgumentException("Bookshelf is null");
        }

        // Set search parameters, depending on the orientation of the layout
        int counter = 0;
        int i = 0;

        if (horizontal) {
            // Check every row until there are no more full rows
            while (i < b.getRows() && b.isRowFull(i)) {
                List<Item> rowContent = b.getRowContent(i);
                if (getDifferentColors(rowContent) >= getMinDifferent() && getDifferentColors(rowContent) <= getMaxDifferent()) {
                    counter++;
                }
                i++;
            }
        } else {
            // Check every column
            for (int j = 0; j < b.getColumns(); j++) {
                if (b.isColumnFull(j)) {
                    List<Item> colContent = b.getColumnContent(j);
                    int differentColors = getDifferentColors(colContent);
                    if (differentColors >= getMinDifferent() && differentColors <= getMaxDifferent()) {
                        counter++;
                    }
                }
            }
        }
        return counter >= occurrences;
    }

    public String getInfo() {
        return super.getInfo() + " -type= fullLine -occurrences" + occurrences;
    }
}
