package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utils.CLIUtilities;

public class CornersView extends LayoutView {
    public CornersView() {
        description = "Four tiles of the same type in the four " +
                "corners of the bookshelf. ";
    }

    @Override
    public void printLayout() {
        StringBuilder card = new StringBuilder();
        // Draw an m×n card with fullCells in the corners and emptyCells everywhere else
        int m = 5;
        int n = 6;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n - 1; j++) {
                if ((i == 0 && j == 0) || (i == 0 && j == n - 2) || (i == m - 1 && j == 0) || (i == m - 1 && j == n - 2)) {
                    card.append(CLIUtilities.filledCell);
                } else {
                    card.append(CLIUtilities.emptyCell);
                    if (i == 1 && j == n - 2) {
                        card.append("  ").append(description);
                    }
                }
            }
            card.append("\n");
        }
        System.out.println(card);
    }

    @Override
    public void drawLayout() {
        // work in progress...
    }
}
