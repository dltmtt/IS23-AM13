package it.polimi.ingsw.utils;

import java.util.List;

public interface Message {

    String getType();

    String getUsername();

    int getAge();

    boolean getIsFirstGame();

    boolean getIsFirstPlayer();

    List<Coordinates> getMove();
}
