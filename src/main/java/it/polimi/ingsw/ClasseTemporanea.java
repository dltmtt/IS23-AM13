package it.polimi.ingsw;

public class ClasseTemporanea {

    //calcola le adiacenze di quel colore

    public int calculateGroups(Bookshelf b, int row, int column, Color color) {
        int matches = 0;
        if (row == 6 && column == 5) {
            if (matches < 3) {
                return 0;
            }
            if (matches == 3) {
                return 2;
            }
            if (matches == 4) {
                return 3;
            }
            if (matches == 5) {
                return 5;
            }
            if (matches >= 6) {
                return 8;
            }
        }
        //solo ilcolore ??
        if (b.getItemAt(row, column).equals(b.getItemAt(row + 1, column))) {
            return matches + calculateGroups(b, row + 1, column, color);
        }
        if (b.getItemAt(row, column).equals(b.getItemAt(row, column + 1))) {
            return matches + calculateGroups(b, row, column + 1, color);
        }
        return 0;
    }


}
