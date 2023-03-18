package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Beatrice
 * This class is for the CommonGoal cards
 */
public class CommonGoal {
    private final Layout layout;
    private List<Integer> scoringList;

    public CommonGoal(int numofplayers, Layout layout) {
        List<Integer> scoringList = new ArrayList<>();

        if (numofplayers == 2) {
            /**
             * i=2 --> 4*i=8
             * i=1 --> 4*i=4
             */
            for (int i = numofplayers; i > 0; i--) {
                scoringList.add(4 * i);
            }
        } else if (numofplayers == 3) {
            /**
             * i=3 --> (4*i+4)/2=8
             * i=2 --> (4*i+4)/2=6
             * i=1 --> (4*i+4)/2=4
             */
            for (int i = numofplayers; i > 0; i--) {
                scoringList.add((4 * i + 4) / 2);
            }
        } else {
            for (int i = numofplayers; i > 0; i--) {
                scoringList.add(i * 2);
            }
        }

        this.layout = layout;
    }

    /*   public int assignScore(Player player){

       }
*/
    public List getScoringList() {
        return scoringList;
    }


//points depending on the number of players
    // 2 players: 4,8
    // 3 players: 4,6,8
    //4 players: 2,4,6,8

    public int getScoring() {
        int val = scoringList.get(0);
        scoringList.remove(scoringList.get(0));
        return val;
    }
}
