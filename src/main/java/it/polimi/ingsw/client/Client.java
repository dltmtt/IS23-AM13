package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.GameView;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.Bookshelf;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This is abstract (non instantiable) because each client will either be an RMI client or a Socket client
 */

public abstract class Client extends UnicastRemoteObject implements Serializable, ClientCommunicationInterface {

    private final Object lock;
    private final Object lock1;
    private final Object lock2;
    private final Object insertRearrangeLock;
    /**
     * The <code>GameView</code> associated with this client.
     */
    public GameView gameView;
    /**
     * The username of the player using this client.
     */
    public String username;
    public Locale locale = new Locale.Builder().setLanguage("en").setRegion("US").build(); // Default locale
    public ResourceBundle bundle = ResourceBundle.getBundle("game", locale);
    private Boolean allReconnected = false; // Whether all the players have reconnected
    private Boolean theOnlyOne = false; // Whether this client is the only one in the game
    private Boolean serverConnection = false; // Whether there is a connection to the server

    public Client() throws RemoteException {
        super();
        gameView = MyShelfie.gameView;

        gameView.initiateConnection();

        boolean connected = false;
        long now = System.currentTimeMillis();
        long timeout = now + 10 * 1500; // 15 seconds

        // This is done in order to be able to start the client and
        // the server in parallel from the IDE (otherwise the client
        // could start before the server and fail to connect)
        while (System.currentTimeMillis() < timeout) {
            try {
                connect();

                // If we get here, no exception was thrown and we are connected
                connected = true;
                break;
            } catch (NotBoundException | IOException e) {
                // Unable to connect to server, retrying...
            }
        }

        if (connected) {
            gameView.connectionSuccess();
        } else {
            gameView.connectionError();
            System.exit(1);
        }
        lock = new Object();
        lock1 = new Object();
        lock2 = new Object();
        insertRearrangeLock = new Object();
    }

    /**
     * Sends a message to the server and returns the response.
     *
     * @param message the message to send
     */
    public abstract void sendMessage(Message message);

    /**
     * Terminates the client.
     */
    public void stop() {
        System.exit(0);
    }

    public void setView(GameView gameView) {
        this.gameView = gameView;
    }

    /**
     * Connects to the server.
     *
     * @throws IOException       if the connection fails
     * @throws NotBoundException if the server is not bound
     */
    public abstract void connect() throws IOException, NotBoundException;

