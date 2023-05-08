package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.utils.CliUtilities;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public int readAge() {
        int age = 0;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            age = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("An error occurred while reading the age.");
        }
        return age;
    }

    @Override
    public void showLogin() {
        showMessage(welcomeMessage);
        showMessage(insertUsernamePrompt);
        String username = readUsername();
        try {
            gameController.login(username);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("An error occurred while logging in.");
        }
    }

    public boolean readFirstGame() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            String response = in.readLine();
            return Objects.equals(response, "Y");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("An error occurred while reading the age.");
            return false;
        }
    }

    public int readNumberOfPlayers() {
        int numberOfPlayers = 0;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            numberOfPlayers = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("An error occurred while reading the age.");
        }
        return numberOfPlayers;
    }

    public int promptAge() {
        showMessage(insertAgePrompt);
        return readAge();
    }

    public boolean promptFirstGame() {
        try {
            return CliUtilities.askYesNoQuestion(firstGamePrompt);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //        return readFirstGame();
    }

    public int promptNumberOfPlayers() {
        showMessage(insertNumberOfPlayersPrompt);
        return readNumberOfPlayers();
    }

    @Override
    public void showPersonalCard(int card) throws IOException, ParseException {
        PersonalGoalView personalGoalView = new PersonalGoalView(card);
        personalGoalView.printLayout();
    }

    @Override
    public void showCommonGoal(String card, int occurences, int size, boolean horizontal) throws IOException, ParseException {
        CommonGoalView.print(card, occurences, size, horizontal);
    }

    @Override
    public List<Integer> showMove() {
        System.out.println("It's your turn! Do your move!");
        List<Integer> move = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int row_column = 0;
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                row_column = Integer.parseInt(in.readLine());
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("An error occurred while reading the age.");
            }
            move.add(row_column);
        }

        return move;
    }

    @Override
    public void showBoard(Board board) {
        BoardView boardView = new BoardView(board);
        System.out.println("The board:");
        boardView.printBoard();
    }

    @Override
    public void showBookshelf(Bookshelf bookshelf) {
        BookshelfView bookshelfView = new BookshelfView(bookshelf);
        System.out.println("Your Bookshelf:");
        bookshelfView.printBookshelf();
    }

    @Override
    public void showMessage(String message) {
        System.out.print(message);
    }
}
