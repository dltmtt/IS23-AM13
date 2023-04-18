package it.polimi.ingsw.utils;

import java.util.Random;

/**
 * The color of an item. It identifies the different types of items.
 * Each color has a different hex color code.
 */
public enum Color {
    GREEN("#8e9d47"),
    WHITE("#ebe1bf"),
    YELLOW("#e1a546"),
    BLUE("#00648d"),
    LIGHTBLUE("#62b2b1"),
    PINK("#c64b79");

    private final String hexCode;

    Color(String hexColorCode) {
        this.hexCode = hexColorCode;
    }

    /**
     * @return a random color
     */
    public static Color getRandomColor() {
        Random random = new Random();
        Color[] colors = Color.values();
        return colors[random.nextInt(colors.length)];
    }

    /**
     * @return the hex color code of a random color
     */
    public static String getRandomHexColor() {
        return getRandomColor().getHexColorCode();
    }

    public String getHexColorCode() {
        return hexCode;
    }
}
