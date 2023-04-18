package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.PersonalGoal;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utils.BookshelfUtilities;
import it.polimi.ingsw.utils.Coordinates;
import it.polimi.ingsw.utils.SettingLoader;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    // This value is tied to the switch statement in the PersonalGoal class
    private static final int personalGoalDeckSize = 12;
    private final List<Player> players;
    private final Board livingRoom;
    private final List<Integer> personalGoalDeck;
    private List<CommonGoal> commonGoalDeck;
    private Player currentPlayer;
    private boolean lastRound;

    public Game(int numOfPlayers) throws IllegalAccessException, IOException, ParseException {
        BookshelfUtilities.loadSettings();
        this.players = new ArrayList<>(numOfPlayers); // Living room is created and filled
        livingRoom = new Board(numOfPlayers);
        livingRoom.fill();
        commonGoalDeck = new ArrayList<>();
        personalGoalDeck = new ArrayList<>();

        fillCommonGoalDeck(numOfPlayers);
        fillPersonalGoalDeck();

    }

    public Board getLivingRoom() {
        return livingRoom;
    }

    /**
     * Creates the <code>personalGoalDeck</code>, the <code>commonGoalDeck</code> and the <code>livingRoom</code>.
     */
    public void initialize() throws IOException, ParseException {

        // decks created when game is created
        // for test purposes, we can initialize it again
        // TODO: fix tests
        // TODO: add logged in players
        commonGoalDeck.clear();
        personalGoalDeck.clear();
        fillCommonGoalDeck(players.size());
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
        // Remove the extracted personal goal from the deck so that it can't be drawn again.
        personalGoalDeck.remove(randomPersonalGoalIndex);
        return new PersonalGoal(randomPersonalGoalIndex);
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
            // Remove the extracted common goal from the deck so that it can't be drawn again.
            commonGoalDeck.remove(randomLayoutIndex);
        }

        return extracted;
    }

    /**
     * Creates a deck with all the possible layouts for the common goals.
     */
    private void fillCommonGoalDeck(int numOfPlayers) throws IOException, ParseException {
        //int dimension = Math.min(Bookshelf.getColumns(), Bookshelf.getRows());
        commonGoalDeck = SettingLoader.commonGoalLoader(numOfPlayers);

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

    public List<CommonGoal> getCommonGoalDeck() {
        return commonGoalDeck;
    }
}
