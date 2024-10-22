package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.MyShelfie;
import it.polimi.ingsw.commons.Message;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * This is the controller for the login gui.
 * It contains the methods to change the language.
 * It also contains the method to warn the player if the username is already taken.
 */
public class LoginGuiController {

    private Client client;
    @FXML
    private TextField username, serverIp;
    @FXML
    private Label usernameError, connectionStatus, userMessage, insertUsername, selectedLang, confirmLanguage;
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

    @FXML
    private ImageView background, header;

    private Stage stage;

    /**
     * This is the constructor for the class.
     */
    public LoginGuiController() {
        this.client = MyShelfie.client;
        this.stage = GuiView.stage;
        this.stage.setResizable(false);

        stage= GuiView.stage;
    }

    /**
     * Called when the start button is clicked, sending the login message to the server.
     *
     * @see LoginGuiController#sendLoginMessage() for the actual sending of the message.
     */
    @FXML
    public void onStartButtonClicked() {
        if (username.getText().isEmpty()) {
            usernameError.setText("Username cannot be empty");
        } else {
            showWaiting();
            this.client = MyShelfie.client;
            client.gameView.loadLanguage();
            Thread sendLoginMessageThread = new Thread(this::sendLoginMessage);
            sendLoginMessageThread.start();
        }
    }

    /**
     * Sends the <code>completeLogin</code> message to the server.
     *
     * @see Message#Message(String, String, int, boolean, int) for the message constructor.
     */
    public void sendLoginMessage() {
        client.sendMessage(new Message("completeLogin", username.getText(), 0, firstGame.isSelected(), 0));
    }

    /**
     * Warns that the username has already been taken.
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
     * Makes it possible to change the language of the game.
     *
     * @param event mouse event
     */
    @FXML
    public void changeLanguage(ActionEvent event) {
        String lang = language.getValue();
        this.client = MyShelfie.client;
        switch (lang) {
            case "English" -> client.setLanguage("en", "US");
            case "Italian" -> client.setLanguage("it", "IT");
            case "Sicilian" -> client.setLanguage("si", "IT");
            case "Bergamasco" -> client.setLanguage("bg", "IT");
            case "French" -> client.setLanguage("fr", "FR");
            case "Spanish" -> client.setLanguage("es", "ES");
            case "Japanese" -> client.setLanguage("ja", "JP");
            case "Catalan" -> client.setLanguage("ca", "ES");
        }
        selectedLang.setText(language.getValue());
        selectedLang.setVisible(true);
        confirmLanguage.setVisible(true);
        changeDisplayedLanguageOnLogin();

    }

    private void changeDisplayedLanguageOnLogin() {
        ResourceBundle language = client.bundle;
        insertUsername.setText(language.getString("username"));
        startButton.setText(language.getString("startGame"));
        firstGame.setText(language.getString("firstPlay"));
    }

    /**
     * Loads the settings in the Settings tab.
     */
    @FXML
    public void loadSettings() {
        if(language.getItems().isEmpty()) {
            language.getItems().addAll("English", "Italian", "Sicilian", "Bergamasco", "French", "Spanish", "Japanese", "Catalan");
            language.setValue("English");
        }

    }

    /**
     * Shows the waiting element, to entertain the user while waiting for a response from the server.
     */
    public void showWaiting() {
        waiting.setVisible(true);
        startButton.setDisable(true);
        username.setDisable(true);
        firstGame.setDisable(true);
    }

    /**
     * Hides the waiting element, to show the user that the server has responded.
     */
    public void hideWaiting() {
        waiting.setVisible(false);
        startButton.setDisable(false);
        username.setDisable(false);
        firstGame.setDisable(false);
    }

    /**
     * Called when the connect button is clicked, to connect to the server.
     */
    public void connectToServer() {
        if (serverIp.getText().isEmpty() || connectionType.getValue() == null) {
            connectionStatus.setTextFill(javafx.scene.paint.Color.RED);
            connectionStatus.setText("Server IP or connection Type cannot be empty");
        } else {
            if (!MyShelfie.isIpValid(serverIp.getText())) {
                connectionStatus.setTextFill(javafx.scene.paint.Color.RED);
                connectionStatus.setText("Server IP is not valid");
            } else {
                connectionType.setDisable(true);
                connect.setDisable(true);
                serverIp.setDisable(true);
                Thread connectToServerThread = new Thread(() -> {
                    MyShelfie.setParameters(serverIp.getText(), connectionType.getValue().toLowerCase(), GuiView.gui);
                });
                connectToServerThread.start();
            }
        }
    }

    /**
     * Sets the possible connection settings in the connect area.
     */
    public void setSettings() {
        connectionType.getItems().addAll("TCP", "RMI");
        connectionType.setValue("TCP");
        settings.setDisable(true);
    }

    /**
     * Called when the connection is initiated, showing the loading graphic.
     */
    public void initiateConnection() {
        waitingConnection.setVisible(true);
    }

    /**
     * Called when the connection is successful, showing the login area (where the username can be inserted).
     */
    public void connectionSuccess() {
        waitingConnection.setVisible(false);
        login.setVisible(true);
        settings.setDisable(false);
        connect.setDisable(true);
        serverIp.setDisable(true);
        connectionType.setDisable(true);
        connectionStatus.setTextFill(javafx.scene.paint.Color.GREEN);
        connectionStatus.setText("Connected");
        this.client = MyShelfie.client;
    }

    /**
     * Called when the connection is unsuccessful, showing the error message.
     */
    public void connectionError() {
        waitingConnection.setVisible(false);
        connectionStatus.setTextFill(javafx.scene.paint.Color.RED);
        connectionStatus.setText("Connection error, is the server running (" + serverIp.getText() + ")?");
        connect.setDisable(false);
        serverIp.setDisable(false);
        connectionType.setDisable(false);
    }

}
