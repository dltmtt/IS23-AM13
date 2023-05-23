package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.GameView;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.utils.FullRoomException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.rmi.NotBoundException;

import static it.polimi.ingsw.utils.CliUtilities.RESET;
import static it.polimi.ingsw.utils.CliUtilities.SUCCESS_COLOR;

// This is abstract (non instantiable) because each client will either
// be an RMI client or a Socket client
public abstract class Client {

    public GameView gameView;
    protected Thread loginThread;
    protected Thread pingThread;
    private int myPosition;

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
    }

    /**
     * Sends a message to the server and returns the response.
     *
     * @param message the message to send
     * @return the response from the server
     */
    public abstract Message sendMessage(Message message);

    public void startPingThread(String finalUsername) {
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
    }

    public void start() {
        gameView.startView();
        login();
        // loginThread.start();
    }

    public void setView(GameView gameView) {
        this.gameView = gameView;
    }

    public abstract void connect() throws IOException, NotBoundException;

    public void login() {
        gameView.loginProcedure();
    }

    public void waitingRoom() throws FullRoomException, IOException, ParseException, IllegalAccessException {
        gameView.waitingRoom();
    }

    public void startGame() throws FullRoomException, IOException, ParseException, IllegalAccessException {
        gameView.startGame();
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
        gameView.waitForTurn();
    }

    /**
     * Shows the board and asks the user to pick some tiles, then, if the pick is valid, asks the user to rearrange the tiles (if the player want),
     * then asks the user to choose a column to place the tiles in. at the end of the turn, the player returns to the waiting room.
     */
    public void myTurn() throws FullRoomException, IOException, IllegalAccessException, ParseException {
        gameView.myTurn();
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
}
