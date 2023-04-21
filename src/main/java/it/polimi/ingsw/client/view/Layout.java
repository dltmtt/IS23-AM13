package it.polimi.ingsw.client.view;

public abstract class Layout {
    public String description;

    /**
     * Prints the layout description and its representation on the CLI.
     */
    public abstract void printLayout();

    /**
     * Draws the layout on the GUI.
     */
    public abstract void drawLayout();
}
