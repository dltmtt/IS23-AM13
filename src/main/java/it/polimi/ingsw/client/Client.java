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

import static it.polimi.ingsw.utils.CliUtilities.RESET;
import static it.polimi.ingsw.utils.CliUtilities.SUCCESS_COLOR;

// This is abstract (non instantiable) because each client will either
// be an RMI client or a Socket client
public abstract class Client extends UnicastRemoteObject implements Serializable, ClientCommunicationInterface {

    public GameView gameView;
    public String username;
    boolean theOnlyOne = false;
    boolean serverConnection = false;
    private int myPosition;

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
    }

    /**
     * Sends a message to the server and returns the response.
     *
     * @param message the message to send
     */
    public abstract void sendMessage(Message message);

    public void stop() {
        System.exit(0);
    }

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

    public abstract void connect() throws IOException, NotBoundException;

    /**
     * Shows a message or a graphic to let the player know he has to wait
     * for other players to join in order to start the game.
     */
    public void waitingRoom() {
        gameView.waitingRoom();
    }

    /**
     * Shows the board and asks the user to pick some tiles.
     * If the pick is valid, asks the user to rearrange the tiles (if he wants to),
     * then asks the user to choose a column to place the tiles in.
     * At the end of the turn, the player returns to the waiting room. // TODO: does he?
     */


    /*
    public void endGame() {
        gameView.endGame();
    }
     */
    public int getMyPosition() {
        return myPosition;
    }

    public void setMyPosition(int position) {
        this.myPosition = position;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void startGame(Message message) {
        gameView.startGame(message);
    }

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
                // System.out.println("Ping!");
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
                HashMap<Bookshelf, String> bookshelves = message.getAllBookshelves();
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
                    insert(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case "pickRetry" -> {
                gameView.showMessage("Invalid pick. Retry.");

                // showPick() sends the message to the server
                gameView.showPick();
            }
            case "winners" ->
                    gameView.showEndGame(message.getWinners(), message.getWinnersScore(), message.getOtherPlayersName(), message.getOtherScores());
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
            case "disconnected" -> {
                System.err.println("You have been disconnected from the server. Let Matteo know about this.");
                // List<String> disconnected = message.getDisconnected();
                // gameView.showDisconnection(disconnected);
                // stop();
            }
            case "reconnected" -> {
                theOnlyOne = false;
                System.out.println("Reconnected");
                String username = message.getArgument();
                gameView.showMessage(username + " reconnected.\n");
            }
            case "youAloneBitch" -> {
                gameView.showMessage("You are the only player in the game. Please wait for other players to reconnect.\n");
                theOnlyOne = true;
                waitForReconnection();
            }
            default -> throw new IllegalArgumentException("Invalid message category: " + category);
        }
    }

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

    public void checkServerConnection() {
        new Thread(() -> {
            while (true) {
                try {
                    sendMessage(new Message("ping"));
                    Thread.sleep(10000);
                    // System.out.println("after sleep");
                    if (!serverConnection) {
                        System.err.println("Server is down. Exiting...");
                        System.exit(0);
                    }
                    serverConnection = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void myTurn() {
        Thread turnThread = new Thread(() -> {
            gameView.showPick();
        });
        turnThread.start();
    }

    public void insert(Message message) {
        Thread insThread = new Thread(() -> {
            gameView.promptInsert();
        });
        insThread.start();
    }

    public void rearrange(Message message) {
        Thread threadRearrange = new Thread(() -> {
            gameView.rearrangeProcedure(message.getPicked());
        });
        threadRearrange.start();
    }
}
