package it.polimi.ingsw.Controllers;

import it.polimi.ingsw.Models.CommonGoalLayout.*;
import it.polimi.ingsw.Models.Game.Board;
import it.polimi.ingsw.Models.Game.Player;
import it.polimi.ingsw.Models.Goal.CommonGoal;
import it.polimi.ingsw.Models.Goal.PersonalGoal;
import it.polimi.ingsw.Models.Utility.Coordinates;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class Game {
    private static final int personalGoalDeckSize = 12;
    private final List<Player> players;
    private final Board livingRoom;
    private final int bookshelfColumns;
    private final int bookshelfRows;
    private final List<CommonGoal> commonGoalDeck;
    private final List<PersonalGoal> personalGoalDeck;
    private Player currentPlayer;

    public Game(int numOfPlayer) throws IllegalAccessException {

        // Read the settings from the properties file
        int rowsSetting;
        int colsSetting;

        Properties prop = new Properties();
        //In case the file is not found, the default values will be used
        try (InputStream input = new FileInputStream("settings/settings.properties")) {

            // Load a properties file
            prop.load(input);
            rowsSetting = parseInt(prop.getProperty("bookshelf.rows"));
            colsSetting = parseInt(prop.getProperty("bookshelf.columns"));
        } catch (IOException ex) {
            ex.printStackTrace();
            // If there is an error, use the default values
            rowsSetting = 5;
            colsSetting = 6;
        }
        this.bookshelfRows = rowsSetting;
        this.bookshelfColumns = colsSetting;

        this.players = new ArrayList<>(numOfPlayer);// Living room is created and filled
        livingRoom = new Board(numOfPlayer);
        livingRoom.fill();
        commonGoalDeck = new ArrayList<>();
        personalGoalDeck = new ArrayList<>();

    }

    public Board getLivingRoom() {
        return livingRoom;
    }

    /**
     * Creates the <code>personalGoalDeck</code>, the <code>commonGoalDeck</code> and the <code>livingRoom</code>.
     *
     * @throws IllegalAccessException if the number of players is invalid
     */
    public void initialize(int numOfPlayer) throws IllegalAccessException {

        // TODO: add logged in players

        // CommonGoalDeck is created and filled
        commonGoalDeck_creation();

        // PersonalGoalDeck is created and filled
        personalGoalDeck_creation();


        // Draw a personal goal card for each player
        for (Player player : players) {
            player.setPersonalGoal(drawPersonalGoal());
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
    public void commonGoalDeck_creation() {

        int dimension = Math.min(bookshelfColumns, bookshelfRows);
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
        layouts.add(new FullLine(bookshelf.getRows(), bookshelf.getRows(), 2, false));

        // Row where each item has a different type. Its length is equal to the number of columns.
        layouts.add(new FullLine(bookshelf.getColumns(), bookshelf.getColumns(), 2, true));

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
    public void personalGoalDeck_creation() {
        for (int i = 0; i < personalGoalDeckSize; i++) {
            // Add a new personal goal in the List
            personalGoalDeck.add(new PersonalGoal(i));
        }
    }

    /**
     * Moves column or a row of items from the board to the player's bookshelf.
     *
     * @param player the player who wants to move the items
     * @param from   the starting board's coordinates of the items to move
     * @param to     the final board's coordinates of the items to move
     * @param column the index of the bookshelf's column where the items will be moved
     */
    public void move(Player player, Coordinates from, Coordinates to, int column) {
        player.move(from, to, column);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
}
