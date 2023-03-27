package it.polimi.ingsw;

public class Diagonal extends Layout {
    /**
     * this attribute specifies whether is a double diagonal (true) (X Shape) or a simple diagonal (false)
     */
    public Diagonal(int dimension, int minDifferent, int maxDifferent) {
        super(dimension, dimension, minDifferent, maxDifferent);
    }


    public boolean check(Bookshelf b) {
        if (checkRight(b, 0, 0) || checkRight(b, 1, 0)) {
            return true;
        }
        return checkLeft(b, 0, b.getColumns()-1) || checkLeft(b, 1, b.getColumns()-1); //|| checkRight(b, 0, 0);
    }

    public boolean checkRight(Bookshelf b, int row, int column) {
        int counter = 1;

        while (row < b.getRows() - 1 && column < b.getColumns() - 1) {
            if (b.getItemAt(row, column).isPresent() && b.getItemAt(row + 1, column + 1).isPresent()) {
                if (!b.getItemAt(row, column).get().getColor().equals(b.getItemAt(row + 1, column + 1).get().getColor())) {
                    break;
                } else {
                    counter++;
                    row++;
                    column++;
                }
            } else {
                break;
            }
        }

        return counter == 5;
    }


    public boolean checkLeft(Bookshelf b, int row, int column) {
        int counter = 1;
        while (row < b.getRows() - 1 && column > 0) {
            if (b.getItemAt(row, column).isPresent() && b.getItemAt(row + 1, column - 1).isPresent()) {
                if (!b.getItemAt(row, column).get().getColor().equals(b.getItemAt(row + 1, column - 1).get().getColor())) {
                    break;
                } else {
                    counter++;
                    row++;
                    column--;
                }
            } else {
                break;
            }
        }

        return counter == 5;

    }

    public String getInfo() {
        return super.getInfo() + "-type=diagonal";
    }
}
