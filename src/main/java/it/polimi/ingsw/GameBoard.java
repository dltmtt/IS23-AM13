package it.polimi.ingsw;

import java.util.HashMap;
import java.util.List;

public class GameBoard {
    private List<PersonalGoal> personalGoalDeck;

    public GameBoard() {
        initilize();
    }

    public void initilize() {
        //cretion of personalGoalDeck

        for (int i = 0; i < 11; i++) {
            //is added a new personal goal in the List
            personalGoalDeck.add(new PersonalGoal(i));
        }

    }

    // this method creates a new personal card depending on the num of card reached during the 'for' in the initilize method

}
