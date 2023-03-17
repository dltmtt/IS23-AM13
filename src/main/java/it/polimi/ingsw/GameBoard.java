package it.polimi.ingsw;

import java.util.HashMap;
import java.util.List;

public class GameBoard {
    private List<PersonalGoal> personalGoalDeck;

    public void initilize() {
        //cretion of personalGoalDeck

        for (int i = 0; i < 11; i++) {
            //is added a new personal goal in the List
            personalGoalDeck.add(createCard(i));
        }

    }

    // this method creates a new personal card depending on the num of card reached during the 'for' in the initilize method
    public PersonalGoal createCard(int numOfCard) {
        HashMap<IntegerPair, Color> map = new HashMap<>();
        if (numOfCard == 0) {
            map.put(new IntegerPair(0, 2), Color.PINK);
            map.put(new IntegerPair(1, 4), Color.PINK);
            map.put(new IntegerPair(0, 2), Color.PINK);
            map.put(new IntegerPair(0, 2), Color.PINK);
            map.put(new IntegerPair(0, 2), Color.PINK);
            map.put(new IntegerPair(0, 2), Color.PINK);
        }
    }

}
