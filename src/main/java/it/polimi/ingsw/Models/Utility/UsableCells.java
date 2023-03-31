package it.polimi.ingsw.Models.Utility;

import java.util.ArrayList;
import java.util.List;

public class UsableCells {
    private final List<Coordinates> usableCells;

    /**
     * Creates a list of cells that can be filled with items.
     *
     * @param numOfPlayer the number of players
     * @throws IllegalArgumentException if the number of players is invalid
     */
    public UsableCells(int numOfPlayer) {
        // These cells have to be filled either way
        usableCells = new ArrayList<>();

        for (int i = 1; i <= 7; i++) {
            if (i < 7) {
                for (int j = 4; j <= 5; j++) {
                    usableCells.add(new Coordinates(i, j));
                }
            }
            if (i > 1) {
                usableCells.add(new Coordinates(i, 3));
            }
            if (i >= 3 && i <= 5) {
                usableCells.add(new Coordinates(i, 2));
                usableCells.add(new Coordinates(i, 6));
            }
            if (i >= 3 && i <= 4) {
                usableCells.add(new Coordinates(i, 1));
            }
            if (i >= 4 && i <= 5) {
                usableCells.add(new Coordinates(i, 7));
            }
        }
        usableCells.add(new Coordinates(7, 4));

        // These cells have to be filled only with at least 3 players
        if (numOfPlayer >= 3) {
            usableCells.add(new Coordinates(0, 5));
            usableCells.add(new Coordinates(2, 2));
            usableCells.add(new Coordinates(3, 0));
            usableCells.add(new Coordinates(2, 6));
            usableCells.add(new Coordinates(8, 3));
            usableCells.add(new Coordinates(6, 2));
            usableCells.add(new Coordinates(6, 6));
            usableCells.add(new Coordinates(5, 8));
        }

        // These cells have to be filled only with 4 players
        if (numOfPlayer == 4) {
            usableCells.add(new Coordinates(0, 4));
            usableCells.add(new Coordinates(1, 3));
            usableCells.add(new Coordinates(3, 7));
            usableCells.add(new Coordinates(4, 0));
            usableCells.add(new Coordinates(4, 8));
            usableCells.add(new Coordinates(5, 1));
            usableCells.add(new Coordinates(7, 5));
            usableCells.add(new Coordinates(8, 4));
        }
    }

    public List<Coordinates> getList() {
        return usableCells;
    }
}
