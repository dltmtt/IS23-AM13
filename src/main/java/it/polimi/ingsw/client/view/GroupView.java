package it.polimi.ingsw.client.view;


import it.polimi.ingsw.utils.CLIUtilities;

public class GroupView extends LayoutView {
    private final int occurrences;
    private final int size;

    public GroupView(int occurrences1, int size1) {
        this.occurrences = occurrences1;
        this.size = size1;
        description = occurrences + " groups each containing at least  2 tiles of the same type (not necessarily in the depicted shape).";
    }

    @Override
    public void printLayout() {
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

    @Override
    public void drawLayout() {

    }
}
