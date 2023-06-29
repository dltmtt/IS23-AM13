package it.polimi.ingsw.utils;

/**
 * This class is used to represent the coordinates of the items in the board
 * @param x
 * @param y
 */
public record Coordinates(Integer x, Integer y) {
    // There's no need to override default methods such as equals,
    // records take care of them.
}
