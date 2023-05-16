package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.GameCliView;
import it.polimi.ingsw.client.view.GameView;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.utils.Coordinates;
import it.polimi.ingsw.utils.FullRoomException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import static it.polimi.ingsw.server.CommunicationInterface.HOSTNAME;
import static it.polimi.ingsw.server.CommunicationInterface.PORT_SOCKET;

public class ClientSocket extends Client {

    // data output
    public final DataOutputStream dos;
    public Socket s;
    // buffered reader and keyboard
    public BufferedReader br, kb;

    // threads to listen and send
    public Thread listenThread;
    public Thread sendThread;

    public Thread pingThread;

    public GameController controller;

    public GameView gameView = new GameCliView();

    int myPosition;

    /**
     * Constructor to create DataOutputStream and BufferedReader
     * Creates socket, DataOutputStream, BufferedReader from the server and keyboard
     */
    public ClientSocket(GameController controller) {
        super();

        // This is to send data to the server
        try {
            dos = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            System.err.println("Unable to create output stream");
            throw new RuntimeException(e);
        }

        // This is used to read data coming from the server
        try {
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            System.err.println("Unable to create input stream");
            throw new RuntimeException(e);
        }

        // To read data from the keyboard
        kb = new BufferedReader(new InputStreamReader(System.in));

        // Listen for messages coming from the server
        listenThread = new Thread(() -> {
            while (true) {
                try {
                    String str = br.readLine();
                    System.out.println("From " + s.getInetAddress() + ": " + br.readLine());
                } catch (IOException e) {
                    System.out.println("Server disconnected, unable to read.");
                    close();
                    break;
                }
            }
        });

        // Send messages to the server
        sendThread = new Thread(() -> {
            while (true) {
                // Send keyboard input to the server
                try {
                    sendInput();
                } catch (IOException e) {
                    System.out.println("Server disconnected, unable to send.");
                    close();
                    break;
                }
            }
        });
        this.controller = controller;
    }

