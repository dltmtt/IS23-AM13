package it.polimi.ingsw;

public class Diagonal extends Layout {
    /**
     * this attribute specifies whether is a double diagonal (true) (X Shape) or a simple diagonal (false)
     */
    private boolean doublediagonal;

    public Diagonal(int dimension, int minDifferent, int maxDifferent, boolean doublediagonal) {
        super(dimension, dimension, minDifferent, maxDifferent);
        setDoublediagonal(doublediagonal);
    }


    public boolean check(Bookshelf b) {

        if (checkRight(b, 0, 0) || checkRight(b, 1, 0)) {
            return true;
        }
        return checkLeft(b, 0, 4) || checkLeft(b, 1, 4) || checkRight(b, 0, 0);
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
    //    public boolean check(Bookshelf b) {
//        return switch (getDirection()) {
//            case 'l' -> check_left(b);
//            case 'r' -> check_right(b);
//            default -> check_right(b) || check_left(b);
//        };
//    }
//
//    /**
//     * function to check if a bookshelf contains any diagonal leaning towards left
//     * -----------
//     * | | | | | |
//     * -----------
//     * | | | | | |
//     * -----------
//     * | | |X| | |
//     * -----------
//     * | | |-|X| |
//     * -----------
//     * | | |-|-|X|
//     * -----------
//     * @param b is the bookshelf to check
//     * @return whether the bookshelf contains a diagonal leaning towards left
//     */
//
//
//    private boolean check_left(Bookshelf b){
//        //internal list for checking the number of colors in the diagonal
//        List<Color> colorlist=new ArrayList<>();
//
//        //counter for distinct colors found on the diagonal
//        int counter=0;
//
//        //variable used to save the color value of a cell
//        Color itemcolor;
//
//        //if width and height aren't equal, then the most restrictive dimension is chosen
//        final int dimension=Math.min(getHeight(), getWidth());
//
//
//        //check algorithm boudaries
//
//        //start checking from row 0 to row (#Rows-dimension)
//        int startingrow=0;
//        int endingrow=b.getRows()-getHeight();
//
//        //start checking from the column  to column (#Column-dimension)
//        int startingcolumn=b.getColumns()-1;
//        int endingcolumn=b.getColumns()-getWidth()-1;
//
//
//
//        //check algorithm boundaries
//        for(int col=startingcolumn; col>endingcolumn; col--){
//            for(int row=startingrow; row<endingrow; row++){
//
//                //clears the list of different colors
//                colorlist.clear();
//
//                //diagonal index counter
//                counter=0;
//
//                //the check procedure can begin only of the leftmost cell is filled
//                if(b.getItemAt(row, col).isPresent()){
//
//                    //keeps checking the selected diagonal only the cell is filled      AND     the counter hasn't reached the minimum dimension
//                    while(b.getItemAt(row+counter, col-counter).isPresent() && counter<dimension){
//                        //saves the item's color
//                        itemcolor=b.getItemAt(row+counter, col-counter).get().getColor();
//
//                        //the color is added only if isn't already in the list
//                        if(!colorlist.contains(itemcolor)){
//                            colorlist.add(itemcolor);
//                        }
//
//                        //adds the symmetric tile, if not present breaks from the check of the current diagonal
//                        if(doublediagonal){
//                            if(b.getItemAt(row+counter, startingcolumn+counter).isPresent()){
//                                //saves the item's color
//                                itemcolor=b.getItemAt(row+counter, startingcolumn+counter).get().getColor();
//
//                                //the color is added only if isn't already in the list
//                                if(!colorlist.contains(itemcolor)){
//                                    colorlist.add(itemcolor);
//                                }
//                            }else{
//                                break;
//                            }
//                        }
//
//                        counter++;
//
//                        //if the upper limit for distinct colors is already not valid then break from checking the current diagonal
//                        if(colorlist.size()>getMaxDifferent()){
//                            break;
//                        }
//                    }
//
//                    //if the analyzed diagonal fits the rules, then the goal is found
//                    if(counter==dimension && colorlist.size()>=getMinDifferent() && colorlist.size()<=getMaxDifferent()){
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }
//
//    private boolean check_right(Bookshelf b){
//        //internal list for checking the number of colors in the diagonal
//        List<Color> colorlist=new ArrayList<>();
//
//        //counter for distinct colors found on the diagonal
//        int counter=0;
//
//        //variable used to save the color value of a cell
//        Color itemcolor;
//
//        //if width and height aren't equal, then the most restrictive dimension is chosen
//        final int dimension=Math.min(getHeight(), getWidth());
//
//
//        //check algorithm boudaries
//
//        //start checking from row 0 to row (#Rows-dimension)
//        int startingrow=0;
//        int endingrow=b.getRows()-getHeight();
//
//        //start checking from column 0 to column (#Column-dimension)
//        int startingcolumn=0;
//        int endingcolumn=b.getColumns()-getWidth();
//
//
//
//        //check algorithm boundaries
//        for(int col=startingcolumn; col<endingcolumn; col++){
//            for(int row=startingrow; row<endingrow; row++){
//
//                //clears the list of different colors
//                colorlist.clear();
//
//                //diagonal index counter
//                counter=0;
//
//                //the check procedure can begin only of the leftmost cell is filled
//                if(b.getItemAt(row, col).isPresent()){
//                    //keeps checking the selected diagonal only the cell is filled      AND     the counter hasn't reached the minimum dimension
//                    while(b.getItemAt(row+counter, col+counter).isPresent() && counter<dimension){
//                        //saves the item's color
//                        itemcolor=b.getItemAt(row+counter, col+counter).get().getColor();
//
//                        //the color is added only if isn't already in the list
//                        if(!colorlist.contains(itemcolor)){
//                            colorlist.add(itemcolor);
//                        }
//
//                        //adds the symmetric tile, if not present breaks from the check of the current diagonal
//                        if(doublediagonal){
//                            if(b.getItemAt(row+counter, endingcolumn-counter).isPresent()){
//                                //saves the item's color
//                                itemcolor=b.getItemAt(row+counter, endingcolumn-counter).get().getColor();
//
//                                //the color is added only if isn't already in the list
//                                if(!colorlist.contains(itemcolor)){
//                                    colorlist.add(itemcolor);
//                                }
//                            }else{
//                                break;
//                            }
//                        }
//
//                        counter++;
//
//                        //if the upper limit for distinct colors is already not valid then break from checking the current diagonal
//                        if(colorlist.size()>getMaxDifferent()){
//                            break;
//                        }
//                    }
//
//                    //if the analyzed diagonal fits the rules, then the goal is found
//                    if(counter==dimension && colorlist.size()>=getMinDifferent() && colorlist.size()<=getMaxDifferent()){
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }

    public String getInfo() {
        return super.getInfo() + "-type=diagonal -doublediagonal=" + doublediagonal;
    }

    public void setDoublediagonal(boolean doublediagonal) {
        this.doublediagonal = doublediagonal;
    }

    public boolean isDoublediagonal() {
        return doublediagonal;
    }

}
