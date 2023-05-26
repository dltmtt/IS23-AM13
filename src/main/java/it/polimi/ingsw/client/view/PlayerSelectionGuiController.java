package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.MyShelfie;
import it.polimi.ingsw.commons.Message;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;

public class PlayerSelectionGuiController {

    @FXML
    public Button createWaitingRoom;

    @FXML
    public Slider playerSlider;

    public PlayerSelectionGuiController() {
    }

    @FXML
    public void startWaitingRoom() {
        MyShelfie.client.sendMessage(new Message("numOfPlayers", "", 0, false, (int) playerSlider.getValue()));
    }
}
