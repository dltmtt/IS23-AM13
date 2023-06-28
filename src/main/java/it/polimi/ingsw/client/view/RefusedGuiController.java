package it.polimi.ingsw.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RefusedGuiController {

    private static GuiView view;

    @FXML
    private Button homeButton;

    /**
     * Constructor for the class
     *
     * @param guiView the gui view
     */
    public RefusedGuiController(GuiView guiView) {
        view = guiView;
    }

    /**
     * This method is called when the home button is clicked, changing the scene to the login one
     */
    @FXML
    public void homeScreen(ActionEvent event) {
        view.loginProcedure();
    }
}
