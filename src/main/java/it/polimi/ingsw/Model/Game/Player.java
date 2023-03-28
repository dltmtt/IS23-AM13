package it.polimi.ingsw.Model.Game;

import it.polimi.ingsw.Model.Goals.CommonGoal;
import it.polimi.ingsw.Model.Goals.PersonalGoal;
import it.polimi.ingsw.Model.Items.Item;
import it.polimi.ingsw.Model.Utilities.Coordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")

public class Player {
    // Maybe we don't need this, we assign the common goal points in the CommonGoal class
    private static final CommonGoal[] commonGoals = new CommonGoal[2];
    private static Board board;
    private final String nickname;
    private final int age;
    private final boolean isFirstGame;
    private final boolean isFirstPlayer;
    private final boolean hasEndGameCard;
    private final int[] commonGoalPoints = new int[commonGoals.length];
    private final Bookshelf bookshelf;
    private PersonalGoal personalGoal;

    public Player(String nickname, int age, boolean isFirstGame, boolean isFirstPlayer, boolean hasEndGameCard, Bookshelf bookshelf, Board board) {
        this.nickname = nickname;
        this.age = age;
        this.isFirstGame = isFirstGame;
        this.isFirstPlayer = isFirstPlayer;
        this.hasEndGameCard = hasEndGameCard;
        this.bookshelf = bookshelf;
        Player.board = board;
        Arrays.fill(commonGoalPoints, 0);
    }

    public static void setCommonGoal(CommonGoal commonGoal, int index) {
        commonGoals[index] = commonGoal;
    }

    public String getNickname() {
        return nickname;
    }

    public int getAge() {
        return age;
    }

    public boolean isFirstGame() {
        return isFirstGame;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public boolean hasEndGameCard() {
        return hasEndGameCard;
    }

    public void setPersonalGoal(PersonalGoal personalGoal) {
        this.personalGoal = personalGoal;
    }

    public void setCommonGoalPoints(CommonGoal commonGoal) {
        commonGoalPoints[Arrays.asList(commonGoals).indexOf(commonGoal)] = commonGoal.getScoring();
    }

    /**
     * This method moves a straight line of tiles from the board to the bookshelf.
     *
     * @param from   the starting point of the line
     * @param to     the ending point of the line
     * @param column the column of the bookshelf where the tiles will be placed (starting from 0)
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
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method rearranges the picked items according to the given order.
     * For example, if the order is [2, 0, 1], the first item will be placed in the third position,
     * the second item will be placed in the first position and the third item will be placed in the second position.
     *
     * @param items the items to rearrange
     * @param order the order in which the items will be placed
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
     * This method calculates the score of the player.
     *
     * @return the score of the player
     */
    public int calculateScore() {
        int score = 0;

        if (hasEndGameCard) {
            score += 1;
        }

        // Do we really have to implement "getPoints()" in CommonGoal?
        for (int scoring : commonGoalPoints) {
            score += scoring;
        }

        score += personalGoal.getPoints();
        score += bookshelf.getPoints();

        return score;
    }
}
