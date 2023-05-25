package it.polimi.ingsw.client;

import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.CommunicationInterface;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.utils.Coordinates;
import it.polimi.ingsw.utils.FullRoomException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;

import static it.polimi.ingsw.server.CommunicationInterface.HOSTNAME;
import static it.polimi.ingsw.server.CommunicationInterface.PORT_RMI;

public class ClientRmi extends Client implements RmiClientIf {

    private Registry registry;
    private CommunicationInterface server;

    /**
     * Starts the client
     */
    public ClientRmi() throws RemoteException {
        super();
    }

    @Override
    public void sendMessage(Message message) {

        try {
            server.callBackSendMessage(message, this);
        } catch (FullRoomException | Exception e) {
            // throw new RuntimeException(e);
        }
    }

    @Override
    public void receivedMessage(Message message) {
        String category = message.getCategory();
        String username = getUsername();
        switch (category) {
            case "username":
                setUsername(message.getUsername());
                break;
            case "UsernameRetry":

                gameView.usernameError();
                break;

            case "UsernameRetryCompleteLogin":

                gameView.completeLoginError();
                break;

            case "chooseNumOfPlayer":
                gameView.playerChoice();

                break;
            case "numOfPlayersNotOK":

                gameView.playerNumberError();
                break;
            case "update":
                HashMap<Bookshelf, String> bookshelves = message.getAllBookshelves();
                gameView.pickMyBookshelf(bookshelves);
                gameView.pickOtherBookshelf(bookshelves);
                // gameView.showCurrentScore(message.getIntMessage("score"));
                gameView.showBoard(message.getBoard());

                break;
            case "startGame":
                System.out.println("Game started.");
                gameView.startGame(message);

                break;
            case "turn":
                myTurn();
                break;
            case "otherTurn":
                gameView.showMessage("It's " + message.getArgument() + "'s turn.");
                break;
            case "picked":
                try {
                    if (gameView.showRearrange(message.getPicked())) {
                        sendMessage(new Message("sort", gameView.rearrange(message.getPicked())));
                    }
                    int column = gameView.promptInsert();
                    sendMessage(new Message("insert", "insert", column));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "PickRetry":
                gameView.showMessage("Invalid pick. Retry.");
                List<Coordinates> pick = gameView.showPick();
                Message myPick = new Message(pick.get(0), pick.get(1));
                sendMessage(myPick);
                break;
            case "endGame":
                gameView.showEndGame(message.getWinners());
                break;
            case "disconnection":
                gameView.showDisconnection();
                break;
            case "waitingRoom":
                waitingRoom();
                break;
            default:
                throw new IllegalArgumentException("Invalid message category: " + category);
        }
    }

    @Override
    public void myTurn() {
        gameView.showMessage("It's your turn!");
        List<Coordinates> pick = gameView.showPick();
        Message myPick = new Message(pick.get(0), pick.get(1));
        sendMessage(myPick);
    }

    @Override
    public Message numOfPlayers() {
        // isn't used, what is promptNumberofPlayers()?

        // int numPlayer = gameView.promptNumberOfPlayers();
        // return new Message("numPlayer", "", 0, false, numPlayer);
        return new Message("numPlayer", "", 0, false, 0);
    }

    public void sendMe() throws RemoteException, NotBoundException {
        server.sendClient();
    }

    @Override
    public void startGame(Message message) {
        gameView.startGame(message);
    }

    @Override
    public void connect() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(HOSTNAME, PORT_RMI);
        server = (CommunicationInterface) registry.lookup("CommunicationInterface");
    }

    @Override
    public void callBackSendMessage(Message message) throws RemoteException {
        receivedMessage(message);
    }
}
