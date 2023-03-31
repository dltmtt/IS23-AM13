package it.polimi.ingsw.Models.Goal;

import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Utility.Coordinates;

import java.util.HashMap;

public class PersonalGoal {
    private final HashMap<Coordinates, Color> personalGoalCard;
    private int currentReached;
    private int counter;

    /**
     * Creates a personal goal card.
     * It is a representation of the spaces that a player
     * has to fill with his bookshelf's items.
     *
     * @param layout the layout of the personal goal.
     */
    // TODO: add the possibility to load custom personal goals from a file
    public PersonalGoal(int layout) {
        personalGoalCard = new HashMap<>();

        switch (layout) {
            case 0 -> {
                personalGoalCard.put(new Coordinates(0, 2), Color.PINK);
                personalGoalCard.put(new Coordinates(1, 4), Color.GREEN);
                personalGoalCard.put(new Coordinates(2, 3), Color.LIGHTBLUE);
                personalGoalCard.put(new Coordinates(3, 0), Color.YELLOW);
                personalGoalCard.put(new Coordinates(3, 1), Color.WHITE);
                personalGoalCard.put(new Coordinates(4, 5), Color.BLUE);
            }
            case 1 -> {
                personalGoalCard.put(new Coordinates(0, 3), Color.WHITE);
                personalGoalCard.put(new Coordinates(1, 1), Color.BLUE);
                personalGoalCard.put(new Coordinates(1, 4), Color.YELLOW);
                personalGoalCard.put(new Coordinates(3, 0), Color.PINK);
                personalGoalCard.put(new Coordinates(3, 2), Color.GREEN);
                personalGoalCard.put(new Coordinates(4, 5), Color.LIGHTBLUE);
            }
            case 2 -> {
                personalGoalCard.put(new Coordinates(0, 3), Color.YELLOW);
                personalGoalCard.put(new Coordinates(1, 4), Color.WHITE);
                personalGoalCard.put(new Coordinates(2, 2), Color.BLUE);
                personalGoalCard.put(new Coordinates(2, 5), Color.PINK);
                personalGoalCard.put(new Coordinates(3, 0), Color.LIGHTBLUE);
                personalGoalCard.put(new Coordinates(4, 1), Color.GREEN);
            }
            case 3 -> {
                personalGoalCard.put(new Coordinates(0, 0), Color.PINK);
                personalGoalCard.put(new Coordinates(1, 1), Color.YELLOW);
                personalGoalCard.put(new Coordinates(2, 5), Color.LIGHTBLUE);
                personalGoalCard.put(new Coordinates(3, 1), Color.BLUE);
                personalGoalCard.put(new Coordinates(3, 3), Color.WHITE);
                personalGoalCard.put(new Coordinates(4, 5), Color.GREEN);
            }
            case 4 -> {
                personalGoalCard.put(new Coordinates(0, 5), Color.PINK);
                personalGoalCard.put(new Coordinates(1, 2), Color.YELLOW);
                personalGoalCard.put(new Coordinates(2, 0), Color.LIGHTBLUE);
                personalGoalCard.put(new Coordinates(2, 5), Color.BLUE);
                personalGoalCard.put(new Coordinates(3, 3), Color.WHITE);
                personalGoalCard.put(new Coordinates(4, 4), Color.GREEN);
            }
            case 5 -> {
                personalGoalCard.put(new Coordinates(0, 3), Color.GREEN);
                personalGoalCard.put(new Coordinates(1, 4), Color.PINK);
                personalGoalCard.put(new Coordinates(2, 3), Color.YELLOW);
                personalGoalCard.put(new Coordinates(3, 1), Color.LIGHTBLUE);
                personalGoalCard.put(new Coordinates(4, 0), Color.BLUE);
                personalGoalCard.put(new Coordinates(4, 2), Color.WHITE);
            }
            case 6 -> {
                personalGoalCard.put(new Coordinates(0, 3), Color.LIGHTBLUE);
                personalGoalCard.put(new Coordinates(1, 1), Color.WHITE);
                personalGoalCard.put(new Coordinates(2, 1), Color.GREEN);
                personalGoalCard.put(new Coordinates(2, 3), Color.BLUE);
                personalGoalCard.put(new Coordinates(3, 2), Color.PINK);
                personalGoalCard.put(new Coordinates(4, 5), Color.YELLOW);
            }
            case 7 -> {
                personalGoalCard.put(new Coordinates(0, 2), Color.LIGHTBLUE);
                personalGoalCard.put(new Coordinates(0, 5), Color.GREEN);
                personalGoalCard.put(new Coordinates(1, 3), Color.PINK);
                personalGoalCard.put(new Coordinates(2, 0), Color.WHITE);
                personalGoalCard.put(new Coordinates(3, 4), Color.BLUE);
                personalGoalCard.put(new Coordinates(4, 1), Color.YELLOW);
            }
            case 8 -> {
                personalGoalCard.put(new Coordinates(0, 0), Color.GREEN);
                personalGoalCard.put(new Coordinates(1, 4), Color.PINK);
                personalGoalCard.put(new Coordinates(2, 3), Color.BLUE);
                personalGoalCard.put(new Coordinates(2, 5), Color.WHITE);
                personalGoalCard.put(new Coordinates(3, 2), Color.LIGHTBLUE);
                personalGoalCard.put(new Coordinates(4, 1), Color.YELLOW);
            }
            case 9 -> {
                personalGoalCard.put(new Coordinates(0, 0), Color.BLUE);
                personalGoalCard.put(new Coordinates(1, 1), Color.LIGHTBLUE);
                personalGoalCard.put(new Coordinates(2, 3), Color.GREEN);
                personalGoalCard.put(new Coordinates(2, 5), Color.YELLOW);
                personalGoalCard.put(new Coordinates(4, 1), Color.PINK);
                personalGoalCard.put(new Coordinates(4, 2), Color.WHITE);
            }
            case 10 -> {
                personalGoalCard.put(new Coordinates(0, 0), Color.YELLOW);
                personalGoalCard.put(new Coordinates(1, 2), Color.BLUE);
                personalGoalCard.put(new Coordinates(1, 4), Color.LIGHTBLUE);
                personalGoalCard.put(new Coordinates(2, 2), Color.WHITE);
                personalGoalCard.put(new Coordinates(3, 0), Color.GREEN);
                personalGoalCard.put(new Coordinates(4, 1), Color.PINK);
            }
            case 11 -> {
                personalGoalCard.put(new Coordinates(0, 0), Color.WHITE);
                personalGoalCard.put(new Coordinates(0, 4), Color.BLUE);
                personalGoalCard.put(new Coordinates(1, 2), Color.GREEN);
                personalGoalCard.put(new Coordinates(2, 3), Color.PINK);
                personalGoalCard.put(new Coordinates(3, 4), Color.YELLOW);
                personalGoalCard.put(new Coordinates(4, 2), Color.LIGHTBLUE);
            }
        }
    }

    public Color getColor(Coordinates key) {
        return personalGoalCard.get(key);
    }

    /**
     * Gets the number of points gained by the player according to how many spaces are matched.
     *
     * @return the number of points gained by the player
     */
    public int getPoints() {
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
}

