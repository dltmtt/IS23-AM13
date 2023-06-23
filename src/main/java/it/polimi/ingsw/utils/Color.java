package it.polimi.ingsw.utils;

import java.util.Random;

import static java.lang.Integer.parseInt;

/**
 * The color of an item. It identifies the different types of items.
 * Each color has a different hex color code.
 */
public enum Color {
    GREEN("#8e9d47", "92"), WHITE("#ebe1bf", "97"), YELLOW("#e1a546", "93"), BLUE("#00648d", "94"), LIGHTBLUE("#62b2b1", "96"), PINK("#c64b79", "95");

    public static final String RESET_COLOR = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    private static final String ESC_STR = "\u001B";
    private final String hexCode;
    private final String ansiColorCode;

    Color(String hexColorCode, String ansiColorCode) {
        this.hexCode = hexColorCode;
        this.ansiColorCode = ansiColorCode;
    }

    /**
     *
     * @return a random color
     */
    public static Color getRandomColor() {
        Random random = new Random();
        Color[] colors = Color.values();
        return colors[random.nextInt(colors.length)];
    }
    /**
     *
     * @param color the color to convert
     * @param isBackground if true, the color is used as background, otherwise as foreground
     * @return the ANSI code of the color
     * @throws IllegalArgumentException if the color is null
     */
    public static String toANSItext(Color color, boolean isBackground) throws IllegalArgumentException {
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }

        int ansiCode = parseInt(color.getAnsiColorCode());
        int backgroundOffset = isBackground ? 10 : 0;
        ansiCode += backgroundOffset;

        return ESC_STR + "[" + ansiCode + "m";
    }
    /**
     *
     * @return the ANSI color code of the color
     */
    public String getAnsiColorCode() {
        return ansiColorCode;
    }
}
