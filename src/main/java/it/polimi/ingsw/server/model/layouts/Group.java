package it.polimi.ingsw.server.model.layouts;

import it.polimi.ingsw.server.model.Bookshelf;

/**
 *This class includes two different common goals:
 * 1. the common goal with six groups of at least two equal items
 * 2. the common goal with four groups of at least four equal items
 */
public class Group extends Layout {

    /**
     * The number of occurrences of the layout.
     */
    private final int occurrences;

    /**
     * The size of the group.
     */
    private final int size;

    /**
     * This method creates a group layout.
     *
     * @param minDifferent the minimum number of different colors in the layout.
     * @param maxDifferent the maximum number of different colors in the layout.
     * @param occurrences  the number of occurrences of the layout.
     * @param size         the size of the group.
     * @throws IllegalArgumentException if the parameters are invalid.
     */
    public Group(int minDifferent, int maxDifferent, int occurrences, int size) throws IllegalArgumentException {
        super(0, 0, minDifferent, maxDifferent);
        if (occurrences < 0 || size < 0) {
            String ErrorMessage = "Invalid Parameters: ";
            if (occurrences < 0) {
                ErrorMessage += "occurrences set to a negative value (" + occurrences + ") ";
            }
            if (size < 0) {
                ErrorMessage += "size set to a negative value (" + size + ") ";
            }
            throw new IllegalArgumentException(ErrorMessage);
        }
        this.occurrences = occurrences;
        this.size = size;
    }

    /**
     * This method gets the name of the layout.
     *
     * @return the name of the layout.
     */
    @Override
    public String getName() {
        return "group";
    }

    /**
     * This method checks if the layout is valid for the bookshelf.
     *
     * @param b the bookshelf to check.
     * @return true if the layout is valid for the bookshelf, false otherwise.
     */
    @Override
    public boolean check(Bookshelf b) throws IllegalArgumentException {
        int counter = 0;
        b.clearBooleanMatrix();
        for (int row = 0; row < Bookshelf.getRows(); row++) {
            for (int col = 0; col < Bookshelf.getColumns(); col++) {
                if (b.getItemAt(row, col).isPresent()) {
                    if (b.adjacentGroups(b.getItemAt(row, col).get().color(), row, col) >= size) {
                        counter++;
                    }
                }
            }
        }
        return counter >= occurrences;
    }

    /**
     * This method gets the number of occurrences of the layout.
     *
     * @return the number of occurrences of the layout.
     */
    public int getOccurrences() {
        return occurrences;
    }

    /**
     * This method gets the size of the group.
     *
     * @return the size of the group
     */
    public int getSize() {
        return size;
    }

    /**
     * This method gets the info of the layout.
     *
     * @return the info of the layout.
     */
    @Override
    public String getInfo() {
        return super.getInfo() + " -type=Group -occurrences=" + occurrences + " -size=" + size;
    }
}
