package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public EndGameGuiController(GuiView guiView, Client client) {
        view = guiView;
        this.client = client;
    }

    @FXML
    void homeScreen(ActionEvent event) {
        view.loginProcedure();
    }

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

        //order scores by value


        List<String> players = new ArrayList<>(scores.keySet());


        for (int i = 0; i < players.size(); i++) {
            String playername = players.get(i);
            switch (i) {
                case 0 -> {
                    player0.setText(playername);
                    points0.setText(scores.get(playername).toString());
                }
                case 1 -> {
                    player1.setText(playername);
                    points1.setText(scores.get(playername).toString());
                }
                case 2 -> {
                    player2.setText(playername);
                    points2.setText(scores.get(playername).toString());
                }
                case 3 -> {
                    player3.setText(playername);
                    points3.setText(scores.get(playername).toString());
                }
            }

        }
    }
}
