package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.MyShelfie;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is the controller for the end game gui.
 * It shows the winner and the scores of all the players.
 * It also shows a different message if the player is the winner.
 */

public class EndGameGuiController {

    private final GuiView view;
    private final Client client;
    @FXML
    private Button homeButton;

    @FXML
    private Label player1;

    @FXML
    private Label player2;

    @FXML
    private Label player3;

    @FXML
    private Label player0;

    @FXML
    private Label points1;

    @FXML
    private Label points2;

    @FXML
    private Label points3;

    @FXML
    private Label points0;

    @FXML
    private Label winner;

    @FXML
    private Label winnerPhrase;

    @FXML
    private ImageView background, header;

    private Stage stage;

    /**
     * Constructor for the class
     *
     * @param guiView the gui view
     * @param client  the client
     */
    public EndGameGuiController(GuiView guiView, Client client) {
        view = guiView;
        this.client = client;
        stage= GuiView.stage;
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
                setBackgroundSize(stage.getWidth(), stage.getHeight());
        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);
        stage.setResizable(true);
    }

    private void setBackgroundSize(double width, double height) {
        background.setPreserveRatio(false);
        background.setFitWidth(width);
        background.setFitHeight(height);
        header.setFitWidth(width);
        header.setPreserveRatio(true);
    }

    /**
     * Called when the home button is pressed, changing the scene to the login one
     *
     * @param event the event of the button being pressed
     */
    @FXML
    void homeScreen(ActionEvent event) {
        view.loginProcedure();
    }

    /**
     * Called when the scene is loaded, it sets the winner and the scores
     *
     * @param winners the hash map associating the winner of the game to their score
     * @param losers  the hash map associating the losers of the game to their scores
     */
    public void setWinner(HashMap<String, Integer> winners, HashMap<String, Integer> losers) {
        player0.setText("");
        points0.setText("");
        player1.setText("");
        points1.setText("");
        player2.setText("");
        points2.setText("");
        player3.setText("");
        points3.setText("");
        this.winner.setText("");

        for (String s : winners.keySet()) {
            if (s.equals(client.getUsername())) {
                this.winner.setText(this.winner.getText() + view.bundle.getString("self"));
                this.winnerPhrase.setText(view.bundle.getString("selfWinPhrase"));
            } else {
                this.winner.setText(this.winner.getText() + s);
                this.winnerPhrase.setText(view.bundle.getString("winPhrase"));
            }
        }

        HashMap<String, Integer> scores = new HashMap<>(winners);
        scores.putAll(losers);
        List<String> players = new ArrayList<>(scores.keySet());


        for (int i = 0; i < players.size(); i++) {
            String playerName = players.get(i);
            switch (i) {
                case 0 -> {
                    player0.setText(playerName);
                    points0.setText(scores.get(playerName).toString());
                }
                case 1 -> {
                    player1.setText(playerName);
                    points1.setText(scores.get(playerName).toString());
                }
                case 2 -> {
                    player2.setText(playerName);
                    points2.setText(scores.get(playerName).toString());
                }
                case 3 -> {
                    player3.setText(playerName);
                    points3.setText(scores.get(playerName).toString());
                }
            }

        }
    }

    /**
     * Called when the player is alone in the game, it sets the winner (the champion) and the scores (null)
     */
    public void endAlone() {
        player0.setText("");
        points0.setText("");
        player1.setText("");
        points1.setText("");
        player2.setText("");
        points2.setText("");
        player3.setText("");
        points3.setText("");
        this.winner.setText("");
        this.winnerPhrase.setText(view.bundle.getString("alone"));
    }
}
