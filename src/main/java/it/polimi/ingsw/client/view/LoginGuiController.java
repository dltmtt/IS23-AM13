package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.MyShelfie;
import it.polimi.ingsw.commons.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginGuiController {

    private final Client client;
    @FXML
    private TextField username;
    @FXML
    private Slider ageSlider, playerSlider;
    @FXML
    private Label ageValue, playerValue, usernameError, ageError, connectionError, userMessage;
    @FXML
    private CheckBox firstGame;
    @FXML
    private Button startButton, setLanguage;
    @FXML
    private ComboBox<String> language;

    @FXML
    private Label l;

    @FXML
    private Tab settings;

    public LoginGuiController() {
        this.client = MyShelfie.client;
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
        if (username.getText().isEmpty()) {
            usernameError.setText("Username cannot be empty");
            return;
        }
        client.gameView.loadLanguage();
        client.sendMessage(new Message("completeLogin", username.getText(), 0, firstGame.isSelected(), 0));
        client.startPingThread(username.getText());
    }

    @FXML
    public void usernameAlreadyTaken() {
        usernameError.setText("Username already taken (" + username.getText() + ", retry");
    }

    @FXML
    public void changeLanguage(ActionEvent event) {
        if (language.getValue().equals("English")) {
            client.setLanguage("en", "US");
        } else {
            client.setLanguage("it", "IT");
        }
    }

    @FXML
    void loadSettings() {
        language.getItems().addAll("English", "Italian");
        //l.setText("player");
    }
}