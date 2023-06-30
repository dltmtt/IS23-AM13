package it.polimi.ingsw;

import it.polimi.ingsw.server.model.GameModel;
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

import static it.polimi.ingsw.SettingLoader.BASE_PATH;

public class JsonPersonalGoalTest {

    @Test
    void drawTest() throws IOException, ParseException {
        List<Player> players = new ArrayList<>(3);

        GameModel g = new GameModel(players);

        g.fillPersonalGoalDeck();
        PersonalGoal p = g.drawPersonalGoal();
        System.out.println(p.getPersonalGoalCard());
    }
}
