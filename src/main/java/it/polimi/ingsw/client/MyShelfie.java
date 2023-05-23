package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.GameCliView;
import it.polimi.ingsw.client.view.GameGuiView;
import it.polimi.ingsw.client.view.GameView;
import it.polimi.ingsw.utils.SettingLoader;
import org.apache.commons.cli.*;

/**
 * This is the entry point of the client. It parses the command line arguments
 * and starts the client in the selected mode.
 */
public class MyShelfie {

    public static void main(String[] args) {
        SettingLoader.loadBookshelfSettings();
        Option protocol = new Option("p", "protocol", true, "select network protocol to use (default: RMI)");
        Option view = new Option("m", "view", true, "launch CLI or GUI (default: CLI)");
        Option help = new Option("h", "help", false, "show this help message");

        Options options = new Options();
        options.addOption(protocol);
        options.addOption(view);
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

        String protocolType = line.getOptionValue("protocol", "rmi");

        Client client = null;
        switch (protocolType) {
            case "rmi" -> client = new ClientRmi();
            case "socket" -> client = new ClientTcp();
            default -> {
                System.err.println("Invalid protocol: " + protocolType + ". Use 'rmi' or 'socket'.");
                System.exit(1);
            }
        }

        String modeType = line.getOptionValue("view", "cli"); //setSocketOption
        GameView gameView = null;
        switch (modeType) {
            case "cli" -> gameView = new GameCliView();
            case "gui" -> gameView = new GameGuiView();
            default -> {
                System.err.println("Invalid view: " + modeType + ". Use 'cli' or 'gui'.");
                System.exit(1);
            }
        }

        client.setView(gameView);
        gameView.setClient(client);
        client.start();
    }
}
