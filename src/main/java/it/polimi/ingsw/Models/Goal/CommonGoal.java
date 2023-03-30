package it.polimi.ingsw.Models.Goal;

import it.polimi.ingsw.Models.CommonGoalLayout.Layout;
import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Game.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Beatrice
 * This class is for the CommonGoal cards
 */
public class CommonGoal {
    private final Layout layout;
    private List<Integer> scoringList;

    public CommonGoal(Layout layout) {
        this.layout = layout;
    }

    public void assignScore(Player player) {
        player.setCommonGoalPoints(this);
        scoringList.remove(0);
    }

    public List<Integer> getScoringList() {
        return scoringList;
    }

    /**
     * This method sets the scoring for the common goals according to the number of players.
     * If there are 2 players, the scoringList is filled with 4 and 8.
     * If there are 3 players, the scoringList is filled with 4, 6 and 8.
     * If there are 4 players, the scoringList is filled with 2, 4, 6 and 8.
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

    // TODO: check the exception type

    /**
     * Return the highest common goal scoring and remove it from the stack.
     *
     * @return the first element of the scoringList
     * @throws IndexOutOfBoundsException if the list is empty
     * @author Beatrice
     */
    public int getScoring() throws IndexOutOfBoundsException {
        if (scoringList.size() == 0)
            throw new IndexOutOfBoundsException();

        int value = scoringList.get(0);
        scoringList.remove(scoringList.get(0));
        return value;
    }

    /**
     * This method checks if the bookshelf satisfies the layout.
     *
     * @param bookshelf the bookshelf to check
     * @return true if the bookshelf satisfies the layout, false otherwise§
     */
    public boolean check(Bookshelf bookshelf) {
        return layout.check(bookshelf);
    }
}
