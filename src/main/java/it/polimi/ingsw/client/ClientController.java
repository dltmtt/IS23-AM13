package it.polimi.ingsw.client;

import it.polimi.ingsw.client.socket.ClientCSSocket;

import static it.polimi.ingsw.utils.SettingLoader.getServerIp;
import static it.polimi.ingsw.utils.SettingLoader.getServerPort;

public class ClientController {
    ClientCSSocket clientSocket;

    public ClientController() {
        clientSocket = new ClientCSSocket(getServerIp(), getServerPort());
    }
}
