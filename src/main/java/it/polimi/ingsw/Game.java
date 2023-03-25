package it.polimi.ingsw;

import java.util.List;
import java.util.Random;

public class Game {
    private List<PersonalGoal> personalGoalDeck;
    private List<CommonGoal> commonGoalDeck;
    private Board livingRoom;

    private List<Player> players;

    private Player currentPlayer;

    public Game() throws IllegalAccessException {
        initialize();
    }

    public void initialize() throws IllegalAccessException {
        //aggiungere creazione commonGoalDeck
        /**
         * living room is created and filled
         */
        livingRoom = new Board(players.size());
        livingRoom.fill();
        /**
         * personalGoalDeck is created with this 'for' which adds in the list a new card based on the assigned number
         */
        for (int i = 0; i < 11; i++) {
            //is added a new personal goal in the List
            personalGoalDeck.add(new PersonalGoal(i));
        }
        //potrebbe essere fatto un metodo a parte
        for (Player player : players) {
            player.setPersonalGoal(drawPersonalCard());
        }

    }

    public PersonalGoal drawPersonalCard() {
        Random randomNumberGenerator = new Random();
        int number = randomNumberGenerator.nextInt(personalGoalDeck.size());
        PersonalGoal chosen = personalGoalDeck.get(number);
        personalGoalDeck.remove(number);
        return chosen;
    }


//    public void Move(Player player,List<Coordinates> coord, int column) throws IllegalAccessException {
//       player.getBookshelf().insert(column,livingRoom.pickFromBoard(coord));
//    }


}
