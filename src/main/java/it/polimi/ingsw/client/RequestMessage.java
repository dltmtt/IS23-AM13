package it.polimi.ingsw.client;

import it.polimi.ingsw.utils.Coordinates;

import java.util.List;

public class RequestMessage {
    private final String message;

    public RequestMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return "Type";
    }

    public String getUsername() {
        return "Nickname";
    }

    public int getAge() {
        return 0;
    }

    public boolean getIsFirstGame() {
        return false;
    }

    public boolean getIsFirstPlayer() {
        return false;
    }

    public List<Coordinates> getMove() {
        return null;
    }

}
