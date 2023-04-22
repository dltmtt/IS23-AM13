package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utils.CLIUtilities;

public class XShapeView extends Layout {
    public XShapeView() {
        description = "Five tiles of the same type forming an X";
    }

    @Override
    public void printLayout() {
        StringBuilder card = new StringBuilder();

        // Draw an X with filledCells in a (2k + 1)×(2k + 1) matrix.
        int k = 1;
        for (int i = 0; i < 2 * k + 1; i++) {
            for (int j = 0; j < 2 * k + 1; j++) {
                if (i == j || i + j == 2 * k) {
                    card.append(CLIUtilities.filledCell);
                } else {
                    card.append(CLIUtilities.emptyCell);
                }
            }
            card.append("\n");
        }

        System.out.println(card);
    }

    @Override
    public void drawLayout() {
        // TODO: implement
    }
}
