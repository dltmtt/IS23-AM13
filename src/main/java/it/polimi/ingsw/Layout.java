package it.polimi.ingsw;

public abstract class Layout {
    private int width;
    private int height;
    private int minDifferent;
    private int maxDifferent;
    //private char direction;

    public Layout(int width, int height, int minDifferent, int maxDifferent) {
        if(width<=0 || height<=0 || minDifferent>maxDifferent || minDifferent<0 || maxDifferent<0){
            String ErrorMessage="Invalid Parameters: ";
            if(width<=0){
                ErrorMessage+="width set to a negative value ("+width+") ";
            }

            if(height<=0){
                ErrorMessage+="height set to a negative value ("+height+") ";
            }

            if(minDifferent<0){
                ErrorMessage+="minDifferent set to a negative value ("+minDifferent+") ";
            }

            if(maxDifferent<0){
                ErrorMessage+="maxDifferent set to a negative value ("+maxDifferent+") ";
            }

            if(minDifferent>maxDifferent){
                ErrorMessage+="minDifferent is greater than maxDifferent("+minDifferent+">"+maxDifferent+") ";
            }
            throw new IllegalArgumentException(ErrorMessage);
        }
        this.width = width;
        this.height = height;
        this.minDifferent = minDifferent;
        this.maxDifferent = maxDifferent;
    }

    public boolean check(Bookshelf B) {
        return false;
    }

    public String getInfo() {
        return "-width="+width + " -height=" + height + "-minDifferent=" + minDifferent + "-maxDifferent=" + maxDifferent;
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