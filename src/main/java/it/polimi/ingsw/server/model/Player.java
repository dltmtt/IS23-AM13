package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.view.BookshelfView;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a player of the game and contains all their information.
 */
public class Player {

    private static List<CommonGoal> commonGoals = new ArrayList<>();
    private static Board board;
    private final String nickname;
    private final int age;
    private List<Integer> commonGoalPoints = new ArrayList<>(2);
    private boolean isFirstPlayer;
    private final boolean isFirstGame;
    private final List<Boolean> commonGoalCompleted = new ArrayList<>(2);
    private Bookshelf bookshelf;
    private boolean hasEndGameCard;
    private PersonalGoal personalGoal;

    /**
     * This method creates a player.
     *
     * @param nickname the player's nickname
     * @param age      the player's age
     */
    public Player(String nickname, int age, boolean isFirstGame, boolean isFirstPlayer, boolean hasEndGameCard) {
        this.nickname = nickname;
        this.age = age;
        this.isFirstGame = isFirstGame;
        this.isFirstPlayer = isFirstPlayer;
        this.hasEndGameCard = hasEndGameCard;
        commonGoalCompleted.add(false);
        commonGoalCompleted.add(false);
    }

    /**
     * This method creates a player.
     *
     * @param nickname the player's nickname
     * @param age the player's age
     * @param isFirstGame  true if the player is playing the first game
     * @param isFirstPlayer true if the player is the first player
     * @param hasEndGameCard true if the player has the end game card
     * @param commonGoalCompleted a list of booleans that indicates if the player has completed the common goals
     */

    public Player(String nickname, int age, boolean isFirstGame, boolean isFirstPlayer, boolean hasEndGameCard, List<Boolean> commonGoalCompleted) {
        this.nickname = nickname;
        this.age = age;
        this.isFirstGame = isFirstGame;
        this.isFirstPlayer = isFirstPlayer;
        this.hasEndGameCard = hasEndGameCard;
        this.commonGoalCompleted.addAll(commonGoalCompleted);
    }

    /**
     * This method sets the CommonGoals.
     * @param commonGoals the common goals to set
     */
    public static void setCommonGoal(List<CommonGoal> commonGoals) {
        Player.commonGoals = commonGoals;
    }

    /**
     * This method sets the board.
     * @param board the board to set
     */
    public static void setBoard(Board board) {
        Player.board = board;
    }

    /**
     * This method gets the CommonGoals.
     * @return the list of common goals
     */
    public static List<CommonGoal> getCommonGoals() {
        return commonGoals;
    }

    /**
     * This method gets the CommonGoals names.
     * @return the list of common goals names
     */
    public List<String> getCommonNames() {
        List<String> names = new ArrayList<>();
        for (CommonGoal commonGoal : commonGoals) {
            names.add(commonGoal.getLayout().getName());
        }
        return names;
    }

    /**
     * This method sets the first player.
     * @param isFirstPlayer true if the player is the first player
     */
    public void setIsFirstPlayer(boolean isFirstPlayer) {
        this.isFirstPlayer = isFirstPlayer;
    }

    /**
     * This method gets the nickname of the player.
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * This method gets the bookshelf.
     * @return the bookshelf of the player
     */
    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    /**
     * This method sets the bookshelf.
     * @param bookshelf the bookshelf to set
     */
    public void setBookshelf(Bookshelf bookshelf) {
        this.bookshelf = bookshelf;
    }

    /**
     * This method gets the PersonalGoal of the player.
     * @return the personal goal of the player
     */
    public PersonalGoal getPersonalGoal() {
        return personalGoal;
    }

    /**
     * This method sets the PersonalGoal of the player.
     * @param personalGoal the personal goal to set
     */
    public void setPersonalGoal(PersonalGoal personalGoal) {
        this.personalGoal = personalGoal;
    }

    /**
     * This method tells whether it is the player's first game or not.
     * If it is, the game will be played with only one common goal instead of two.
     *
     * @return true if it is the player's first game, false otherwise
     */
    public boolean isFirstGame() {
        return isFirstGame;
    }

    /**
     * This method tells whether the player is the first to play or not.
     * If it is, the player will hold the 1st player seat.
     *
     * @return true if the player is the first to play, false otherwise
     */
    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    /**
     * This method sets whether the player has the end game card or not.
     *
     * @param hasEndGameCard true if the player has the end game card, false otherwise
     */
    public void setHasEndGameCard(boolean hasEndGameCard) {
        this.hasEndGameCard = hasEndGameCard;
    }

