package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.commongoallayout.*;
import it.polimi.ingsw.model.game.Board;
import it.polimi.ingsw.model.game.Bookshelf;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.goal.CommonGoal;
import it.polimi.ingsw.model.goal.PersonalGoal;
import it.polimi.ingsw.model.utility.Coordinates;
import it.polimi.ingsw.utils.BookshelfUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Game {
    private static final int personalGoalDeckSize = 12;
    private final List<Player> players;
    private final Board livingRoom;
    private final List<CommonGoal> commonGoalDeck;
    private final List<PersonalGoal> personalGoalDeck;
    private Player currentPlayer;
    private boolean lastRound = false;

    public Game(int numOfPlayers) throws IllegalAccessException {
        BookshelfUtilities.loadSettings();
        this.players = new ArrayList<>(numOfPlayers); // Living room is created and filled
        livingRoom = new Board(numOfPlayers);
        livingRoom.fill();
        commonGoalDeck = new ArrayList<>();
        personalGoalDeck = new ArrayList<>();
    }

    public Board getLivingRoom() {
        return livingRoom;
    }

    /**
     * Creates the <code>personalGoalDeck</code>, the <code>commonGoalDeck</code> and the <code>livingRoom</code>.
     */
    public void initialize() {
        // TODO: add logged in players

        // CommonGoalDeck is created and filled
        createCommonGoalDeck();

        // PersonalGoalDeck is created and filled
        createPersonalGoalDeck();

        // Draw a personal goal card for each player
        for (Player player : players) {
            player.setPersonalGoal(drawPersonalGoal());
            if (player.isFirstPlayer()) {
                currentPlayer = player;
            }
        }

        // Draw two common goal cards
        Player.setCommonGoal(drawCommonGoals());
    }

    // drawPersonalGoal() and drawCommonGoals() are basically the same method,
    // we could parametrize them and use a single method for both.

    /**
     * Draws a personal goal card from the personalGoalDeck for the player.
     *
     * @return the personal goal card drawn
     */
    public PersonalGoal drawPersonalGoal() {
        Random randomNumberGenerator = new Random();

        int randomPersonalGoalIndex = randomNumberGenerator.nextInt(personalGoalDeck.size());
        PersonalGoal extracted = personalGoalDeck.get(randomPersonalGoalIndex);

        // Remove the extracted personal goal from the deck so that it can't be drawn again.
        personalGoalDeck.remove(randomPersonalGoalIndex);

        return extracted;
    }

    /**
     * Draw the appropriate number of common goal cards from the commonGoalDeck and set their scoring according to the number of players.
     * The number of common goal cards is 1 if it is the first game for any of the players, otherwise it is 2.
     *
     * @return the list of common goal cards drawn
     */
    public List<CommonGoal> drawCommonGoals() {
        Random randomNumberGenerator = new Random();
        List<CommonGoal> extracted = new ArrayList<>();

        // If it is the first game for any of the players, play with 1 common goal, otherwise play with 2.
        boolean firstGame = false;
        for (Player player : players) {
            if (player.isFirstGame()) {
                firstGame = true;
                break;
            }
        }
        int commonGoalNumber = firstGame ? 1 : 2;

        for (int i = 0; i < commonGoalNumber; i++) {
            int randomLayoutIndex = randomNumberGenerator.nextInt(commonGoalDeck.size());
            extracted.add(commonGoalDeck.get(randomLayoutIndex));
            commonGoalDeck.get(randomLayoutIndex).setScoringList(players.size());

            // Remove the extracted common goal from the deck so that it can't be drawn again.
            commonGoalDeck.remove(randomLayoutIndex);
        }

        return extracted;
    }

    // TODO: Add missing layouts

    /**
     * Creates a deck with all the possible layouts for the common goals.
     */
    public void createCommonGoalDeck() {
        int dimension = Math.min(Bookshelf.getColumns(), Bookshelf.getRows());
        List<Layout> layouts = new ArrayList<>();

        // TODO: parametrize the rest of the layouts.
        layouts.add(new Stair(dimension));
        layouts.add(new Diagonal(dimension, 1, 1));
        layouts.add(new Corners(1, 1));
        layouts.add(new XShape(3, 1, 1));
        // columnLine layout
        layouts.add(new FullLine(1, 3, 3, false));
        // rowLine layout
        layouts.add(new FullLine(1, 3, 4, true));

        // Column where each item has a different type. Its length is equal to the number of rows.
        layouts.add(new FullLine(Bookshelf.getRows(), Bookshelf.getRows(), 2, false));

        // Row where each item has a different type. Its length is equal to the number of columns.
        layouts.add(new FullLine(Bookshelf.getColumns(), Bookshelf.getColumns(), 2, true));

        // TODO: aggiungere Square
        // TODO: aggiungere Rectangle

        for (Layout layout : layouts) {
            commonGoalDeck.add(new CommonGoal(layout));
        }
    }

    /**
     * Creates a deck with all the possible personal goals.
     * A personal goal is a matrix with highlighted spaces with the corresponding item tiles
     * that players have to replicate in their bookshelves to get points.
     */
    public void createPersonalGoalDeck() {
        for (int i = 0; i < personalGoalDeckSize; i++) {
            // Add a new personal goal in the List
            personalGoalDeck.add(new PersonalGoal(i));
        }
    }

    /**
     * Moves column or a row of items from the board to the player's bookshelf. If the bookshelf is full, the player gets the end game card and
     * the last round starts. At the end, the turn is changed.
     *
     * @param from   the starting board's coordinates of the items to move
     * @param to     the final board's coordinates of the items to move
     * @param column the index of the bookshelf's column where the items will be moved
     */
    public void move(Coordinates from, Coordinates to, int column) {
        currentPlayer.move(from, to, column);
        if (currentPlayer.getBookshelf().isBookshelfFull()) {
            if (!lastRound) {
                currentPlayer.setHasEndGameCard(true);
                lastRound = true;
            }
        }
        changeTurn();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Changes the turn to the next player.
     */
    public void changeTurn() {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        int nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
        if (lastRound) {
            if (players.get(nextPlayerIndex).isFirstPlayer()) {
                printWinners(setWinner());
            } else {
                currentPlayer = players.get(nextPlayerIndex);
            }
        } else {
            currentPlayer = players.get(nextPlayerIndex);
        }
    }

    public List<Player> setWinner() {
        List<Player> winners = new ArrayList<>();
        List<Integer> finalScoring = new ArrayList<>();
        //TODO: add case of tie

        for (Player player : players) {
            finalScoring.add(player.calculateScore());
        }
        if (finalScoring.stream().distinct().count() == players.size()) {
            int max = finalScoring.stream().max(Integer::compare).get();
            int winnerIndex = finalScoring.indexOf(max);
            winners.add(players.get(winnerIndex));
        } else {
            List<Integer> maxScores = finalScoring.stream().filter(i -> Objects.equals(i, finalScoring.stream().max(Integer::compare).get())).toList();
            for (Integer score : maxScores) {
                winners.add(players.get(maxScores.indexOf(score)));
            }
        }
        return winners;

    }

    public void printWinners(List<Player> winners) {
        if (winners.size() > 1) {
            winners.removeIf(Player::isFirstPlayer);
        }
        for (Player winner : winners) {
            System.out.println("The winner is " + winner.getNickname());
        }
    }

}
