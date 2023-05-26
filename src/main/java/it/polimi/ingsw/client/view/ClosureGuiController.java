package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ClosureGuiController {

    private final Client client;
    private final GuiView view;

    @FXML
    private Button homeButton;

    public ClosureGuiController(Client client, GuiView view) {
        this.client = client;
        this.view = view;
    }

    @FXML
    public void onHomeButtonClicked() {
        view.loginProcedure();
    }
}