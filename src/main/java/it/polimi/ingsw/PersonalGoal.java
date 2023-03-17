package it.polimi.ingsw;

import java.util.HashMap;

public class PersonalGoal {
    private final HashMap<IntegerPair, Color> personalGoalCard;
    private int currentReached;
    private int counter;

//    Constructor

    public PersonalGoal (int numOfCard) {
        HashMap<IntegerPair, Color> map = new HashMap<IntegerPair,Color>();

        switch(numOfCard){
            case 0:
                map.put(new IntegerPair(0, 2), Color.PINK);
                map.put(new IntegerPair(1, 4), Color.GREEN);
                map.put(new IntegerPair(2, 3), Color.LIGHTBLUE);
                map.put(new IntegerPair(3, 0), Color.YELLOW);
                map.put(new IntegerPair(3, 1), Color.WHITE);
                map.put(new IntegerPair(4, 5), Color.BLUE);
                break;
            case 1:
                map.put(new IntegerPair(0, 3), Color.WHITE);
                map.put(new IntegerPair(1, 1), Color.BLUE);
                map.put(new IntegerPair(1, 4), Color.YELLOW);
                map.put(new IntegerPair(3, 0), Color.PINK);
                map.put(new IntegerPair(3, 2), Color.GREEN);
                map.put(new IntegerPair(4, 5), Color.LIGHTBLUE);
                break;
            case 2:
                map.put(new IntegerPair(0, 3), Color.YELLOW);
                map.put(new IntegerPair(1, 4), Color.WHITE);
                map.put(new IntegerPair(2, 2), Color.BLUE);
                map.put(new IntegerPair(2, 5), Color.PINK);
                map.put(new IntegerPair(3, 0), Color.LIGHTBLUE);
                map.put(new IntegerPair(4, 1), Color.GREEN);
                break;
            case 3:
                map.put(new IntegerPair(0, 0), Color.PINK);
                map.put(new IntegerPair(1, 1), Color.YELLOW);
                map.put(new IntegerPair(2, 5), Color.LIGHTBLUE);
                map.put(new IntegerPair(3, 1), Color.BLUE);
                map.put(new IntegerPair(3, 3), Color.WHITE);
                map.put(new IntegerPair(4, 5), Color.GREEN);
                break;
            case 4:
                map.put(new IntegerPair(0, 5), Color.PINK);
                map.put(new IntegerPair(1, 2), Color.YELLOW);
                map.put(new IntegerPair(2, 0), Color.LIGHTBLUE);
                map.put(new IntegerPair(2, 5), Color.BLUE);
                map.put(new IntegerPair(3, 3), Color.WHITE);
                map.put(new IntegerPair(4, 4), Color.GREEN);
                break;
            case 5:
                map.put(new IntegerPair(0, 3), Color.GREEN);
                map.put(new IntegerPair(1, 4), Color.PINK);
                map.put(new IntegerPair(2, 3), Color.YELLOW);
                map.put(new IntegerPair(3, 1), Color.LIGHTBLUE);
                map.put(new IntegerPair(4, 0), Color.BLUE);
                map.put(new IntegerPair(4, 2), Color.WHITE);
                break;
            case 6:
                map.put(new IntegerPair(0, 3), Color.LIGHTBLUE);
                map.put(new IntegerPair(1, 1), Color.WHITE);
                map.put(new IntegerPair(2, 1), Color.GREEN);
                map.put(new IntegerPair(2, 3), Color.BLUE);
                map.put(new IntegerPair(3, 2), Color.PINK);
                map.put(new IntegerPair(4, 5), Color.YELLOW);
                break;
            case 7:
                map.put(new IntegerPair(0, 2), Color.LIGHTBLUE);
                map.put(new IntegerPair(0, 5), Color.GREEN);
                map.put(new IntegerPair(1, 3), Color.PINK);
                map.put(new IntegerPair(2, 0), Color.WHITE);
                map.put(new IntegerPair(3, 4), Color.BLUE);
                map.put(new IntegerPair(4, 1), Color.YELLOW);
                break;
            case 8:
                map.put(new IntegerPair(0, 0), Color.GREEN);
                map.put(new IntegerPair(1, 4), Color.PINK);
                map.put(new IntegerPair(2, 3), Color.BLUE);
                map.put(new IntegerPair(2, 5), Color.WHITE);
                map.put(new IntegerPair(3, 2), Color.LIGHTBLUE);
                map.put(new IntegerPair(4, 1), Color.YELLOW);
                break;
            case 9:
                map.put(new IntegerPair(0, 0), Color.BLUE);
                map.put(new IntegerPair(1, 1), Color.LIGHTBLUE);
                map.put(new IntegerPair(2, 3), Color.GREEN);
                map.put(new IntegerPair(2, 5), Color.YELLOW);
                map.put(new IntegerPair(4, 1), Color.PINK);
                map.put(new IntegerPair(4, 2), Color.WHITE);
                break;
            case 10:
                map.put(new IntegerPair(0, 0), Color.YELLOW);
                map.put(new IntegerPair(1, 2), Color.BLUE);
                map.put(new IntegerPair(1, 4), Color.LIGHTBLUE);
                map.put(new IntegerPair(2, 2), Color.WHITE);
                map.put(new IntegerPair(3, 0), Color.GREEN);
                map.put(new IntegerPair(4, 1), Color.PINK);
                break;
            case 11:
                map.put(new IntegerPair(0, 0), Color.WHITE);
                map.put(new IntegerPair(0, 4), Color.BLUE);
                map.put(new IntegerPair(1, 2), Color.GREEN);
                map.put(new IntegerPair(2, 3), Color.PINK);
                map.put(new IntegerPair(3, 4), Color.YELLOW);
                map.put(new IntegerPair(4, 2), Color.LIGHTBLUE);
                break;
            default:
                break;
        }
        personalGoalCard=map;
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

