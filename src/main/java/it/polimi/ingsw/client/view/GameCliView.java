package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
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

public class GameCliView implements GameView {

    private static final String illegalNumberOfPlayersError = "The number of players must be between 2 and 4 (inclusive): ";
    public Client client;

    @Override
    public void loginProcedure() {
        showStartGame();
        String username = showLogin();
        boolean firstGame = promptFirstGame();
        client.startPingThread(username);
        client.sendMessage(new Message("completeLogin", username, 0, firstGame, 0));
        client.checkServerConnection();
    }

    @Override
    public void startView(Client client) {
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
                n = Integer.parseInt(in.readLine());
                valid = true;
            } catch (NumberFormatException e) {
                System.err.print("The input is not a number. Please insert a number: ");
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

    public void showPersonalGoal(int card) throws IOException, ParseException {
        PersonalGoalView personalGoalView = new PersonalGoalView(card);
        personalGoalView.printLayout();
    }

    public void showCommonGoal(String card, int occurrences, int size, boolean horizontal) {
        CommonGoalView.print(card, occurrences, size, horizontal);
    }

    @Override
    public void showPick() {
        List<Coordinates> coordinates = new ArrayList<>();
        showMessage("Insert the coordinates of the first and the last cell you want to to pick, e.g. (1, 2) - (2, 3): ");
        boolean valid = false;
        while (!valid) {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
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
                valid = true;
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.err.print("The input is not valid. Please insert the coordinates in the correct format: ");
                coordinates.clear(); // Without this, a coordinate could be added to the list even if the input in its entirety is not valid
            } catch (IOException e) {
                System.err.println("An error occurred while reading the coordinates.");
            }
        }

        client.sendMessage(new Message(coordinates.get(0), coordinates.get(1)));
    }

    @Override
    public void showBoard(Board board) {
        BoardView boardView = new BoardView(board);
        showMessage("Here's the board:\n");
        boardView.printBoard();
    }

    @Override
    public void showBookshelf(Bookshelf bookshelf) {
        BookshelfView bookshelfView = new BookshelfView(bookshelf);
        showMessage("Here's your bookshelf:\n");
        bookshelfView.printBookshelf();
    }

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

    @Override
    public void showEndGame(List<String> winners, List<Integer> scores, List<String> losers, List<Integer> loserScores) {
        showMessage("The game is over!\n");
        showMessage("The winners are:\n");
        for (int i = 0; i < winners.size(); i++) {
            showMessage(winners.get(i) + " with " + scores.get(i) + " points\n");
        }
        showMessage("The losers are:\n");
        for (int i = 0; i < losers.size(); i++) {
            showMessage(losers.get(i) + " with " + loserScores.get(i) + " points\n");
        }
    }

    @Override
    public boolean showRearrange(List<Item> items) {
        ItemView.printItems(items);
        return CliUtilities.askYesNoQuestion("Do you want to rearrange the items you've picked?", "n");
    }

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
                String input = in.readLine();

                input = input.replaceAll("\\s", ""); // Ignore whitespaces

                String[] order = input.split(","); // Split the input into the new order
                if (order.length != items.size())
                    throw new NumberFormatException(); // If the new order doesn't contain all the items

                for (String index : order) {
                    // This conversion throws NumberFormatException if the input is not a number
                    int i = Integer.parseInt(index);

                    // Check if the index is within the items list
                    if (i < 0 || i > items.size() - 1) {
                        throw new NumberFormatException();
                    }

                    newOrder.add(i);
                }
                valid = true;
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.err.print("The input is not valid. Please insert the new order in the correct format: ");
                newOrder.clear(); // Without this, an index could be added to the list even if the input in its entirety is not valid
            } catch (IOException e) {
                System.err.println("An error occurred while reading the new order.");
            }
        }
        return newOrder;
    }

    @Override
    public void showCurrentScore(int score) {
        showMessage("Your current score is " + score + "\n");
    }

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
        HashMap<Bookshelf, String> bookshelves = message.getAllBookshelves();
        pickMyBookshelf(bookshelves);
        // showCurrentScore(message.getIntMessage("score"));
        pickOtherBookshelf(bookshelves);

        // Board
        showBoard(message.getBoard());
    }

    /*
    @Override
    public void endGame() {
        showMessage("The game is over!\n");
    }

     */

    @Override
    public void pickMyBookshelf(HashMap<Bookshelf, String> bookshelves) {
        String name = client.getUsername();
        for (Bookshelf bookshelf : bookshelves.keySet()) {
            if (bookshelves.get(bookshelf).equals(name)) {
                showBookshelf(bookshelf);
                break;
            }
        }
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
}
