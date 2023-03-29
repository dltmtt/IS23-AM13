package it.polimi.ingsw.Models.Goal;

import it.polimi.ingsw.Models.CommonGoalLayout.Layout;
import it.polimi.ingsw.Models.Games.Bookshelf;
import it.polimi.ingsw.Models.Games.Player;

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

    public void setScoringList(int numOfPlayers) {
        scoringList = new ArrayList<>();

        if (numOfPlayers == 2) {
            /*
             * i=2 --> 4*i=8
             * i=1 --> 4*i=4
             */
            for (int i = numOfPlayers; i > 0; i--) {
                scoringList.add(4 * i);
            }
        } else if (numOfPlayers == 3) {
            /*
             * i=3 --> (4*i+4)/2=8
             * i=2 --> (4*i+4)/2=6
             * i=1 --> (4*i+4)/2=4
             */
            for (int i = numOfPlayers; i > 0; i--) {
                scoringList.add(2 * i + 2);
            }
        } else {
            for (int i = numOfPlayers; i > 0; i--) {
                scoringList.add(i * 2);
            }
        }
    }

    // Points depending on the number of players
    // 2 players: 4, 8
    // 3 players: 4, 6, 8
    // 4 players: 2, 4, 6, 8

    // TODO: check the exception type

    /**
     * @return val, which is the first element of the scoringList, and then it is removed
     * @throws IndexOutOfBoundsException if the list is empty
     * @author Beatrice
     */
    public int getScoring() throws IndexOutOfBoundsException {
        if (scoringList.size() == 0)
            throw new IndexOutOfBoundsException();

        int val = scoringList.get(0);
        scoringList.remove(scoringList.get(0));
        return val;
    }

    /**
     * This method checks if the bookshelf satisfies the layout
     *
     * @param bookshelf
     * @return
     */
    public boolean check(Bookshelf bookshelf) {
        return layout.check(bookshelf);
    }
}
