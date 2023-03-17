package it.polimi.ingsw;

public abstract class Layout {
    private final int width;
    private final int height;
    private final int minDifferent;
    private final int maxDifferent;
    private final int occurrences;
    private final boolean rotate;

    public Layout(int width, int height, int minDifferent, int maxDifferent, int occurrences, boolean rotate) {
        this.width = width;
        this.height = height;
        this.minDifferent = minDifferent;
        this.maxDifferent = maxDifferent;
        this.occurrences = occurrences;
        this.rotate = rotate;
    }

    public Layout(int width, int height, int minDifferent, int maxDifferent, int occurrences, boolean rotate, boolean diagonal, boolean doublediagonal) {
        this.width = width;
        this.height = height;
        this.minDifferent = minDifferent;
        this.maxDifferent = maxDifferent;
        this.occurrences = occurrences;
        this.rotate = rotate;
    }

    public boolean check(Bookshelf B) {
        return false;
    }

    public int getCurrent(Bookshelf B) {
        return 0;
    }

    public String getLayout() {
        return width + "-" + height + "-" + minDifferent + "-" + maxDifferent + "-" + occurrences + "-" + rotate;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMinDifferent() {
        return minDifferent;
    }

    public int getMaxDifferent() {
        return maxDifferent;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public boolean getRotate() {
        return rotate;
    }
}