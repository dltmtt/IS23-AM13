package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.utils.CLIUtilities;

public class FullLineView extends LayoutView {
    private final int occurrences;
    private final boolean horizontal;

    public FullLineView(int occurrences, boolean horizontal) {
        this.occurrences = occurrences;
        this.horizontal = horizontal;
        description = occurrences + " ";
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
    }

    @Override
    public void printLayout() {
        StringBuilder card = new StringBuilder();
        card.append(CLIUtilities.upperLeftBox);
        if (horizontal) {
            card.append(" ".repeat(12));
            card.append(CLIUtilities.upperRightBox);
            card.append("\n");
            card.append(" ");
            if (occurrences == 2) {
                for (int i = 0; i < Bookshelf.getColumns(); i++) {
                    card.append(CLIUtilities.filledCell);
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
                    card.append(CLIUtilities.filledCell);
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

    @Override
    public void drawLayout() {

    }
}
