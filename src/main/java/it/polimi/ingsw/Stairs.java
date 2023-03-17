package it.polimi.ingsw;

/** This class implements the methods of Layout for a diagonal/stair common goal
 *
 */
public class Stairs extends Layout {
    /** this attribute specifies whether is a diagonal (true), or a stair (diagonal+lower half)
     */
    private boolean diagonal;

    /** this attribute specifies whether is a double diagonal (true) (X Shape) or a simple diagonal (false)
     */
    private boolean doublediagonal;

    /**Constructor
     *
     * @param width width of the
     * @param height
     * @param minDifferent
     * @param maxDifferent
     * @param occurrences
     * @param rotate
     * @param diagonal
     * @param doublediagonal
     */
    public Stairs(int width, int height, int minDifferent, int maxDifferent, int occurrences, boolean rotate, boolean diagonal, boolean doublediagonal) {
        super(width, height, minDifferent, maxDifferent, occurrences, rotate, diagonal, doublediagonal);
    }


    public boolean check(Bookshelf B) {
        int counter=0;

        for (int i = getWidth()-1; i < (B.getColumns() - getWidth()); i++) {
// Add implementation
        }
        return false;
    }
}
