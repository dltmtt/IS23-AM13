package it.polimi.ingsw;

import it.polimi.ingsw.client.socket.ClientSocket;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ClientSocketTest {

    @BeforeAll
    public static void setUp() {
        SettingLoader.loadBookshelfSettings();
    }

    @Test
    public void ClientTest() {
        ClientSocket clientSocket = null;
        try {
            clientSocket = new ClientSocket();
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
        clientSocket.sendMessage("test");
        clientSocket.sendMessage("shutdown");
        clientSocket.close();
    }
}
