package it.polimi.ingsw;

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
}
