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

public class PersonalGoal {
    private final HashMap<Coordinates, Color> personalGoalCard;
    private int currentReached;
    private int counter;

    /**
     * Creates a personal goal card.
     * It is a representation of the spaces that a player
     * has to fill with his bookshelf's items.
     * @param randomPersonalGoalIndex the index of the personal goal card to be created
     */
    // TODO: add the possibility to load custom personal goals from a file
    public PersonalGoal(int randomPersonalGoalIndex) throws IOException, ParseException {
        personalGoalCard = new HashMap<>();
        JSONParser parser = new JSONParser();
        JSONObject personalGoalJson = (JSONObject) parser.parse(new FileReader("src/main/resources/personal_goals.json"));
        JSONArray deck = (JSONArray) personalGoalJson.get("personal_goal_configurations");

        JSONObject personalGoal = (JSONObject) deck.get(randomPersonalGoalIndex);


        JSONArray configuration = (JSONArray) personalGoal.get("configuration");
        for (Object o : configuration) {
            JSONObject cell = (JSONObject) o;
            personalGoalCard.put(new Coordinates(Math.toIntExact((Long) cell.get("x")), Math.toIntExact((Long) cell.get("y"))), Color.valueOf((String) cell.get("color")));
        }

//
//        switch (layout) {
//            case 0 -> {
//                personalGoalCard.put(new Coordinates(2, 0), Color.PINK);
//                personalGoalCard.put(new Coordinates(4, 1), Color.GREEN);
//                personalGoalCard.put(new Coordinates(3, 2), Color.LIGHTBLUE);
//                personalGoalCard.put(new Coordinates(0, 3), Color.YELLOW);
//                personalGoalCard.put(new Coordinates(1, 3), Color.WHITE);
//                personalGoalCard.put(new Coordinates(5, 4), Color.BLUE);
//            }
//            case 1 -> {
//                personalGoalCard.put(new Coordinates(3, 0), Color.WHITE);
//                personalGoalCard.put(new Coordinates(1, 1), Color.BLUE);
//                personalGoalCard.put(new Coordinates(4, 1), Color.YELLOW);
//                personalGoalCard.put(new Coordinates(0, 3), Color.PINK);
//                personalGoalCard.put(new Coordinates(2, 3), Color.GREEN);
//                personalGoalCard.put(new Coordinates(5, 4), Color.LIGHTBLUE);
//            }
//            case 2 -> {
//                personalGoalCard.put(new Coordinates(3, 0), Color.YELLOW);
//                personalGoalCard.put(new Coordinates(4, 1), Color.WHITE);
//                personalGoalCard.put(new Coordinates(2, 2), Color.BLUE);
//                personalGoalCard.put(new Coordinates(5, 2), Color.PINK);
//                personalGoalCard.put(new Coordinates(0, 3), Color.LIGHTBLUE);
//                personalGoalCard.put(new Coordinates(1, 4), Color.GREEN);
//            }
//            case 3 -> {
//                personalGoalCard.put(new Coordinates(0, 0), Color.PINK);
//                personalGoalCard.put(new Coordinates(1, 1), Color.YELLOW);
//                personalGoalCard.put(new Coordinates(5, 2), Color.LIGHTBLUE);
//                personalGoalCard.put(new Coordinates(1, 3), Color.BLUE);
//                personalGoalCard.put(new Coordinates(3, 3), Color.WHITE);
//                personalGoalCard.put(new Coordinates(5, 4), Color.GREEN);
//            }
//            case 4 -> {
//                personalGoalCard.put(new Coordinates(5, 0), Color.PINK);
//                personalGoalCard.put(new Coordinates(2, 1), Color.YELLOW);
//                personalGoalCard.put(new Coordinates(0, 2), Color.LIGHTBLUE);
//                personalGoalCard.put(new Coordinates(5, 2), Color.BLUE);
//                personalGoalCard.put(new Coordinates(3, 3), Color.WHITE);
//                personalGoalCard.put(new Coordinates(4, 4), Color.GREEN);
//            }
//            case 5 -> {
//                personalGoalCard.put(new Coordinates(3, 0), Color.GREEN);
//                personalGoalCard.put(new Coordinates(4, 1), Color.PINK);
//                personalGoalCard.put(new Coordinates(3, 2), Color.YELLOW);
//                personalGoalCard.put(new Coordinates(1, 3), Color.LIGHTBLUE);
//                personalGoalCard.put(new Coordinates(0, 4), Color.BLUE);
//                personalGoalCard.put(new Coordinates(2, 4), Color.WHITE);
//            }
//            case 6 -> {
//                personalGoalCard.put(new Coordinates(3, 0), Color.LIGHTBLUE);
//                personalGoalCard.put(new Coordinates(1, 1), Color.WHITE);
//                personalGoalCard.put(new Coordinates(1, 2), Color.GREEN);
//                personalGoalCard.put(new Coordinates(3, 2), Color.BLUE);
//                personalGoalCard.put(new Coordinates(2, 3), Color.PINK);
//                personalGoalCard.put(new Coordinates(5, 4), Color.YELLOW);
//            }
//            case 7 -> {
//                personalGoalCard.put(new Coordinates(2, 0), Color.LIGHTBLUE);
//                personalGoalCard.put(new Coordinates(5, 0), Color.GREEN);
//                personalGoalCard.put(new Coordinates(3, 1), Color.PINK);
//                personalGoalCard.put(new Coordinates(0, 2), Color.WHITE);
//                personalGoalCard.put(new Coordinates(4, 3), Color.BLUE);
//                personalGoalCard.put(new Coordinates(1, 4), Color.YELLOW);
//            }
//            case 8 -> {
//                personalGoalCard.put(new Coordinates(0, 0), Color.GREEN);
//                personalGoalCard.put(new Coordinates(4, 1), Color.PINK);
//                personalGoalCard.put(new Coordinates(3, 2), Color.BLUE);
//                personalGoalCard.put(new Coordinates(5, 2), Color.WHITE);
//                personalGoalCard.put(new Coordinates(2, 3), Color.LIGHTBLUE);
//                personalGoalCard.put(new Coordinates(1, 4), Color.YELLOW);
//            }
//            case 9 -> {
//                personalGoalCard.put(new Coordinates(0, 0), Color.BLUE);
//                personalGoalCard.put(new Coordinates(1, 1), Color.LIGHTBLUE);
//                personalGoalCard.put(new Coordinates(3, 2), Color.GREEN);
//                personalGoalCard.put(new Coordinates(5, 2), Color.YELLOW);
//                personalGoalCard.put(new Coordinates(1, 4), Color.PINK);
//                personalGoalCard.put(new Coordinates(2, 4), Color.WHITE);
//            }
//            case 10 -> {
//                personalGoalCard.put(new Coordinates(0, 0), Color.YELLOW);
//                personalGoalCard.put(new Coordinates(2, 1), Color.BLUE);
//                personalGoalCard.put(new Coordinates(4, 1), Color.LIGHTBLUE);
//                personalGoalCard.put(new Coordinates(2, 2), Color.WHITE);
//                personalGoalCard.put(new Coordinates(0, 3), Color.GREEN);
//                personalGoalCard.put(new Coordinates(1, 4), Color.PINK);
//            }
//            case 11 -> {
//                personalGoalCard.put(new Coordinates(0, 0), Color.WHITE);
//                personalGoalCard.put(new Coordinates(4, 0), Color.BLUE);
//                personalGoalCard.put(new Coordinates(2, 1), Color.GREEN);
//                personalGoalCard.put(new Coordinates(3, 2), Color.PINK);
//                personalGoalCard.put(new Coordinates(4, 3), Color.YELLOW);
//                personalGoalCard.put(new Coordinates(2, 4), Color.LIGHTBLUE);
//            }
//        }
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
}
