package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utils.CLIUtilities;

public class ItemsPerColorView extends LayoutView {
    public ItemsPerColorView() {
        description = "\tEight tiles of the same type. No restriction about the positions.";
    }

    @Override
    public void printLayout() {
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

    @Override
    public void drawLayout() {
        // TODO: implement
    }
}
