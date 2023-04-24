package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utils.CLIUtilities;
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// I (@MDXZ-delti) am not sure if the following code should be in the view package.
// We should discuss this.
public class MainView {
    private static final String insertUsernamePrompt = "Insert your username: ";
    private static final String insertUsernameAgainPrompt = "Please, insert your username again: ";

    public static void main(String[] args) {
        Options options = new Options();

        Option protocol = new Option("p", "protocol", true, "network protocol to use (default: rmi)");
        Option mode = new Option("m", "mode", true, "launch cli or gui (default: cli)");
        Option help = new Option("h", "help", false, "show help message");

        options.addOption(protocol); // Not used yet
        options.addOption(mode);
        options.addOption(help);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine line = null;

        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        if (line.hasOption("help")) {
            formatter.printHelp("myshelfie", options);
            System.exit(0);
        }

        // Use the default values if the user does not specify them
        String modeValue = line.getOptionValue("mode", "cli");
        String protocolValue = line.getOptionValue("protocol", "rmi");

        switch (protocolValue) {
            case "rmi" -> System.out.println("Using RMI (not implemented yet).");
            case "socket" -> System.out.println("Using socket (not implemented yet).");
            default -> {
                System.err.println("Invalid protocol: " + protocolValue + ". Use 'rmi' or 'socket'.");
                System.exit(1);
            }
        }

        switch (modeValue) {
            case "cli" -> {
                MainView mainView = new MainView();
                System.out.print(insertUsernamePrompt);
                mainView.readUsernameCLI();
            }
            case "gui" -> System.out.println("GUI has not been implemented yet.");
            default -> {
                System.err.println("Invalid mode: " + modeValue + ". Use 'cli' or 'gui'.");
                System.exit(1);
            }
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
