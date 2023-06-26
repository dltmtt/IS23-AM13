package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.GameCliView;
import it.polimi.ingsw.client.view.GameView;
import it.polimi.ingsw.client.view.GuiView;
import it.polimi.ingsw.utils.SettingLoader;
import org.apache.commons.cli.*;

import java.rmi.RemoteException;

/**
 * This is the entry point of the client. It parses the command line arguments
 * and starts the client in the selected mode.
 */
public class MyShelfie {

    public static Client client; // It's static so that it can be accessed from static methods

    /**
     * The hostname of the server. It's set when the game is launched.
     * It's shared by RMI and TCP.
     */
    public static String HOSTNAME;

    public static void main(String[] args) {
        SettingLoader.loadBookshelfSettings();
        Option protocol = new Option("p", "protocol", true, "select network protocol to use (default: RMI)");
        Option view = new Option("m", "view", true, "launch CLI or GUI (default: CLI)");
        Option hostname = new Option("n", "hostname", true, "set the hostname of the server (default: localhost)");
        Option help = new Option("h", "help", false, "show this help message");

        Options options = new Options();
        options.addOption(protocol);
        options.addOption(view);
        options.addOption(hostname);
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

        MyShelfie.HOSTNAME = line.getOptionValue("hostname", "localhost");
        String protocolType = line.getOptionValue("protocol", "rmi");

        client = null;
        switch (protocolType) {
            case "rmi" -> {
                try {
                    client = new ClientRmi();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            case "tcp" -> {
                try {
                    client = new ClientTcp();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            default -> {
                System.err.println("Invalid protocol: " + protocolType + ". Use 'rmi' or 'tcp'.");
                System.exit(1);
            }
        }

        String modeType = line.getOptionValue("view", "gui"); // setSocketOption
        GameView gameView = null;
        switch (modeType) {
            case "cli" -> gameView = new GameCliView();
            case "gui" -> gameView = new GuiView();
            default -> {
                System.err.println("Invalid view: " + modeType + ". Use 'cli' or 'gui'.");
                System.exit(1);
            }
        }

        client.setView(gameView);
        gameView.setClient(client);
        client.start();
    }

    /**
     *
     * @param client The client to set.
     */
    public void setClient(Client client) {
        MyShelfie.client = client;
    }
}
