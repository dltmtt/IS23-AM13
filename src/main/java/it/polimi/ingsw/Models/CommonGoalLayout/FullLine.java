package it.polimi.ingsw.Models.CommonGoalLayout;

import it.polimi.ingsw.Models.Games.Bookshelf;
import it.polimi.ingsw.Models.Item.Item;

import java.util.List;

/**
 * This class represents the layout of the common goal card "Full Line"
 * A full line is a row or a column with every cell filled
 *
 * @author Simone
 */
public class FullLine extends Layout {
    /**
     * The number of occurrences of the layout
     */
    private final int occurrences;
    /**
     * True if the layout is horizontal (a row), false if it is vertical (a column)
     */
    private final boolean horizontal;

    /**
     * Constructor of the class
     *
     * @param width        the width of the bookshelf
     * @param height       the height of the bookshelf
     * @param minDifferent the minimum number of different colors
     * @param maxDifferent the maximum number of different colors
     * @param occurrences  the number of occurrences of the layout
     * @param horizontal   true if the layout is horizontal (a row), false if it is vertical (a column)
     */
    public FullLine(int width, int height, int minDifferent, int maxDifferent, int occurrences, boolean horizontal) {
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

            //check every column
            for (int j = 0; j < b.getColumns(); j++) {

                if (b.isColumnFull(j)) {
                    List<Item> colContent = b.getColumnContent(j);
                    int differentcolors = getDifferentColors(colContent);
                    if (differentcolors >= getMinDifferent() && differentcolors <= getMaxDifferent()) {
                        counter++;
                    }
                }
            }
        }
        return counter >= occurrences;
    }

    public String getInfo() {
        return super.getInfo() + " FullLine -occurrences" + occurrences;
    }
}
