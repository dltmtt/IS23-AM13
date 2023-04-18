package it.polimi.ingsw.utils;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.layouts.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingLoader {
    public static List<CommonGoal> commonGoalLoader(int numOfPlayers) throws IOException, ParseException {
        List<CommonGoal> loadedCommonGoals = new ArrayList<>();
        // Parsing JSON file for common goals configurations
        JSONParser parser = new JSONParser();

        JSONObject a;
        try {
            a = (JSONObject) parser.parse(new FileReader("src/main/resources/common_goals.json"));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e + " common_goals.json not found in filling the Common Goal Deck");
        }
        JSONArray layouts = (JSONArray) a.get("common_goal_configurations");
        int minDifferent = 0;
        int maxDifferent = 0;
        int occurrences = 0;
        int size = 0;
        int dimension = 0;
        boolean horizontal = false;

        for (Object o : layouts) {
            JSONObject configuration = (JSONObject) o;

            //loading parameters object
            JSONArray parameters = (JSONArray) configuration.get("parameters");
            JSONObject loadedParameters = (JSONObject) parameters.get(0);

            String type = (String) configuration.get("type");

            minDifferent = Integer.parseInt(loadedParameters.get("minDifferent").toString());
            maxDifferent = Integer.parseInt(loadedParameters.get("maxDifferent").toString());
            Layout loadedLayout;
            switch (type) {
                case "group":
                    occurrences = Integer.parseInt(loadedParameters.get("occurrences").toString());
                    size = Integer.parseInt(loadedParameters.get("size").toString());
                    loadedLayout = new Group(minDifferent, maxDifferent, occurrences, size);
                    break;
                case "square":
                    occurrences = Integer.parseInt(loadedParameters.get("occurrences").toString());
                    size = Integer.parseInt(loadedParameters.get("size").toString());
                    loadedLayout = new Square(minDifferent, maxDifferent, occurrences, size);
                    break;
                case "corners":
                    loadedLayout = new Corners(minDifferent, maxDifferent);
                    break;
                case "fullLine":
                    horizontal = Boolean.parseBoolean(loadedParameters.get("horizontal").toString());
                    occurrences = Integer.parseInt(loadedParameters.get("occurrences").toString());
                    loadedLayout = new FullLine(minDifferent, maxDifferent, occurrences, horizontal);
                    break;
                case "itemsPerColor":
                    loadedLayout = new ItemsPerColor(minDifferent, maxDifferent);
                    break;
                case "diagonal":
                    dimension = Integer.parseInt(loadedParameters.get("dimension").toString());
                    loadedLayout = new Diagonal(minDifferent, maxDifferent, dimension);
                    break;
                case "xShape":
                    dimension = Integer.parseInt(loadedParameters.get("dimension").toString());
                    loadedLayout = new XShape(minDifferent, maxDifferent, dimension);
                    break;
                case "stair":
                    loadedLayout = new Stair(minDifferent, maxDifferent, Bookshelf.getDimension());
                    break;
                default:
                    throw new RuntimeException("Error in loading common goals");
            }

            loadedCommonGoals.add(new CommonGoal(loadedLayout, numOfPlayers));
        }


        return loadedCommonGoals;
    }
}
