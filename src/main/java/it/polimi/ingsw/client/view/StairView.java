package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.utils.CLIUtilities;

public class StairView extends LayoutView {
    public StairView() {
        description = "\t\tFive columns of increasing or decreasing" +
                "height. Tiles can be of any type.  ";
    }

    @Override
    public void printLayout() {
        StringBuilder cell = new StringBuilder();
        for (int i = 0; i < Bookshelf.getColumns(); i++) {
            cell.append(CLIUtilities.filledCell.repeat(i + 1));
            if (i == 2) {
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
