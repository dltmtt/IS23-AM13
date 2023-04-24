package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Coordinates;

import java.util.HashMap;

public class PersonalGoalview {
    private final HashMap<Coordinates, Color> personalGoalCard;

    public PersonalGoalview(HashMap<Coordinates, Color> personalGoal) {
        this.personalGoalCard = personalGoal;
    }

    public void printLayout() {
        StringBuilder cell;
        for (int row = Bookshelf.getRows() - 1; row >= 0; row--) {
            cell = new StringBuilder();
            for (int column = 0; column < Bookshelf.getColumns(); column++) {
                cell.append("[");
                if (!personalGoalCard.containsKey(new Coordinates(row, column))) {
                    cell.append(Color.BLACK)
                            //.append(boardMatrix[row][column].color().toString().charAt(0))
                            .append("⏹")
                            .append(Color.RESET_COLOR);
                } else {
                    cell.append(Color.toANSItext(personalGoalCard.get(new Coordinates(row, column)), false))
                            //.append(boardMatrix[row][column].color().toString().charAt(0))
                            .append("⏹")
                            .append(Color.RESET_COLOR);
                }
                cell.append("]");
            }
            System.out.println(cell);
        }
    }
}
