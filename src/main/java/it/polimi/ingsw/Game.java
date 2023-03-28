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
        // TODO: Aggiungere creazione commonGoalDeck
        // Living room is created and filled
        livingRoom = new Board(players.size());
        livingRoom.fill();
        // PersonalGoalDeck is created with this 'for' which adds in the list a new card based on the assigned number
        for (int i = 0; i < 11; i++) {
            // Add a new personal goal in the List
            personalGoalDeck.add(new PersonalGoal(i));
        }
        // Potrebbe essere fatto un metodo a parte
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

    public void move(Player player, Coordinates from, Coordinates to, int column) {
        player.move(from, to, column);
    }
}
