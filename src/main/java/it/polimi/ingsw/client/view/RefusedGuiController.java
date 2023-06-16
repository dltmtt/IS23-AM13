package it.polimi.ingsw.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RefusedGuiController {

    private static GuiView view;

    @FXML
    private Button homeButton;

    public RefusedGuiController(GuiView guiView) {
        view = guiView;
    }

    @FXML
    void homeScreen(ActionEvent event) {
        view.loginProcedure();
    }
}
