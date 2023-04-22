package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utils.CLIUtilities;

public class XShapeView extends LayoutView {
    public XShapeView() {
        description = "     Five tiles of the same type forming an X.";

    }

    @Override
    public void printLayout() {
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

    @Override
    public void drawLayout() {
        // TODO: implement
    }
}
