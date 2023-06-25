package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.view.BookshelfView;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a player of the game and contains all their information.
 */
// @SuppressWarnings("unused")
public class Player {

    // Maybe we don't need this, we assign the common goal points in the CommonGoal class
    private static List<CommonGoal> commonGoals = new ArrayList<>();
    private static Board board;
    private final String nickname;
    private final int age;
    private List<Integer> commonGoalPoints = new ArrayList<>(2);
    private boolean isFirstPlayer;
    private boolean isFirstGame;
    private List<Boolean> commonGoalCompleted = new ArrayList<>(2);
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

    public Player(String nickname, int age, boolean isFirstGame, boolean isFirstPlayer, boolean hasEndGameCard, List<Boolean> commonGoalCompleted) {
        this.nickname = nickname;
        this.age = age;
        this.isFirstGame = isFirstGame;
        this.isFirstPlayer = isFirstPlayer;
        this.hasEndGameCard = hasEndGameCard;
        this.commonGoalCompleted.addAll(commonGoalCompleted);
    }

    /**
     * @param commonGoals the common goals to set
     */
    public static void setCommonGoal(List<CommonGoal> commonGoals) {
        Player.commonGoals = commonGoals;

    }

    /**
     * @param board the board to set
     */
    public static void setBoard(Board board) {
        Player.board = board;
    }

    /**
     * @return the list of common goals
     */
    public static List<CommonGoal> getCommonGoals() {
        return commonGoals;
    }

    // metodo temporaneo

    public void setCommonGoals(List<CommonGoal> commonGoals) {
        Player.commonGoals = commonGoals;
    }

    public List<String> getCommonNames() {
        List<String> names = new ArrayList<>();
        for (CommonGoal commonGoal : commonGoals) {
            names.add(commonGoal.getLayout().getName());
        }
        return names;
    }

    public void setIsFirstPlayer(boolean isFirstPlayer) {
        this.isFirstPlayer = isFirstPlayer;
    }

    /**
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return the bookshelf of the player
     */
    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    /**
     * @param bookshelf the bookshelf to set
     */
    public void setBookshelf(Bookshelf bookshelf) {
        this.bookshelf = bookshelf;
    }

    /**
     * @return the personal goal of the player
     */
    public PersonalGoal getPersonalGoal() {
        return personalGoal;
    }

    /**
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
     * This method sets whether it is the player's first game or not.
     *
     * @param isFirstGame true if it is the player's first game, false otherwise
     */
    public void setFirstGame(boolean isFirstGame) {
        this.isFirstGame = isFirstGame;
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
     * Tells whether the player has the end game card or not.
     * The end game card is assigned to the first player to fill their bookshelf.
     * It gives a bonus point to the player who holds it.
     *
     * @return true if the player has the end game card, false otherwise
     */
    public boolean hasEndGameCard() {
        return hasEndGameCard;
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
            System.out.println("Checking common goal " + commonGoals.get(i).getLayout().getName() + "...");
            System.out.println(commonGoalCompleted.get(i));
            if (!commonGoalCompleted.get(i)) {
                if (commonGoals.get(i).check(bookshelf)) {
                    System.out.println("Player " + nickname + " completed the common goal " + commonGoals.get(i).getLayout().getName() + " and earned " + commonGoals.get(i).getScoringList().get(0) + " points!");
                    BookshelfView bokkshelfView = new BookshelfView(bookshelf);
                    bokkshelfView.printBookshelf();
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
        /*
        if (order.stream().distinct().count() != order.size()) {
            throw new IllegalArgumentException("An item is placed in two different positions.");
        }

         */

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

        // Do we really have to implement getPoints in CommonGoal?
        for (int scoring : commonGoalPoints) {
            score += scoring;
        }
        score += personalGoal.getPoints(bookshelf);
        score += bookshelf.getPoints();
        System.out.println(this.getNickname() + " " + score);
        return score;
    }

    /**
     * @return the points given by adjacent items in the bookshelf
     */
    public int getAdjacentPoints() {
        return bookshelf.getPoints();
    }

    /**
     * @return the points given by personal goals
     */
    public int getPersonalGoalPoints() {
        return personalGoal.getPoints(bookshelf);
    }

    /**
     * @return the points given by common goals
     */
    public int getCommonGoalPoints() {
        int score = 0;
        for (int scoring : commonGoalPoints) {
            score += scoring;
        }
        return score;
    }

    public void setCommonGoalPoints(int commonGoalPoints) {
        this.commonGoalPoints.add(commonGoalPoints);
    }

    public void setCommonGoalPoints(List<Integer> commonGoalPoints) {
        this.commonGoalPoints = commonGoalPoints;
    }

    /**
     * @param commonGoal the points of the common goal to be set
     */
    public void setCommonGoalPoints(CommonGoal commonGoal) {
        //        commonGoalPoints[List.of(commonGoals).indexOf(commonGoal)] = commonGoal.getScoring();
        commonGoalPoints.add(commonGoal.getScoring());
    }

    public List<Boolean> getCommonGoalCompleted() {
        return commonGoalCompleted;
    }

    public void setCommonGoalCompleted(List<Boolean> commonGoalCompleted) {
        this.commonGoalCompleted = commonGoalCompleted;
    }

    public List<Integer> getCommonGoalScoreList() {
        return commonGoalPoints;
    }

    public void printPlayer() {
        System.out.println("Player: " + nickname);
        System.out.println("Personal Goal: " + personalGoal.toString());
        System.out.println("Common Goals: ");
        for (CommonGoal commonGoal : commonGoals) {
            System.out.println(commonGoal.getLayout().getName());
        }
        System.out.println("Bookshelf: ");
        BookshelfView bookshelfView = new BookshelfView(bookshelf);
        bookshelfView.printBookshelf();
    }
}
