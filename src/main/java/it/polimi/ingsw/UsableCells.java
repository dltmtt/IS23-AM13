package it.polimi.ingsw;

import javax.swing.*;
import java.util.List;

public class UsableCells {
    private List<IntegerPair> usableCells;

    public UsableCells() {
        usableCells.add(new IntegerPair(1,4));
        usableCells.add(new IntegerPair(1,5));
        usableCells.add(new IntegerPair(2,3));
        usableCells.add(new IntegerPair(2,4));
        usableCells.add(new IntegerPair(2,5));
        usableCells.add(new IntegerPair(3,1));
        usableCells.add(new IntegerPair(3,2));
        usableCells.add(new IntegerPair(3,3));
        usableCells.add(new IntegerPair(3,4));
        usableCells.add(new IntegerPair(3,5));
        usableCells.add(new IntegerPair(3,6));
        usableCells.add(new IntegerPair(4,1));
        usableCells.add(new IntegerPair(4,2));
        usableCells.add(new IntegerPair(4,3));
        usableCells.add(new IntegerPair(4,4));
        usableCells.add(new IntegerPair(4,5));
        usableCells.add(new IntegerPair(4,6));
        usableCells.add(new IntegerPair(4,7));
        usableCells.add(new IntegerPair(5,2));
        usableCells.add(new IntegerPair(5,3));
        usableCells.add(new IntegerPair(5,4));
        usableCells.add(new IntegerPair(5,5));
        usableCells.add(new IntegerPair(5,6));
        usableCells.add(new IntegerPair(5,7));
        usableCells.add(new IntegerPair(6,3));
        usableCells.add(new IntegerPair(6,4));
        usableCells.add(new IntegerPair(6,5));
        usableCells.add(new IntegerPair(7,3));
        usableCells.add(new IntegerPair(7,4));


    }

    public List<IntegerPair> getList(){
        return usableCells;
    }
}
