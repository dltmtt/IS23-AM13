package it.polimi.ingsw.utils;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.PersonalGoal;
import it.polimi.ingsw.server.model.layouts.*;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static java.lang.Integer.parseInt;

/**
 * This class provides methods to load the settings of the game from the JSON files and the properties file.
 * It also provides methods to create the decks of the game.
 */
public class SettingLoader {

    /**
     * Loads the settings of the common goals from the JSON file.
     *
     * @throws IOException    if the file is not found
     * @throws ParseException if the file is not in JSON format
     * @return the complete common goal deck
     */

    public static PersonalGoal loadSpecificPersonalGoal(int randomPersonalGoalIndex) throws IOException, ParseException {

        JSONParser parser = new JSONParser();

        JSONObject personalGoalCards = (JSONObject) parser.parse(new FileReader("src/main/resources/personal_goals.json"));
        JSONArray personalGoalConfigurations = (JSONArray) personalGoalCards.get("personal_goal_configurations");
        JSONObject personalGoalCard = (JSONObject) personalGoalConfigurations.get(randomPersonalGoalIndex);
        JSONArray configuration = (JSONArray) personalGoalCard.get("configuration");


        List<Coordinates> loadedCoordinates = new ArrayList<>();
        List<Color> loadedColors = new ArrayList<>();
        for (Object o : configuration) {
            JSONObject cell = (JSONObject) o;
            loadedCoordinates.add(new Coordinates(parseInt(cell.get("x").toString()), parseInt(cell.get("y").toString())));
            loadedColors.add(Color.valueOf(cell.get("color").toString()));
        }

        return new PersonalGoal(loadedCoordinates, loadedColors);
    }

    public static List<PersonalGoal> personalGoalLoader() throws IOException, ParseException {
        List<PersonalGoal> loadedPersonalGoals = new ArrayList<>();
        // Parsing JSON file for personal goals configurations
        JSONParser parser = new JSONParser();
        JSONObject personalGoalJson = (JSONObject) parser.parse(new FileReader("src/main/resources/personal_goals.json"));
        // accessing the array of personal goals configurations
        JSONArray personalGoalConfigurations = (JSONArray) personalGoalJson.get("personal_goal_configurations");
        for(Object o: personalGoalConfigurations){
            JSONObject personalGoalCard = (JSONObject) o;
            JSONArray configuration = (JSONArray) personalGoalCard.get("configuration");

            List<Coordinates> loadedCoordinates = new ArrayList<>();
            List<Color> loadedColors = new ArrayList<>();
            for (Object o1 : configuration) {
                JSONObject cell = (JSONObject) o1;
                loadedCoordinates.add(new Coordinates(parseInt(cell.get("x").toString()), parseInt(cell.get("y").toString())));
                loadedColors.add(Color.valueOf(cell.get("color").toString()));
            }
            loadedPersonalGoals.add(new PersonalGoal(loadedCoordinates, loadedColors));
        }

        //giving a big 'ol shuffle to the personal goal deck
        for(int j = 0; j < loadedPersonalGoals.size(); j++){
            Random rand = new Random();

            for (int i = 0; i < loadedPersonalGoals.size(); i++) {
                int randomIndexToSwap = rand.nextInt(loadedPersonalGoals.size());
                PersonalGoal temp = loadedPersonalGoals.get(randomIndexToSwap);
                loadedPersonalGoals.set(randomIndexToSwap, loadedPersonalGoals.get(i));
                loadedPersonalGoals.set(i, temp);
            }
        }
        return loadedPersonalGoals;
    }

    public static @NotNull List<CommonGoal> commonGoalLoader(int numOfPlayers) throws IOException, ParseException {
        List<CommonGoal> loadedCommonGoals = new ArrayList<>();
        // Parsing JSON file for common goals configurations
        JSONParser parser = new JSONParser();

        JSONObject a;

        //retrieving from file
        try {
            a = (JSONObject) parser.parse(new FileReader("src/main/resources/common_goals.json"));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e + " common_goals.json not found in filling the Common Goal Deck");
        }
        JSONArray layouts = (JSONArray) a.get("common_goal_configurations");
        //setting default values
        int minDifferent = 0;
        int maxDifferent = 0;
        int occurrences = 0;
        int size = 0;
        int dimension = 0;
        boolean horizontal = false;

