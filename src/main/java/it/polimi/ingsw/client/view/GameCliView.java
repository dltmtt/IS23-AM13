package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utils.CliUtilities;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Coordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
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
    public void showPersonalCard(HashMap<Coordinates, Color> card) {
        PersonalGoalView personalGoalView = new PersonalGoalView(card);
        personalGoalView.printLayout();
    }

    @Override
    public void showMessage(String message) {
        System.out.print(message);
    }
}