    /**
     * Shows a message or a graphic to let the player know he has to wait
     * for other players to join in order to start the game.
     */
    public void waitingRoom() {
        gameView.waitingRoom();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Parser method, called when a message is received from the server.
     *
     * @param message the message to parse
     */
    @Override
    public void callBackSendMessage(Message message) {
        String category = message.getCategory();

        switch (category) {
            case "ping" -> serverConnection = true;
            case "pong" -> serverConnection = true;
            case "username" -> {
                setUsername(message.getUsername());
                checkServerConnection();
            }
            case "UsernameRetry" -> gameView.usernameError();
            case "UsernameRetryCompleteLogin" -> gameView.completeLoginError();
            case "chooseNumOfPlayer" -> gameView.playerChoice();
            case "numOfPlayersNotOK" -> gameView.playerNumberError();
            case "update" -> {
                HashMap<String, Bookshelf> bookshelves = message.getAllBookshelves();
                gameView.pickMyBookshelf(bookshelves);
                gameView.pickOtherBookshelf(bookshelves);
                gameView.showCurrentScore(message.getScore().get(3));
                gameView.showBoard(message.getBoard());
                gameView.updateScore(message.getTopOfScoringList(), message.getScore());
            }
            case "startGame" -> gameView.startGame(message);
            case "turn" -> myTurn();
            case "otherTurn" -> gameView.showMessage(otherTurnMessage(message.getArgument()));
            case "picked" -> {
                try {
                    if (gameView.showRearrange(message.getPicked())) {
                        rearrange(message);
                    }
                    insert();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case "pickRetry" -> {
                gameView.showMessage("Invalid pick. Retry.\n");

                // showPick() sends the message to the server
                gameView.showPick();
            }
            case "winners" -> gameView.showEndGame(message.getWinners(), message.getLosers());
            case "waitingRoom" -> waitingRoom();
            case "lastRound" -> gameView.showLastRound();
            case "gameAlreadyStarted" -> gameView.showGameAlreadyStarted();
            case "removePlayer" -> // stop();
                    gameView.showRemovePlayer();
            case "insertRetry" -> {
                String argument = message.getArgument();
                if (argument.equals("notValidNumber")) {
                    gameView.showMessage("Invalid column. Retry.");
                } else {
                    gameView.showMessage("There are not enough free cells in the column. Retry.\n");
                }
                gameView.promptInsert();
            }
            case "checkingDisconnection" -> gameView.showWaiting();
            case "reconnected" -> {
                theOnlyOne = false;
                gameView.setTheOnlyOne(false);
                String username = message.getArgument();
                Boolean isMyTurn = message.getPlayerTurn().equals(this.username);
                gameView.enableGame(isMyTurn);
                gameView.showMessage(reconnectionMessage(username));
            }
            case "youAloneBitch" -> {
                gameView.showMessage(onlyPlayerMessage());
                theOnlyOne = true;
                gameView.setTheOnlyOne(true);
                gameView.disableGame();
                waitForReconnection();
            }
            case "waitingRoomForReconnect" -> {
                gameView.showMessage("Waiting for other players to reconnect...\n");
                waitForReconnectionAfterServerDown();
            }
            case "AllIn" -> allReconnected = true;
            default -> throw new IllegalArgumentException("Invalid message category: " + category);
        }
    }

    private String onlyPlayerMessage() {
        return bundle.getString("onlyPlayer");
    }

    private String reconnectionMessage(String username) {
        return bundle.getString("reconnection") + username;
    }

    /**
     * Waits 60 seconds for other players to reconnect. If nobody reconnects,
     * player wins the game and exits.
     */
    public void waitForReconnection() {
        new Thread(() -> {
            synchronized (lock1) {
                try {
                    Thread.sleep(120000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (theOnlyOne) {
                    gameView.showMessage("Nobody reconnected, everyone hates you, nobody wants to play with you. You won champion!\n");
                    System.exit(0);
                }
            }
        }).start();
    }

    public void waitForReconnectionAfterServerDown() {
        Thread wait = new Thread(() -> {
            synchronized (lock2) {
                try {
                    Thread.sleep(120000);
                    if (!allReconnected) {
                        gameView.showMessage("Other players didn't reconnect. Exiting...");
                        System.exit(0);
                    }
                    allReconnected = false;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        wait.start();
    }

    /**
     * Checks if the server is still connected.
     * If it's not, exits the game.
     */
    public void checkServerConnection() {
        new Thread(() -> {
            while (true) {
                try {
                    sendMessage(new Message("ping"));
                    synchronized (lock) {
                        Thread.sleep(20000);
                        if (!serverConnection) {
                            System.err.println("Server is down. Exiting...");
                            System.exit(0);
                        }
                        serverConnection = false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Asks the player to pick some tiles and sends the message to the server.
     */
    public void myTurn() {
        gameView.showMessage(yourTurnMessage());
        Thread turnThread = new Thread(() -> gameView.showPick());
        turnThread.start();
    }

    /**
     * Asks the player to insert the tiles they picked and sends the
     * message to the server.
     */
    public void insert() {
        Thread insThread = new Thread(() -> {
            synchronized (insertRearrangeLock) {
                gameView.promptInsert();
            }
        });
        insThread.start();
    }

    /**
     * Asks the player to rearrange the tiles they picked and sends the
     * message to the server.
     *
     * @param message the message containing the tiles to rearrange
     */
    public void rearrange(Message message) {
        Thread threadRearrange = new Thread(() -> {
            synchronized (insertRearrangeLock) {
                gameView.rearrangeProcedure(message.getPicked());
            }
        });
        threadRearrange.start();
    }

    /**
     * Sets the game language to the specified one.
     *
     * @param language the language selected (2 letters)
     * @param country  the country selected (2 letters)
     */
    public void setLanguage(String language, String country) {
        locale = new Locale.Builder().setLanguage(language).setRegion(country).build();
        bundle = ResourceBundle.getBundle("game", locale);
    }

    public String yourTurnMessage() {
        return bundle.getString("yourTurn");
    }

    public String otherTurnMessage(String username) {
        return bundle.getString("otherTurn") + username + bundle.getString("otherTurn2");
    }
}
