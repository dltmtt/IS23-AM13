package it.polimi.ingsw;

import it.polimi.ingsw.client.socket.ClientSocket;
import it.polimi.ingsw.utils.SettingLoader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.fail;

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
        clientSocket.sendMessage("test simone");
        clientSocket.sendMessage("shutdown");
        clientSocket.close();
    }


    @Test
    public void JSONClientTest() {
        ClientSocket clientSocket = null;
        try {
            clientSocket = new ClientSocket();
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
        JSONParser parser = new JSONParser();
        JSONObject TestJSON = null;
        try {
            TestJSON = (JSONObject) parser.parse(new FileReader("src/test/java/it/polimi/ingsw/test.json"));
        } catch (IOException | ParseException e) {
            fail();
        }

        clientSocket.sendJsonObjet(TestJSON);
        clientSocket.sendMessage("shutdown");
        clientSocket.close();
    }
}
