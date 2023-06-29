package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Bookshelf;

/**
 * This class is used to print the common goal in the cli
 */
public class CommonGoalView {
    private static final String emptyCell = "⬜️";
    private static final String filledCell = "⬛";
    private static final String diffCell = "\uD83D\uDFE5";
    private static final String upperLeftBox = "╭";
    private static final String upperRightBox = "╮";
    private static final String lowerLeftBox = "╰";
    private static final String lowerRightBox = "╯";


    /**
     * Used to print the given common goal, featuring a switch-case statement in which the specific method is called based on the common goal's type
     *
     * @param type        the common goal's type
     * @param occurrences the number of occurrences of the common goal
     * @param size        the size of the common goal's layout
     * @param horizontal  true if the common goal is horizontal, false if it's vertical
     */
    public static void print(String type, int occurrences, int size, boolean horizontal) {
        System.out.println("This is your common goal card: ");
        switch (type) {
            case "corners" -> cornersPrintLayout();
            case "diagonal" -> diagonalPrintLayout();
            case "fullLine" -> fullLinePrintLayout(occurrences, horizontal);
            case "group" -> groupPrintLayout(occurrences, size);
            case "xShape" -> xShapePrintLayout();
            case "itemsPerColor" -> itemsPerColorPrintLayout();
            case "stair" -> stairPrintLayout();
            case "square" -> squarePrintLayout();
            default -> System.out.println("Error in CommonGoalView");
        }
    }

    /**
     * Used to print in CLI the corner common goal
     */