    public Message receiveMessage() {
        String str = null;
        do {
            try {
                // read a string
                str = br.readLine();
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject messageFromClient = (JSONObject) parser.parse(str);
                    return new Message(messageFromClient);
                } catch (ParseException e) {
                    System.out.println(str);
                    return new Message(str);
                }
            } catch (IOException e) {
                System.err.println("Lost connection to server.");
            }
        } while (str == null);
        return null;
    }

    /**
     * Starts the threads to listen from the server and send data
     */
    @Override
    public void start() {
        // Start the threads
        // listenThread.start();
        // sendThread.start();
        // pingThread.start();
        login();
        System.out.println("Login successful.");
    }

    /**
     * Sends the login message to the server
     */
    @Override
    public void login() {
        Message responseMessage;
        controller.startGame();
        String username;
        do {
            username = controller.showLoginScreen();
            // nothing is sent, just created
            Message usernameMessage = parser.sendUsername(username);
            sendMessage(usernameMessage.getJSONstring());

            // it waits an answer from the server
            responseMessage = receiveMessage();

            // System.out.println(responseMessage.getCategory());
            // System.out.println(responseMessage.getMessage());

            if (responseMessage.getCategory().equals("retry")) {
                System.out.println("Username already taken. Retry.");
            }
        } while ("retry".equals(responseMessage.getCategory()));
        String finalUsername = username;
        pingThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    Message ping = parser.sendPing(finalUsername);
                    sendMessage(ping.getJSONstring());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        pingThread.start();
        System.out.println(responseMessage.getMessage());

        int age = controller.showAgeScreen();

        gameView.showMessage(responseMessage.getMessage());
        Message ageMessage = parser.sendAge(age);
        sendMessage(ageMessage.getJSONstring());
        responseMessage = receiveMessage();
        if (!responseMessage.getMessage().startsWith("ok")) {
            System.out.println("Remember that you need to be supervised by an adult to play this game.");
        }

        boolean firstGame = controller.showFirstGameScreen();

        Message firstGameMessage = parser.sendFirstGame(firstGame);
        sendMessage(firstGameMessage.getJSONstring());
        int nextStep = parser.getPosition(receiveMessage());

        // int nextStep = parser.getPosition(server.sendMessage(parser.sendFirstGame(firstGame)));
        if (nextStep == 1) {
            int numPlayer = controller.showNumberOfPlayersScreen();
            Message numPlayerMessage = parser.sendNumPlayer(numPlayer);
            sendMessage(numPlayerMessage.getJSONstring());
            responseMessage = receiveMessage();
            while (responseMessage.getCategory().equals("retry")) {
                System.out.println("Illegal number of players. Retry.");
                numPlayer = controller.showNumberOfPlayersScreen();
                numPlayerMessage = parser.sendNumPlayer(numPlayer);
                sendMessage(numPlayerMessage.getJSONstring());
                responseMessage = receiveMessage();
            }
        }
        myPosition = nextStep;
        System.out.println("Your position is " + myPosition);
        try {
            waitingRoom();
        } catch (FullRoomException | IOException | ParseException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void waitingRoom() throws FullRoomException, IOException, ParseException, IllegalAccessException {
        System.out.println("Waiting for other players to join...");
        Message responseMessage, readyMessage;
        do {
            readyMessage = parser.sendReady();
            sendMessage(readyMessage.getJSONstring());
            responseMessage = receiveMessage();
        } while (responseMessage.getCategory() == null);
        startGame();
    }

    public void startGame() throws FullRoomException, IOException, ParseException, IllegalAccessException {
        Message myGame = parser.sendPosition(myPosition);
        sendMessage(myGame.getJSONstring());
        Message responseMessage = receiveMessage();

        controller.showPersonalGoal(parser.getPersonalGoal(responseMessage));

        controller.showCommonGoal(parser.getCardsType(myGame), parser.getCardOccurrences(myGame), parser.getCardSize(myGame), parser.getCardHorizontal(myGame));

        // TODO: show bookshelf and board
        controller.showBoard(parser.getBoard(myGame));
        controller.showBookshelf(parser.getBookshelf(myGame));
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
        Message turnMessage, responseMessage;
        while (myTurn != 1) {
            if (myTurn == -1) {
                endGame();
                break;
            } else {
                turnMessage = parser.sendTurn("turn", myPosition);
                sendMessage(turnMessage.getJSONstring());
                responseMessage = receiveMessage();
                myTurn = parser.getTurn(responseMessage);
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

        Message requestBoard = parser.sendMessage("board");
        sendMessage(requestBoard.getJSONstring());
        Message currentBoard = receiveMessage();

        controller.showBoard(parser.getBoard(currentBoard));

        // Shows and returns the pick
        Message isMyPickOk;
        Message myPick;
        do {
            List<Coordinates> pick = controller.showPickScreen();
            myPick = parser.sendPick(pick);
            sendMessage(myPick.getJSONstring());
            isMyPickOk = receiveMessage();
            if (!"picked".equals(parser.getMessage(isMyPickOk))) {
                System.out.println("Pick not ok,please retry");
            }
        } while (!"picked".equals(parser.getMessage(isMyPickOk)));

        System.out.println("Pick ok");

        if (controller.showRearrangeScreen(parser.getPicked(isMyPickOk))) {
            Message myRearrange = parser.sendRearrange(controller.rearrangeScreen(parser.getPicked(isMyPickOk)));
            sendMessage(myRearrange.getJSONstring());
        }

        Message myInsert;
        Message responseMessage;
        do {
            myInsert = parser.sendInsert(controller.showInsertScreen());
            sendMessage(myInsert.getJSONstring());
            responseMessage = receiveMessage();
            if (!"update".equals(parser.getMessage(responseMessage))) {
                gameView.showMessage(gameView.insertError);
            }
        } while (!"update".equals(parser.getMessage(responseMessage)));

        controller.showBookshelf(parser.getBookshelf(myInsert));
        controller.showScore(parser.getScore(myInsert));
        //        controller.showBoard(parser.getBoard(myInsert));

        waitForTurn();
    }

    /**
     * Ends the game
     */
    public void endGame() throws FullRoomException, RemoteException, IllegalAccessException {
        Message winners = parser.sendMessage("endGame");
        sendMessage(winners.getJSONstring());
        Message responseMessage = receiveMessage();
        controller.showEndGame(parser.getWinners(responseMessage));
    }

    @Override
    public void connect() throws IOException, NotBoundException {
        s = new Socket(HOSTNAME, PORT_SOCKET);
        System.out.println("Connected to server " + s.getInetAddress().getHostName() + ":" + s.getPort());
    }

    /**
     * Sends a string message to the server
     *
     * @param str the message to send
     */
    public void sendMessage(String str) {

        try {
            synchronized (dos) {
                dos.flush();
                dos.writeBytes(str + "\n");
            }
        } catch (IOException e) {
            System.err.println("unable to send message, output not available...");
        }
    }

    /**
     * Sends the input from the keyboard to the server
     */
    public void sendInput() throws IOException {
        String str = kb.readLine();
        dos.writeBytes(str + "\n");
    }

    /**
     * Closes the socket and the streams
     */
    public void close() {
        try {
            dos.close();
            br.close();
            kb.close();
            s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        listenThread.interrupt();
        sendThread.interrupt();
        System.exit(0);
    }
}
