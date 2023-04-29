package it.polimi.ingsw;

import it.polimi.ingsw.client.socket.Client;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ClientSocketTest {

    @BeforeAll
    public static void setUp() {
        SettingLoader.loadBookshelfSettings();
    }

    @Test
    public void ClientTest() {
        Client client = new Client();
        try {
            client.sendMessage("ciao");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}