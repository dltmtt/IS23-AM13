package it.polimi.ingsw;

import it.polimi.ingsw.server.socket.MultiEchoServer;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ServerSocketTest {

    @BeforeAll
    public static void setUp() {
        SettingLoader.loadBookshelfSettings();
    }

    @Test
    public static void main(String[] args) {
        MultiEchoServer server = new MultiEchoServer();
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}