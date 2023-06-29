package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Coordinates;
import it.polimi.ingsw.utils.SettingLoader;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class is used to print the personal goal card in the cli.
 */
public class PersonalGoalView {

    private final HashMap<Coordinates, Color> personalGoalCard;

    /**
     * This is the constructor of the class.
     * @param index the index of the personal goal card.
     * @throws IOException if the file is not found.
     * @throws ParseException if the file is not found.
     */
    public PersonalGoalView(int index) throws IOException, ParseException {
        personalGoalCard = SettingLoader.loadSpecificPersonalGoal(index).getPersonalGoalCard();
    }

    /**
     * It prints the personal goal card.
     */
    public void printLayout() {
        System.out.println("This is your Personal Goal Card");
        StringBuilder cell;
        for (int row = Bookshelf.getRows() - 1; row >= 0; row--) {
            cell = new StringBuilder();
            for (int column = 0; column < Bookshelf.getColumns(); column++) {
                cell.append("[");
                if (!personalGoalCard.containsKey(new Coordinates(row, column))) {
                    cell.append(Color.BLACK)
                            .append("⏹").append(Color.RESET_COLOR);
                } else {
                    cell.append(Color.toANSItext(personalGoalCard.get(new Coordinates(row, column)), false))
                            .append("⏹").append(Color.RESET_COLOR);
                }
                cell.append("]");
            }
            System.out.println(cell);
        }
    }
}
