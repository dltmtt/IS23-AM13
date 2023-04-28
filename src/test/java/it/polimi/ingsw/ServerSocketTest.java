package it.polimi.ingsw;

import it.polimi.ingsw.client.socket.ClientCSSocket;
import it.polimi.ingsw.server.socket.ServerCSHandler;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class ServerSocketTest {

    @BeforeAll
    public static void setUp() {
        SettingLoader.loadBookshelfSettings();
        SettingLoader.loadConnectionSettings();
    }

    @Test
    public void ServerTest() {
        ServerCSHandler server = new ServerCSHandler();
        server.run();
        for (ClientCSSocket client : server.getSocketList().values()) {
            client.sendMessage("test dal server");
        }
        assertEquals("shutdown", server.getLine());
        //server.serverClose();
    }
}
