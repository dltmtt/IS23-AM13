package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.CliUtilities;
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

    @Override
    public List<Integer> showPick() {
        System.out.println("It's your turn! Do your move!");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String input;
        List<Integer> move = new ArrayList<>();
        String[] coordinates;
        String start, end;
        try {
            do {
                do {
                    input = in.readLine(); // Input example: 1,2-2,3
                    input = input.replace(" ", ""); // Ignore spaces
                    coordinates = input.split("-"); // Split start and end coordinate
                } while (coordinates.length != 2);
                start = coordinates[0];
                end = coordinates[1];
                //split start and end coordinate in row and column
            } while (start.split(",").length != 2 || end.split(",").length != 2);

            String[] startCoordinate = start.split(",");
            String[] endCoordinate = end.split(",");

            // Convert string to int
            move.add(Integer.parseInt(startCoordinate[0]));
            move.add(Integer.parseInt(startCoordinate[1]));
            move.add(Integer.parseInt(endCoordinate[0]));
            move.add(Integer.parseInt(endCoordinate[1]));
        } catch (IOException e) {
            System.err.println("An error occurred while reading the move.");
        }
        return move;
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
        showMessage(welcomeMessage);
        String title = """
                                                          ^?55P?.                                                  \s
                        ?555^   :Y5PJ                    !GB7JG&P  :^^^.             .!~.  ~JJ?7.                  \s
                        ^5#B^   .G&B~                    Y&Y JB#Y. ^P#P:             .GP.:PP!77?B^                 \s
                         ?B#7   !#&P       .:   :::.     ?#B~ .:    ?#J              .G5.Y#:^B#PG^                 \s
                         ?BGB~ :BP#P    ~YGBJ  ^G#?:      ?#&P!.    ?#Y:^^:          .P5:5B: :~~~^                 \s
                         ?BJ?G:557BP     .7BG^^G#7         ^JB&BJ.  ?BBBGBGJ.  .^~~: .P5 ~B?   .YB!   :~~:         \s
                         7GY JBP:7B5       ~PBBG~         .  .!B#P: ?BB7::P#~ !5?!!Y? 55.?PB5J~:JY~ ~YJ!!JY.       \s
                         7G5 7BY ?B5  :!7!. ?BP^        75PY!  7BB? ?BY   YG.~GP??7JJ.5Y...YG: :PB^:5G?77?Y:       \s
                         7GP.:^^.JG5 ~PGGG7!GY:        .GBGP5^:YB5! 7GJ  :PY !GJ  75?.YY   7B! .5G:^P5. ~5Y:       \s
                        ^JPG?   !5GP~:JPPPPG?.          ~Y5P55PPY! .YPY. ?PP^.?5J?5P?:YY. .YP7 :5P^ 75Y?5GY:       \s
                        .::::.  ::::: .^~!~:              .:^^^:.  .:::. ::::  .^^^. .::. .::: .::.  .^^^:         \s
                """; // https://www.text-image.com/convert/
        System.out.println(title);
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
    public boolean showRearrange() {
        return CliUtilities.askYesNoQuestion("Do you want to rearrange your picked items?", "n");
    }

    @Override
    public List<Integer> rearrange(List<Item> items, int size) {
        ItemView.printItems(items);
        showMessage("Insert the new order (starting from 0, the syntax is a,b,...,n): \n");
        List<Integer> newOrder = new ArrayList<>();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str;
        try {
            str = in.readLine();
            str = str.replace(" ", "");
            String[] numb = str.split(",");
            newOrder.add(Integer.parseInt(numb[0]));
            newOrder.add(Integer.parseInt(numb[1]));
        } catch (IOException e) {
            System.err.println("An error occurred while reading the move.");
        }
        return newOrder;
    }

    @Override
    public void showCurrentScore(int score) {
        System.out.println("Your current score is " + score);
    }

    @Override
    public void showMessage(String message) {
        System.out.print(message);
    }
}
