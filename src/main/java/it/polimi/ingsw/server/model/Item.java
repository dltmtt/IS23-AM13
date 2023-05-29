package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utils.Color;

/**
 * An item made of a color and a number identifying its drawing.
 */

public record Item(Color color, int number) {

    /**
     * This method checks if the item is equal to another one.
     * @param check the item to check
     * @return true if the items are equal, false otherwise
     */
    public boolean equals(Item check) {
        return color() == check.color() && number() == check.number();
    }
}
