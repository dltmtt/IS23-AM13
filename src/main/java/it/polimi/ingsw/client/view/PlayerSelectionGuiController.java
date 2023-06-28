package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.MyShelfie;
import it.polimi.ingsw.commons.Message;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;

public class PlayerSelectionGuiController {

    /**
     * This is the button that starts the waiting room
     */
    @FXML
    public Button createWaitingRoom;

    /**
     * This is the slider that allows the player to choose the number of players, from 2 to 4
     */
    @FXML
    public Slider playerSlider;

    /**
     * Constructor for the class
     */
    public PlayerSelectionGuiController() {
    }

    /**
     * This method is called when the create waiting room button is clicked, sending the number of players to the server
     */
    @FXML
    public void startWaitingRoom() {
        MyShelfie.client.sendMessage(new Message("numOfPlayersMessage", "numOfPlayers", (int) playerSlider.getValue()));
    }
}
