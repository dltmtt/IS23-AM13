package it.polimi.ingsw;

import java.util.HashMap;
import java.util.List;

public class GameBoard {
    private List<PersonalGoal> personalGoalDeck;
    private List<CommonGoal> commonGoalDeck;
    private Board livingRoom;

    private List<Player> players;

    public GameBoard() {
        initilize();
    }

    public void initilize() {
        //aggiungere creazione commonGoalDeck
        /**
         * livìng room is created and filled
         */
        livingRoom=new Board(players.size());
        livingRoom.fill();
        /**
         * personalGoalDeck is created with this 'for' which adds in the list a new card based on the assigned number
         */
        for (int i = 0; i < 11; i++) {
            //is added a new personal goal in the List
            personalGoalDeck.add(new PersonalGoal(i));
        }

    }

    // this method creates a new personal card depending on the num of card reached during the 'for' in the initilize method

}
