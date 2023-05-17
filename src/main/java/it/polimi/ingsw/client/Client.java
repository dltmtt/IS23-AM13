package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.GameCliView;
import it.polimi.ingsw.client.view.GameView;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.utils.Coordinates;
import it.polimi.ingsw.utils.FullRoomException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import static it.polimi.ingsw.utils.CliUtilities.RESET;
import static it.polimi.ingsw.utils.CliUtilities.SUCCESS_COLOR;

// This is abstract (non instantiable) because each client will either
// be an RMI client or a Socket client
public abstract class Client {

    public GameView gameView = new GameCliView();
    protected Thread loginThread;
    protected Thread pingThread;
    int myPosition;

    public Client() {
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

        loginThread = new Thread(this::login);
    }

    /**
     * Sends a message to the server and returns the response.
     *
     * @param message the message to send
     * @return the response from the server
     */
    protected abstract Message sendMessage(Message message);

    public void start() {
        loginThread.start();
    }

    public void setView(GameView gameView) {
        this.gameView = gameView;
    }

    public abstract void connect() throws IOException, NotBoundException;

    public void login() {
        int age;
        boolean firstGame;
        try {
            gameView.showStartGame();
            String username = gameView.showLogin();
            String finalUsername = username;
            pingThread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(5000);
                        sendMessage(new Message("ping", finalUsername));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            pingThread.start();
            Message response = sendMessage(new Message("username", username, 0, false, 0));
            String responseMessage = response.getCategory();
            while ("retry".equals(responseMessage)) {
                // This happens when the username is already taken and the player needs to choose another one
                System.out.println("Username already taken. Retry.");
                username = gameView.showLogin();
                responseMessage = sendMessage(new Message("username", username, 0, false, 0)).getCategory();
            }
            if ("index".equals(responseMessage)) {
                // This happens when the game is already started and the player is reconnecting
                myPosition = response.getPosition();
                startGame();
                return;
            }
            age = gameView.promptAge();

            gameView.showMessage(responseMessage);
            String ageResponse = sendMessage(new Message("age", "", age, false, 0)).getCategory();
            if (!ageResponse.startsWith("ok")) {
                System.out.println("Remember that you need to be supervised by an adult to play this game.");
            }
            firstGame = gameView.promptFirstGame();

            int nextStep = sendMessage(new Message("firstGame", "", 0, firstGame, 0)).getPosition();
            if (nextStep == 1) {
                int numPlayer = gameView.promptNumberOfPlayers();
                String numPlayerResponse = sendMessage(new Message("numPlayer", "", 0, false, numPlayer)).getCategory();
                while (numPlayerResponse.startsWith("retry")) {
                    System.out.println("Illegal number of players. Retry.");
                    numPlayer = gameView.promptNumberOfPlayers();
                    numPlayerResponse = sendMessage(new Message("numPlayer", "", 0, false, numPlayer)).getCategory();
                }
            }
            myPosition = nextStep;
            System.out.println("Your position is " + myPosition);
            waitingRoom();
        } catch (RemoteException e) {
            throw new RuntimeException(e); // TODO: handle this exception
        } catch (FullRoomException | IOException | ParseException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void waitingRoom() throws FullRoomException, IOException, ParseException, IllegalAccessException {
        Thread animatedDots = new Thread(() -> {
            System.out.print("Waiting for other players to join");
            int dotCounter = 0;
            while (true) {
                try {
                    // noinspection BusyWait
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // Always print 3 dots before closing the thread
                    System.out.println(".".repeat(3 - dotCounter));
                    return; // Close the thread
                }

                if (dotCounter == 3) {
                    System.out.print("\b\b\b"); // Remove the 3 dots
                    dotCounter = 0;
                } else {
                    System.out.print(".");
                    ++dotCounter;
                }
            }
        });

        animatedDots.start();

        String response = sendMessage(new Message("ready", "", 0, false, 0)).getCategory();
        while (response == null) {
            response = sendMessage(new Message("ready", "", 0, false, 0)).getCategory();
        }

        animatedDots.interrupt();

        startGame();
    }

    public void startGame() throws FullRoomException, IOException, ParseException, IllegalAccessException {
        Message myGame = sendMessage(new Message(myPosition));

        gameView.showPersonalGoal(myGame.getPersonalGoal());

        List<String> cards = myGame.getCardType();
        List<Integer> occurrences = myGame.getCardOccurrences();
        List<Integer> size = myGame.getCardSize();
        List<Boolean> horizontal = myGame.getCardHorizontal();
        for (int i = 0; i < cards.size(); i++) {
            gameView.showCommonGoal(cards.get(i), occurrences.get(i), size.get(i), horizontal.get(i));
        }

        waitForTurn();
    }

    /**
     * Waits for the turn of the player. It depends on the number received by the server:
     * <ul>
     *     <li>-1: the game is over</li>
     *     <li>0: it's not the player's turn</li>
     *     <li>1: it's the player's turn</li>
     * </ul>
     */
    public void waitForTurn() throws IOException, IllegalAccessException, ParseException, FullRoomException {
        int myTurn = 0;
        boolean disconnected = false;
        while (myTurn != 1) {
            if (myTurn == -1) {
                endGame();
                break;
            } else if (myTurn == 2) {
                if (!disconnected) {
                    gameView.showDisconnection();
                    disconnected = true;
                }
                myTurn = sendMessage(new Message("turn", myPosition)).getTurn();
            } else {
                myTurn = sendMessage(new Message("turn", myPosition)).getTurn();
            }
        }
        if (myTurn == 1) {
            myTurn();
        }
    }

    /**
     * Shows the board and asks the user to pick some tiles, then, if the pick is valid, asks the user to rearrange the tiles (if the player want),
     * then asks the user to choose a column to place the tiles in. at the end of the turn, the player returns to the waiting room.
     */
    public void myTurn() throws FullRoomException, IOException, IllegalAccessException, ParseException {
        // Sends the message to server to get the board
        Message currentBoard = sendMessage(new Message("board"));
        GameView.cleanScreen();
        gameView.showBoard(currentBoard.getBoard());
        // Shows and returns the pick
        List<Coordinates> pick = gameView.showPick();
        Message myPick = new Message(pick.get(0), pick.get(1));
        Message isMyPickOk = sendMessage(myPick);

        while (!"picked".equals(isMyPickOk.getCategory())) {
            System.out.println("Pick not ok,please retry");
            pick = gameView.showPick();
            myPick = new Message(pick.get(0), pick.get(1));
            isMyPickOk = sendMessage(myPick);
        }
        System.out.println("Pick ok");

        if (gameView.showRearrange(isMyPickOk.getPicked())) {
            sendMessage(new Message("sort", gameView.rearrange(isMyPickOk.getPicked())));
        }

        Message myInsert = sendMessage(new Message("insert", "insert", gameView.promptInsert()));
        while (!"update".equals(myInsert.getCategory())) {
            gameView.showMessage(gameView.insertError);
            myInsert = sendMessage(new Message("insert", "insert", gameView.promptInsert()));
        }

        gameView.showBookshelf(myInsert.getBookshelf());
        gameView.showCurrentScore(myInsert.getIntMessage("score"));
        // gameView.showBoard(parser.getBoard(myInsert));

        waitForTurn();
    }

    public void endGame() {
        Message winners = sendMessage(new Message("endGame"));
        gameView.showEndGame(winners.getWinners());
    }
}
