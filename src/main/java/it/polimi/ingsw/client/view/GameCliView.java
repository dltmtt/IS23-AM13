package it.polimi.ingsw.client.view;

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
import java.util.List;

public class GameCliView extends GameView {

    /**
     * Reads the username from the command line and prompts the user to confirm it.
     * <p>If the user confirms the input, the method prints a welcome message.
     * <p>If the user does not confirm the input, the method prints a prompt to
     * insert the username again and calls itself recursively.
     *
     * @return the username inserted by the user
     */
    @Override
    public String readUsername() {
        String username = null;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            username = in.readLine();
            boolean answerConfirmed = CliUtilities.confirmInput(username);

            while (!answerConfirmed) {
                System.out.print(insertUsernameAgainPrompt);
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
    @Override
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

    @Override
    public String showLogin() {
        showMessage(insertUsernamePrompt);
        return readUsername();
    }

    public int promptAge() {
        showMessage(insertAgePrompt);
        int age = readNumber();
        while (age < 0 || age > 125) {
            System.err.print("Are you serious? You can lie, but be realistic: ");
            age = readNumber();
        }
        return age;
    }

    public boolean promptFirstGame() {
        return CliUtilities.askYesNoQuestion(firstGameQuestion, "y");
    }

    public int promptNumberOfPlayers() {
        showMessage(insertNumberOfPlayersPrompt);
        int n = readNumber();
        while (n < 2 || n > 4) {
            System.err.print("The number of players must be between 2 and 4 (inclusive): ");
            n = readNumber();
        }
        return n;
    }

    @Override
    public int promptInsert() {
        showMessage("Insert the index of the column where you want to insert the picked items (starting from 0): ");
        int index = readNumber();
        while (index < 0 || index > Bookshelf.getColumns() - 1) {
            System.err.print(insertError);
            index = readNumber();
        }
        return index;
    }

    @Override
    public void showPersonalGoal(int card) throws IOException, ParseException {
        PersonalGoalView personalGoalView = new PersonalGoalView(card);
        personalGoalView.printLayout();
    }

    @Override
    public void showCommonGoal(String card, int occurrences, int size, boolean horizontal) {
        CommonGoalView.print(card, occurrences, size, horizontal);
    }

    /**
     * @return a list of coordinates representing the first and last cell of the pick
     */
    @Override
    public List<Coordinates> showPick() {
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
        return coordinates;
    }

    @Override
    public void showBoard(Board board) {
        BoardView boardView = new BoardView(board);
        System.out.println("Here's your board:");
        boardView.printBoard();
    }

    @Override
    public void showBookshelf(Bookshelf bookshelf) {
        BookshelfView bookshelfView = new BookshelfView(bookshelf);
        System.out.println("Here's your bookshelf:");
        bookshelfView.printBookshelf();
    }

    @Override
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
    public void showEndGame(List<String> winners) {
        System.out.println("The game is over!");
        System.out.println("The winners are: ");
        for (String winner : winners) {
            System.out.println(winner);
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
                For example, if you picked three items you could enter 2, 0, 1:
                the first item will be moved to the third position, the second to the first, and the third to the second.
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
        System.out.println("Your current score is " + score);
    }

    @Override
    public void showDisconnection() {
        showMessage("All other players have disconnected. Please wait...\n");
    }

    @Override
    public void showMessage(String message) {
        System.out.print(message);
    }
}
