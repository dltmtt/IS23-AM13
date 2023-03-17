package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class CommonGoal {
    private final Layout layout;
    private List<Integer> scoringList;

    public CommonGoal(int numofplayers, Layout layout) {
        List<Integer> scoringList = new ArrayList<>();

        if (numofplayers == 2) {
            for (int i = numofplayers; i > 0; i--) {
                scoringList.add(4 * i);
            }
        } else if (numofplayers == 3) {
            scoringList.add(8);
            scoringList.add(6);
            scoringList.add(4);
        } else {
            for (int i = numofplayers; i > 0; i--) {
                scoringList.add(i * 2);
            }
        }
        this.layout = layout;
    }

    /*   public int assignScore(Player player){

       }

    public List getScoringList() {

    }

*/
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
