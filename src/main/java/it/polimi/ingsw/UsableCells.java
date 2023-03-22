package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class UsableCells {
    private final List<IntegerPair> usableCells;

    public UsableCells(int numOfPlayer) {
        /**
         * these cells have to be filled either ways
         */
        usableCells = new ArrayList<>();

        for (int i = 1; i <= 7; i++) {
            if (i < 7) {
                for (int j = 4; j <= 5; j++) {
                    usableCells.add(new IntegerPair(i, j));
                }
            }
            if (i > 1) {
                usableCells.add(new IntegerPair(i, 3));
            }
            if (i >= 3 && i <= 5) {
                usableCells.add(new IntegerPair(i, 2));
                usableCells.add(new IntegerPair(i, 6));
            }
            if (i >= 3 && i <= 4) {
                usableCells.add(new IntegerPair(i, 1));
            }
            if (i >= 4 && i <= 5) {
                usableCells.add(new IntegerPair(i, 7));
            }
        }
        usableCells.add(new IntegerPair(7, 4));

        /**
         * these cells have to be filled only with at least 3 players
         */
        if (numOfPlayer >= 3) {
            usableCells.add(new IntegerPair(0, 5));
            usableCells.add(new IntegerPair(2, 2));
            usableCells.add(new IntegerPair(3, 0));
            usableCells.add(new IntegerPair(2, 6));
            usableCells.add(new IntegerPair(8, 3));
            usableCells.add(new IntegerPair(6, 2));
            usableCells.add(new IntegerPair(6, 6));
            usableCells.add(new IntegerPair(8, 5));
        }
        /**
         * these cells have to be filled only with 4 players
         */
        if(numOfPlayer==4){
            usableCells.add(new IntegerPair(0,4));
            usableCells.add(new IntegerPair(1,5));
            usableCells.add(new IntegerPair(3,1));
            usableCells.add(new IntegerPair(4,0));
            usableCells.add(new IntegerPair(4,8));
            usableCells.add(new IntegerPair(5,7));
            usableCells.add(new IntegerPair(7,3));
            usableCells.add(new IntegerPair(8,4));
        }


    }

    public List<IntegerPair> getList(){
        return usableCells;
    }
}
