package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.PersonalGoal;
import it.polimi.ingsw.server.model.layouts.*;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Coordinates;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static java.lang.Integer.parseInt;

/**
 * This class provides methods to load the settings of the game from the JSON files and the properties file.
 * It also provides methods to create the decks of the game.
 */
public class SettingLoader {

    public static final String BASE_PATH = "src/main/resources/it/polimi/ingsw/";

    /**
     * Loads the settings of the common goals from the JSON file.
     *
     * @param randomPersonalGoalIndex the index of the personal goal to be loaded
     * @return the complete common goal deck
     * @throws IOException    if the file is not found
     * @throws ParseException if the file is not in JSON format
     */
    public static PersonalGoal loadSpecificPersonalGoal(int randomPersonalGoalIndex) throws IOException, ParseException {

        JSONParser parser = new JSONParser();

        JSONObject personalGoalCards = (JSONObject) parser.parse(new InputStreamReader(SettingLoader.class.getResource("personal_goals.json").openStream()));
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

        return new PersonalGoal(loadedCoordinates, loadedColors, randomPersonalGoalIndex);
    }

    public static @NotNull List<CommonGoal> commonGoalLoader(int numOfPlayers) {
        List<CommonGoal> loadedCommonGoals = new ArrayList<>();
        // Parsing JSON file for common goals configurations
        JSONParser parser = new JSONParser();

        JSONObject a;

        // Retrieving from file
        try {
            a = (JSONObject) parser.parse(new InputStreamReader(SettingLoader.class.getResource("common_goals.json").openStream()));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e + " common_goals.json not found in filling the Common Goal Deck");
        }
        JSONArray layouts = (JSONArray) a.get("common_goal_configurations");
        // Setting default values
        int minDifferent;
        int maxDifferent;
        int occurrences;
        int size;
        int dimension;
        boolean horizontal;

        // Loading configurations
        for (Object o : layouts) {
            JSONObject configuration = (JSONObject) o;

            // Loading parameters object
            JSONArray parameters = (JSONArray) configuration.get("parameters");
            JSONObject loadedParameters = (JSONObject) parameters.get(0);

            // Loading type
            String type = (String) configuration.get("type");

            // Loading common parameters
            minDifferent = parseInt(loadedParameters.get("minDifferent").toString());
            maxDifferent = parseInt(loadedParameters.get("maxDifferent").toString());
            Layout loadedLayout;

            // Layout creation switch
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
            // Adding the new common goal to the deck
            loadedCommonGoals.add(new CommonGoal(loadedLayout, numOfPlayers));
        }

        // Giving a big ol' shuffle to the common goals
        for (int j = 0; j < loadedCommonGoals.size(); j++) {
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

        //JSONObject personalGoalCards = (JSONObject) parser.parse(new InputStreamReader(SettingLoader.class.getResource("personal_goals.json").openStream()));

        try (InputStreamReader settings = new InputStreamReader(SettingLoader.class.getResource("settings.properties").openStream())) {
            prop.load(settings);
            rowsSetting = parseInt(prop.getProperty("bookshelf.rows"));
            colsSetting = parseInt(prop.getProperty("bookshelf.columns"));
        } catch (IOException ex) {
            System.err.println("settings.properties file not found at " + BASE_PATH + ", using default values");
            // In case the file is not found or there is and error reading the file,
            // the default values will be used instead
            rowsSetting = 6;
            colsSetting = 5;
        }

        Bookshelf.setRows(rowsSetting);
        Bookshelf.setColumns(colsSetting);
    }

    public static JSONObject loadUsableCells(){
        JSONParser parser = new JSONParser();
        try {
            return (JSONObject) parser.parse(new InputStreamReader(SettingLoader.class.getResource("usable_cells.json").openStream()));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }


}
