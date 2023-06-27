package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientRmi;
import it.polimi.ingsw.client.ClientTcp;
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
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static it.polimi.ingsw.utils.CliUtilities.RESET;
import static it.polimi.ingsw.utils.CliUtilities.SUCCESS_COLOR;

public class GameCliView implements GameView {

    private static final String illegalNumberOfPlayersError = "The number of players must be between 2 and 4 (inclusive): ";
    public Client client;
    private boolean theOnlyOne = false;

    public GameCliView() {
        String ip = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.println("Insert Server IP: ");
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
        do {
            System.out.println("Choose a protocol (rmi or tcp): ");
            try {
                protocolType = reader.readLine();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
            if (!protocolType.equals("rmi") && !protocolType.equals("tcp")) {
                System.err.println("Invalid protocol: " + protocolType + ". Use 'rmi' or 'tcp'.");
            }
        } while (!protocolType.equals("rmi") && !protocolType.equals("tcp"));

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

    @Override
    public void loginProcedure() {
        showStartGame();
        String username = showLogin();
        boolean firstGame = promptFirstGame();
        client.startPingThread(username);
        client.sendMessage(new Message("completeLogin", username, 0, firstGame, 0));
    }

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

    public String showLogin() {
        showMessage(insertUsernamePrompt);
        return readUsername();
    }

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
     * @param card the number of the card to show
     * @throws IOException if the file cannot be read
     * @throws ParseException if the file is not in the correct format
     */
    public void showPersonalGoal(int card) throws IOException, ParseException {
        PersonalGoalView personalGoalView = new PersonalGoalView(card);
        personalGoalView.printLayout();
    }

    /**
     * Shows the PublicGoal cards.
     * @param card the number of the card to show
     * @param occurrences the number of occurrences of the card
     * @param size is the number of items in each occurrence of the common goal
     * @param horizontal true if the cards are to be printed horizontally, false if vertically
     */
    public void showCommonGoal(String card, int occurrences, int size, boolean horizontal) {
        CommonGoalView.print(card, occurrences, size, horizontal);
    }

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
     * @param board the board to show
     *
     */
    @Override
    public void showBoard(Board board) {
        BoardView boardView = new BoardView(board);
        showMessage("Here's the board:\n");
        boardView.printBoard();
    }

    /**
     * This method prints the bookshelf.
     * @param bookshelf the bookshelf to show
     *
     */
    @Override
    public void showBookshelf(Bookshelf bookshelf) {
        BookshelfView bookshelfView = new BookshelfView(bookshelf);
        showMessage("Here's your bookshelf:\n");
        bookshelfView.printBookshelf();
    }

    /**
     * This method prints the opening image of the CLI.
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
     * This method prints the end game message.
     * @param winners the list of winners
     * @param losers the list of losers
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
     * This method asks the player if they want to rearrange the items they've picked.
     * @param items the items the player has picked
     * @return true if the player wants to rearrange the items, false otherwise
     */
    @Override
    public boolean showRearrange(List<Item> items) {
        ItemView.printItems(items);
        return CliUtilities.askYesNoQuestion("Do you want to rearrange the items you've picked?", "n");
    }

    /**
     * This method rearranges the order of the picked items.
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
     * This method shows the current score of the player.
     * @param score the current score of the player
     */
    @Override
    public void showCurrentScore(int score) {
        showMessage("Your current score is " + score + "\n");
    }

    /**
     * This method shows the message of the waiting room.
     */
    @Override
    public void waitingRoom() {
        showMessage("Waiting for other players to join...\n");
    }

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
        // showCurrentScore(message.getIntMessage("score"));
        pickOtherBookshelf(bookshelves);

        // Board
        showBoard(message.getBoard());
    }

    @Override
    public void pickMyBookshelf(HashMap<String, Bookshelf> bookshelves) {
        String name = client.getUsername();
        showBookshelf(bookshelves.get(name));
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void showOtherBookshelf(Bookshelf bookshelf, String name) {
        showMessage(name + "'s bookshelf:\n");
        BookshelfView bookshelfView = new BookshelfView(bookshelf);
        bookshelfView.printOtherBookshelf();
    }

    // TODO: check if this works
    @Override
    public void usernameError() {
        System.err.println("Username already taken. Retry.");
        String username = showLogin();
        boolean firstGame1 = promptFirstGame();
        client.sendMessage(new Message("completeLogin", username, 0, firstGame1, 0));
    }

    @Override
    public void completeLoginError() {
        usernameError();
    }

    @Override
    public void playerNumberError() {
        System.err.println(illegalNumberOfPlayersError);
        playerChoice();
    }

    @Override
    public void playerChoice() {
        int numOfPlayers = promptNumberOfPlayers();
        client.sendMessage(new Message("numOfPlayersMessage", "numOfPlayers", numOfPlayers));
    }

    @Override
    public void showLastRound() {
        showMessage("Last round!\n");
    }

    @Override
    public void showGameAlreadyStarted() {
        showMessage("Game already started! You have to wait for the next one :(\n");
        client.stop();
    }

    /**
     * This method tells the player if the waiting room is already full.
     */
    @Override
    public void showRemovePlayer() {
        showMessage("Sorry, there are already enough players for this match 😞😭💔😔.\n");
        showMessage("Try again later.\n");
        client.stop();
    }

    @Override
    public void showMessage(String message) {
        System.out.print(message);
    }

    public void showDisconnection(List<String> disconnected) {
        showMessage("The following players disconnected: ");
        for (String player : disconnected) {
            showMessage(player + " ");
        }
        showMessage("\n");
    }

    @Override
    public void rearrangeProcedure(List<Item> items) {
        client.sendMessage(new Message("sort", rearrange(items)));
    }

    @Override
    public void updateScore(List<Integer> topOfScoringList, List<Integer> score) {
        // does nothing as it's a method for GUI
    }

    @Override
    public void loadLanguage() {

    }

    @Override
    public void setTheOnlyOne(boolean b) {
        this.theOnlyOne = b;
    }

    @Override
    public void showWaiting() {
        System.out.println("Server is checking if you disconnected...");
    }

    @Override
    public void disableGame() {
        // does nothing as it's a method for GUI
    }

    @Override
    public void enableGame(Boolean currentTurn) {
        // does nothing as it's a method for GUI
    }

    @Override
    public void initiateConnection() {
        System.out.print("Connecting to server... ");
    }

    @Override
    public void connectionSuccess() {
        System.out.println(SUCCESS_COLOR + "connected" + RESET);
    }

    @Override
    public void connectionError() {
        System.err.println("Unable to connect to the server. Is it running?");
        System.exit(1);
    }
}
