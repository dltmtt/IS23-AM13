package it.polimi.ingsw.commons;

import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.SettingLoader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Message implements Serializable {

    private static final String BASE_PATH = "src/main/java/it/polimi/ingsw/commons/";
    private final JSONObject gson;
    private int commonGoals;

    public Message(JSONObject json) {
        gson = json;
    }

    /**
     * Constructor for the login message.
     * When called, only one of the parameters is used, the others are set to default values.
     *
     * @param category    category of the message (what's the message about)
     * @param username    username of the player
     * @param age         age of the player
     * @param isFirstGame if it's the first game of the player
     * @param numPlayer   number of players in the game
     */

    public Message(String category, String username, int age, boolean isFirstGame, int numPlayer) {
        gson = new JSONObject();
        String path = BASE_PATH + "LoginMessage.json";
        gson.put("category", category);
        gson.put("argument", username);
        gson.put("value", age);
        gson.put("bool", isFirstGame);
        gson.put("numPlayer", numPlayer);
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message(String category, int position) {
        gson = new JSONObject();
        String path = BASE_PATH + "LoginMessage.json";
        gson.put("category", category);
        gson.put("position", position);
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message(String category, String type, int n) {
        gson = new JSONObject();
        String path = BASE_PATH + "LoginMessage.json";
        gson.put("category", category);
        gson.put(type, n);
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for the single message.
     * It is used for response messages from the server.
     *
     * @param singleMessage
     */
    public Message(String singleMessage) {
        gson = new JSONObject();
        String path = BASE_PATH + "SingleMessage.json";
        gson.put("category", singleMessage);
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message(int size, List<String> names) {
        gson = new JSONObject();
        String path = BASE_PATH + "WinnersMessage.json";
        gson.put("category", "winners");
        gson.put("size", size);

        for (int i = 0; i < size; i++) {
            gson.put("name" + i, names.get(i));
        }

        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message(int position) {
        gson = new JSONObject();
        String path = BASE_PATH + "PositionMessage.json";
        gson.put("category", "index");
        gson.put("position", position);
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for the game message type (Goals, Bookshelf, and Board)
     *
     * @param personalGoal   personal goal of the player
     * @param commonGoalList list of common goals
     * @param bookshelf      bookshelf of the player
     * @param board          board of the game
     */
    public Message(int personalGoal, List<CommonGoal> commonGoalList, Bookshelf bookshelf, Board board) {
        gson = new JSONObject();
        String path = BASE_PATH + "GameMessage.json";
        SettingLoader.loadBookshelfSettings();
        gson.put("category", "startGame");
        gson.put("personal_goal", personalGoal);

        for (int i = 0; i < commonGoalList.size(); i++) {
            gson.put("commonGoalLayout " + i, commonGoalList.get(i).getLayout().getName());

            if ("fullLine".equals(commonGoalList.get(i).getLayout().getName())) {
                gson.put("occurrences " + i, commonGoalList.get(i).getLayout().getOccurrences());
                gson.put("horizontal " + i, commonGoalList.get(i).getLayout().isHorizontal());
                gson.put("size " + i, 0);
            } else if ("group".equals(commonGoalList.get(i).getLayout().getName())) {
                gson.put("occurrences " + i, commonGoalList.get(i).getLayout().getOccurrences());
                gson.put("horizontal " + i, false);
                gson.put("size " + i, commonGoalList.get(i).getLayout().getSize());
            } else {
                gson.put("occurrences " + i, 0);
                gson.put("horizontal " + i, false);
                gson.put("size " + i, 0);
            }
        }
        commonGoals = commonGoalList.size();

        gson.put("bookshelf", bookshelfJson(bookshelf));

        gson.put("board", boardJson(board));

        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message(String category, Board board) {
        gson = new JSONObject();
        String path = BASE_PATH + "GameMessage.json";
        gson.put("category", category);
        gson.put("board", boardJson(board));
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message(boolean turn) {
        gson = new JSONObject();
        String path = BASE_PATH + "TurnMessage.json";
        gson.put("category", "turn");
        gson.put("turn", turn);
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message(int startRow, int startColumn, int finalRow, int finalColumn) {
        gson = new JSONObject();
        String path = BASE_PATH + "MoveMessage.json";
        gson.put("category", "pick");
        gson.put("startRow", startRow);
        gson.put("startColumn", startColumn);
        gson.put("finalRow", finalRow);
        gson.put("finalColumn", finalColumn);
        int size = 0;
        if (startRow == finalRow) {
            size = finalColumn - startColumn + 1;
        } else if (startColumn == finalColumn) {
            size = finalRow - startRow + 1;
        }
        gson.put("size", size);
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message(List<Item> picked) {
        gson = new JSONObject();
        String path = BASE_PATH + "PickMessage.json";
        gson.put("category", "picked");
        JSONArray ItemList = new JSONArray();
        for (int i = 0; i < picked.size(); i++) {
            JSONObject Item = new JSONObject();
            Item.put("color", picked.get(i).color().toString());
            Item.put("value", picked.get(i).number());
            ItemList.add(Item);
        }
        gson.put("picked", ItemList);
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message(String category, Bookshelf bookshelf, Board board, int score) {
        gson = new JSONObject();
        String path = BASE_PATH + "UpdateMessage.json";
        gson.put("category", category);
        gson.put("bookshelf", bookshelfJson(bookshelf));

        gson.put("board", boardJson(board));
        gson.put("score", score);

        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message(String category, List<Integer> sort) {
        gson = new JSONObject();
        String path = BASE_PATH + "SortMessage.json";
        gson.put("category", category);
        JSONArray array = new JSONArray();
        JSONObject item = new JSONObject();
        for (int i : sort) {
            item.put("value", i);
            array.add(i);
            item.clear();
        }
        gson.put("sort", array);
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getPick() {
        List<Integer> pick = new ArrayList<>();
        pick.add((int) gson.get("startRow"));
        pick.add((int) gson.get("startColumn"));
        pick.add((int) gson.get("finalRow"));
        pick.add((int) gson.get("finalColumn"));
        return pick;
    }

    public List<String> getWinners() {
        List<String> winners = new ArrayList<>();
        int size = (int) gson.get("size");
        for (int i = 0; i < size; i++) {
            String name = (String) gson.get("name" + i);
            winners.add(name);
        }
        return winners;
    }

    public List<Integer> getSort() {
        List<Integer> sort = new ArrayList<>();
        JSONArray array = (JSONArray) gson.get("sort");
        for (int i = 0; i < array.size(); i++) {
            sort.add((int) array.get(i));
        }
        return sort;
    }

    public JSONArray bookshelfJson(Bookshelf bookshelf) {
        JSONArray bookshelfItemList = new JSONArray();
        for (int i = 0; i < Bookshelf.getRows(); i++) {
            for (int j = 0; j < Bookshelf.getColumns(); j++) {
                JSONObject bookshelfItem = new JSONObject();
                bookshelfItem.put("row", i);
                bookshelfItem.put("column", j);
                JSONObject item = new JSONObject();
                if (bookshelf.getItemAt(i, j).isPresent()) {
                    JSONArray itemThings = new JSONArray();
                    item.put("color", bookshelf.getItemAt(i, j).get().color().toString());
                    item.put("value", bookshelf.getItemAt(i, j).get().number());
                    itemThings.add(item);
                    bookshelfItem.put("item", itemThings);
                } else {
                    bookshelfItem.put("item", null);
                }
                bookshelfItemList.add(bookshelfItem);
            }
        }
        return bookshelfItemList;
    }

    public JSONArray boardJson(Board board) {
        JSONArray boardMatrix = new JSONArray();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JSONObject boardItem = new JSONObject();
                boardItem.put("row", i);
                boardItem.put("column", j);
                JSONObject item = new JSONObject();
                if (board.getItem(i, j) == null) {
                    boardItem.put("item", null);
                } else {
                    JSONArray itemThings = new JSONArray();
                    item.put("color", board.getItem(i, j).color().toString());
                    item.put("value", board.getItem(i, j).number());
                    itemThings.add(item);
                    boardItem.put("item", itemThings);
                }
                boardMatrix.add(boardItem);
            }
        }
        return boardMatrix;
    }

    public int getTurn() {
        return (int) gson.get("turn");
    }

    public int getIntMessage(String type) {
        return (int) gson.get(type);
    }

    public int getInsert() {
        return (int) gson.get("insert");
    }

    public int getPickedSize() {
        return (int) gson.get("size");
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

    public int getPosition() {
        return (int) gson.get("position");
    }

    public int getPersonalGoal() {
        return (int) gson.get("personal_goal");
    }

    public String getCommonGoalLayout(int i) {
        return (String) gson.get("commonGoalLayout" + i);
    }

    public List<Item> getPicked() {
        List<Item> picked = new ArrayList<>();
        JSONArray pickedJson = (JSONArray) gson.get("picked");
        for (Object obj : pickedJson) {
            JSONObject item = (JSONObject) obj;
            String color = (String) item.get("color");
            int value = (int) item.get("value");
            picked.add(new Item(Color.valueOf(color), value));
        }
        return picked;
    }

    public Bookshelf getBookshelf() {
        //TODO: change JSON Bookshelf to and array of columns (array of arrays)
        Bookshelf bookshelf = new Bookshelf();
        JSONArray bookshelfJson = (JSONArray) gson.get("bookshelf");
        for (Object obj : bookshelfJson) {
            JSONObject bookshelfItem = (JSONObject) obj;
            int row = (int) bookshelfItem.get("row");
            int column = (int) bookshelfItem.get("column");
            JSONArray itemJson = (JSONArray) bookshelfItem.get("item");
            if (itemJson == null) {
                bookshelf.setItem(row, column, Optional.empty());
            } else {
                JSONObject item = (JSONObject) itemJson.get(0);
                String color = (String) item.get("color");
                int value = (int) item.get("value");
                bookshelf.setItem(row, column, Optional.of(new Item(Color.valueOf(color), value)));
            }
        }
        return bookshelf;
    }

    public Board getBoard() throws IOException, ParseException {
        Board board = new Board();
        JSONArray boardJson = (JSONArray) gson.get("board");
        for (Object obj : boardJson) {
            JSONObject boardItem = (JSONObject) obj;
            int row = (int) boardItem.get("row");
            int column = (int) boardItem.get("column");
            JSONArray itemJson = (JSONArray) boardItem.get("item");
            if (itemJson == null) {
                board.setItem(row, column, null);
            } else {
                JSONObject item = (JSONObject) itemJson.get(0);
                String color = (String) item.get("color");
                int value = (int) item.get("value");
                board.setItem(row, column, new Item(Color.valueOf(color), value));
            }
        }
        return board;
    }

    public List<String> getCardType() {
        List<String> cardType = new ArrayList<>();
        for (int i = 0; i < commonGoals; i++) {
            cardType.add((String) gson.get("commonGoalLayout " + i));
        }
        return cardType;
    }

    public List<Integer> getCardOccurrences() {
        List<Integer> cardOccurrences = new ArrayList<>();
        for (int j = 0; j < commonGoals; j++) {
            cardOccurrences.add((int) gson.get("occurrences " + j));
        }
        return cardOccurrences;
    }

    public List<Integer> getCardSize() {
        List<Integer> cardSize = new ArrayList<>();
        for (int j = 0; j < commonGoals; j++) {
            cardSize.add((int) gson.get("size " + j));
        }
        return cardSize;
    }

    public int getSize() {
        return (int) gson.get("size");
    }

    public List<Boolean> getCardHorizontal() {
        List<Boolean> cardHorizontal = new ArrayList<>();
        for (int i = 0; i < commonGoals; i++) {
            cardHorizontal.add((boolean) gson.get("horizontal " + i));
        }
        return cardHorizontal;
    }

    public JSONObject getGson() {
        return gson;
    }

    public String getJSONstring() {
        return gson.toJSONString();
    }
}
