package it.polimi.ingsw.server;

public class ServerParser {


    public String getMessageCategory(String message) {
        String category = null;
        if (message.startsWith("username")) {
            return "username";
        } else if (message.startsWith("age")) {
            return "age";
        }
        return null;
    }

    public String getUsername(String message) {
        return message.substring(8);
    }

    public int getAge(String message) {
        return Integer.parseInt(message.substring(3));
    }

    public boolean getFirstGame(String message) {
        return Boolean.parseBoolean(message.substring(9));
    }
}
