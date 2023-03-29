package it.polimi.ingsw.Models.CommonGoalLayout;

import it.polimi.ingsw.Models.Games.Bookshelf;

public class FullLine extends Layout {
    private final int occurrences;

    public FullLine(int width, int height, int minDifferent, int maxDifferent, int occurrences) {
        super(width, height, minDifferent, maxDifferent);
        this.occurrences = occurrences;
    }

    public boolean check(Bookshelf b) {
        return false;
    }

    public String getInfo() {
        return super.getInfo() + " FullLine -occurrences" + occurrences;
    }
}
