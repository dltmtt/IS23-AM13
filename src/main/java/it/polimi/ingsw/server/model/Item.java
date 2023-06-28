package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utils.Color;

/**
 * An item made of a color and a number identifying its drawing.
 *
 * @param color  the color of the item
 * @param number the number of the item, identifying its drawing
 */
public record Item(Color color, int number) {

    /**
     * @return the name of the image file containing the image of the item
     */
    public String fileName() {
        return color().toString().toLowerCase().charAt(0) + String.valueOf(number()) + ".png";
    }
}
