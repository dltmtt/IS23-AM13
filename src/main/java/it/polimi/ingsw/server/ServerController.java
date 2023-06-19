package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientCommunicationInterface;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Coordinates;
import it.polimi.ingsw.utils.FullRoomException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ServerController {

    /**
     * The names of the players that have lost the connection
     */
    public final List<String> disconnectedPlayers;
    public final HashMap<String, Integer> pongLost;
    public final List<String> pongReceived;
    private final List<Player> players;
    private final List<String> winnersNickname;
    private final HashMap<String, ClientCommunicationInterface> rmiClients;
    private final HashMap<String, SocketClientHandler> tcpClients;
    private final List<Integer> finalPoints;
    public int numberOfPlayers = 0;
    private HashMap<ClientCommunicationInterface, PersonalGoal> personalGoalRMI;
    private HashMap<SocketClientHandler, PersonalGoal> personalGoalTCP;
    private boolean gameIsStarted = false;
    private boolean printedTurn = false;
    private List<Item> currentPicked;
    private GameModel gameModel;
    private Room room = null;

    /**
     * Constructor for the ServerController class
     * It initializes the lists of players, winners, pings and disconnectedPlayers players
     * It also initializes the HashMap of RMI clients
     */
    public ServerController() {
        players = new ArrayList<>();
        winnersNickname = new ArrayList<>();
        currentPicked = new ArrayList<>();
        disconnectedPlayers = new ArrayList<>();
        rmiClients = new HashMap<>();
        tcpClients = new HashMap<>();
        pongLost = new HashMap<>();
        pongReceived = new ArrayList<>();
        finalPoints = new ArrayList<>();
    }

    public List<Integer> getFinalPoints() {
        return finalPoints;
    }

    /**
     * Get the usernames of the players in the game.
     *
     * @return the list of the players' usernames
     */
    public List<String> getPlayersUsername() {
        return players.stream().map(Player::getNickname).toList();
    }

    public void pong(String username) {
        if (!pongReceived.contains(username)) {
            pongReceived.add(username);
        }
        pongLost.remove(username);
        pongLost.put(username, 0);
    }

    public void removePong(String username) {
        pongReceived.remove(username);
    }

    public void addPongLost(String username) {
        int currentPongLost = pongLost.get(username);
        pongLost.remove(username);
        pongLost.put(username, currentPongLost + 1);
    }

    public void removeClientByUsername(String username) {
        tcpClients.remove(username);
        rmiClients.remove(username);
    }

    /**
     * Checks whether the game has been saved or not. This is done by
     * checking if the JSON which contains the state of the game exists.
     *
     * @return true if the game has been saved, false otherwise
     */
    public boolean isGameSaved() {
        File jsonGame = new File("src/main/java/it/polimi/ingsw/commons/backUp.json");
        return jsonGame.exists();
    }

    /**
     * Loads the game saved in the JSON file.
     *
     * @throws IOException if the file does not exist
     */
    public void loadLastGame() throws IOException {
        File jsonGame = new File("src/main/java/it/polimi/ingsw/commons/backUp.json");
        Message lastGame = new Message(jsonGame);
        players.addAll(lastGame.getPlayers());
        System.out.println("Loading last game...");
        gameModel = new GameModel(players);
        gameModel.setGame(lastGame.getBoard(), lastGame.getCommonGoals(players.size()));
        gameModel.setCurrentPlayer(lastGame.getCurrentPlayer());
        disconnectedPlayers.addAll(players.stream().map(Player::getNickname).toList());
        changeTurn();
        System.out.println("Last game loaded");
    }

    // public int getPosition(SocketClientHandler client) {
    //     for (int i = 0; i < tcpClients.size(); i++) {
    //         if (tcpClients.get(i).equals(client)) {
    //         }
    //     }
    // }

    /**
     * Adds a player to the list of disconnected players
     *
     * @param username the identifier of the disconnected player
     */
    public void disconnect(String username) {
        disconnectedPlayers.add(username);
        removeClientByUsername(username);
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        room.setNumberOfPlayers(numberOfPlayers);
    }

    /**
     * Return the list of players' nicknames that cannot play because
     * there are already 4 players in the room.
     *
     * @return the list of players' nicknames that cannot play
     */
    public List<String> getExtraPlayers() {
        List<String> extraPlayers = new ArrayList<>();
        for (int i = numberOfPlayers; i < players.size(); i++) {
            extraPlayers.add(players.get(i).getNickname());
        }
        removePlayers(extraPlayers);
        System.out.println("Extra players: " + extraPlayers);
        return extraPlayers;
    }

    public void removePlayers(List<String> playersToRemove) {
        playersToRemove.forEach((player) -> {
            players.removeIf(p -> p.getNickname().equals(player));
            rmiClients.remove(player);
            tcpClients.remove(player);
            disconnectedPlayers.remove(player);
        });
    }

    /**
     * Method to add a player to the game
     *
     * @param username is the nickname of the player
     * @param client   is the client associated to the player
     */
    public void addClient(String username, ClientCommunicationInterface client) {
        rmiClients.put(username, client);
    }

    /**
     * Method to add a player to the game
     *
     * @param username the nickname of the player
     * @param client   the client associated to the player
     */
    public void addClient(String username, SocketClientHandler client) {
        tcpClients.put(username, client);
    }

    /**
     * @return hashmap of RMI clients
     */
    public HashMap<String, ClientCommunicationInterface> getRmiClients() {
        return rmiClients;
    }

    /**
     * @return hashmap of TCP clients
     */
    public HashMap<String, SocketClientHandler> getTcpClients() {
        return tcpClients;
    }

    /**
     * @param username the username chosen by the player
     * @return a number representing the availability of the username
     * <ul>
     *   <li>1: the username is available</li>
     *   <li>0: the username is already taken</li>
     *   <li>-1: the username was disconnectedPlayers</li>
     *   <li>-2: the username has reconnected after the server went down</li>
     * </ul>
     */
    public int checkUsername(String username) {
        // if (isPresentInJson(username)) {
        //     return -2;
        // }
        for (Player player : players) {
            if (player.getNickname().equals(username)) {
                if (disconnectedPlayers.contains(username)) {
                    disconnectedPlayers.remove(username);
                    return -1;
                }
                return 0;
            }
        }
        return 1;
    }

    /**
     * Return the position of the player in the list of players.
     * If the player is not present in the list, it returns -1.
     * The list is ordered by the order of the players' turns.
     *
     * @param username the username of the player
     * @return the position of the player in the list of players,
     * or -1 if the player is not in the list
     */
    public int getPositionByUsername(String username) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getNickname().equals(username)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Saves a player in the list of players.
     *
     * @param username  the username of the player
     * @param age       the age of the player
     * @param firstGame true if it's the first game of the player
     */
    public void addPlayer(String username, int age, boolean firstGame) {
        players.add(new Player(username, age, firstGame, false, false));
    }

    /**
     * Creates a new room and adds the player to it if it doesn't exist,
     * otherwise adds the player to the existing room. If the room didn't
     * exist, the player is the first player of the game.
     *
     * @throws FullRoomException if the room is full
     */
    public void startRoom() throws FullRoomException {
        if (room == null) {
            Random random = new Random();
            int idRoom = random.nextInt(1000);
            room = new Room(idRoom);
            players.get(players.size() - 1).setIsFirstPlayer(true);
            room.addPlayer(players.get(players.size() - 1));
        } else if (!room.full()) {
            players.get(players.size() - 1).setIsFirstPlayer(false);
            room.addPlayer(players.get(players.size() - 1));
        } else {
            // TODO: gestire l'eccezione
            throw new FullRoomException("Room is full");
        }
    }

    /**
     * Starts the game if it's possible.
     *
     * @return a number representing the status of the room:
     * <ul>
     *     <li>1: the room is full and the game can start</li>
     *     <li>0: the maximum number of players is not set</li>
     *     <li>-1: the room is full and there are too many players</li>
     * </ul>
     */
    public int checkRoom() {
        if (room.isMaxPlayersSet() && room.full()) {
            gameModel = new GameModel(players);
            gameModel.start();
            gameIsStarted = true;
            return 1;
        }
        if (room.isMaxPlayersSet() && room.tooManyPlayers()) {
            gameModel = new GameModel(players);
            gameModel.start();
            gameIsStarted = true;
            return -1;
        }
        return 0;
    }

    public boolean isGameStarted() {
        return gameIsStarted;
    }

    public String getCurrentPlayer() {
        return gameModel.getCurrentPlayer().getNickname();
    }

    public ClientCommunicationInterface getCurrentClientRmi() {
        return rmiClients.get(gameModel.getCurrentPlayer().getNickname());
    }

    public SocketClientHandler getCurrentClientTcp() {
        return tcpClients.get(gameModel.getCurrentPlayer().getNickname());
    }

    public int getScore(int position) {
        return gameModel.getPlayers().get(position).calculateScore();
    }

    /**
     * @return a map of the bookshelves and the players who own them
     */
    public HashMap<Bookshelf, String> getBookshelves() {
        return gameModel.getPlayers().stream().collect(Collectors.toMap(Player::getBookshelf, Player::getNickname, (a, b) -> b, HashMap::new));
    }

    /**
     * Checks if the number of players is valid (between 2 and 4).
     *
     * @param numPlayer the number of players inserted by the first user
     * @return "ok" if the number of players is valid, "retry" otherwise
     */
    public String checkNumPlayer(int numPlayer) {
        if (numPlayer > 4 || numPlayer < 2) {
            return "retry";
        }
        return "ok";
    }

    public int getPersonalGoalCard(int index) {
        Player player = players.get(index);
        PersonalGoal pg = player.getPersonalGoal();
        return pg.getIndex();
    }

    public List<CommonGoal> getCommonGoals() {
        return Player.getCommonGoals();
    }

    public Bookshelf getBookshelf(int index) {
        return room.getListOfPlayers().get(index).getBookshelf();
    }

    public Bookshelf getCurrentPlayerBookshelf() {
        return gameModel.getCurrentPlayer().getBookshelf();
    }

    /**
     * Refills the board if there are no more items to pick and if the item bag is not empty.
     *
     * @return true if the board has been refilled, false otherwise
     */
    public boolean refill() {
        // If there are no more items to pick or if all the items are isolated and if the item bag is not empty
        return (gameModel.getLivingRoom().numLeft() == 0 || gameModel.getLivingRoom().allIsolated()) && !gameModel.getLivingRoom().getItemBag().isEmpty();
    }

    /**
     * Returns the board and refills it if it's necessary.
     *
     * @return the board
     */
    public Board getBoard() {
        if (refill()) {
            gameModel.getLivingRoom().fill();
        }
        return gameModel.getLivingRoom();
    }

    /**
     * this method checks if the game is ended or not or if it's the last round
     *
     * @return -1,0 or 1:
     * <ul>
     *   <li>-1: the game has ended
     *   <li>0: it's the last round
     *   <li>1: the game is not ended and it's not the last round
     * </ul>
     */
    public int checkGameStatus() {
        if (gameModel.isTheGameEnded()) {
            return -1;
        } else if (gameModel.isLastRound()) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Checks if a pick is valid.
     *
     * @param move the representation of the pick
     * @return "ok" if the pick is valid, "no" otherwise
     */
    public String checkPick(List<Integer> move) {
        if (move.size() != 4) {
            return "no";
        }
        if (Objects.equals(move.get(0), move.get(2)) || Objects.equals(move.get(1), move.get(3))) {
            List<Coordinates> coordinatesOfPick = createCoordinateList(move);
            if (gameModel.getLivingRoom().OrderAndMaxOf3(coordinatesOfPick)) {
                if (gameModel.getLivingRoom().allNotNull(coordinatesOfPick)) {
                    if (gameModel.getLivingRoom().AtLeastOneFree(coordinatesOfPick)) {
                        return "ok";
                    }
                }
            }
        }
        return "no";
    }

    /**
     * Changes the turn in the model or ends the game.
     */
    public void changeTurn() {
        int currentPlayerIndex = players.indexOf(gameModel.getCurrentPlayer());
        int nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
        if (gameModel.isLastRound()) {
            if (players.get(nextPlayerIndex).isFirstPlayer()) {
                gameModel.setTheGameEnded(true);
            } else {
                gameModel.setCurrentPlayer(players.get(nextPlayerIndex));
            }
        } else {
            gameModel.setCurrentPlayer(players.get(nextPlayerIndex));
        }
        printedTurn = false;
    }

    /**
     * @return the list of winners
     */
    public List<String> setWinner() {
        List<Player> winners = new ArrayList<>();
        List<Integer> finalScoring = new ArrayList<>();

        for (Player player : players) {
            finalScoring.add(player.calculateScore());
        }
        finalPoints.addAll(finalScoring);

        Integer max = finalScoring.stream().max(Integer::compare).get();

        // if the value max is contained more than one time in the list finalScoring
        if (Collections.frequency(finalScoring, max) > 1) {
            // There is a tie
            for (Integer score : finalScoring) {
                if (score == max) {
                    if (!players.get(finalScoring.indexOf(score)).isFirstPlayer()) {
                        winners.add(players.get(finalScoring.indexOf(score)));
                        finalPoints.remove(finalScoring.indexOf(score));
                    }
                }
            }
        } else {
            winners.add(players.get(finalScoring.indexOf(max)));
            finalPoints.remove(finalScoring.indexOf(max));
        }

        List<String> officialWinners = winners.stream().map(Player::getNickname).collect(Collectors.toList());
        winnersNickname.addAll(officialWinners);

        return officialWinners;
    }

    /**
     * Picks the items from the board and returns them.
     *
     * @param picked the representation of the pick
     * @return the list of picked items
     */
    public List<Item> getPicked(List<Integer> picked) {
        currentPicked.clear();
        List<Coordinates> currentPickedCoordinates = new ArrayList<>();
        for (int i = 0; i < picked.size(); i += 2) {
            currentPickedCoordinates.add(new Coordinates(picked.get(i), picked.get(i + 1)));
        }
        currentPicked = gameModel.getLivingRoom().pickFromBoard(currentPickedCoordinates);
        return currentPicked;
    }

    /**
     * Rearranges the picked items according to the order given by the player.
     *
     * @param sort the list of indexes of the items in the order given by the player
     */
    public void rearrangePicked(List<Integer> sort) {
        currentPicked = gameModel.getCurrentPlayer().rearrangePickedItems(currentPicked, sort);
    }

    /**
     * this method checks if the player can insert the picked items in the bookshelf
     *
     * @param column the column where the player wants to insert the picked items
     * @return -1,0 or 1:
     * <ul>
     *     <li>-1: the player can't insert the picked items in the bookshelf because there are not enough free cells</li>
     *     <li>0: the player can't insert the picked items in the bookshelf because the column is not valid</li>
     *     <li>1: the player can insert the picked items in the bookshelf</li>
     */
    public int checkInsert(int column) {
        if (gameModel.getCurrentPlayer().getBookshelf().getFreeCellsInColumn(column) < currentPicked.size()) {
            System.out.println("You can't insert the picked items in the bookshelf because there are not enough free cells");
            return -1;
        }

        if (column < 0 || column > 4) {
            System.out.println("You can't insert the picked items in the bookshelf because the column is not valid");
            return 0;
        }

        gameModel.move(currentPicked, column);
        System.out.println("You have inserted the picked items in the bookshelf");
        // saveGame();
        return 1;
    }

    public void saveGame() {
        new Message(players, getCommonGoals(), getBoard(), gameModel.getTopScoringPoints(), gameModel.getCurrentPlayer().getNickname());
    }

    public List<Coordinates> createCoordinateList(List<Integer> integers) {
        List<Coordinates> coordinates = new ArrayList<>();
        for (int i = 0; i < integers.size(); i += 2) {
            coordinates.add(new Coordinates(integers.get(i), integers.get(i + 1)));
        }
        return coordinates;
    }

    public List<String> getWinnersNickname() {
        return setWinner();
    }

    /**
     * @return the list of the winners' scores
     */
    public List<Integer> getWinnersScore() {
        List<Integer> winnersScore = new ArrayList<>();
        for (String nickname : winnersNickname) {
            for (Player player : players) {
                if (player.getNickname().equals(nickname)) {
                    winnersScore.add(player.calculateScore());
                    players.remove(player);
                }
            }
        }
        return winnersScore;
    }

    /**
     * @return true if the player is the first player, false otherwise
     */
    public boolean isFirst() {
        return tcpClients.size() + rmiClients.size() == 1;
    }

    public int getCurrentPlayerPersonalGoal() {
        return gameModel.getCurrentPlayer().getPersonalGoal().getIndex();
    }

    public Bookshelf getBookshelf(JSONObject json) {
        Bookshelf bookshelf = new Bookshelf();
        JSONArray bookshelfJson = (JSONArray) json.get("bookshelf");
        for (Object obj : bookshelfJson) {
            JSONObject bookshelfItem = (JSONObject) obj;
            String rowString = (String) bookshelfItem.get("row");
            String columnString = (String) bookshelfItem.get("column");
            int row = Integer.parseInt(rowString);
            int column = Integer.parseInt(columnString);
            JSONArray itemJson = (JSONArray) bookshelfItem.get("item");
            if (itemJson == null) {
                bookshelf.setItem(row, column, Optional.empty());
            } else {
                JSONObject item = (JSONObject) itemJson.get(0);
                String color = (String) item.get("color");
                String valueString = (String) item.get("value");
                int value = Integer.parseInt(valueString);
                bookshelf.setItem(row, column, Optional.of(new Item(Color.valueOf(color), value)));
            }
        }
        return bookshelf;
    }

    /**
     * @return the top scoring list of each common goal
     */
    public List<Integer> getTopOfScoring() {
        return gameModel.getTopScoringPoints();
    }

    /**
     * @param position the position of the player
     * @return the list of the points of the player
     */
    public List<Integer> allPoints(int position) {
        return gameModel.getAllPoints(players.get(position));
    }
}
