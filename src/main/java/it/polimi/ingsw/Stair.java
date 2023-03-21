package it.polimi.ingsw;

/** This class implements the methods of Layout for a diagonal/stair common goal
 *
 */
public class Stair extends Layout {
    /** this attribute specifies whether is a diagonal (true), or a stair (diagonal+lower half)
     */
    private boolean diagonal;

    /** this attribute specifies whether is a double diagonal (true) (X Shape) or a simple diagonal (false)
     */
    private boolean doublediagonal;

    /**Constructor
     *
     * @param width width of the diagonal
     * @param height height of the diagonal
     * @param minDifferent minimum number of different types
     * @param maxDifferent maximum number of different types
     * @param occurrences number of occurrences needed
     * @param direction specifies the direction of search (l: left, r: right, b: both)
     * @param diagonal whether is a diagonal (true), or a stair (diagonal+lower half)
     * @param doublediagonal this attribute specifies whether is a double diagonal (true) (X Shape) or a simple diagonal (false)
     */
    public Stair(int width, int height, int minDifferent, int maxDifferent, int occurrences, char direction, boolean diagonal, boolean doublediagonal) {
        super(width, height, minDifferent, maxDifferent, occurrences, direction);
        setDiagonal(diagonal);
        setDoublediagonal(doublediagonal);
    }


    public boolean check(Bookshelf b) {
        if(getCurrent(b)==getOccurrences()){
            return true;
        }else{
            return false;
        }
    }

    public int getCurrent(Bookshelf b){
        int counter=0;
            // Add implementation
        return 1;
    }

    public String getInfo(){
        return super.getInfo()+"-type=stair -diagonal="+diagonal+" -doublediagonal="+doublediagonal;
    }

    public void setDiagonal(boolean diagonal) {
        this.diagonal = diagonal;
    }

    public void setDoublediagonal(boolean doublediagonal) {
        this.doublediagonal = doublediagonal;
    }

    public boolean isDiagonal() {
        return diagonal;
    }

    public boolean isDoublediagonal() {
        return doublediagonal;
    }
}

