package it.polimi.ingsw.Model.Items;

/**
 * @author Beatrice
 * This class is for the Item, which is the single card put on the board
 */
public record Item(Color color, int number) {
    public boolean equals(Item check) {
        return color() == check.color() && number() == check.number();
    }
}
