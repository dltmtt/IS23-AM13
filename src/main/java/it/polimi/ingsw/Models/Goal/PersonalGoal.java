package it.polimi.ingsw.Models.Goal;

import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Utility.Coordinates;

import java.util.HashMap;

public class PersonalGoal {
    private final HashMap<Coordinates, Color> personalGoalCard;
    private int currentReached;
    private int counter;

    /**
     * This constructor creates a personal goal card.
     *
     * @param numOfCard
     */
    public PersonalGoal(int numOfCard) {
        personalGoalCard = new HashMap<>();

        switch (numOfCard) {
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
            default -> {
            }
        }
    }

    public Color getColor(Coordinates key) {
        return personalGoalCard.get(key);

    }

    /**
     * This method returns the number of points of the personal goal card.
     *
     * @return the number of points of the personal goal card
     */
    public int getPoints() {
        int points = currentReached;
        if (points < 3) {
            return points;
        }
        return points + counter;
    }

    /**
     * This method returns the number of colors reached.
     */
    public void ColorReached() {
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

