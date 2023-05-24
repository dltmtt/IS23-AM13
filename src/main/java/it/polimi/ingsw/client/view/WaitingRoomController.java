package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WaitingRoomController {
    @FXML
    private Label text;

    public Client client;

    @FXML
    public void onTextChanged(){
        text.setText("          You're in the waiting room...\n" + "Please wait for other players to join the game.");
    }


}
