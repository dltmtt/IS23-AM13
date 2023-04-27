package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.utils.CLIUtilities;

public class CommonGoalView {
    public static void cornersPrintLayout() {
        String description = "Four tiles of the same type in the four " +
                "corners of the bookshelf. ";
        StringBuilder card = new StringBuilder();
        // Draw an m×n card with fullCells in the corners and emptyCells everywhere else
        int m = 5;
        int n = 6;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n - 1; j++) {
                if ((i == 0 && j == 0) || (i == 0 && j == n - 2) || (i == m - 1 && j == 0) || (i == m - 1 && j == n - 2)) {
                    card.append(CLIUtilities.filledCell);
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

    public static void diagonalPrintLayout() {
        String description = "  Five tiles of the same type forming a " +
                "diagonal. ";
        StringBuilder cell = new StringBuilder();

        for (int i = 0; i < Bookshelf.getColumns(); i++) {
            for (int j = 0; j < Bookshelf.getColumns(); j++) {
                if (i == j)
                    cell.append(CLIUtilities.filledCell);
                else
                    cell.append("  ");
                if (i == 1 && j == Bookshelf.getColumns() - 1)
                    cell.append(description);
            }
            cell.append("\n");
        }
        System.out.println(cell);

    }

    public static void fullLinePrintLayout(int occurrences, boolean horizontal) {
        String description = fullLineDescription(occurrences, horizontal);
        StringBuilder card = new StringBuilder();
        card.append(CLIUtilities.upperLeftBox);
        if (horizontal) {
            card.append(" ".repeat(12));
            card.append(CLIUtilities.upperRightBox);
            card.append("\n");
            card.append(" ");
            if (occurrences == 2) {
                for (int i = 0; i < Bookshelf.getColumns(); i++) {
                    card.append(CLIUtilities.diffCell);
                }

            } else {
                for (int i = 0; i < Bookshelf.getColumns(); i++) {
                    card.append(CLIUtilities.emptyCell);
                }
            }
            card.append("\t\t").append(description);
            card.append("\n");
            card.append(CLIUtilities.lowerLeftBox);
            card.append(" ".repeat(12));
            card.append(CLIUtilities.lowerRightBox);

        } else {
            card.append(" ".repeat(7));
            card.append(CLIUtilities.upperRightBox);
            card.append("\n");
            if (occurrences == 2) {
                for (int i = 0; i < Bookshelf.getRows(); i++) {
                    card.append(" ".repeat(3));
                    card.append(CLIUtilities.diffCell);
                    if (i == 2) {
                        card.append("\t\t").append(description);
                    }
                    card.append("\n");
                }
            } else {
                for (int i = 0; i < Bookshelf.getRows(); i++) {
                    card.append(" ".repeat(3));
                    card.append(CLIUtilities.emptyCell);
                    if (i == 2) {
                        card.append("\t\t").append(description);
                    }
                    card.append("\n");
                }
            }
            card.append(CLIUtilities.lowerLeftBox);
            card.append(" ".repeat(7));
            card.append(CLIUtilities.lowerRightBox);
        }
        card.append(" ");
        card.append("x").append(occurrences);
        if (occurrences == 3 || occurrences == 4) {
            card.append(", max 3 different colors");
        }
        System.out.println(card);
    }

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

    public static void groupPrintLayout(int occurrences, int size) {
        String description = occurrences + " groups each containing at least  2 tiles of the same type (not necessarily in the depicted shape).";
        StringBuilder cell;
        //occurrences can be either 4 or 6
        cell = new StringBuilder(CLIUtilities.upperLeftBox +
                " ".repeat(5) +
                CLIUtilities.upperRightBox +
                "\n");
        for (int i = 0; i < size; i++) {
            cell.append(" ".repeat(2));
            cell.append(CLIUtilities.filledCell);
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
        cell.append(CLIUtilities.lowerLeftBox);
        cell.append(" ".repeat(5));
        cell.append(CLIUtilities.lowerRightBox);
        cell.append(" " + "x").append(occurrences);
        System.out.println(cell);
    }

    public static void itemsPerColorPrintLayout() {
        String description = "\tEight tiles of the same type. No restriction about the positions.";
        StringBuilder cell = new StringBuilder();
        cell.append(" ").append(CLIUtilities.filledCell).append(CLIUtilities.filledCell).append(" ").append("\n");
        for (int i = 0; i < 2; i++) {
            cell.append(CLIUtilities.filledCell.repeat(3));
            if (i == 0) {
                cell.append(description);
            }
            cell.append("\n");
        }
        System.out.println(cell);
    }

    public static void squarePrintLayout() {
        String description = "   Two groups each containing 4 tiles of " +
                "the same type in a 2x2 square. The two squares are independent. ";
        String cell = CLIUtilities.upperLeftBox +
                " ".repeat(7) +
                CLIUtilities.upperRightBox +
                "\n" +
                "  " +
                CLIUtilities.filledCell.repeat(2) +
                " ".repeat(7) + description +
                "\n" +
                "  " +
                CLIUtilities.filledCell.repeat(2) +
                "\n" +
                CLIUtilities.lowerLeftBox +
                " ".repeat(7) +
                CLIUtilities.lowerRightBox +
                " ".repeat(3) + "x2";

        System.out.println(cell);
    }

    public static void stairPrintLayout() {
        String description = "\t\tFive columns of increasing or decreasing" +
                "height. Tiles can be of any type.  ";
        StringBuilder cell = new StringBuilder();
        for (int i = 0; i < Bookshelf.getColumns(); i++) {
            cell.append(CLIUtilities.emptyCell.repeat(i + 1));
            if (i == 2) {
                cell.append(description);
            }
            cell.append("\n");
        }
        System.out.println(cell);
    }

    public static void xShapePrintLayout() {
        String description = "     Five tiles of the same type forming an X.";
        StringBuilder card = new StringBuilder();
        int k = 1;
        for (int i = 0; i < 2 * k + 1; i++) {
            for (int j = 0; j < 2 * k + 1; j++) {
                if (i == j || i + j == 2 * k) {
                    card.append(CLIUtilities.filledCell);
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
