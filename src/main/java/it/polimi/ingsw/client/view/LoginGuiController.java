package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.commons.Message;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginGuiController {

    private final Client client;
    private final GuiView view;
    @FXML
    private TextField username;
    @FXML
    private Slider ageSlider, playerSlider;
    @FXML
    private Label ageValue, playerValue, usernameError, ageError, connectionError, userMessage;
    @FXML
    private CheckBox firstGame;
    @FXML
    private Button startButton;

    public LoginGuiController(Client client, GuiView view) {
        this.client = client;
        this.view = view;
    }

    @FXML
    public void onSliderChanged() {
        ageValue.setText(String.valueOf((int) ageSlider.getValue()));
    }

    @FXML
    public void onPlayerSliderChanged() {
        playerValue.setText(String.valueOf((int) playerSlider.getValue()));
    }

    @FXML
    public void onStartButtonClicked() {
        client.sendMessage(new Message("completeLogin", username.getText(), 0, firstGame.isSelected(), (int) playerSlider.getValue()));

        client.startPingThread(username.getText());
    }
}