package it.polimi.ingsw.commons;

import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;

public class Message implements Serializable {
    private final JSONObject gson;

    public Message(String category, String username, int age, boolean firstgame, int numPlayer) {
        gson = new JSONObject();
        String path = "src/main/java/it/polimi/ingsw/commons/Message.json";
        gson.put("category", category);
        gson.put("argument", username);
        gson.put("value", age);
        gson.put("bool", firstgame);
        gson.put("numPlayer", numPlayer);
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message(String singleMessage) {
        gson = new JSONObject();
        String path = "src/main/java/it/polimi/ingsw/commons/Message.json";
        gson.put("message", singleMessage);
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCategory() {
        return (String) gson.get("category");
    }

    public String getUsername() {
        return (String) gson.get("argument");
    }

    public int getAge() {
        return (int) gson.get("value");
    }

    public boolean getFirstGame() {
        return (boolean) gson.get("bool");
    }

    public int getNumPlayer() {
        return (int) gson.get("numPlayer");
    }

    public String getMessage() {
        return (String) gson.get("message");
    }


}
