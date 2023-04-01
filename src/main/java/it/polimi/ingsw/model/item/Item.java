package it.polimi.ingsw.model.item;

/**
 * An item made of a color and a number identifying its drawing.
 */
public record Item(Color color, int number) {
    public boolean equals(Item check) {
        return color() == check.color() && number() == check.number();
    }
}