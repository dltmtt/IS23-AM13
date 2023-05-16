package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.GameCliView;
import it.polimi.ingsw.client.view.GameView;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.CommunicationInterface;
import it.polimi.ingsw.utils.Coordinates;
import it.polimi.ingsw.utils.FullRoomException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import static it.polimi.ingsw.server.CommunicationInterface.HOSTNAME;
import static it.polimi.ingsw.server.CommunicationInterface.PORT_RMI;

public class ClientRmi extends Client {

    GameView gameView = new GameCliView(); // TODO: this should be injected by the controller (cli or gui depending on user)
    int myPosition;
    private Registry registry;
    private CommunicationInterface server;

    /**
     * Starts the client
     */
    public ClientRmi() {
        super();
    }

    @Override
    public void connect() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(HOSTNAME, PORT_RMI);
        server = (CommunicationInterface) registry.lookup("CommunicationInterface");
    }

    /**
     * Starts the login procedure, asks the user his info and sends them to the server.
     */
    @Override
    public void login() {
        int age;
        boolean firstGame;
        try {
            gameView.showStartGame();
            String username = gameView.showLogin();
            String finalUsername = username;
            Thread pingThread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(5000);
                        server.sendMessage(parser.sendPing(finalUsername));
                    } catch (InterruptedException | IOException | FullRoomException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
            pingThread.start();
            Message response = server.sendMessage(parser.sendUsername(username));
            String responseMessage = parser.getMessage(response); // This message will be a JSON
            // TODO: parse the JSON (now it's plain text)
            while ("retry".equals(responseMessage)) {
                System.out.println("Username already taken. Retry.");
                username = gameView.showLogin();
                responseMessage = parser.getMessage(server.sendMessage(parser.sendUsername(username)));
            }
            if ("index".equals(responseMessage)) {
                myPosition = parser.getPosition(response);
                startGame();
                return;
            }
            age = gameView.promptAge();

            gameView.showMessage(responseMessage);
            String ageResponse = parser.getMessage(server.sendMessage(parser.sendAge(age)));
            if (!ageResponse.startsWith("ok")) {
                System.out.println("Remember that you need to be supervised by an adult to play this game.");
            }
            firstGame = gameView.promptFirstGame();

            int nextStep = parser.getPosition(server.sendMessage(parser.sendFirstGame(firstGame)));
            if (nextStep == 1) {
                int numPlayer = gameView.promptNumberOfPlayers();
                String numPlayerResponse = parser.getMessage(server.sendMessage(parser.sendNumPlayer(numPlayer)));
                while (numPlayerResponse.startsWith("retry")) {
                    System.out.println("Illegal number of players. Retry.");
                    numPlayer = gameView.promptNumberOfPlayers();
                    numPlayerResponse = parser.getMessage(server.sendMessage(parser.sendNumPlayer(numPlayer)));
                }
                // end of login
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

    @Override
    public void start() {
        loginThread.start();
    }

    public void waitingRoom() throws FullRoomException, IOException, ParseException, IllegalAccessException {
        System.out.println("Waiting for other players to join...");
        String response = parser.getMessage(server.sendMessage(parser.sendReady()));
        while (response == null) {
            response = parser.getMessage(server.sendMessage(parser.sendReady()));
        }
        startGame();
    }

    public void startGame() throws FullRoomException, IOException, ParseException, IllegalAccessException {
        Message myGame = server.sendMessage(parser.sendPosition(myPosition));

        gameView.showPersonalGoal(parser.getPersonalGoal(myGame));

        List<String> cards = parser.getCardsType(myGame);
        List<Integer> occurrences = parser.getCardOccurrences(myGame);
        List<Integer> size = parser.getCardSize(myGame);
        List<Boolean> horizontal = parser.getCardHorizontal(myGame);
        for (int i = 0; i < cards.size(); i++) {
            gameView.showCommonGoal(cards.get(i), occurrences.get(i), size.get(i), horizontal.get(i));
        }

        GameView.cleanScreen();
        gameView.showBoard(parser.getBoard(myGame));
        gameView.showBookshelf(parser.getBookshelf(myGame));

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
                myTurn = parser.getTurn(server.sendMessage(parser.sendTurn("turn", myPosition)));
            } else {
                myTurn = parser.getTurn(server.sendMessage(parser.sendTurn("turn", myPosition)));
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
        Message currentBoard = server.sendMessage(parser.sendMessage("board"));
        GameView.cleanScreen();
        gameView.showBoard(parser.getBoard(currentBoard));
        // Shows and returns the pick
        List<Coordinates> pick = gameView.showPick();
        Message myPick = parser.sendPick(pick);
        Message isMyPickOk = server.sendMessage(myPick);

        while (!"picked".equals(parser.getMessage(isMyPickOk))) {
            System.out.println("Pick not ok,please retry");
            pick = gameView.showPick();
            myPick = parser.sendPick(pick);
            isMyPickOk = server.sendMessage(myPick);
        }
        System.out.println("Pick ok");

        if (gameView.showRearrange(parser.getPicked(isMyPickOk))) {
            server.sendMessage(parser.sendRearrange(gameView.rearrange(parser.getPicked(isMyPickOk))));
        }

        Message myInsert = server.sendMessage(parser.sendInsert(gameView.promptInsert()));
        while (!"update".equals(parser.getMessage(myInsert))) {
            gameView.showMessage(gameView.insertError);
            myInsert = server.sendMessage(parser.sendInsert(gameView.promptInsert()));
        }

        gameView.showBookshelf(parser.getBookshelf(myInsert));
        gameView.showCurrentScore(parser.getScore(myInsert));
        // gameView.showBoard(parser.getBoard(myInsert));

        waitForTurn();
    }

    /**
     * Ends the game
     */
    public void endGame() throws FullRoomException, RemoteException, IllegalAccessException {
        Message winners = server.sendMessage(parser.sendMessage("endGame"));
        gameView.showEndGame(parser.getWinners(winners));
    }
}
