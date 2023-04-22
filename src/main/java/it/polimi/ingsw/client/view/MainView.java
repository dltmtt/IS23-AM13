package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utils.CLIUtilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// I (@MDXZ-delti) am not sure if the following code should be in the view package.
// We should discuss this.
public class MainView {
    private static final String insertUsernamePrompt = "Insert your username: ";
    private static final String insertUsernameAgainPrompt = "Please, insert your username again: ";

    public static void main(String[] args) {
        // This section is temporary. Mode will be read from args or from a config file.
        String mode = "cli";
        if (mode.equals("cli")) {
            MainView mainView = new MainView();
            System.out.print(insertUsernamePrompt);
            mainView.readUsernameCLI();
        } else if (mode.equals("gui")) {
            System.out.println("GUI has not been implemented yet but this code will never run anyway.");
        }
    }

    /**
     * Reads the username from the command line and prompts the user to confirm it.
     * <p>If the user confirms the input, the method prints a welcome message.
     * <p>If the user does not confirm the input, the method prints a prompt to
     * insert the username again and calls itself recursively.
     */
    public void readUsernameCLI() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            String username = in.readLine();
            boolean answerConfirmed = CLIUtilities.confirmInput(username);

            if (answerConfirmed) {
                System.out.println("Welcome, " + username + "!");
            } else {
                System.out.print(insertUsernameAgainPrompt);
                this.readUsernameCLI();
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the username.");
            e.printStackTrace();
        }
    }
}
