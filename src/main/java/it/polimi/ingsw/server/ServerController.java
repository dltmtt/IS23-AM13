package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientCommunicationInterface;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utils.Coordinates;
import it.polimi.ingsw.utils.FullRoomException;

import java.util.*;
import java.util.stream.Collectors;

public class ServerController {

    private final List<Player> players;
    private final List<String> winnersNickname;
    private final List<String> pongs;
    private final List<String> disconnected;
    private final HashMap<String, ClientCommunicationInterface> rmiClients;
    private final HashMap<String, SocketClientHandler> tcpClients;
    private HashMap<ClientCommunicationInterface, PersonalGoal> personalGoalRMI;
    private HashMap<SocketClientHandler, PersonalGoal> personalGoalTCP;
    private int numPlayer = 0;
    private boolean gameIsStarted = false;
    private boolean printedTurn = false;
    private List<Item> currentPicked;
    private GameModel gameModel;
    private Room room = null;
    private boolean printedConn = false;
    private boolean printedDisco = false;

    /**
     * Constructor for the ServerController class
     * It initializes the lists of players, winners, pings and disconnected players
     * It also initializes the HashMap of RMI clients
     */
    public ServerController() {
        players = new ArrayList<>();
        winnersNickname = new ArrayList<>();
        currentPicked = new ArrayList<>();
        pongs = new ArrayList<>();
        disconnected = new ArrayList<>();
        rmiClients = new HashMap<>();
        tcpClients = new HashMap<>();
    }

    // public int getPostion(SocketClientHandler client) {
    //     for(int i = 0; i < tcpClients.size(); i++) {
    //         if(tcpClients.get(i).equals(client)) {
    //             return tcpClients.
    //         }
    //     }
    // }
public void addDisconnection(String username) {
        disconnected.add(username);
    }
    public void setNumPlayer(int numPlayer) {
        this.numPlayer = numPlayer;
        room.setNumberOfPlayers(numPlayer);
    }

    public List<String> getExtraPlayers() {
        List<String> ExtraPlayers = new ArrayList<>();
        for (int i = numPlayer; i < players.size(); i++) {
            ExtraPlayers.add(players.get(i).getNickname());
        }
        removePlayers(ExtraPlayers);
        System.out.println("Extra players: " + ExtraPlayers);
        return ExtraPlayers;
    }

    public void removePlayers(List<String> playersToRemove) {
        for (String player : playersToRemove) {
            remove(player);
        }
    }

