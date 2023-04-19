package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Coordinates;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class PersonalGoal {
    private final HashMap<Coordinates, Color> personalGoalCard;
    private int currentReached;
    private int counter;

    /**
     * Creates a personal goal card.
     * It is a representation of the spaces that a player
     * has to fill with his bookshelf's items.
     * @param loadedCoordinates the coordinates of the spaces to fill, read from a file
     * @param loadedColors the colors of the spaces to fill, read from a file
     */
    public PersonalGoal(List<Coordinates> loadedCoordinates, List<Color> loadedColors) {
        personalGoalCard = new HashMap<>();
        for(int i=0; i<Math.min(loadedCoordinates.size(), loadedColors.size()); i++){
            personalGoalCard.put(loadedCoordinates.get(i), loadedColors.get(i));
        }
    }

    public Color getColor(Coordinates key) {
        return personalGoalCard.get(key);
    }

    /**
     * Gets the number of points gained by the player according to how many spaces are matched.
     *
     * @return the number of points gained by the player
     */
    public int getPoints(Bookshelf bookshelf) {
        for (Coordinates coordinates : personalGoalCard.keySet()) {
            if (bookshelf.getItemAt(coordinates.x(), coordinates.y()).isPresent()) {
                if (bookshelf.getItemAt(coordinates.x(), coordinates.y()).get().color() == personalGoalCard.get(coordinates)) {
                    colorReached();
                }
            }
        }
        return currentReached < 3 ? currentReached : currentReached + counter;
    }

    // TODO: ask if you mean the number of matches?

    /**
     * Returns the number of colors reached.
     */
    public void colorReached() {
        currentReached++;

        if (currentReached > 2) {
            if (currentReached < 5) {
                counter = (currentReached - 2);
            } else {
                if (currentReached == 5) {
                    counter = currentReached - 1;
                } else {
                    counter = currentReached;
                }
            }
        }

    }

    public void cli_print(){
        for(Coordinates c: personalGoalCard.keySet()){
            System.out.println("x :"+c.x()+"\n"+"y :"+c.y()+"\n"+personalGoalCard.get(c)+"\n");
        }
    }

    public HashMap<Coordinates, Color> getPersonalGoalCard() {
        return personalGoalCard;
    }

    public int getCounter() {
        return counter;
    }

    public int getCurrentReached() {
        return currentReached;
    }
}