    /**
     * This method moves a straight line of tiles from the board to the bookshelf.
     *
     * @param items  the list of the tiles to move
     * @param column the index of the column of the bookshelf where the tiles will be placed (starting from 0)
     * @throws IllegalArgumentException if the line is not straight or if the selection is empty
     */
    public void move(List<Item> items, int column) throws IllegalArgumentException {
        bookshelf.insert(column, items);
        for (int i = 0; i < commonGoals.size(); i++) {
            if (!commonGoalCompleted.get(i)) {
                if (commonGoals.get(i).check(bookshelf)) {
                    System.out.println("Player " + nickname + " completed the common goal " + commonGoals.get(i).getLayout().getName() + " and earned " + commonGoals.get(i).getScoringList().get(0) + " points!");
                    BookshelfView bookshelfView = new BookshelfView(bookshelf);
                    bookshelfView.printBookshelf();
                    setCommonGoalPoints(commonGoals.get(i));
                    commonGoalCompleted.set(i, true);
                }
            }
        }
    }

    /**
     * This method rearranges the picked items in the given order.
     * The order is an array of integers, where the i-th element is the new position of the i-th item (starting from 0).
     * For example, if the order is (2, 0, 1), the first item will be placed in the third position,
     * the second item will be placed in the first position and the third item will be placed in the second position.
     *
     * @param items the items to rearrange
     * @param order the desired order
     * @throws IllegalArgumentException  if the number of items and the number of positions are not the same or if an item is placed in two different positions
     * @throws IndexOutOfBoundsException if the new position is out of bounds
     */
    public List<Item> rearrangePickedItems(List<Item> items, List<Integer> order) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (items.size() != order.size()) {
            throw new IllegalArgumentException("The number of items and the number of positions are not the same.");
        }

        List<Item> rearrangedItems = new ArrayList<>();

        for (int i : order) {
            if (i > items.size()) {
                throw new IndexOutOfBoundsException("The new position is out of bounds.");
            }

            rearrangedItems.add(items.get(i));
        }

        items.clear();
        items.addAll(rearrangedItems);
        System.out.println(items);
        return items;
    }

    /**
     * This method calculates the score of the player.
     * It is made up of:
     * <ul>
     *     <li>the points given by adjacent items in the bookshelf</li>
     *     <li>the points given by personal and common goals</li>
     *     <li>the points given by the end game card (if the player has it)</li>
     *  </ul>
     *
     * @return the score of the player
     */
    public int calculateScore() {
        int score = 0;

        if (hasEndGameCard) {
            score += 1;
        }

        for (int scoring : commonGoalPoints) {
            score += scoring;
        }
        score += personalGoal.getPoints(bookshelf);
        score += bookshelf.getPoints();
        System.out.println(this.getNickname() + " " + score);
        return score;
    }

    /**
     * This method returns the points given by the PersonalGoal.
     * @return the points given by personal goals
     */
    public int getPersonalGoalPoints() {
        return personalGoal.getPoints(bookshelf);
    }

    /**
     * This method returns the points given by the CommonGoals.
     * @return the points given by common goals
     */
    public int getCommonGoalPoints() {
        int score = 0;
        for (int scoring : commonGoalPoints) {
            score += scoring;
        }
        return score;
    }

    /**
     * This method sets the CommonGoal points.
     * @param commonGoalPoints the points of the common goals to be set
     */
    public void setCommonGoalPoints(List<Integer> commonGoalPoints) {
        this.commonGoalPoints = commonGoalPoints;
    }

    /**
     * This method sets the CommonGoal points.
     * @param commonGoal the points of the common goal to be set
     */
    public void setCommonGoalPoints(CommonGoal commonGoal) {
        commonGoalPoints.add(commonGoal.getScoring());
    }

    /**
     * This method returns the list of booleans that tells whether the CommonGoal is completed or not.
     * @return the list of booleans that tells whether the CommonGoal is completed or not
     */
    public List<Boolean> getCommonGoalCompleted() {
        return commonGoalCompleted;
    }

    /**
     * This method gets the list of scores of the CommonGoals.
     * @return the list of scores of the CommonGoals
     */
    public List<Integer> getCommonGoalScoreList() {
        return commonGoalPoints;
    }
}
