package it.polimi.ingsw.server;

import it.polimi.ingsw.commons.Message;

public class ServerParser {


    public String getMessageCategory(Message message) {
        String category = message.getCategory();
        return switch (category) {
            case "username" -> "username";
            case "age" -> "age";
            case "firstGame" -> "firstGame";
            case "numPlayer" -> "numPlayer";
            default -> null;
        };
    }

    public String getUsername(Message message) {
        return message.getUsername();
    }

    public int getAge(Message message) {
        return message.getAge();
    }

    public boolean getFirstGame(Message message) {
        return message.getFirstGame();
    }

    public int getNumPlayer(Message message) {
        return message.getNumPlayer();
    }
}
