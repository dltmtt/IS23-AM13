package it.polimi.ingsw;

import java.util.HashMap;

public class PersonalGoal {
    private final HashMap<IntegerPair, Color> personalGoalCard;
    private int currentReached;
    private int counter;

//    Constructor

    public PersonalGoal(HashMap map) {
        this.personalGoalCard = map;
        currentReached = 0;
        counter = 0;
    }

    public Color getColor(IntegerPair key) {
        return personalGoalCard.get(key);

    }

    public int getPoints() {
        int points = currentReached;
        if (points < 3) {
            return points;
        }
        return points + counter;

    }

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

