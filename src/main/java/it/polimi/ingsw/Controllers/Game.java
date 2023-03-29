package it.polimi.ingsw.Controllers;


import it.polimi.ingsw.Models.CommonGoalLayout.*;
import it.polimi.ingsw.Models.Games.Board;
import it.polimi.ingsw.Models.Games.Player;
import it.polimi.ingsw.Models.Goal.CommonGoal;
import it.polimi.ingsw.Models.Goal.PersonalGoal;
import it.polimi.ingsw.Models.Utility.Coordinates;

import java.util.ArrayList;
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
        // CommonGoalDeck is created and filled
        commonGoalDeck_creation();

        // PersonalGoalDeck is created and filled
        personalGoalDeck_creation();

        // Living room is created and filled
        livingRoom = new Board(players.size());
        livingRoom.fill();

        // draw a personal goal card for each player
        for (Player player : players) {
            player.setPersonalGoal(drawPersonalCard());
        }
        //draw two common goal cards
        Player.setCommonGoal(drawCommonCard());

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

    public List<CommonGoal> drawCommonCard() {
        Random randomNumberGenerator = new Random();
        int first = randomNumberGenerator.nextInt(commonGoalDeck.size());
        List<CommonGoal> chosen = new ArrayList<>();
        chosen.add(commonGoalDeck.get(first));
        commonGoalDeck.remove(first);
        int second = randomNumberGenerator.nextInt(commonGoalDeck.size());
        chosen.add(commonGoalDeck.get(second));
        return chosen;
    }

    /**
     * This method creates the commonGoalDeck
     * TODO: Aggiungere tutti i layout
     */
    public void commonGoalDeck_creation() {
        //dimension=5->da mettere nel file di configurazione
        List<Layout> layouts = new ArrayList<>();
        layouts.add(new Stair(5));
        layouts.add(new Diagonal(5, 1, 1));
        layouts.add(new Corners(1, 1));
        layouts.add(new XShape(3, 1, 1));
        //columnLine_layout
        layouts.add(new FullLine(1, 1, 1, 3, 3, false));
        //rowLine_layout
        layouts.add(new FullLine(1, 1, 1, 3, 4, true));
        //twoColumnLine_layout
        layouts.add(new FullLine(1, 1, 6, 6, 2, false));
        //twoRowLine_layout
        layouts.add(new FullLine(1, 1, 5, 5, 2, true));

        //da aggiungere square
        //da aggiungere rectangle

        for (Layout layout : layouts) {
            commonGoalDeck.add(new CommonGoal(layout));
        }

    }

    /**
     * This method creates the personalGoalDeck
     */
    public void personalGoalDeck_creation() {
        for (int i = 0; i < 12; i++) {
            // Add a new personal goal in the List
            personalGoalDeck.add(new PersonalGoal(i));
        }
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
