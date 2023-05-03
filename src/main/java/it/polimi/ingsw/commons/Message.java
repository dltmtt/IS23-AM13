package it.polimi.ingsw.commons;

import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.Item;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class Message implements Serializable {
    private final JSONObject gson;

    /**
     * Constructor for the login type of message
     * when called, only one of the parameters is used, the others are set to default values
     *
     * @param category  category of the message (what's the message about)
     * @param username  username of the player
     * @param age       age of the player
     * @param firstgame if it's the first game of the player
     * @param numPlayer number of players in the game
     */
    public Message(String category, String username, int age, boolean firstgame, int numPlayer) {
        gson = new JSONObject();
        String path = "src/main/java/it/polimi/ingsw/commons/LoginMessage.json";
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

    /**
     * Constructor for the single message type of message
     * it is used for response messages from the server
     *
     * @param singleMessage
     */

    public Message(String singleMessage) {
        gson = new JSONObject();
        String path = "src/main/java/it/polimi/ingsw/commons/SingleMessage.json";
        gson.put("category", singleMessage);
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message(int posix) {
        gson = new JSONObject();
        String path = "src/main/java/it/polimi/ingsw/commons/PosixMessage.json";
        gson.put("category", "index");
        gson.put("posix", posix);
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for the game type of message (Goals, Bookshelf and Board)
     *
     * @param personalGoal   personal goal of the player
     * @param commonGoalList list of common goals
     * @param bookshelf      bookshelf of the player
     * @param board          board of the game
     */
    public Message(int personalGoal, List<CommonGoal> commonGoalList, Bookshelf bookshelf, Board board) {
        gson = new JSONObject();
        String path = "src/main/java/it/polimi/ingsw/commons/GameMessage.json";

        gson.put("category", "startGame");
        gson.put("personal_goal", personalGoal);

        for (int i = 0; i < commonGoalList.size(); i++) {
            gson.put("commonGoalLayout " + i, commonGoalList.get(i).getLayout().getName());
        }
//        gson.put("bookshelf", bookshelf.getItems());
//        gson.put("board", board.getBoardMatrix());
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

    public int getPosix() {
        return (int) gson.get("posix");
    }


    public int getPersonalGoal() {
        return (int) gson.get("personal_goal");
    }

    public String getCommonGoalLayout(int i) {
        return (String) gson.get("commonGoalLayout" + i);
    }

    public Optional<Item>[][] getBookshelf() {
        return (Optional<Item>[][]) gson.get("bookshelf");
    }

    public Item[][] getBoard() {
        return (Item[][]) gson.get("board");
    }
}
