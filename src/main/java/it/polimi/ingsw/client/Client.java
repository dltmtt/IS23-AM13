package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.GameView;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.utils.Coordinates;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;

import static it.polimi.ingsw.utils.CliUtilities.RESET;
import static it.polimi.ingsw.utils.CliUtilities.SUCCESS_COLOR;

// This is abstract (non instantiable) because each client will either
// be an RMI client or a Socket client
public abstract class Client extends UnicastRemoteObject implements Serializable, ClientCommunicationInterface {

    public GameView gameView;
    String username = null;
    boolean serverConnected = true;
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

    public void parseReceivedMessage(Message message) {
        String category = message.getCategory();
        switch (category) {
            case "pong" -> {
                // System.out.println("Pong received.");
                serverConnected = true;
            }
            case "username" -> setUsername(message.getUsername());
            case "UsernameRetry" -> gameView.usernameError();
            case "UsernameRetryCompleteLogin" -> gameView.completeLoginError();
            case "chooseNumOfPlayer" -> gameView.playerChoice();
            case "numOfPlayersNotOK" -> gameView.playerNumberError();
            case "update" -> {
                HashMap<Bookshelf, String> bookshelves = message.getAllBookshelves();
                gameView.pickMyBookshelf(bookshelves);
                gameView.pickOtherBookshelf(bookshelves);
                // gameView.showCurrentScore(message.getIntMessage("score"));
                gameView.showBoard(message.getBoard());
            }
            case "startGame" -> {
                System.out.println("Game started.");
                gameView.startGame(message);
            }
            case "turn" -> myTurn();
            case "otherTurn" -> gameView.showMessage("It's " + message.getArgument() + "'s turn.\n");
            case "picked" -> {
                try {
                    if (gameView.showRearrange(message.getPicked())) {
                        sendMessage(new Message("sort", gameView.rearrange(message.getPicked())));
                    }
                    int column = gameView.promptInsert();
                    sendMessage(new Message("insertMessage", "insert", column));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case "PickRetry" -> {
                gameView.showMessage("Invalid pick. Retry.");
                List<Coordinates> pick = gameView.showPick();
                Message myPick = new Message(pick.get(0), pick.get(1));
                sendMessage(myPick);
            }
            case "endGame" -> gameView.showEndGame(message.getWinners());
            case "disconnection" -> gameView.showDisconnection();
            case "waitingRoom" -> waitingRoom();
            case "lastRound" -> gameView.showLastRound();
            case "gameAlreadyStarted" -> {
                gameView.showGameAlreadyStarted();
                stop();
            }
            case "removePlayer" -> {
                gameView.showRemovePlayer();
                stop();
            }
            case "insertRetry" -> {
                String argument = message.getArgument();
                if (argument.equals("notValidNumber")) {
                    gameView.showMessage("Invalid column. Retry.");
                } else {
                    gameView.showMessage("There are not enough free cells in the column. Retry.");
                }
                int column = gameView.promptInsert();
                sendMessage(new Message("insertMessage", "insert", column));
            }
            default -> throw new IllegalArgumentException("Invalid message category: " + category);
        }
    }

    public void stop() {
        System.exit(0);
    }

    public Message numOfPlayers() {
        return new Message("numPlayer", "", 0, false, 0);
    }

    public void startPingThread(String username) {
        Thread pingThread = new Thread(() -> {
            while (true) {
                try {
                    // noinspection BusyWait
                    Thread.sleep(5000);
                    sendMessage(new Message("ping", username));
                    Thread.sleep(3000);
                    checkServerConnection();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        pingThread.start();
        // Thread checkPongThread = new Thread(() -> {
        //     while (true) {
        //         try {
        //             // noinspection BusyWait
        //             Thread.sleep(6000);
        //             if (!serverConnected) {
        //                 System.err.println("\nServer disconnected.");
        //                 System.exit(0);
        //             }
        //             serverConnected = false;
        //         } catch (InterruptedException e) {
        //             e.printStackTrace();
        //         }
        //     }
        // });
        // checkPongThread.start();
    }

    public abstract void checkServerConnection();

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
     * Starts the login procedure, in which are asked the username, age, number of players, first game experience.
     */
    public void login() {
        // gameView.setClient(this);

        // maybe its problematic
        // gameView.loginProcedure();
        // waitingRoom();
    }

    public void waitingRoom() {
        gameView.waitingRoom();
    }

    /**
     * Shows the board and asks the user to pick some tiles.
     * If the pick is valid, asks the user to rearrange the tiles (if he wants to),
     * then asks the user to choose a column to place the tiles in.
     * At the end of the turn, the player returns to the waiting room. // TODO: does he?
     */
    public void myTurn() {
        gameView.showMessage("It's your turn!\n");
        List<Coordinates> pick = gameView.showPick();
        Message myPickMessage = new Message(pick.get(0), pick.get(1));
        sendMessage(myPickMessage);
    }

    public void endGame() {
        gameView.endGame();
    }

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
        parseReceivedMessage(message);
    }
}
