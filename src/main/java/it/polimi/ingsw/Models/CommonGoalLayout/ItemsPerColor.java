package it.polimi.ingsw.Models.CommonGoalLayout;

import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Item.Color;

import java.util.Arrays;

public class ItemsPerColor extends Layout {
    public ItemsPerColor(int minDifferent, int maxDifferent) throws IllegalArgumentException {
        super(1, 1, minDifferent, maxDifferent);
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " -type=ItemsPerColor";
    }

    /**
     * Checks if there are at least the minimum number of items of any color in the bookshelf.
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
        for (int i = 0; i < b.getRows(); i++) {
            for (int j = 0; j < b.getColumns(); j++) {
                if (b.getItemAt(i, j).isPresent()) {
                    numberOfItems[b.getItemAt(i, j).get().color().ordinal()]++;
                }
            }
        }
        return Arrays.stream(numberOfItems).anyMatch(x -> (x >= getMinDifferent() && x <= getMaxDifferent()));
    }
}
