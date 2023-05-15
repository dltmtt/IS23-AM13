package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utils.Coordinates;
import it.polimi.ingsw.utils.FullRoomException;

import java.util.*;
import java.util.stream.Collectors;

public class ServerController {

    private final List<Player> players;
    private final List<String> winnersNickname;
    private final List<String> pings;
    private final List<String> disconnected;
    private List<Item> currentPicked;
    private GameModel gameModel;
    private Room room = null;

    public ServerController() {
        players = new ArrayList<>();
        winnersNickname = new ArrayList<>();
        currentPicked = new ArrayList<>();
        pings = new ArrayList<>();
        disconnected = new ArrayList<>();
    }

    public void pingReceived(String username) {
        if (!pings.contains(username)) {
            pings.add(username);
        }
    }

    public void checkPings() {

        if (!new HashSet<>(pings).containsAll(players.stream().map(Player::getNickname).toList())) {
            missingOnes();
            for (String missing : disconnected) {
                System.out.println(missing + " disconnected");
            }
        } else {
            for (String ping : pings) {
                disconnected.remove(ping);
            }
            System.out.println("All connected");
        }
        pings.clear();
    }

    public void missingOnes() {
        for (Player player : players) {
            if (!pings.contains(player.getNickname()) && !disconnected.contains(player.getNickname())) {
                disconnected.add(player.getNickname());
            }
        }
    }

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
                return i + 1;
            }
        }
        return -1;
    }

    public void addPlayerByUsername(String username) {
        players.add(new Player(username, 0, false, false, false));
    }

    public void addPlayerAge(int age) {
        players.get(players.size() - 1).setAge(age);
    }

    public void addPlayerFirstGame(boolean firstGame) {
        players.get(players.size() - 1).setFirstGame(firstGame);
    }

    public int startRoom() throws FullRoomException {
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
        Thread pongThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30000);
                    checkPings();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        pongThread.start();
        return room.getListOfPlayers().size();
    }

    public String checkRoom() {
        if (room.full()) {
            gameModel = new GameModel(players);
            gameModel.start();
            return "Game started";
        }
        return null;
    }

    public String checkNumPlayer(int numPlayer) {
        if (numPlayer > 4 || numPlayer < 2) {
            return "retry";
        }
        room.setNumberOfPlayers(numPlayer);
        return "ok";
    }

    public int getPersonalGoalCard(int index) {
        return room.getListOfPlayers().get(index - 1).getPersonalGoal().getIndex();
    }

    public List<CommonGoal> getCommonGoals() {
        return Player.getCommonGoals();
    }

    public Bookshelf getBookshelf(int index) {
        return room.getListOfPlayers().get(index - 1).getBookshelf();
    }

    public Bookshelf getCurrentePlayerBookshelf() {
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
     *     <li>-1: the game is ended</li>
     *     <li>2: all other players are disconnected and there is only one connected</li>
     */

    public int yourTurn(int index) {
        if (disconnected.size() == players.size() - 1) {
            // tutti gli altri sono disconnessi, ne è rimasto solo uno
            return 2;
        }
        if (disconnected.contains(gameModel.getCurrentPlayer().getNickname())) {
            System.out.println(gameModel.getCurrentPlayer().getNickname() + " ' turn");
            changeTurn();
            return 0;
        }
        if (gameModel.isTheGameEnded()) {
            return -1;
        }
        if (gameModel.getCurrentPlayer().equals(room.getListOfPlayers().get(index - 1))) {
            return 1;
        }
        return 0;
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

    public boolean checkInsert(int column) {
        if (column >= 0 && column <= 4 && gameModel.getCurrentPlayer().getBookshelf().getFreeCellsInColumn(column) >= currentPicked.size()) {
            gameModel.move(currentPicked, column);
            return true;
        }
        return false;
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

    public int getCurrentPlayerPersonalGoal() {
        return gameModel.getCurrentPlayer().getPersonalGoal().getIndex();
    }
}
