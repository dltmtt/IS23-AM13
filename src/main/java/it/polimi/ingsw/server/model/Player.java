package it.polimi.ingsw.server.model;


import it.polimi.ingsw.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Player {
    // Maybe we don't need this, we assign the common goal points in the CommonGoal class
    private static List<CommonGoal> commonGoals = new ArrayList<>();
    private static Board board;
    private final List<Boolean> commonGoalCompleted = new ArrayList<>(2);
    private final String nickname;
    private final int age;
    private final boolean isFirstGame;
    private final boolean isFirstPlayer;
    private final List<Integer> commonGoalPoints = new ArrayList<>(2);
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

    public String getNickname() {
        return nickname;
    }

    public int getAge() {
        return age;
    }

    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    public void setBookshelf(Bookshelf bookshelf) {
        this.bookshelf = bookshelf;
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

    public void setPersonalGoal(PersonalGoal personalGoal) {
        this.personalGoal = personalGoal;
    }

    public void setCommonGoalPoints(CommonGoal commonGoal) {
//        commonGoalPoints[List.of(commonGoals).indexOf(commonGoal)] = commonGoal.getScoring();
        commonGoalPoints.add(commonGoal.getScoring());
    }

    //metodo temporaneo
    public List<CommonGoal> getCommonGoals() {
        return commonGoals;
    }

    /**
     * Moves a straight line of tiles from the board to the bookshelf.
     *
     * @param from   the starting point of the line
     * @param to     the ending point of the line
     * @param column the index of the column of the bookshelf where the tiles will be placed (starting from 0)
     * @throws IllegalArgumentException if the line is not straight or if the selection is empty
     */
    public void move(Coordinates from, Coordinates to, int column) throws IllegalArgumentException {
        List<Coordinates> list = new ArrayList<>();
        int lengthX = Math.abs(from.x() - to.x());
        int lengthY = Math.abs(from.y() - to.y());
        int length = Math.max(lengthX, lengthY);

        if (length == 0) {
            throw new IllegalArgumentException("You must move at least one tile.");
        }

        if (lengthX > 1 && lengthY > 1) {
            throw new IllegalArgumentException("You can only move in a straight line.");
        }

        if (Math.abs(from.x() - to.x()) > Math.abs(from.y() - to.y())) {
            for (int i = 0; i < length; i++) {
                list.add(new Coordinates(from.x() + i, from.y()));
            }
        } else {
            for (int i = 0; i < length; i++) {
                list.add(new Coordinates(from.x(), from.y() + i));
            }
        }

        try {
            bookshelf.insert(column, board.pickFromBoard(list));
            for (int i = 0; i < commonGoals.size(); i++) {
                if (!commonGoalCompleted.get(i)) {
                    if (commonGoals.get(i).check(this.bookshelf)) {
                        setCommonGoalPoints(commonGoals.get(i));
                        commonGoalCompleted.set(i, true);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
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
    public void rearrangePickedItems(List<Item> items, int[] order) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (items.size() != order.length) {
            throw new IllegalArgumentException("The number of items and the number of positions are not the same.");
        }

        List<Item> rearrangedItems = new ArrayList<>();

        for (int i : order) {
            if (i >= items.size()) {
                throw new IndexOutOfBoundsException("The new position is out of bounds.");
            }

            if (rearrangedItems.contains(items.get(i))) {
                throw new IllegalArgumentException("You can't place the same item in two different positions.");
            }

            rearrangedItems.add(items.get(i));
        }

        items.clear();
        items.addAll(rearrangedItems);
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
}
