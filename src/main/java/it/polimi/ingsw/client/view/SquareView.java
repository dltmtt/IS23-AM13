package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utils.CLIUtilities;

public class SquareView extends LayoutView {

    public SquareView() {
        description = "   Two groups each containing 4 tiles of " +
                "the same type in a 2x2 square. The two squares are independent. ";
    }

    @Override
    public void printLayout() {
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

    @Override
    public void drawLayout() {

    }
}
