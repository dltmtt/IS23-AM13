package it.polimi.ingsw.utils;

public record Coordinates(Integer x, Integer y) {
    // There's no need to override default methods such as equals,
    // records take care of them.
}
