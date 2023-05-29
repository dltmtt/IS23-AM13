package it.polimi.ingsw.server.model.layouts;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.utils.Color;

import java.util.Arrays;

public class ItemsPerColor extends Layout {

    public ItemsPerColor(int minDifferent, int maxDifferent) throws IllegalArgumentException {
        super(1, 1, minDifferent, maxDifferent);
    }

    /**
     *
     * @return the name of the layout
     */
    @Override
    public String getInfo() {
        return super.getInfo() + " -type=ItemsPerColor";
    }

    /**
     *
     * @return the name of the layout
     */
    @Override
    public String getName() {
        return "itemsPerColor";
    }

    /**
     * This method checks if there are at least the minimum number of items of any color in the bookshelf.
     *
     * @param b the bookshelf to check
     * @return true if the layout is valid for the bookshelf, false otherwise
     */
    @Override
    public boolean check(Bookshelf b) throws IllegalArgumentException {
        if (b == null) {
            throw new IllegalArgumentException("Invalid bookshelf");
        }
        int[] numberOfItems = new int[(int) Arrays.stream(Color.values()).count()];
        for (int i = 0; i < Bookshelf.getRows(); i++) {
            for (int j = 0; j < Bookshelf.getColumns(); j++) {
                if (b.getItemAt(i, j).isPresent()) {
                    numberOfItems[b.getItemAt(i, j).get().color().ordinal()]++;
                }
            }
        }
        return Arrays.stream(numberOfItems).anyMatch(x -> (x >= getMinDifferent() && x <= getMaxDifferent()));
    }
}