        //loading configurations
        for (Object o : layouts) {
            JSONObject configuration = (JSONObject) o;

            //loading parameters object
            JSONArray parameters = (JSONArray) configuration.get("parameters");
            JSONObject loadedParameters = (JSONObject) parameters.get(0);

            //loading type
            String type = (String) configuration.get("type");

            //loading common parameters
            minDifferent = parseInt(loadedParameters.get("minDifferent").toString());
            maxDifferent = parseInt(loadedParameters.get("maxDifferent").toString());
            Layout loadedLayout;

            //layout creation switch
            switch (type) {
                case "group" -> {
                    occurrences = parseInt(loadedParameters.get("occurrences").toString());
                    size = parseInt(loadedParameters.get("size").toString());
                    loadedLayout = new Group(minDifferent, maxDifferent, occurrences, size);
                }
                case "square" -> {
                    occurrences = parseInt(loadedParameters.get("occurrences").toString());
                    size = parseInt(loadedParameters.get("size").toString());
                    loadedLayout = new Square(minDifferent, maxDifferent, occurrences, size);
                }
                case "corners" -> loadedLayout = new Corners(minDifferent, maxDifferent);
                case "fullLine" -> {
                    horizontal = Boolean.parseBoolean(loadedParameters.get("horizontal").toString());
                    occurrences = parseInt(loadedParameters.get("occurrences").toString());
                    loadedLayout = new FullLine(minDifferent, maxDifferent, occurrences, horizontal);
                }
                case "itemsPerColor" -> loadedLayout = new ItemsPerColor(minDifferent, maxDifferent);
                case "diagonal" -> {
                    dimension = parseInt(loadedParameters.get("dimension").toString());
                    loadedLayout = new Diagonal(minDifferent, maxDifferent, dimension);
                }
                case "xShape" -> {
                    dimension = parseInt(loadedParameters.get("dimension").toString());
                    loadedLayout = new XShape(minDifferent, maxDifferent, dimension);
                }
                case "stair" -> loadedLayout = new Stair(minDifferent, maxDifferent, Bookshelf.getDimension());
                default -> throw new RuntimeException("Error in loading common goals");
            }
            //adding the new common goal to the deck
            loadedCommonGoals.add(new CommonGoal(loadedLayout, numOfPlayers));
        }

        //giving a big 'ol shuffle to the common goals
        for(int j = 0; j < loadedCommonGoals.size(); j++){
            Random rand = new Random();

            for (int i = 0; i < loadedCommonGoals.size(); i++) {
                int randomIndexToSwap = rand.nextInt(loadedCommonGoals.size());
                CommonGoal temp = loadedCommonGoals.get(randomIndexToSwap);
                loadedCommonGoals.set(randomIndexToSwap, loadedCommonGoals.get(i));
                loadedCommonGoals.set(i, temp);
            }
        }
        return loadedCommonGoals;
    }

    public static void loadBookshelfSettings() {
        Properties prop = new Properties();
        int rowsSetting;
        int colsSetting;

        try (InputStream settings = new FileInputStream("src/main/resources/settings.properties")) {
            prop.load(settings);
            rowsSetting = parseInt(prop.getProperty("bookshelf.rows"));
            colsSetting = parseInt(prop.getProperty("bookshelf.columns"));
        } catch (IOException ex) {
            ex.printStackTrace();
            // In case the file is not found or there is and error reading the file,
            // the default values will be used instead
            rowsSetting = 5;
            colsSetting = 6;
        }

        Bookshelf.setRows(rowsSetting);
        Bookshelf.setColumns(colsSetting);
    }


}