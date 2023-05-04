package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.layouts.Layout;

import java.util.ArrayList;
import java.util.List;

/**
 * A common goal card, which has a layout to be matched by the players'
 * bookshelves and a stack of scores to be assigned to the players.
 *
 * @see Layout
 * @see Player
 * @see Bookshelf
 */
public class CommonGoal {

    private final Layout layout;
    private final List<Integer> scoringList;

    /**
     * Creates a new common goal with the given layout.
     * Sets the scoring for the common goals according to the number of players.
     * <ul>
     *  <li>If there are 2 players, the scoringList is filled with 4 and 8.
     *  <li>If there are 3 players, the scoringList is filled with 4, 6 and 8.
     *  <li>If there are 4 players, the scoringList is filled with 2, 4, 6 and 8.
     * </ul>
     *
     * @param layout       the layout of the common goal
     * @param numOfPlayers the number of players
     */
    public CommonGoal(Layout layout, int numOfPlayers) {
        this.layout = layout;

        scoringList = new ArrayList<>();

        for (int i = numOfPlayers; i > 0; i--) {
            switch (numOfPlayers) {
                case 2 -> scoringList.add(4 * i); // Add 4, 8
                case 3 -> scoringList.add(2 * i + 2); // Add 4, 6, 8
                default -> scoringList.add(2 * i); // Add 2, 4, 6, 8
            }
        }
    }

    public List<Integer> getScoringList() {
        return scoringList;
    }

    public Layout getLayout() {
        return layout;
    }

    /**
     * Returns the highest common goal scoring and removes it from the stack.
     *
     * @return the first element of <code>scoringList</code>
     * @throws IndexOutOfBoundsException if the list is empty
     */
    public int getScoring() throws IndexOutOfBoundsException {
        if (scoringList.size() == 0)
            throw new IndexOutOfBoundsException();

        int value = scoringList.get(0);
        scoringList.remove(scoringList.get(0));
        return value;
    }

    /**
     * Checks if the bookshelf fulfills the layout of the common goal.
     *
     * @param bookshelf the bookshelf to compare with the layout
     * @return true if the bookshelf fulfills the layout, false otherwise
     */
    public boolean check(Bookshelf bookshelf) {
        return layout.check(bookshelf);
    }
}
