package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utils.Coordinates;
import it.polimi.ingsw.utils.SettingLoader;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameModel {
    // This value is tied to the switch statement in the PersonalGoal class
    private static final int personalGoalDeckSize = 12;
    private final List<Player> players;
    private final Board livingRoom;
    private List<PersonalGoal> personalGoalDeck;
    private List<CommonGoal> commonGoalDeck;
    private Player currentPlayer;
    private boolean lastRound;

    // Note for @Valeria: I created this constructor in order to be able
    // to test my work.
    public GameModel() {
        SettingLoader.loadBookshelfSettings();
        players = new ArrayList<>();
        livingRoom = null;
        commonGoalDeck = new ArrayList<>();
        personalGoalDeck = new ArrayList<>();
    }

    public GameModel(List<Player> players) {
        SettingLoader.loadBookshelfSettings();
        this.players = new ArrayList<>();
        this.players.addAll(players);
        try {
            livingRoom = new Board(players.size());
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e + ", error in loading the board");
        }

        Player.setBoard(livingRoom);

        commonGoalDeck = new ArrayList<>();
        personalGoalDeck = new ArrayList<>();

        try {
            fillCommonGoalDeck(players.size());
            fillPersonalGoalDeck();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e + ", error in loading the goal decks");
        }

    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getLivingRoom() {
        return livingRoom;
    }

    /**
     * Creates the <code>personalGoalDeck</code>, the <code>commonGoalDeck</code> and the <code>livingRoom</code>.
     */
    public void start() {
        // Draw a personal goal card for each player
        boolean isFirstGame = false;
        for (Player player : players) {

            player.setBookshelf(new Bookshelf(Bookshelf.getRows(), Bookshelf.getColumns()));

            player.setPersonalGoal(drawPersonalGoal());
            if (player.isFirstPlayer()) {
                currentPlayer = player;
            }
            if (player.isFirstGame()) {
                isFirstGame = true;
            }
        }
        // Draw two common goal cards
        Player.setCommonGoal(drawCommonGoals(isFirstGame));

        try {
            livingRoom.fill();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }


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
        // Remove the extracted personal goal from the deck so that it can't be drawn again.
        PersonalGoal picked = personalGoalDeck.get(randomPersonalGoalIndex);
        personalGoalDeck.remove(randomPersonalGoalIndex);
        return picked;
    }

    /**
     * Draw the appropriate number of common goal cards from the commonGoalDeck and set their scoring according to the number of players.
     * The number of common goal cards is 1 if it is the first game for any of the players, otherwise it is 2.
     *
     * @return the list of common goal cards drawn
     */
    public List<CommonGoal> drawCommonGoals(boolean isFirstGame) {
        Random randomNumberGenerator = new Random();
        List<CommonGoal> extracted = new ArrayList<>();

        int commonGoalNumber = isFirstGame ? 1 : 2;

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
    public void fillPersonalGoalDeck() throws IOException, ParseException {
        personalGoalDeck = SettingLoader.personalGoalLoader();
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

    public List<PersonalGoal> getPersonalGoalDeck() {
        return personalGoalDeck;
    }
}