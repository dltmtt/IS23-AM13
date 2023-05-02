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
    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.sendMessage("ciao, messaggio mandato in automatico");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        client.close();
    }
}