package it.polimi.ingsw;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.layouts.*;
import it.polimi.ingsw.utils.BookshelfUtilities;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Coordinates;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Game {
    // This value is tied to the switch statement in the PersonalGoal class
    private static final int personalGoalDeckSize = 12;
    private final List<Player> players;
    private final Board livingRoom;
    private final List<CommonGoal> commonGoalDeck;
    private final List<Integer> personalGoalDeck;
    private Player currentPlayer;
    private boolean lastRound;

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
    public void initialize() throws IOException, ParseException {
        // TODO: add logged in players

        fillCommonGoalDeck();
        fillPersonalGoalDeck();

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
    public PersonalGoal drawPersonalGoal() throws IOException, ParseException {
        Random randomNumberGenerator = new Random();

        int randomPersonalGoalIndex = randomNumberGenerator.nextInt(personalGoalDeck.size());
        HashMap<Coordinates, Color> personalGoalCard = new HashMap<>();

        JSONParser parser = new JSONParser();
        JSONObject personalGoalJson = (JSONObject) parser.parse(new FileReader("src/main/resources/personal_goals.json"));
        JSONArray deck = (JSONArray) personalGoalJson.get("personal_goal_configurations");

        JSONObject personalGoal = (JSONObject) deck.get(randomPersonalGoalIndex);


        JSONArray configuration = (JSONArray) personalGoal.get("configuration");
        for (Object o : configuration) {
            JSONObject cell = (JSONObject) o;
            personalGoalCard.put(new Coordinates(Math.toIntExact((Long) cell.get("x")), Math.toIntExact((Long) cell.get("y"))), Color.valueOf((String) cell.get("color")));
        }
        PersonalGoal extracted = new PersonalGoal(personalGoalCard);

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

    /**
     * Creates a deck with all the possible layouts for the common goals.
     */
    private void fillCommonGoalDeck() {
        List<Layout> layouts = new ArrayList<>();
        int dimension = Math.min(Bookshelf.getColumns(), Bookshelf.getRows());

        // Four tiles of the same type in the four corners of the bookshelf.
        layouts.add(new Corners(1, 1));

        // As many tiles of the same type as the smaller dimension of the bookshelf forming a diagonal.
        layouts.add(new Diagonal(dimension, 1, 1));

        // Three columns each formed tiles of maximum three different types.
        layouts.add(new FullLine(1, 3, 3, false));

        // Four lines each formed tiles of maximum three different types.
        layouts.add(new FullLine(1, 3, 4, true));

        // Two columns where each item has a different type. Their length is equal to the number of rows.
        layouts.add(new FullLine(Bookshelf.getRows(), Bookshelf.getRows(), 2, false));

        // Two rows where each item has a different type. Their length is equal to the number of columns.
        layouts.add(new FullLine(Bookshelf.getColumns(), Bookshelf.getColumns(), 2, true));

        // TODO: replace placeholders (parameters set to 0).
        // Six groups each containing at least 2 tiles of the same type.
        layouts.add(new Group(0, 0, 6, 0));

        // TODO: replace placeholders (parameters set to 0).
        // Four groups each containing at least 4 tiles of the same type.
        layouts.add(new Group(0, 0, 4, 0));

        // TODO: replace placeholders (parameters set to 0).
        // Eight tiles of the same type.
        layouts.add(new ItemsPerColor(0, 0));

        // Two 2×2 squares with items of the same type.
        layouts.add(new Square(2, 1, 1, 2));

        // A stair as big as the smaller dimension of the bookshelf.
        layouts.add(new Stair(dimension));

        // Five tiles of the same type forming an X.
        layouts.add(new XShape(3, 1, 1));

        for (Layout layout : layouts) {
            commonGoalDeck.add(new CommonGoal(layout));
        }
    }

    /**
     * Creates a deck with all the possible personal goals.
     * A personal goal is a matrix with highlighted spaces with the corresponding item tiles
     * that players have to replicate in their bookshelves to get points.
     */
    public void fillPersonalGoalDeck() {
        for (int i = 0; i < personalGoalDeckSize; i++) {
            personalGoalDeck.add(i);
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
        // TODO: add case of tie

        for (Player player : players) {
            finalScoring.add(player.calculateScore());
        }

        if (finalScoring.stream().distinct().count() < players.size()) {
            //there is a tie
            int max = finalScoring.stream().max(Integer::compare).get();
            for (Integer score : finalScoring) {
                if (score == max) {
                    winners.add(players.get(finalScoring.indexOf(score)));
                    players.remove(finalScoring.indexOf(score));
                }
            }
        } else {
            int max = finalScoring.stream().max(Integer::compare).get();
            winners.add(players.get(finalScoring.indexOf(max)));
        }

        return winners;
    }

    public void printWinners(List<Player> winners) {

        if (winners.size() > 1) {
            for (Player winner : winners) {
                if (winner.isFirstPlayer()) {
                    winners.remove(winner);
                }
            }
        }

        for (Player winner : winners) {
            System.out.println("The winner is " + winner.getNickname());
        }
    }
}
