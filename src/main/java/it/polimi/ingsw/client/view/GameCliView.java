package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utils.CliUtilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    @Override
    public void showMessage(String message) {
        System.out.print(message);
    }
}
