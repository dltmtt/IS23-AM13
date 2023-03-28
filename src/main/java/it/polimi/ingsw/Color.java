package it.polimi.ingsw;

import java.util.Random;

/**
 * @author Beatrice
 * This enum is for the colors of the different cards
 */
public enum Color {
    GREEN,
    WHITE,
    YELLOW,
    BLUE,
    LIGHTBLUE,
    PINK;

    private static final Random rnd = new Random();

    /**
     * function to obtain a random color
     *
     * @return a random color
     */
    public static Color randomColor() {
        Color[] colors = values();
        return colors[rnd.nextInt(colors.length)];
    }
}
