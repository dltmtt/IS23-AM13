package it.polimi.ingsw.server.model.layouts;

import it.polimi.ingsw.server.model.Bookshelf;

public class Group extends Layout {
    private final int occurrences;
    private final int size;

    /**
     * Creates a group layout.
     *
     * @param minDifferent the minimum number of different colors in the layout
     * @param maxDifferent the maximum number of different colors in the layout
     * @param occurrences  the number of occurrences of the layout
     * @param size         the size of the group
     * @throws IllegalArgumentException if the parameters are invalid
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
     * Checks if the layout is valid for the bookshelf.
     *
     * @param b the bookshelf to check
     * @return true if the layout is valid for the bookshelf, false otherwise
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


    public int getOccurrences() {
        return occurrences;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " -type=Group -occurrences=" + occurrences + " -size=" + size;
    }
}
