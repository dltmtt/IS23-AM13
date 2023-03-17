package it.polimi.ingsw;

public class Stairs extends Layout {
    private boolean diagonal;
    private boolean doublediagonal;

    public Stairs(int width, int height, int minDifferent, int maxDifferent, int occurrences, boolean rotate, boolean diagonal, boolean doublediagonal) {
        super(width, height, minDifferent, maxDifferent, occurrences, rotate, diagonal, doublediagonal);
    }

    public boolean check(Bookshelf B) {
        for (int i = getWidth(); i < (B.getColumns() - getWidth()); i++) {
// Add implementation
        }
    }
}
