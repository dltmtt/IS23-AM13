package it.polimi.ingsw;

import it.polimi.ingsw.server.socket.TCPServer;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class ServerSocketTest {

    @BeforeAll
    public static void setUp() {
        SettingLoader.loadBookshelfSettings();
    }

    @Test
    public void ServerTest() {
        TCPServer server = new TCPServer();
        server.run();
//        assert server.isRunning();
        assertEquals("shutdown", server.getLine());
        server.serverClose();
    }
}
