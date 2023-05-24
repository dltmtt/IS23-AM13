package it.polimi.ingsw.guiTest;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientRmi;
import it.polimi.ingsw.client.view.GameView;
import it.polimi.ingsw.client.view.GuiView;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.Board;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

public class gameguitest {

    Client client;
    Server server;

    Board board;

    @Test
    void test1() {
        // SettingLoader.loadBookshelfSettings();
        try {
            client = new ClientRmi();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        board = new Board(2);
        board.fill();

        GameView gui = new GuiView();
        client.setView(gui);
        gui.setClient(client);
        gui.startView(client);

        // gui.startGame(new Message(0));
    }
}
