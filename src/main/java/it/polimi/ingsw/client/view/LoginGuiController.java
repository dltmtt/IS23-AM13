package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
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
        //     if (username.getText().isEmpty()) {
        //         usernameError.setTextFill(javafx.scene.paint.Color.RED);
        //         usernameError.setText("Username cannot be empty");
        //     } else {
        //         usernameError.setText("");
        //         String finalUsername = username.getText();
        //         // client.startPingThread(finalUsername);
        //
        //         // Message response = client.sendMessage(new Message("username", username.getText(), (int) ageSlider.getValue(), firstGame.isSelected(), (int) playerSlider.getValue()));
        //         String responseMessage = response.getCategory();
        //         if ("retry".equals(responseMessage)) {
        //             // This happens when the username is already taken and the player needs to choose another one
        //             usernameError.setText("Username already taken. Retry.");
        //         } else {
        //             int myPosition;
        //             if ("index".equals(responseMessage)) {
        //                 // This happens when the game is already started and the player is reconnecting
        //                 myPosition = response.getPosition();
        //                 try {
        //                     client.startGame();
        //                 } catch (FullRoomException | IOException | ParseException | IllegalAccessException e) {
        //                     throw new RuntimeException(e);
        //                 }
        //                 return;
        //             }
        //             int age = (int) ageSlider.getValue();
        //             String ageResponse = client.sendMessage(new Message("age", "", age, false, 0)).getCategory();
        //             if (!ageResponse.startsWith("ok")) {
        //                 ageError.setText("Remember that you need to be supervised by an adult to play this game.");
        //             }
        //
        //             int nextStep = client.sendMessage(new Message("completeLogin", username.getText(), (int) ageSlider.getValue(), firstGame.isSelected(), 0)).getPosition();
        //
        //             // TODO: figure out why next step starts from 1 and not from 0, as it should be. Otherwise it throws an exception when trying to read the personalGoalCard, cause it's out of bounds, trying to read the 2nd index of the array, which has 2 elements which indexes are 0 and 1
        //             // TODO: this is a temporary fix, but it's not the best solution
        //             myPosition = nextStep - 1;
        //
        //             // TODO: the following instructions should not be here, as this is a VIEW, not a CONTROLLER
        //             client.setMyPosition(myPosition);
        //
        //             // System.out.println("Your position is " + myPosition);
        //             // userMessage.setText("username: " + username.getText() + " age: " + ageSlider.getValue() + " players: " + playerSlider.getValue() + " first game: " + firstGame.isSelected());
        //
        //             // Successful Login
        //             view.waitingRoom();
        //         }
        //     }
        // }
    }
}