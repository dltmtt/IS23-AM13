package it.polimi.ingsw.utils;

import java.util.Random;

import static java.lang.Integer.parseInt;

/**
 * The color of an item. It identifies the different types of items.
 * Each color has a different hex color code.
 */
public enum Color {
    GREEN("#8e9d47", "92"),
    WHITE("#ebe1bf", "97"),
    YELLOW("#e1a546", "93"),
    BLUE("#00648d", "94"),
    LIGHTBLUE("#62b2b1", "96"),
    PINK("#c64b79", "95");

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

    public static String toANSItext(Color color, boolean isBackground) throws IllegalArgumentException {
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }

        int ansiCode = parseInt(color.getAnsiColorCode());
        int backgroundOffset = isBackground ? 10 : 0;
        ansiCode += backgroundOffset;

        return ESC_STR + "[" + ansiCode + "m";
    }

    public String getHexColorCode() {
        return hexCode;
    }

    public String getAnsiColorCode() {
        return ansiColorCode;
    }

    // TODO not working at the moment, come back later :)
    public String fromHEXtoANSI(String hexCode, boolean isBackground) throws IllegalArgumentException {
        if (hexCode == null) {
            throw new IllegalArgumentException("Hex code cannot be null");
        }
        if (hexCode.length() != 7) {
            throw new IllegalArgumentException("Hex code must be 7 characters long");
        }
        if (hexCode.charAt(0) != '#') {
            throw new IllegalArgumentException("Hex code must start with #");
        }

        var r = parseInt(hexCode.substring(1, 3), 16);
        var g = parseInt(hexCode.substring(3, 5), 16);
        var b = parseInt(hexCode.substring(5, 7), 16);

        return ESC_STR + "[" + (isBackground ? "48" : "38") + ";2;" + r + ";" + g + ";" + b + "m";
    }
}
