package it.polimi.ingsw.server.model;

/**
 * This interface defines which objects can get the player points.
 */
public interface AbleToGetPoints {

    /**
     * This method returns the points of the object.
     * @return the points of the object
     */
    int getPoints();
}
