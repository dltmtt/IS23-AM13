package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.GameCliView;
import it.polimi.ingsw.client.view.GameView;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.CommunicationInterface;
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
import static it.polimi.ingsw.utils.CliUtilities.*;

public class ClientRmi extends Client {

    public Thread loginThread;
    GameView gameView = new GameCliView(); // TODO: this should be injected by the controller (cli or gui depending on user)
    GameController controller = new GameController(null, gameView, this);
    int myPosix;
    private Registry registry;
    private CommunicationInterface server;
    private Message message;

    public ClientRmi() {
        // All these messages should probably be moved to the view
        System.out.print("Connecting to server... ");

        // TODO: create a connect method in the Client class and maybe
        // use something else for the timer
        boolean connected = false;
        long now = System.currentTimeMillis();
        long timeout = now + 10 * 1000; // 10 seconds

        // This is done in order to be able to start the client and
        // the server in parallel from the IDE (otherwise the client
        // could start before the server and fail to connect)
        while (System.currentTimeMillis() < timeout) {
            try {
                registry = LocateRegistry.getRegistry(HOSTNAME, PORT_RMI);
                server = (CommunicationInterface) registry.lookup("CommunicationInterface");

                // If we get here, no exception was thrown and we are connected
                connected = true;
                break;
            } catch (RemoteException | NotBoundException e) {
                // Unable to connect to server, retrying...
            }
        }

        if (connected) {
            System.out.println(SUCCESS_COLOR + "connected" + RESET);
        } else {
            System.err.println(ERROR_COLOR + "error" + RESET);
            System.out.println("Unable to connect to the server. Is it running?");
            System.exit(1);
        }

        loginThread = new Thread(this::login);
    }

    public void start() {
        loginThread.start();
    }

    @Override
    public void login(String username) {

    }

    public void login() {
        int age = 0;
        boolean firstGame;
        try {
            controller.startGame();
            String username = controller.showLoginScreen();
            String responseMessage = parser.getMessage(server.sendMessage(parser.sendUsername(username))); // This message will be a JSON
            // TODO: parse the JSON (now it's plain text)
            while (responseMessage.equals("retry")) {
                System.out.println("Username already taken. Retry.");
                username = controller.showLoginScreen();
                responseMessage = parser.getMessage(server.sendMessage(parser.sendUsername(username)));
            }
            age = controller.showAgeScreen();

            gameView.showMessage(responseMessage);
            String ageResponse = parser.getMessage(server.sendMessage(parser.sendAge(age)));
            if (!ageResponse.startsWith("ok")) {
                System.out.println("Remember that you need to be supervised by an adult to play this game.");
            }
            firstGame = controller.showFirstGameScreen();
            int nextStep = parser.getPosix(server.sendMessage(parser.sendFirstGame(firstGame)));
            if (nextStep == 1) {
                int numPlayer = controller.showNumberOfPlayersScreen();
                String numPlayerResponse = parser.getMessage(server.sendMessage(parser.sendNumPlayer(numPlayer)));
                while (numPlayerResponse.startsWith("retry")) {
                    System.out.println("Illegal number of players. Retry.");
                    numPlayer = controller.showNumberOfPlayersScreen();
                    numPlayerResponse = parser.getMessage(server.sendMessage(parser.sendNumPlayer(numPlayer)));
                }
                //end of login
            }
            myPosix = nextStep;
            System.out.println("Your position is " + myPosix);
            waitingRoom();
        } catch (RemoteException e) {
            throw new RuntimeException(e); // TODO: handle this exception
        } catch (FullRoomException | IOException | ParseException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
        Message myGame = server.sendMessage(parser.sendPosix(myPosix));
        controller.showPersonalGoal(parser.getPersonalGoal(myGame));
        controller.showCommonGoal(parser.getCardsType(myGame), parser.getCardOccurrences(myGame), parser.getCardSize(myGame), parser.getCardHorizontal(myGame));
        //        System.out.println("Game started!");
        //TODO: show bookshelf and board
        controller.showBoard(parser.getBoard(myGame));
        controller.showBookshelf(parser.getBookshelf(myGame));
        waitForTurn();
    }

    public void waitForTurn() throws IOException, FullRoomException, IllegalAccessException, ParseException {
        int myTurn = 0;
        Message currentBoard;
        while (myTurn != 1) {
            if (myTurn == -1) {
                endGame();
            }
            myTurn = parser.getTurn(server.sendMessage(parser.sendTurn("turn", myPosix)));
        }
        myTurn();
    }

    public void myTurn() throws FullRoomException, IOException, IllegalAccessException, ParseException {
        Message currentBoard = server.sendMessage(parser.sendMessage("board"));
        controller.showBoard(parser.getBoard(currentBoard));
        List<Integer> pick = controller.showPickScreen();
        Message myPick = parser.sendPick(pick.get(0), pick.get(1), pick.get(2), pick.get(3));
        Message isMyPickOk = server.sendMessage(myPick);

        while (!parser.getMessage(isMyPickOk).equals("picked")) {
            System.out.println("Pick not ok,please retry");
            pick = controller.showPickScreen();
            myPick = parser.sendPick(pick.get(0), pick.get(1), pick.get(2), pick.get(3));
            isMyPickOk = server.sendMessage(myPick);
        }
        System.out.println("Pick ok");

        if (controller.showRearrangeScreen()) {
            server.sendMessage(parser.sendRearrange(controller.rearrangeScreen(parser.getPicked(isMyPickOk), parser.getPickedSize(myPick))));
        }

        Message myInsert = server.sendMessage(parser.sendInsert(controller.showInsertScreen()));
        while (!parser.getMessage(myInsert).equals("update")) {
            System.out.println("Insert not ok,please retry");
            myInsert = server.sendMessage(parser.sendTurn("insert", controller.showInsertScreen()));
        }

        controller.showBookshelf(parser.getBookshelf(myInsert));
        controller.showBoard(parser.getBoard(myInsert));
        waitForTurn();
    }

    public void endGame() {
        System.out.println("Game ended");
    }
}
