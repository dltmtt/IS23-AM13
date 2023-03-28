package it.polimi.ingsw;

public abstract class Layout {
    private final int width;
    private final int height;
    private final int minDifferent;
    private final int maxDifferent;
//    private char direction;

    public Layout(int width, int height, int minDifferent, int maxDifferent) throws IllegalArgumentException{
        if (width < 0 || height < 0 || minDifferent > maxDifferent || minDifferent < 0 || maxDifferent < 0) {
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

    public boolean check(Bookshelf B) {
        throw new RuntimeException("The function for check(Bookshelf b) was called from a Layout-type, please use the specific class instead (Corners, Diagonal, FullLine, Stair, XShape)");
    }

    public String getInfo() {
        return "-width=" + width + " -height=" + height + "-minDifferent=" + minDifferent + "-maxDifferent=" + maxDifferent;
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
}