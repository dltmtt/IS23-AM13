package it.polimi.ingsw;

/**
 * @author Beatrice
 * This class is for the Item, which is the single card put on the board
 */
public class Item {
    private final Color color;
    private final int number;


    public Item(Color color, int number) {
        this.color = color;
        this.number = number;
    }


    public int getNumber() {
        return number;
    }

    public Color getColor() {
        return color;
    }

    public boolean equals(Item check){
        return getColor()==check.getColor() && getNumber()==check.getNumber();
    }
}
