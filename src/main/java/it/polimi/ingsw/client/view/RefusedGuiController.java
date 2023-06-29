package it.polimi.ingsw.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * This class is the controller for the refused gui, in case there are too many players who want to play.
 */
public class RefusedGuiController {

    private static GuiView view;

    @FXML
    private Button homeButton;

    /**
     * Constructor for the class.
     *
     * @param guiView the gui view.
     */
    public RefusedGuiController(GuiView guiView) {
        view = guiView;
    }

    /**
     * Called when the home button is clicked, changing the scene to the login one
     * @param event the event of the button being pressed.
     */
    @FXML
    public void homeScreen(ActionEvent event) {
        view.loginProcedure();
    }
}
