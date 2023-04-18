package it.polimi.ingsw;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;

public class JsonCommonGoalTest {
    @Test
    public void ReadingTest() throws IOException, ParseException {

        JSONParser parser = new JSONParser();
        JSONObject a = (JSONObject) parser.parse(new FileReader("src/main/resources/common_goals.json"));
        JSONArray configurations = (JSONArray) a.get("common_goal_configurations");
        System.out.println(configurations);
        for (Object o : configurations) {
            JSONObject config = (JSONObject) o;
            System.out.println(config.get("id") + " " + config.get("type"));
        }
    }
}
