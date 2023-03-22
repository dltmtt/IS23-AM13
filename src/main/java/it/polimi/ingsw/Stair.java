package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;
//import java.util.Optional;

/** This class implements the methods of Layout for a stair common goal
 *
 */
public class Stair extends Layout {

    /**Constructor
     *
     * @param dimension width and height of the diagonal
     * @param minDifferent minimum number of different types
     * @param maxDifferent maximum number of different types
     * @param direction specifies the direction of search (l: left, r: right, b: both)
    */
    public Stair(int dimension, int minDifferent, int maxDifferent, char direction) {
        super(dimension, dimension, minDifferent, maxDifferent, direction);
    }

    private boolean check_left(Bookshelf b){
        return false;
    }

    private boolean check_right(Bookshelf b){
        return false;
    }

    public boolean check(Bookshelf b) {
        return switch (getDirection()) {
            case 'l' -> check_left(b);
            case 'r' -> check_right(b);
            default -> check_right(b) || check_left(b);
        };
    }

    public String getInfo(){
        return super.getInfo();
    }
}

