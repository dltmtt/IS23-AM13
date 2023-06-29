package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.MyShelfie;
import it.polimi.ingsw.commons.Message;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.CliUtilities;
import it.polimi.ingsw.utils.Coordinates;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static it.polimi.ingsw.utils.CliUtilities.RESET;

/**
 * This class handles the CLI view of the game. It contains all the methods used to show the game state to the user by the command line and to get input from the user.
 */

public class GameCliView implements GameView {

    private static final String illegalNumberOfPlayersError = "The number of players must be between 2 and 4 (inclusive): ";
    /**
     * This is the client
     */
    public Client client;
    private boolean theOnlyOne = false;
    private static final String SUCCESS_COLOR = "\u001B[32m";

    /**
     * Constructor of the class. It asks the user for the server IP and the protocol to use (RMI or TCP).
     */
    public GameCliView() {
        String ip = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.print("Insert server IP: ");
            try {
                ip = reader.readLine();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
            if (!MyShelfie.isIpValid(ip)) {
                System.err.println("Invalid IP address: " + ip);
            }
        } while (!MyShelfie.isIpValid(ip));
        String protocolType = null;
        try {
            protocolType = CliUtilities.askCloseEndedQuestion("Choose a protocol (rmi or tcp): ", List.of("rmi", "tcp"), "tcp");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        MyShelfie.setParameters(ip, protocolType, this);
    }

    /**
     * If there is data to be read from the input buffer, read it and discard it.
     * Otherwise, do nothing.
     * This method is used to prevent accidental input from the user during
     * another player's turn to be read as input for his action.
     */
    private void flushInputBuffer() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            if (in.ready()) {
                in.readLine();
            }
        } catch (IOException e) {
            // It's not a big deal if this fails
        }
    }

    /**
     * Starts the login procedure, it asks the user for a username and sends it to the server. It also starts the ping thread for check the connection.
     */
    @Override
    public void loginProcedure() {
        showStartGame();
        String username = showLogin();
        boolean firstGame = promptFirstGame();
        client.sendMessage(new Message("completeLogin", username, 0, firstGame, 0));
    }

    /**
     * Shows the start game.
     */

    @Override
    public void startView() {
        // this.client = client;
        // client.login()
        loginProcedure();
    }

    /**
     * Reads the username from the command line and prompts the user to confirm it.
     * <p>If the user confirms the input, the method prints a welcome message.
     * <p>If the user does not confirm the input, the method prints a prompt to
     * insert the username again and calls itself recursively.
     *
     * @return the username inserted by the user
     */
    public String readUsername() {
        String username = null;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            username = in.readLine();
            boolean answerConfirmed = CliUtilities.confirmInput(username);

            while (!answerConfirmed) {
                showMessage(insertUsernameAgainPrompt);
                username = in.readLine();
                answerConfirmed = CliUtilities.confirmInput(username);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("An error occurred while reading the username.");
        }

        return username;
    }

    /**
     * Reads a number from the command line. Handles errors by prompting the user to insert the number again.
     *
     * @return the number inserted by the user
     */
    public int readNumber() {
        int n = 0;
        boolean valid = false;
        while (!valid) {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                ignoreInputIfAlone(in);
                n = Integer.parseInt(in.readLine());
                if (!theOnlyOne) {
                    valid = true;
                }
            } catch (NumberFormatException e) {
                if (!theOnlyOne) {
                    System.err.print("The input is not a number. Please insert a number: ");
                }
            } catch (IOException e) {
                System.err.println("An error occurred while reading the number.");
            }
        }
        return n;
    }

    /**
     * Shows the loginPrompt for the user.
     */
    public String showLogin() {
        showMessage(insertUsernamePrompt);
        return readUsername();
    }

    /**
     * Shows the first game question for the user.
     */
    public boolean promptFirstGame() {
        return CliUtilities.askYesNoQuestion(firstGameQuestion, "y");
    }

    public int promptNumberOfPlayers() {
        showMessage(insertNumberOfPlayersPrompt);
        int n = readNumber();
        while (n < 2 || n > 4) { // Client-size check
            System.err.print(illegalNumberOfPlayersError);
            n = readNumber();
        }
        return n;
    }

    /**
     * Shows the insert question for the user.
     */
    @Override
    public void promptInsert() {
        showMessage("Insert the index of the column where you want to insert the picked items (starting from 0): ");
        int index = readNumber();
        while (index < 0 || index > Bookshelf.getColumns() - 1) {
            System.err.print(insertError);
            index = readNumber();
        }
        client.sendMessage(new Message("insertMessage", "insert", index));
    }

    /**
     * Shows the PersonalGoal card.
     *
     * @param card the number of the card to show
     * @throws IOException    if the file cannot be read
     * @throws ParseException if the file is not in the correct format
     */
    public void showPersonalGoal(int card) throws IOException, ParseException {
        PersonalGoalView personalGoalView = new PersonalGoalView(card);
        personalGoalView.printLayout();
    }

    /**
     * Shows the PublicGoal cards.
     *
     * @param card        the number of the card to show
     * @param occurrences the number of occurrences of the card
     * @param size        is the number of items in each occurrence of the common goal
     * @param horizontal  true if the cards are to be printed horizontally, false if vertically
     */
    public void showCommonGoal(String card, int occurrences, int size, boolean horizontal) {
        CommonGoalView.print(card, occurrences, size, horizontal);
    }

    /**
     * Shows the pick question for the user and check if the input is valid.
     */
    @Override
    public void showPick() {
        flushInputBuffer();
        List<Coordinates> coordinates = new ArrayList<>();
        showMessage("Insert the coordinates of the first and the last cell you want to to pick, e.g. (1, 2) - (2, 3): ");
        boolean valid = false;
        while (!valid) {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

                // Ignore any input inserted when the player is alone. This mimics a freeze of the game.
                ignoreInputIfAlone(in);

                String input = in.readLine();

                input = input.replaceAll("\\s", ""); // Ignore whitespaces
                input = input.replaceAll("\\(", ""); // Ignore parentheses
                input = input.replaceAll("\\)", ""); // Ignore parentheses

                String[] halves = input.split("-"); // Split the input into two coordinates
                if (halves.length != 2)
                    throw new NumberFormatException(); // If there are not two coordinates separated by a dash

                for (String half : halves) {
                    // These conversions and splits throw NumberFormatException if the input is not a number and ArrayIndexOutOfBoundsException if the input is not in the correct format
                    int x = Integer.parseInt(half.split(",")[0]);
                    int y = Integer.parseInt(half.split(",")[1]);

                    // Check if the coordinates are within the board
                    if (x < 0 || y < 0 || x > Board.boardSize - 1 || y > Board.boardSize - 1) {
                        throw new NumberFormatException();
                    }

                    coordinates.add(new Coordinates(x, y));
                }

                if (!theOnlyOne) {
                    valid = true;
                }
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                coordinates.clear(); // Without this, a coordinate could be added to the list even if the input in its entirety is not valid
                if (!theOnlyOne) {
                    System.err.print("The input is not valid. Please insert the coordinates in the correct format: ");
                }
            } catch (IOException e) {
                System.err.println("An error occurred while reading the coordinates.");
            }
        }

        client.sendMessage(new Message(coordinates.get(0), coordinates.get(1)));
    }

    /**
     * Ignores the input if the player is alone.
     */
    private void ignoreInputIfAlone(BufferedReader in) {
        while (theOnlyOne) {
            try {
                if (in.ready()) {
                    in.readLine();
                }
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    /**
     * Shows the board.
     *
     * @param board the board to show
     */
    @Override
    public void showBoard(Board board) {
        BoardView boardView = new BoardView(board);
        showMessage("Here's the board:\n");
        boardView.printBoard();
    }

    /**
     * Prints the bookshelf.
     *
     * @param bookshelf the bookshelf to show
     */
    @Override
    public void showBookshelf(Bookshelf bookshelf) {
        BookshelfView bookshelfView = new BookshelfView(bookshelf);
        showMessage("Here's your bookshelf:\n");
        bookshelfView.printBookshelf();
    }

    /**
     * Prints the opening image of the CLI.
     */
    public void showStartGame() {
        String title = """
                        ___  ___        _____ _          _  __ _      \s
                        |  \\/  |       /  ___| |        | |/ _(_)    \s
                        | .  . |_   _  \\ `--.| |__   ___| | |_ _  ___\s
                        | |\\/| | | | |  `--. \\ '_ \\ / _ \\ |  _| |/ _ \\
                        | |  | | |_| | /\\__/ / | | |  __/ | | | |  __/
                        \\_|  |_/\\__, | \\____/|_| |_|\\___|_|_| |_|\\___|
                                 __/ |                                \s
                                |___/                                 \s
                """; // Generated by: https://patorjk.com/software/taag/
        showMessage(welcomeMessage + title);
    }

    /**
     * Prints the end game message.
     *
     * @param winners the list of winners
     * @param losers  the list of losers
     */
    @Override
    public void showEndGame(HashMap<String, Integer> winners, HashMap<String, Integer> losers) {
        showMessage("The game is over!\n");
        showMessage("The winners are:\n");
        for (String winner : winners.keySet()) {
            showMessage(winner + " with " + winners.get(winner) + " points\n");
        }

        showMessage("The losers are:\n");
        for (String loser : losers.keySet()) {
            showMessage(loser + " with " + losers.get(loser) + " points\n");
        }
    }

    /**
     * Asks the player if they want to rearrange the items they've picked.
     *
     * @param items the items the player has picked
     * @return true if the player wants to rearrange the items, false otherwise
     */
    @Override
    public boolean showRearrange(List<Item> items) {
        ItemView.printItems(items);
        return CliUtilities.askYesNoQuestion("Do you want to rearrange the items you've picked?", "n");
    }

    /**
     * Rearranges the order of the picked items.
     *
     * @param items the items the player has picked
     * @return the new order of the items
     */
    @Override
    public List<Integer> rearrange(List<Item> items) {
        String infoMessage = """
                Reorder the items you've picked (starting from 0). Separate the new order by commas.
                For example, if you picked three items you could enter 2, 0, 1: the first item will
                be moved to the third position, the second to the first, and the third to the second.
                """;
        showMessage(infoMessage);
        showMessage("Enter your new order: ");
        List<Integer> newOrder = new ArrayList<>();
        boolean valid = false;
        while (!valid) {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

                ignoreInputIfAlone(in);

                String input = in.readLine();

                input = input.replaceAll("\\s", ""); // Ignore whitespaces

                String[] order = input.split(","); // Split the input into the new order
                if (order.length != items.size()) {
                    throw new NumberFormatException(); // If the new order doesn't contain all the items
                }

                for (String index : order) {
                    // This conversion throws NumberFormatException if the input is not a number
                    int i = Integer.parseInt(index);

                    // Check if the index is within the items list
                    if (i < 0 || i > items.size() - 1) {
                        throw new NumberFormatException();
                    }

                    // Check if the number is already in the list
                    if (newOrder.contains(i)) {
                        throw new NumberFormatException();
                    }

                    newOrder.add(i);
                }

                if (!theOnlyOne) {
                    valid = true;
                }
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                newOrder.clear(); // Without this, an index could be added to the list even if the input in its entirety is not valid
                if (!theOnlyOne) {
                    System.err.print("The input is not valid. Please insert the new order in the correct format: ");
                }
            } catch (IOException e) {
                System.err.println("An error occurred while reading the new order.");
            }
        }
        return newOrder;
    }

    /**
     * Shows the current score of the player.
     *
     * @param score the current score of the player
     */
    @Override
    public void showCurrentScore(int score) {
        showMessage("Your current score is " + score + "\n");
    }

    /**
     * Shows the message of the waiting room.
     */
    @Override
    public void waitingRoom() {
        showMessage("Waiting for other players to join...\n");
    }

    /**
     * Shows the game to the player. (the board, their bookshelf, the common goals, the personal goal, the current score, the bookshelves of the other players)
     */
    @Override
    public void startGame(Message message) {
        // Personal goal
        try {
            showPersonalGoal(message.getPersonalGoal());
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        // Common goal(s)
        List<String> cards = message.getCardType();
        List<Integer> occurrences = message.getCardOccurrences();
        List<Integer> sizes = message.getCardSize();
        List<Boolean> horizontal = message.getCardHorizontal();
        for (int i = 0; i < cards.size(); i++) {
            showCommonGoal(cards.get(i), occurrences.get(i), sizes.get(i), horizontal.get(i));
        }

        // Bookshelves
        HashMap<String, Bookshelf> bookshelves = message.getAllBookshelves();
        pickMyBookshelf(bookshelves);
        pickOtherBookshelf(bookshelves);

        // Board
        showBoard(message.getBoard());
    }

    /**
     * Shows to the player their bookshelf (picked from the message from the server).
     *
     * @param bookshelves the bookshelves of all the players
     */
    @Override
    public void pickMyBookshelf(HashMap<String, Bookshelf> bookshelves) {
        String name = client.getUsername();
        showBookshelf(bookshelves.get(name));
    }

    /**
     * Getter for the client.
     *
     * @return the client
     */
    @Override
    public Client getClient() {
        return client;
    }

    /**
     * Setter for the client.
     *
     * @param client the client
     */
    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Shows to the player the bookshelf of another player (picked from the message from the server).
     *
     * @param bookshelf the bookshelf of  the players
     */
    @Override
    public void showOtherBookshelf(Bookshelf bookshelf, String name) {
        showMessage(name + "'s bookshelf:\n");
        BookshelfView bookshelfView = new BookshelfView(bookshelf);
        bookshelfView.printOtherBookshelf();
    }

    /**
     * Shows the message for an invalid username.
     */
    @Override
    public void usernameError() {
        System.err.println("Username already taken. Retry.");
        String username = showLogin();
        boolean firstGame1 = promptFirstGame();
        client.sendMessage(new Message("completeLogin", username, 0, firstGame1, 0));
    }

    /**
     * Shows the message for an invalid username.
     *
     * @see #usernameError()
     */
    @Override
    public void completeLoginError() {
        usernameError();
    }

    /**
     * Shows the message for an invalid number of players.
     */
    @Override
    public void playerNumberError() {
        System.err.println(illegalNumberOfPlayersError);
        playerChoice();
    }

    /**
     * Shows the message for the choice of the player.
     */

    @Override
    public void playerChoice() {
        int numOfPlayers = promptNumberOfPlayers();
        client.sendMessage(new Message("numOfPlayersMessage", "numOfPlayers", numOfPlayers));
    }

    /**
     * Shows the message for the last round.
     */

    @Override
    public void showLastRound() {
        showMessage("Last round!\n");
    }

    /**
     * Shows the message for game already started.
     */

    @Override
    public void showGameAlreadyStarted() {
        showMessage("Game already started! You have to wait for the next one :(\n");
        client.stop();
    }

    /**
     * Tells the player if the waiting room is already full.
     */
    @Override
    public void showRemovePlayer() {
        showMessage("Sorry, there are already enough players for this match 😞😭💔😔.\n");
        showMessage("Try again later.\n");
        client.stop();
    }

    /**
     * Shows a generic message.
     *
     * @param message the message to be shown
     */

    @Override
    public void showMessage(String message) {
        System.out.print(message);
    }

    /**
     * Shows the message for the rearrange procedure.
     */

    @Override
    public void rearrangeProcedure(List<Item> items) {
        client.sendMessage(new Message("sort", rearrange(items)));
    }

    /**
     * This is only for Gui view.
     *
     * @param topOfScoringList the list of the players in the top of the scoring
     * @param score            the list of the scores of the players
     */
    @Override
    public void updateScore(List<Integer> topOfScoringList, List<Integer> score) {
        // does nothing as it's a method for GUI
    }

    /**
     * This is only for Gui view.
     */

    @Override
    public void loadLanguage() {

    }

    /**
     * This sets the only one player.
     */

    @Override
    public void setTheOnlyOne(boolean b) {
        this.theOnlyOne = b;
    }

    /**
     * Shows the waiting message for the server to check if the player disconnected.
     */
    @Override
    public void showWaiting() {
        System.out.println("Server is checking if you disconnected...");
    }

    /**
     * This is only for Gui view.
     */
    @Override
    public void disableGame() {
        // does nothing as it's a method for GUI
    }

    /**
     * This is only for Gui view.
     */
    @Override
    public void enableGame(Boolean currentTurn) {
        // does nothing as it's a method for GUI
    }

    /**
     * Shows the message for the connection to server.
     */

    @Override
    public void initiateConnection() {
        System.out.print("Connecting to server... ");
    }

    /**
     * Shows the message for the connection to server success.
     */

    @Override
    public void connectionSuccess() {
        System.out.println(SUCCESS_COLOR + "connected" + RESET);
    }

    /**
     * Shows the message for the connection to server error.
     */
    @Override
    public void connectionError() {
        System.err.println("Unable to connect to the server. Is it running?");
        System.exit(1);
    }

    /**
     * Shows the message that indicates that nobody reconnected.
     */
    @Override
    public void endAlone() {
        showMessage("Nobody reconnected, everyone hates you, nobody wants to play with you. You won champion!\n");
        System.exit(0);
    }
}
