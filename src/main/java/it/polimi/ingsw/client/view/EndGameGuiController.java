package it.polimi.ingsw.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EndGameGuiController {

    private final GuiView view;
    private final String username;
    @FXML
    private Button homeButton;

    @FXML
    private Label player1;

    @FXML
    private Label player2;

    @FXML
    private Label player3;

    @FXML
    private Label player4;

    @FXML
    private Label points1;

    @FXML
    private Label points2;

    @FXML
    private Label points3;

    @FXML
    private Label points4;

    @FXML
    private Label winner;

    @FXML
    private Label winnerPhrase;

    public EndGameGuiController(GuiView guiView, String username) {
        view = guiView;
        this.username = username;
    }

    @FXML
    void homeScreen(ActionEvent event) {
        view.loginProcedure();
    }

    public void setWinner(String winner) {
        if (winner.equals(username))
            this.winnerPhrase.setText("You");
        else
            this.winner.setText(winner);
    }
}
