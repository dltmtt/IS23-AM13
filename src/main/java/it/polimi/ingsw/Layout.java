package it.polimi.ingsw;

public abstract class Layout {
    private final int width;
    private final int height;
    private final int minDifferent;
    private final int maxDifferent;
    private final char direction;

    public Layout(int width, int height, int minDifferent, int maxDifferent, char direction) {
        this.width = width;
        this.height = height;
        this.minDifferent = minDifferent;
        this.maxDifferent = maxDifferent;
        this.direction = direction;
    }

    public boolean check(Bookshelf B) {
        return false;
    }

    public String getInfo() {
        return "-width="+width + " -height=" + height + "-minDifferent=" + minDifferent + "-maxDifferent=" + maxDifferent+ "-direction=" + direction;
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

    public char getDirection() {
        return direction;
    }
}