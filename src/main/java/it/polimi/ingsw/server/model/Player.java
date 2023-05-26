package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Player {

    // Maybe we don't need this, we assign the common goal points in the CommonGoal class
    private static List<CommonGoal> commonGoals = new ArrayList<>();
    private static Board board;
    private final List<Boolean> commonGoalCompleted = new ArrayList<>(2);
    private final String nickname;
    private final List<Integer> commonGoalPoints = new ArrayList<>(2);
    private boolean isFirstPlayer;
    private boolean isFirstGame;
    private int age;
    private Bookshelf bookshelf;
    private boolean hasEndGameCard;
    private PersonalGoal personalGoal;

    public Player(String nickname, int age, boolean isFirstGame, boolean isFirstPlayer, boolean hasEndGameCard) {
        this.nickname = nickname;
        this.age = age;
        this.isFirstGame = isFirstGame;
        this.isFirstPlayer = isFirstPlayer;
        this.hasEndGameCard = hasEndGameCard;
        commonGoalCompleted.add(false);
        commonGoalCompleted.add(false);
    }

    public static void setCommonGoal(List<CommonGoal> commonGoals) {
        Player.commonGoals = commonGoals;
    }

    public static void setBoard(Board board) {
        Player.board = board;
    }

    // metodo temporaneo
    public static List<CommonGoal> getCommonGoals() {
        return commonGoals;
    }

    public void setIsFirstPlayer(boolean isFirstPlayer) {
        this.isFirstPlayer = isFirstPlayer;
    }

    public String getNickname() {
        return nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    public void setBookshelf(Bookshelf bookshelf) {
        this.bookshelf = bookshelf;
    }

    public PersonalGoal getPersonalGoal() {
        return personalGoal;
    }

    public void setPersonalGoal(PersonalGoal personalGoal) {
        this.personalGoal = personalGoal;
    }

    /**
     * Tells whether it is the player's first game or not.
     * If it is, the game will be played with only one common goal instead of two.
     *
     * @return true if it is the player's first game, false otherwise
     */
    public boolean isFirstGame() {
        return isFirstGame;
    }

    public void setFirstGame(boolean isFirstGame) {
        this.isFirstGame = isFirstGame;
    }

    /**
     * Tells whether the player is the first to play or not.
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

    public void setHasEndGameCard(boolean hasEndGameCard) {
        this.hasEndGameCard = hasEndGameCard;
    }

    /**
     * Moves a straight line of tiles from the board to the bookshelf.
     *
     * @param items  the list of the tiles to move
     * @param column the index of the column of the bookshelf where the tiles will be placed (starting from 0)
     * @throws IllegalArgumentException if the line is not straight or if the selection is empty
     */
    public void move(List<Item> items, int column) throws IllegalArgumentException {

        bookshelf.insert(column, items);
        for (int i = 0; i < commonGoals.size(); i++) {
            if (!commonGoalCompleted.get(i)) {
                if (commonGoals.get(i).check(this.bookshelf)) {
                    setCommonGoalPoints(commonGoals.get(i));
                    commonGoalCompleted.set(i, true);
                }
            }
        }
    }

    /**
     * Rearranges the picked items in the given order.
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
        if (order.stream().distinct().count() != order.size()) {
            throw new IllegalArgumentException("An item is placed in two different positions.");
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
     * Calculates the score of the player.
     * It is made of the points given by adjacent items in the bookshelf,
     * the points given by personal and common goals,
     * and the points given by the end game card (if the player has it).
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

    public int getAdjacentPoints() {
        return bookshelf.getPoints();
    }

    public int getPersonalGoalPoints() {
        return personalGoal.getPoints(bookshelf);
    }

    public int getCommonGoalPoints() {
        int score = 0;
        for (int scoring : commonGoalPoints) {
            score += scoring;
        }
        return score;
    }

    public void setCommonGoalPoints(CommonGoal commonGoal) {
        //        commonGoalPoints[List.of(commonGoals).indexOf(commonGoal)] = commonGoal.getScoring();
        commonGoalPoints.add(commonGoal.getScoring());
    }
}
