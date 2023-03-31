package it.polimi.ingsw.Models.Goal;

import it.polimi.ingsw.Models.CommonGoalLayout.Layout;
import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Game.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * A common goal card, which has a layout to be matched by the players'
 * bookshelves and a stack of scores to be assigned to the players.
 *
 * @author Beatrice
 * @see Layout
 * @see Player
 * @see Bookshelf
 */
public class CommonGoal {
    private final Layout layout;
    private List<Integer> scoringList;

    public CommonGoal(Layout layout) {
        this.layout = layout;
    }


    public List<Integer> getScoringList() {
        return scoringList;
    }

    /**
     * Sets the scoring for the common goals according to the number of players.
     * <ul>
     *  <li>If there are 2 players, the scoringList is filled with 4 and 8.
     *  <li>If there are 3 players, the scoringList is filled with 4, 6 and 8.
     *  <li>If there are 4 players, the scoringList is filled with 2, 4, 6 and 8.
     * </ul>
     *
     * @param numOfPlayers the number of players
     */
    public void setScoringList(int numOfPlayers) {
        scoringList = new ArrayList<>();

        for (int i = numOfPlayers; i > 0; i--) {
            switch (numOfPlayers) {
                case 2 -> scoringList.add(4 * i); // Add 4, 8
                case 3 -> scoringList.add(2 * i + 2); // Add 4, 6, 8
                default -> scoringList.add(2 * i); // Add 2, 4, 6, 8
            }
        }
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
