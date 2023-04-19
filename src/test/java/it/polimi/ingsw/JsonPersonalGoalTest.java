package it.polimi.ingsw;


import it.polimi.ingsw.server.model.PersonalGoal;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Coordinates;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class JsonPersonalGoalTest {

    @Test
    public void firstCardFirstCellTest() throws IOException, ParseException {

        HashMap<Coordinates, Color> personalGoalCard = new HashMap<>();

        JSONParser parser = new JSONParser();
        JSONObject personalGoalJson = (JSONObject) parser.parse(new FileReader("src/main/resources/personal_goals.json"));
        JSONArray deck = (JSONArray) personalGoalJson.get("personal_goal_configurations");

        JSONObject personalGoal = (JSONObject) deck.get(0);


        JSONArray configuration = (JSONArray) personalGoal.get("configuration");
        JSONObject cell = (JSONObject) configuration.get(0);
        personalGoalCard.put(new Coordinates(Math.toIntExact((Long) cell.get("x")), Math.toIntExact((Long) cell.get("y"))), Color.valueOf((String) cell.get("color")));
        assert personalGoalCard.get(new Coordinates(2, 0)).equals(Color.PINK);

    }

    @Test
    void drawTest() throws IOException, ParseException {
        List<Player> players = new ArrayList<>(3);

        Game g = new Game(players);

        g.fillPersonalGoalDeck();
        PersonalGoal p = g.drawPersonalGoal();
        p.cli_print();

    }
}
