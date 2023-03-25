package it.polimi.ingsw;

public class ClasseTemporanea {


    //this function returns the score depending on the number of adjacent items
    public int adjacentGroups(Bookshelf b, Color color) {
        int matches = 0;
        Boolean[][] copyb = new Boolean[6][5];
        int score = 0;

        for (int r = 0; r < b.getRows(); r++) {
            for (int c = 0; c < b.getColumns(); c++) {
                copyb[r][c] = true;
            }
        }

        for (int c = 0; c < b.getColumns(); c++) {
            if (b.getItemAt(0, c).isPresent() && b.getItemAt(0, c).get().getColor().equals(color) && copyb[0][c]) {
                copyb[0][c] = false;
                if ((c - 1 >= 0 && !copyb[0][c - 1]) || (b.getItemAt(0, c + 1).isPresent() && b.getItemAt(0, c + 1).get().getColor().equals(color)) || ((b.getItemAt(1, c).isPresent() && b.getItemAt(1, c).get().getColor().equals(color)))) {
                    matches++;
                }
            }
        }

        System.out.println(matches);


        for (int r = 1; r < b.getRows(); r++) {
            for (int c = 0; c < b.getColumns(); c++) {
                if (b.getItemAt(r, c).isPresent() && b.getItemAt(r, c).get().getColor().equals(color) && copyb[r][c]) {
                    copyb[r][c] = false;
                    matches++;
                    System.out.println("riga-colonna:  " + r + c);
                    System.out.println(matches);

                }
            }
            System.out.println(score);
            System.out.println(score);
        }
        score = score + calculatePoints(matches);
        return score;

    }

    public int calculatePoints(int matches) {
        if (matches < 3)
            return 0;
        else if (matches == 3)
            return 2;
        else if (matches == 4)
            return 3;
        else if (matches == 5)
            return 5;
        else
            return 8;
    }

}
