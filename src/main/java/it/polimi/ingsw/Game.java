package it.polimi.ingsw;

import java.util.List;

public class Game {
    private List<PersonalGoal> personalGoalDeck;
    private List<CommonGoal> commonGoalDeck;
    private Board livingRoom;

    private List<Player> players;

    public Game() throws IllegalAccessException {
        initilize();
    }

    public void initilize() throws IllegalAccessException {
        //aggiungere creazione commonGoalDeck
        /**
         * livìng room is created and filled
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
//        //potrebbe essere fatto un metodo a parte
//        for(int i=0;i<players.size();i++){
//            players.get(i).setPersonalGoal(drawPersonalCard());
//        }

    }

//    public PersonalGoal drawPersonalCard(){
//        Random randomNumberGenerator= new Random();
//        int number=randomNumberGenerator.nextInt(personalGoalDeck.size());
//        PersonalGoal choosen=personalGoalDeck.get(number);
//        personalGoalDeck.remove(number);
//        return choosen;
//    }


//    public void Move(Player player,List<IntegerPair> coord, int column) throws IllegalAccessException {
//       player.getBookshelf().insert(column,livingRoom.pickFromBoard(coord));
//    }


}
