package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.GameView;

// This is abstract (non instantiable) because each client will either
// be an RMI client or a Socket client
public abstract class Client {

    protected final ClientParser parser = new ClientParser();

    public GameView gameView;

    public void setView(GameView gameView) {
        this.gameView = gameView;
    }

    public abstract void start();

    public abstract void login();
}
