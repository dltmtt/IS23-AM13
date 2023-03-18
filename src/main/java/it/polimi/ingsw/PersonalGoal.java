package it.polimi.ingsw;

import java.util.HashMap;
import java.util.HashMap;

public class PersonalGoal {
    private final HashMap<IntegerPair, Color> personalGoalCard;
    private int currentReached;
    private int counter;

//    Constructor

    public PersonalGoal (int numOfCard) {
         personalGoalCard=new HashMap<>();


        switch (numOfCard) {
            case 0 -> {
                personalGoalCard.put(new IntegerPair(0, 2), Color.PINK);
                personalGoalCard.put(new IntegerPair(1, 4), Color.GREEN);
                personalGoalCard.put(new IntegerPair(2, 3), Color.LIGHTBLUE);
                personalGoalCard.put(new IntegerPair(3, 0), Color.YELLOW);
                personalGoalCard.put(new IntegerPair(3, 1), Color.WHITE);
                personalGoalCard.put(new IntegerPair(4, 5), Color.BLUE);
            }
            case 1 -> {
                personalGoalCard.put(new IntegerPair(0, 3), Color.WHITE);
                personalGoalCard.put(new IntegerPair(1, 1), Color.BLUE);
                personalGoalCard.put(new IntegerPair(1, 4), Color.YELLOW);
                personalGoalCard.put(new IntegerPair(3, 0), Color.PINK);
                personalGoalCard.put(new IntegerPair(3, 2), Color.GREEN);
                personalGoalCard.put(new IntegerPair(4, 5), Color.LIGHTBLUE);
            }
            case 2 -> {
                personalGoalCard.put(new IntegerPair(0, 3), Color.YELLOW);
                personalGoalCard.put(new IntegerPair(1, 4), Color.WHITE);
                personalGoalCard.put(new IntegerPair(2, 2), Color.BLUE);
                personalGoalCard.put(new IntegerPair(2, 5), Color.PINK);
                personalGoalCard.put(new IntegerPair(3, 0), Color.LIGHTBLUE);
                personalGoalCard.put(new IntegerPair(4, 1), Color.GREEN);
            }
            case 3 -> {
                personalGoalCard.put(new IntegerPair(0, 0), Color.PINK);
                personalGoalCard.put(new IntegerPair(1, 1), Color.YELLOW);
                personalGoalCard.put(new IntegerPair(2, 5), Color.LIGHTBLUE);
                personalGoalCard.put(new IntegerPair(3, 1), Color.BLUE);
                personalGoalCard.put(new IntegerPair(3, 3), Color.WHITE);
                personalGoalCard.put(new IntegerPair(4, 5), Color.GREEN);
            }
            case 4 -> {
                personalGoalCard.put(new IntegerPair(0, 5), Color.PINK);
                personalGoalCard.put(new IntegerPair(1, 2), Color.YELLOW);
                personalGoalCard.put(new IntegerPair(2, 0), Color.LIGHTBLUE);
                personalGoalCard.put(new IntegerPair(2, 5), Color.BLUE);
                personalGoalCard.put(new IntegerPair(3, 3), Color.WHITE);
                personalGoalCard.put(new IntegerPair(4, 4), Color.GREEN);
            }
            case 5 -> {
                personalGoalCard.put(new IntegerPair(0, 3), Color.GREEN);
                personalGoalCard.put(new IntegerPair(1, 4), Color.PINK);
                personalGoalCard.put(new IntegerPair(2, 3), Color.YELLOW);
                personalGoalCard.put(new IntegerPair(3, 1), Color.LIGHTBLUE);
                personalGoalCard.put(new IntegerPair(4, 0), Color.BLUE);
                personalGoalCard.put(new IntegerPair(4, 2), Color.WHITE);
            }
            case 6 -> {
                personalGoalCard.put(new IntegerPair(0, 3), Color.LIGHTBLUE);
                personalGoalCard.put(new IntegerPair(1, 1), Color.WHITE);
                personalGoalCard.put(new IntegerPair(2, 1), Color.GREEN);
                personalGoalCard.put(new IntegerPair(2, 3), Color.BLUE);
                personalGoalCard.put(new IntegerPair(3, 2), Color.PINK);
                personalGoalCard.put(new IntegerPair(4, 5), Color.YELLOW);
            }
            case 7 -> {
                personalGoalCard.put(new IntegerPair(0, 2), Color.LIGHTBLUE);
                personalGoalCard.put(new IntegerPair(0, 5), Color.GREEN);
                personalGoalCard.put(new IntegerPair(1, 3), Color.PINK);
                personalGoalCard.put(new IntegerPair(2, 0), Color.WHITE);
                personalGoalCard.put(new IntegerPair(3, 4), Color.BLUE);
                personalGoalCard.put(new IntegerPair(4, 1), Color.YELLOW);
            }
            case 8 -> {
                personalGoalCard.put(new IntegerPair(0, 0), Color.GREEN);
                personalGoalCard.put(new IntegerPair(1, 4), Color.PINK);
                personalGoalCard.put(new IntegerPair(2, 3), Color.BLUE);
                personalGoalCard.put(new IntegerPair(2, 5), Color.WHITE);
                personalGoalCard.put(new IntegerPair(3, 2), Color.LIGHTBLUE);
                personalGoalCard.put(new IntegerPair(4, 1), Color.YELLOW);
            }
            case 9 -> {
                personalGoalCard.put(new IntegerPair(0, 0), Color.BLUE);
                personalGoalCard.put(new IntegerPair(1, 1), Color.LIGHTBLUE);
                personalGoalCard.put(new IntegerPair(2, 3), Color.GREEN);
                personalGoalCard.put(new IntegerPair(2, 5), Color.YELLOW);
                personalGoalCard.put(new IntegerPair(4, 1), Color.PINK);
                personalGoalCard.put(new IntegerPair(4, 2), Color.WHITE);
            }
            case 10 -> {
                personalGoalCard.put(new IntegerPair(0, 0), Color.YELLOW);
                personalGoalCard.put(new IntegerPair(1, 2), Color.BLUE);
                personalGoalCard.put(new IntegerPair(1, 4), Color.LIGHTBLUE);
                personalGoalCard.put(new IntegerPair(2, 2), Color.WHITE);
                personalGoalCard.put(new IntegerPair(3, 0), Color.GREEN);
                personalGoalCard.put(new IntegerPair(4, 1), Color.PINK);
            }
            case 11 -> {
                personalGoalCard.put(new IntegerPair(0, 0), Color.WHITE);
                personalGoalCard.put(new IntegerPair(0, 4), Color.BLUE);
                personalGoalCard.put(new IntegerPair(1, 2), Color.GREEN);
                personalGoalCard.put(new IntegerPair(2, 3), Color.PINK);
                personalGoalCard.put(new IntegerPair(3, 4), Color.YELLOW);
                personalGoalCard.put(new IntegerPair(4, 2), Color.LIGHTBLUE);
            }
            default -> {
            }
        }
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

