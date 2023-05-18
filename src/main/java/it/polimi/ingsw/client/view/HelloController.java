package it.polimi.ingsw.client.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class HelloController {

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
            usernameError.setTextFill(javafx.scene.paint.Color.RED);
            usernameError.setText("Username cannot be empty");
        } else {
            usernameError.setText("");
            userMessage.setText("username: " + username.getText() + " age: " + ageSlider.getValue() + " players: " + playerSlider.getValue() + " first game: " + firstGame.isSelected());
        }
    }
}