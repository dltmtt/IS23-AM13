package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.MyShelfie;
import it.polimi.ingsw.commons.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class LoginGuiController {

    private Client client;
    @FXML
    private TextField username, serverIp;
    @FXML
    private Slider ageSlider, playerSlider;
    @FXML
    private Label ageValue, playerValue, usernameError, ageError, connectionStatus, userMessage;
    @FXML
    private CheckBox firstGame;
    @FXML
    private Button startButton, setLanguage, connect;
    @FXML
    private ComboBox<String> language, connectionType;

    @FXML
    private Label l;

    @FXML
    private Tab settings;

    @FXML
    private StackPane waiting;

    @FXML
    private GridPane login;

    @FXML
    private ProgressIndicator waitingConnection;

    public LoginGuiController() {
        this.client = MyShelfie.client;
        //login.setVisible(false);
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
        }else {
            showWaiting();
            client.gameView.loadLanguage();
            Thread sendLoginMessageThread = new Thread(this::sendLoginMessage);
            sendLoginMessageThread.start();
        }
    }

    public void sendLoginMessage(){
        client.sendMessage(new Message("completeLogin", username.getText(), 0, firstGame.isSelected(), 0));
        client.startPingThread(username.getText());
    }

    /**
     * This method warns that the username has already been taken
     */
    @FXML
    public void usernameAlreadyTaken() {
        usernameError.setText("Username already taken (" + username.getText() + "), retry");
        waiting.setVisible(false);
        startButton.setDisable(false);
        username.setDisable(false);
        firstGame.setDisable(false);
    }

    /**
     * This method makes it possible to change the language of the game
     * @param event mouse event
     */
    @FXML
    public void changeLanguage(ActionEvent event) {
        String lang = language.getValue();

        switch (lang) {
            case "English" -> client.setLanguage("en", "US");
            case "Italian" -> client.setLanguage("it", "IT");
            case "Sicilian" -> client.setLanguage("si", "IT");
            case "Pugliese" -> client.setLanguage("pu", "IT");
            case "Bergamasco" -> client.setLanguage("bg", "IT");
            case "French" -> client.setLanguage("fr", "FR");
            case "Spanish" -> client.setLanguage("es", "ES");
            case "Japanese" -> client.setLanguage("ja", "JP");
            case "Catalan" -> client.setLanguage("ca", "ES");
        }

    }

    @FXML
    void loadSettings() {
        language.getItems().addAll("English", "Italian", "Sicilian", "Bergamasco", "Pugliese", "French", "Spanish", "Japanese", "Catalan");
    }

    public void showWaiting() {
        waiting.setVisible(true);
        startButton.setDisable(true);
        username.setDisable(true);
        firstGame.setDisable(true);
    }

    public void hideWaiting() {
        waiting.setVisible(false);
        startButton.setDisable(false);
        username.setDisable(false);
        firstGame.setDisable(false);
    }

    public void connectToServer() {
        if (serverIp.getText().isEmpty() || connectionType.getValue() == null) {
            connectionStatus.setText("Server IP or connection Type cannot be empty");
        }else {
            if(!MyShelfie.isIpValid(serverIp.getText())){
                connectionStatus.setText("Server IP is not valid");
            }else{
                connectionType.setDisable(true);
                connect.setDisable(true);
                serverIp.setDisable(true);
                Thread connectToServerThread = new Thread(()->{
                        MyShelfie.setParameters(serverIp.getText(), connectionType.getValue().toLowerCase(), GuiView.gui);});
                connectToServerThread.start();
            }
        }
    }


    public void setSettings() {
        connectionType.getItems().addAll("TCP", "RMI");
        connectionType.setValue("TCP");
        settings.setDisable(true);
    }

    public void initiateConnection() {
        waitingConnection.setVisible(true);
    }

    public void connectionSuccess() {
        waitingConnection.setVisible(false);
        login.setVisible(true);
        settings.setDisable(false);
        connect.setDisable(true);
        serverIp.setDisable(true);
        connectionType.setDisable(true);
        connectionStatus.setTextFill(javafx.scene.paint.Color.GREEN);
        connectionStatus.setText("Connected");
        this.client=MyShelfie.client;
    }

    public void connectionError() {
        waitingConnection.setVisible(false);
        connectionStatus.setTextFill(javafx.scene.paint.Color.RED);
        connectionStatus.setText("Connection error, is the server running ("+serverIp.getText()+")?");
        connect.setDisable(false);
        serverIp.setDisable(false);
        connectionType.setDisable(false);
    }
}