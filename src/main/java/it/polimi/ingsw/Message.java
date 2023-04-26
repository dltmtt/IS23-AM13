package it.polimi.ingsw;

import it.polimi.ingsw.utils.Coordinates;

import java.util.List;

public interface Message {
    String getType();

    String getUsername();

    int getAge();

    boolean getIsFirstGame();

    boolean getIsFirstPlayer();

    List<Coordinates> getMove();
}
