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

import static it.polimi.ingsw.utils.CliUtilities.RESET;
import static it.polimi.ingsw.utils.CliUtilities.SUCCESS_COLOR;

// This is abstract (non instantiable) because each client will either
// be an RMI client or a Socket client
public abstract class Client extends UnicastRemoteObject implements Serializable, ClientCommunicationInterface {

    private final Object lock;
    /**
     * The <code>GameView</code> associated with this client.
     */
    public GameView gameView;
    /**
     * The username of the player using this client.
     */
    public String username;
    public Locale locale = new Locale.Builder().setLanguage("en").setRegion("US").build(); // Default locale
    private boolean theOnlyOne = false; // Whether this client is the only one in the game
    private Boolean serverConnection = false; // Whether there is a connection to the server


    public Client() throws RemoteException {
        super();
        // All these messages should probably be moved to the view
        System.out.print("Connecting to server... ");

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
            System.out.println(SUCCESS_COLOR + "connected" + RESET);
        } else {
            System.err.println("Unable to connect to the server. Is it running?");
            System.exit(1);
        }
        lock = new Object();
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

    // TODO: check if this is used
    public void startPingThread(String username) {
        Thread pingThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        pingThread.start();
    }

    /**
     * Starts the <code>gameView</code> and starts the login procedure.
     */
    public void start() {
        // gameView.setClient(this);
        gameView.startView(this);
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
     * Shows a message or a graphic to let the player know they has to wait
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
            case "ping" -> {
                serverConnection = true;
                // System.out.println("Pong!");
            }
            case "pong" -> {
                serverConnection = true;
//                System.out.println("Pong!" + serverConnection);
            }
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
                //gameView.setPlayers
                gameView.pickMyBookshelf(bookshelves);
                gameView.pickOtherBookshelf(bookshelves);
                gameView.showCurrentScore(message.getScore().get(3));
                gameView.showBoard(message.getBoard());
                gameView.updateScore(message.getTopOfScoringList(), message.getScore());
            }
            case "startGame" -> gameView.startGame(message);
            case "turn" -> myTurn();
            case "otherTurn" -> gameView.showMessage("It's " + message.getArgument() + "'s turn.\n");
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
            case "winners" ->
                //gameView.showEndGame(message.getWinners(), message.getWinnersScore(), message.getOtherPlayersName(), message.getOtherScores());
                    gameView.showEndGame(message.getWinners(), message.getLosers());
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
            case "checkingDisconnection" -> {
                gameView.showMessage("Server is checking if you disconnected...");
                // List<String> disconnected = message.getDisconnected();
                // gameView.showDisconnection(disconnected);
                // stop();
            }
            case "reconnected" -> {
                theOnlyOne = false;
                String username = message.getArgument();
                gameView.showMessage(username + " reconnected.\n");
            }
            case "youAloneBitch" -> {
                gameView.showMessage("You are the only player in the game. Please wait for other players to reconnect.\n");
                theOnlyOne = true;
                waitForReconnection();
            }
            case "waitingRoomForReconnect" -> {
                gameView.showMessage("Waiting for other players to reconnect...\n");
            }
            default -> throw new IllegalArgumentException("Invalid message category: " + category);
        }
    }

    /**
     * Waits 60 seconds for other players to reconnect. If nobody reconnects,
     * player wins the game and exits.
     */
    public void waitForReconnection() {
        new Thread(() -> {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (theOnlyOne) {
                gameView.showMessage("Nobody reconnected, everyone hates you, nobody wants to play with you. You won champion!\n");
                System.exit(0);
            }
        }).start();
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
//                        System.out.println("ping" + serverConnection);
                        // System.out.println("after sleep");
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
        gameView.showMessage("It's your turn.\n");
        Thread turnThread = new Thread(() -> {
            gameView.showPick();
        });
        turnThread.start();
    }

    /**
     * Asks the player to insert the tiles they picked and sends the
     * message to the server.
     */
    public void insert() {
        Thread insThread = new Thread(() -> {
            gameView.promptInsert();
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
            gameView.rearrangeProcedure(message.getPicked());
        });
        threadRearrange.start();
    }

    public void setLanguage(String language, String country) {
        locale = new Locale.Builder().setLanguage(language).setRegion(country).build();
    }

}
