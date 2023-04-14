package it.polimi.ingsw.utils;

import java.util.Random;

/**
 * The color of an item. It identifies the different types of items.
 */
public enum Color {
    GREEN,
    WHITE,
    YELLOW,
    BLUE,
    LIGHTBLUE,
    PINK;

    /**
     * Picks a random color from the available ones.
     *
     * @return a random color
     */
    public static Color getRandomColor() {
        Random rnd = new Random();
        Color[] colors = values();
        return colors[rnd.nextInt(colors.length)];
    }
}
