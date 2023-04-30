package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.GameView;

// This is abstract (non instantiable) because each client will either
// be an RMI client or a Socket client
public abstract class Client {
    // @Simone if you try to move these constants in a configuration file
    // I'll kill you slowly and painfully. You can however change the value
    // of PORT_SOCKET and use it in your code.
    protected static final int PORT_RMI = 1099;
    protected static final int PORT_SOCKET = 1234;
    protected static final String HOSTNAME = "localhost"; // Shared by RMI and socket
    public GameView gameView;

    public void setView(GameView gameView) {
        this.gameView = gameView;
    }

    public abstract void login(String username);

    public abstract void logout();
}
