package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Coordinates;

import java.util.HashMap;
import java.util.List;

/**
 * This class represents a personal goal card, which contains the disposition of the items to be matched
 * by the player in the bookshelf in order to increase its score.
 */
public class PersonalGoal {

    private final HashMap<Coordinates, Color> personalGoalCard;
    private final int index;
    private int currentlyReached;
    private int counter;

    /**
     * Creates a personal goal card.
     * It is a representation of the spaces that a player
     * has to fill with his bookshelf's items, to increase his final score.
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

    /**
     * Returns the index of the Personal Goal card.
     *
     * @return the index of the personal goal card
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the color of the item to be matched in the space with the given coordinates.
     *
     * @param key the coordinates of the space to check
     * @return the color of the item to be matched
     */
    public Color getColor(Coordinates key) {
        return personalGoalCard.get(key);
    }

    /**
     * Gets the number of points gained by the player according to how many spaces are matched.
     *
     * @return the number of points gained by the player
     */
    public int getPoints(Bookshelf bookshelf) {
        currentlyReached = 0;
        counter = 0;
        for (Coordinates coordinates : personalGoalCard.keySet()) {
            if (bookshelf.getItemAt(coordinates.x(), coordinates.y()).isPresent()) {
                if (bookshelf.getItemAt(coordinates.x(), coordinates.y()).get().color() == personalGoalCard.get(coordinates)) {
                    colorReached();
                }
            }
        }
        return currentlyReached < 3 ? currentlyReached : currentlyReached + counter;
    }

    /**
     * Computes the number of colors reached, depending on how many items-matches the player has reached.
     */
    public void colorReached() {
        currentlyReached++;

        if (currentlyReached > 2) {
            if (currentlyReached < 5) {
                counter = (currentlyReached - 2);
            } else {
                if (currentlyReached == 5) {
                    counter = currentlyReached - 1;
                } else {
                    counter = currentlyReached;
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
     * Returns the HashMap representing the personal goal card of the player.
     *
     * @return the HashMap representing the personal goal card of the player
     */
    public HashMap<Coordinates, Color> getPersonalGoalCard() {
        return personalGoalCard;
    }

    /**
     * Returns the PersonalGoal of the player.
     *
     * @return the PersonalGoal of the player
     */
    public PersonalGoal getPersonalGoal() {
        return this;
    }
}
