package it.polimi.ingsw;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;

public class JsonPersonalGoalTest {
    @Test
    public void test() throws IOException, ParseException {

        JSONParser parser = new JSONParser();
        JSONArray a = (JSONArray) parser.parse(new FileReader("src/main/resources/PersonalGoal.json"));

        for (Object o : a) {
            JSONObject person = (JSONObject) o;

            String name = (String) person.get("name");
            System.out.println(name);

            String city = (String) person.get("city");
            System.out.println(city);

            String job = (String) person.get("job");
            System.out.println(job);

            JSONArray cars = (JSONArray) person.get("cars");

            for (Object c : cars) {
                System.out.println(c);
            }
        }
    }
}
