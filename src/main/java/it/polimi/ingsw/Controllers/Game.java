package it.polimi.ingsw.Controllers;


import it.polimi.ingsw.Models.Games.Board;
import it.polimi.ingsw.Models.Games.Player;
import it.polimi.ingsw.Models.Goal.CommonGoal;
import it.polimi.ingsw.Models.Goal.PersonalGoal;
import it.polimi.ingsw.Models.Utility.Coordinates;

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

    /**
     * This method initializes the game, creating the personalGoalDeck, the commonGoalDeck and the livingRoom
     *
     * @throws IllegalAccessException if the number of players is invalid
     */
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

    /**
     * This method draws a personal goal card from the personalGoalDeck for the player
     *
     * @return the personal goal card drawn
     */
    public PersonalGoal drawPersonalCard() {
        Random randomNumberGenerator = new Random();
        int number = randomNumberGenerator.nextInt(personalGoalDeck.size());
        PersonalGoal chosen = personalGoalDeck.get(number);
        personalGoalDeck.remove(number);
        return chosen;
    }

    /**
     * This is the move method, it calls the move method of the player
     *
     * @param player
     * @param from
     * @param to
     * @param column
     */
    public void move(Player player, Coordinates from, Coordinates to, int column) {
        player.move(from, to, column);
    }
}
