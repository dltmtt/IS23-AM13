package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.utils.CLIUtilities;

public class DiagonalView extends Layout {

    public DiagonalView() {
        description = "  Five tiles of the same type forming a " +
                "diagonal. ";
    }

    @Override
    public void printLayout() {
        StringBuilder cell = new StringBuilder();
        for (int i = 0; i < Bookshelf.getColumns(); i++) {
            for (int j = 0; j < Bookshelf.getColumns(); j++) {
                if (i == j)
                    cell.append(CLIUtilities.filledCell);
                else
                    cell.append(CLIUtilities.emptyCell);
                if (i == 1 && j == Bookshelf.getColumns() - 1)
                    cell.append(description);
            }
            cell.append("\n");
        }
        System.out.println(cell);

    }

    @Override
    public void drawLayout() {

    }
}