    public void remove(String player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getNickname().equals(player)) {
                players.remove(i);
            }
        }
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
     * @param username is the nickname of the player
     * @param client   is the client associated to the player
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
     * Method to check if the ping of a player has been received
     * If it has not been received, the username is added to the list of pings
     *
     * @param username Username of the player
     */
    public void pongReceived(String username) {
        if (!pongs.contains(username)) {
            pongs.add(username);
            // System.out.println("Ping received from " + username);
        }
    }

    public boolean isAlive(String username) {
        if(pongs.contains(username)){
            System.out.println(username+" IS ALIVE");
            pongs.remove(username);
            return true;
        }
        return false;
    }

    // public void checkPings() {
    //     if (!new HashSet<>(pings).containsAll(players.stream().map(Player::getNickname).toList())) {
    //         missingOnes();
    //         if (!printedDisco) {
    //             for (String missing : disconnected) {
    //                 System.out.println(missing + " disconnected");
    //                 printedConn = false;
    //             }
    //             printedDisco = true;
    //             for (String username : tcpClients.keySet()) {
    //                 if (!disconnected.contains(username)) {
    //                     tcpClients.get(username).sendMessageToClient(new Message("disconnected", null, disconnected));
    //                 }
    //             }
    //             // for (String username : rmiClients.keySet()) {
    //             //     if (!disconnected.contains(username)) {
    //             //         try {
    //             //             rmiClients.get(username).callBackSendMessage(new Message("disconnected", null, disconnected));
    //             //         } catch (RemoteException e) {
    //             //             throw new RuntimeException(e);
    //             //         }
    //             //     }
    //             // }
    //         }
    //     } else {
    //         printedDisco = false;
    //         if (!printedConn) {
    //             System.out.println("All connected");
    //             printedConn = true;
    //         }
    //     }
    //     pings.clear();
    // }

    /**
     * This method is called when a ping is not received
     * If the player is not in the list of pings and not in the list of disconnected players, it is added to the list of disconnected players
     */
    // public void missingOnes() {
    //     for (Player player : players) {
    //         if (!pings.contains(player.getNickname()) && !disconnected.contains(player.getNickname())) {
    //             disconnected.add(player.getNickname());
    //         }
    //     }
    // }

    /**
     * @param username the username chosen by the player
     * @return a number representing the availability of the username
     * <ul>
     *   <li>1: the username is available</li>
     *   <li>0: the username is already taken</li>
     *   <li>-1: the username was disconnected</li>
     * </ul>
     */
    public int checkUsername(String username) {
        for (Player player : players) {
            if (player.getNickname().equals(username)) {
                if (disconnected.contains(username)) {
                    disconnected.remove(username);
                    return -1;
                } else {
                    return 0;
                }
            }
        }
        return 1;
    }

    public int getPositionByUsername(String username) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getNickname().equals(username)) {
                return i;
            }
        }
        return -1;
    }

    public void addPlayer(String username, int age, boolean firstGame) {
        players.add(new Player(username, age, firstGame, false, false));
    }

    public boolean checkAge(int age) {
        return age >= 8 && age <= 120;
    }

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
        // Thread pongThread = new Thread(() -> {
        //     while (true) {
        //         try {
        //             Thread.sleep(30000);
        //             checkPings();
        //         } catch (InterruptedException e) {
        //             throw new RuntimeException(e);
        //         }
        //     }
        // });
        // pongThread.start();
    }

    public int checkRoom() {
        if (room.isNumSetted() && room.full()) {
            gameModel = new GameModel(players);
            gameModel.start();
            gameIsStarted = true;
            return 1;
        }
        if (room.isNumSetted() && room.tooManyPlayers()) {
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

    // public ClientCommunicationInterface getCurrentClient() {
    //     return rmiClients.get(gameModel.getCurrentPlayer().getNickname());
    // }

    public SocketClientHandler getCurrentClient() {
        return tcpClients.get(gameModel.getCurrentPlayer().getNickname());
    }

    public int getScore(int position) {
        return gameModel.getPlayers().get(position).calculateScore();
    }

    public HashMap<Bookshelf, String> getBookshelves() {
        return gameModel.getPlayers().stream().collect(Collectors.toMap(Player::getBookshelf, Player::getNickname, (a, b) -> b, HashMap::new));
    }

    public String checkNumPlayer(int numPlayer) {
        if (numPlayer > 4 || numPlayer < 2) {
            return "retry";
        }
        return "ok";
    }

    public int getPersonalGoalCard(int index) {
        List<Player> list = room.getListOfPlayers();
        Player player = list.get(index);
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

    public boolean refill() {
        //(if there are no more items to pick or if all the items are isolated) and if the item bag is not empty
        return (gameModel.getLivingRoom().numLeft() == 0 || gameModel.getLivingRoom().allIsolated()) && !gameModel.getLivingRoom().getItemBag().isEmpty();
    }

    public Board getBoard() {
        if (refill()) {
            gameModel.getLivingRoom().fill();
        }
        return gameModel.getLivingRoom();
    }

    /**
     * @param index is the position of the player in the list of players
     * @return 1, 0, -1 or 2:
     * <ul>
     *     <li>1: it's the player's turn</li>
     *     <li>0: it's not the player's turn</li>
     *     <li>-1: the game has ended</li>
     *     <li>2: all other players are disconnected and there is only one connected</li>
     */
    public int yourTurn(int index) {

        if (!printedTurn) {
            System.out.println("It's " + gameModel.getCurrentPlayer().getNickname() + "'s turn");
            printedTurn = true;
        }
        if (disconnected.size() == players.size() - 1) {
            // tutti gli altri sono disconnessi, ne è rimasto solo uno
            return 2;
        }
        if (disconnected.contains(gameModel.getCurrentPlayer().getNickname())) {
            System.out.println("It's " + gameModel.getCurrentPlayer().getNickname() + "'s turn");
            changeTurn();
            return 0;
        }
        if (gameModel.isTheGameEnded()) {
            return -1;
        }
        if (gameModel.getCurrentPlayer().equals(room.getListOfPlayers().get(index))) {
            return 1;
        }
        return 0;
    }

    /**
     * this method checks if the game is ended or not or if it's the last round
     *
     * @return -1,0 or 1:
     * <ul>
     *     <li>-1: the game has ended</li>
     *      <li>0: it's the last round</li>
     *      <li>1: the game is not ended and it's not the last round</li>
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

    public String pick(List<Integer> move) {
        return checkPick(move);
    }

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

    public List<String> setWinner() {
        List<Player> winners = new ArrayList<>();
        List<Integer> finalScoring = new ArrayList<>();

        for (Player player : players) {
            finalScoring.add(player.calculateScore());
        }

        if (finalScoring.stream().distinct().count() < players.size()) {
            // there is a tie
            int max = finalScoring.stream().max(Integer::compare).get();
            for (Integer score : finalScoring) {
                if (score == max) {
                    winners.add(players.get(finalScoring.indexOf(score)));
                    players.remove(finalScoring.indexOf(score));
                }
            }
        } else {
            int max = finalScoring.stream().max(Integer::compare).get();
            winners.add(players.get(finalScoring.indexOf(max)));
        }

        if (winners.size() > 1) {
            winners.removeIf(Player::isFirstPlayer);
        }
        return winners.stream().map(Player::getNickname).collect(Collectors.toList());
    }

    public List<Item> getPicked(List<Integer> picked) throws IllegalAccessException {
        currentPicked.clear();
        List<Coordinates> currentPickedCoordinates = new ArrayList<>();
        for (int i = 0; i < picked.size(); i += 2) {
            currentPickedCoordinates.add(new Coordinates(picked.get(i), picked.get(i + 1)));
        }
        currentPicked = gameModel.getLivingRoom().pickFromBoard(currentPickedCoordinates);
        return currentPicked;
    }

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
            return -1;
        }
        if (column < 0 || column > 4) {
            return 0;
        }
        gameModel.move(currentPicked, column);
        return 1;
    }

    public List<Coordinates> createCoordinateList(List<Integer> integers) {
        List<Coordinates> coordinates = new ArrayList<>();
        for (int i = 0; i < integers.size(); i += 2) {
            coordinates.add(new Coordinates(integers.get(i), integers.get(i + 1)));
        }
        return coordinates;
    }

    public int getCurrentPlayerScore() {
        int score = gameModel.getCurrentPlayer().calculateScore();
        changeTurn();
        return score;
    }

    public List<String> getWinnersNickname() {
        return setWinner();
    }

    public List<Integer> getWinnersScore() {
        List<Integer> winnersScore = new ArrayList<>();

        for (String nickname : winnersNickname) {
            for (Player player : players) {
                if (player.getNickname().equals(nickname)) {
                    winnersScore.add(player.calculateScore());
                }
            }
        }
        return winnersScore;
    }

    public boolean isFirst() {
        return tcpClients.size() + rmiClients.size() == 1;
    }

    public int getCurrentPlayerPersonalGoal() {
        return gameModel.getCurrentPlayer().getPersonalGoal().getIndex();
    }
}
