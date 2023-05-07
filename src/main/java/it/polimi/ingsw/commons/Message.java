package it.polimi.ingsw.commons;

import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.SettingLoader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Message implements Serializable {

    private static final String BASE_PATH = "src/main/java/it/polimi/ingsw/commons/";
    private final JSONObject gson;
    private int commonGoals = 0;

    /**
     * Constructor for the login type of message
     * when called, only one of the parameters is used, the others are set to default values
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

    public Message(String category, int posix) {
        gson = new JSONObject();
        String path = BASE_PATH + "LoginMessage.json";
        gson.put("category", category);
        gson.put("posix", posix);
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
        String path = BASE_PATH + "SingleMessage.json";
        gson.put("category", singleMessage);
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message(int posix) {
        gson = new JSONObject();
        String path = BASE_PATH + "PosixMessage.json";
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
        String path = BASE_PATH + "GameMessage.json";
        SettingLoader.loadBookshelfSettings();
        gson.put("category", "startGame");
        gson.put("personal_goal", personalGoal);

        for (int i = 0; i < commonGoalList.size(); i++) {
            gson.put("commonGoalLayout " + i, commonGoalList.get(i).getLayout().getName());

            if (commonGoalList.get(i).getLayout().getName().equals("fullLine")) {
                gson.put("occurrences " + i, commonGoalList.get(i).getLayout().getOccurrences());
                gson.put("horizontal " + i, commonGoalList.get(i).getLayout().isHorizontal());
                gson.put("size " + i, 0);
            } else if (commonGoalList.get(i).getLayout().getName().equals("group")) {
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

    public Message(int startRow, int startColumn, int finalRow, int finalColumn, int bookshelfIndex) {
        gson = new JSONObject();
        String path = BASE_PATH + "MoveMessage.json";
        gson.put("category", "move");
        gson.put("startRow", startRow);
        gson.put("startColumn", startColumn);
        gson.put("finalRow", finalRow);
        gson.put("finalColumn", finalColumn);
        gson.put("bookshelfIndex", bookshelfIndex);
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message(String category, Bookshelf bookshelf, Board board) {
        gson = new JSONObject();
        String path = BASE_PATH + "UpdateMessage.json";
        gson.put("category", category);
        gson.put("bookshelf", bookshelfJson(bookshelf));

        gson.put("board", boardJson(board));

        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(gson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getMove() {
        List<Integer> move = new ArrayList<>();
        move.add((int) gson.get("startRow"));
        move.add((int) gson.get("startColumn"));
        move.add((int) gson.get("finalRow"));
        move.add((int) gson.get("finalColumn"));
        move.add((int) gson.get("bookshelfIndex"));
        return move;
    }

    //    public Message(int personalGoal, List<CommonGoal> commonGoalList, Bookshelf bookshelf, Board board) {
    //        gson = new JSONObject();
    //        String path = BASE_PATH + "GameMessage.json";
    //        gson.put("category", "startGame");
    //        gson.put("personal_goal", personalGoal);
    //        JSONArray commonGoalArray = new JSONArray();
    //        commonGoalArray.addAll(commonGoalList);
    //        gson.put("commonGoal", commonGoalArray);
    //        JSONArray bookshelfItemList = new JSONArray();
    //        //        for()
    //        //        gson.put("bookshelf", bookshelfItemList);
    //        //        gson.put("board", board.getBoardMatrix());
    //        try {
    //            FileWriter file = new FileWriter(path);
    //            file.write(gson.toJSONString());
    //            file.close();
    //        } catch (IOException e) {
    //            throw new RuntimeException(e);
    //        }
    //    }

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

    public boolean getTurn() {
        return (boolean) gson.get("turn");
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

    public List<String> getCardType() {
        List<String> cardType = new ArrayList<>();
        for (int i = 0; i < commonGoals; i++) {
            cardType.add((String) gson.get("commonGoalLayout " + i));
        }
        return cardType;
    }

    public List<Integer> getCardOccurences() {
        List<Integer> cardOccurences = new ArrayList<>();
        for (int j = 0; j < commonGoals; j++) {
            cardOccurences.add((int) gson.get("occurrences " + j));
        }
        return cardOccurences;
    }

    public List<Integer> getCardSize() {
        List<Integer> cardSize = new ArrayList<>();
        for (int j = 0; j < commonGoals; j++) {
            cardSize.add((int) gson.get("size " + j));
        }
        return cardSize;
    }

    public List<Boolean> getCardHorizotal() {
        List<Boolean> cardHorizontal = new ArrayList<>();
        for (int i = 0; i < commonGoals; i++) {
            cardHorizontal.add((boolean) gson.get("horizontal " + i));
        }
        return cardHorizontal;
    }

    public JSONObject getGson() {
        return gson;
    }
}