    public static void cornersPrintLayout() {
        String description = "Four tiles of the same type in the four corners of the bookshelf.";
        StringBuilder card = new StringBuilder();
        // Draw an m×n card with fullCells in the corners and emptyCells everywhere else
        int m = 5;
        int n = 6;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n - 1; j++) {
                if ((i == 0 && j == 0) || (i == 0 && j == n - 2) || (i == m - 1 && j == 0) || (i == m - 1 && j == n - 2)) {
                    card.append(filledCell);
                } else {
                    card.append("  ");
                    if (i == 1 && j == n - 2) {
                        card.append("\t").append(description);
                    }
                }
            }
            card.append("\n");
        }
        System.out.println(card);
    }

    /**
     * Used to print the diagonal common goal
     */
    public static void diagonalPrintLayout() {
        String description = "  Five tiles of the same type forming a diagonal. ";
        StringBuilder cell = new StringBuilder();

        for (int i = 0; i < Bookshelf.getColumns(); i++) {
            for (int j = 0; j < Bookshelf.getColumns(); j++) {
                if (i == j)
                    cell.append(filledCell);
                else
                    cell.append("  ");
                if (i == 1 && j == Bookshelf.getColumns() - 1)
                    cell.append(description);
            }
            cell.append("\n");
        }
        System.out.println(cell);
    }

    /**
     * Used to print the full line common goal
     *
     * @param occurrences the number of occurrences of the common goal
     * @param horizontal  whether the common goal is horizontal or not
     */
    public static void fullLinePrintLayout(int occurrences, boolean horizontal) {
        String description = fullLineDescription(occurrences, horizontal);
        StringBuilder card = new StringBuilder();
        card.append(upperLeftBox);
        if (horizontal) {
            card.append(" ".repeat(12));
            card.append(upperRightBox);
            card.append("\n");
            card.append(" ");
            if (occurrences == 2) {
                card.append(diffCell.repeat(Bookshelf.getColumns()));
            } else {
                card.append(emptyCell.repeat(Bookshelf.getColumns()));
            }
            card.append("\t\t").append(description);
            card.append("\n");
            card.append(lowerLeftBox);
            card.append(" ".repeat(12));
            card.append(lowerRightBox);
        } else {
            card.append(" ".repeat(7));
            card.append(upperRightBox);
            card.append("\n");
            if (occurrences == 2) {
                for (int i = 0; i < Bookshelf.getRows(); i++) {
                    card.append(" ".repeat(3));
                    card.append(diffCell);
                    if (i == 2) {
                        card.append("\t\t").append(description);
                    }
                    card.append("\n");
                }
            } else {
                for (int i = 0; i < Bookshelf.getRows(); i++) {
                    card.append(" ".repeat(3));
                    card.append(emptyCell);
                    if (i == 2) {
                        card.append("\t\t").append(description);
                    }
                    card.append("\n");
                }
            }
            card.append(lowerLeftBox);
            card.append(" ".repeat(7));
            card.append(lowerRightBox);
        }
        card.append(" ");
        card.append("x").append(occurrences);
        if (occurrences == 3 || occurrences == 4) {
            card.append(", max 3 different colors");
        }
        System.out.println(card);
    }

    /**
     * Prints the full line description, based on its parameters
     *
     * @param occurrences the number of occurrences of the common goal
     * @param horizontal  whether the common goal is horizontal or not
     * @return the description of the common goal
     */
    public static String fullLineDescription(int occurrences, boolean horizontal) {
        String description = occurrences + " ";
        if (horizontal) {
            description += "rows ";
            if (occurrences == 2) {
                description += "each formed by 5 different types of tiles";
            } else {
                description += "each formed by 5 tiles of maximum three different types.";
            }
        } else {
            description += "columns ";
            if (occurrences == 2) {
                description += "each formed by 6 different types of tiles";
            } else {
                description += "each formed by 6 tiles of maximum three different types.";
            }
        }
        return description;
    }

    /**
     * Used to print the group common goal
     *
     * @param occurrences the number of occurrences of the common goal
     * @param size        the size of the common goal
     */
    public static void groupPrintLayout(int occurrences, int size) {
        String description = occurrences + " groups each containing at least 2 tiles of the same type (not necessarily in the depicted shape).";
        StringBuilder cell;
        // occurrences can be either 4 or 6
        cell = new StringBuilder(upperLeftBox + " ".repeat(5) + upperRightBox + "\n");
        for (int i = 0; i < size; i++) {
            cell.append(" ".repeat(2));
            cell.append(filledCell);
            if (occurrences == 4) {
                if (i == 1) {
                    cell.append(" ".repeat(7)).append(description);
                }
            } else {
                if (i == 0) {
                    cell.append(" ".repeat(7)).append(description);
                }
            }
            cell.append("\n");
        }
        cell.append(lowerLeftBox);
        cell.append(" ".repeat(5));
        cell.append(lowerRightBox);
        cell.append(" " + "x").append(occurrences);
        System.out.println(cell);
    }

    /**
     * Used to print the items per color common goal
     */
    public static void itemsPerColorPrintLayout() {
        String description = "\tEight tiles of the same type. No restriction about the positions.";
        StringBuilder cell = new StringBuilder();
        cell.append(" ").append(filledCell).append(filledCell).append(" ").append("\n");
        for (int i = 0; i < 2; i++) {
            cell.append(filledCell.repeat(3));
            if (i == 0) {
                cell.append(description);
            }
            cell.append("\n");
        }
        System.out.println(cell);
    }

    /**
     * Used to print the square common goal
     */
    public static void squarePrintLayout() {
        String description = "   Two groups each containing 4 tiles of the same type in a 2x2 square. The two squares are independent.";
        String cell = upperLeftBox + " ".repeat(7) + upperRightBox + "\n" + "  " + filledCell.repeat(2) + " ".repeat(7) + description + "\n" + "  " + filledCell.repeat(2) + "\n" + lowerLeftBox + " ".repeat(7) + lowerRightBox + " ".repeat(3) + "x2";

        System.out.println(cell);
    }

    /**
     * Prints the layout of the stair pattern.
     */
    public static void stairPrintLayout() {
        String description = "\t\tFive columns of increasing or decreasing height. Tiles can be of any type.";
        StringBuilder cell = new StringBuilder();
        for (int i = 0; i < Bookshelf.getColumns(); i++) {
            cell.append(emptyCell.repeat(i + 1));
            if (i == 2) {
                cell.append(description);
            }
            cell.append("\n");
        }
        System.out.println(cell);
    }

    /**
     * Prints the layout of the X pattern.
     */
    public static void xShapePrintLayout() {
        String description = "     Five tiles of the same type forming an X.";
        StringBuilder card = new StringBuilder();
        int k = 1;
        for (int i = 0; i < 2 * k + 1; i++) {
            for (int j = 0; j < 2 * k + 1; j++) {
                if (i == j || i + j == 2 * k) {
                    card.append(filledCell);
                } else {
                    card.append("  ");
                }
                if (i == 1 && j == 1) {
                    card.append(description);
                }
            }
            card.append("\n");
        }

        System.out.println(card);
    }
}
