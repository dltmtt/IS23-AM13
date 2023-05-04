package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Coordinates;

import java.util.HashMap;
import java.util.List;

public class PersonalGoal {

    private final HashMap<Coordinates, Color> personalGoalCard;
    private final int index;
    private int currentReached;
    private int counter;

    /**
     * Creates a personal goal card.
     * It is a representation of the spaces that a player
     * has to fill with his bookshelf's items.
     *
     * @param loadedCoordinates the coordinates of the spaces to fill, read from a file
     * @param loadedColors      the colors of the spaces to fill, read from a file
     */
    public PersonalGoal(List<Coordinates> loadedCoordinates, List<Color> loadedColors, int index) {
        personalGoalCard = new HashMap<>();
        for (int i = 0; i < Math.min(loadedCoordinates.size(), loadedColors.size()); i++) {
            personalGoalCard.put(loadedCoordinates.get(i), loadedColors.get(i));
        }
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public Color getColor(Coordinates key) {
        return personalGoalCard.get(key);
    }

    /**
     * Gets the number of points gained by the player according to how many spaces are matched.
     *
     * @return the number of points gained by the player
     */
    public int getPoints(Bookshelf bookshelf) {
        for (Coordinates coordinates : personalGoalCard.keySet()) {
            if (bookshelf.getItemAt(coordinates.x(), coordinates.y()).isPresent()) {
                if (bookshelf.getItemAt(coordinates.x(), coordinates.y()).get().color() == personalGoalCard.get(coordinates)) {
                    colorReached();
                }
            }
        }
        return currentReached < 3 ? currentReached : currentReached + counter;
    }

    // TODO: ask if you mean the number of matches?

    /**
     * Returns the number of colors reached.
     */
    public void colorReached() {
        currentReached++;

        if (currentReached > 2) {
            if (currentReached < 5) {
                counter = (currentReached - 2);
            } else {
                if (currentReached == 5) {
                    counter = currentReached - 1;
                } else {
                    counter = currentReached;
                }
            }
        }
    }

    public String toString() {
        for (Coordinates c : personalGoalCard.keySet()) {
            System.out.println("x :" + c.x() + "\n" + "y :" + c.y() + "\n" + personalGoalCard.get(c) + "\n");
        }
        return null;
    }

    /**
     * Prints the personal goal card.
     * TODO: move this method to the Client package
     */

    public void cli_print() {

        StringBuilder cell;
        for (int row = Bookshelf.getRows() - 1; row >= 0; row--) {
            cell = new StringBuilder();
            for (int column = 0; column < Bookshelf.getColumns(); column++) {
                cell.append("[");
                if (!personalGoalCard.containsKey(new Coordinates(row, column))) {
                    cell.append(Color.BLACK)
                            //.append(boardMatrix[row][column].color().toString().charAt(0))
                            .append("⏹").append(Color.RESET_COLOR);
                } else {
                    cell.append(Color.toANSItext(personalGoalCard.get(new Coordinates(row, column)), false))
                            //.append(boardMatrix[row][column].color().toString().charAt(0))
                            .append("⏹").append(Color.RESET_COLOR);
                }
                cell.append("]");
            }
            System.out.println(cell);
        }
    }

    public HashMap<Coordinates, Color> getPersonalGoalCard() {
        return personalGoalCard;
    }

    public int getCounter() {
        return counter;
    }

    public int getCurrentReached() {
        return currentReached;
    }

    public PersonalGoal getPersonalGoal() {
        return this;
    }
}
