package it.polimi.ingsw.client;

import it.polimi.ingsw.client.socket.ClientSocket;

public class ClientController {
    ClientSocket clientSocket;

    public ClientController() {
        clientSocket = new ClientSocket();
    }
}
