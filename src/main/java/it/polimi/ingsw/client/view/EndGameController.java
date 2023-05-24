package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

public class EndGameController {
    private final List<Client> clients;
  //  private final GuiView view;
    private Label username1;
    private Label username2;
    private Label username3;
    private Label username4;
    private Label score1;
    private Label score2;
    private Label score3;
    private Label score4;

    public EndGameController(List<Client> clients, GuiView view) {
        this.clients = clients;
       // this.view = view;
    }

    //it changes the value of the labels with the usernames of the players
    public void onSliderChanged(){
        if(clients.size()==2) {
            username1.setText(clients.get(0).getUsername());
            username2.setText(clients.get(1).getUsername());
            username2.setText("");
            username3.setText("");
        }
        if(clients.size()==3) {
            username1.setText(clients.get(0).getUsername());
            username2.setText(clients.get(1).getUsername());
            username3.setText(clients.get(2).getUsername());
            username3.setText("");
        }
        if(clients.size()==4){
            username1.setText(clients.get(0).getUsername());
            username2.setText(clients.get(1).getUsername());
            username3.setText(clients.get(2).getUsername());
            username4.setText(clients.get(3).getUsername());
        }
        //this should never occur because players are at least 2
        else{
            username1.setText("");
            username2.setText("");
            username3.setText("");
            username4.setText("");
        }
    }

    public void onStartButtonClicked1(){
        if(username1.getText().isEmpty() && clients.size()>0){  //I have no client
            score1.setTextFill(javafx.scene.paint.Color.RED);
            score1.setText("Score1 cannot be empty");
            }
        //this should never occur because players are at least 2
        /*
        else if(username2.getText().isEmpty()){
            score2.setText("");
        }
         */
        else if(username1.getText().isEmpty()){
            score1.setText("");
        }
        else{
            //button to show the score of the player
           // score1.setText(String.valueOf((int) clients.get(0).calculateScore()));
        }
    }
    public void onStartButtonClicked2(){
        if(username2.getText().isEmpty() && clients.size()==2){
            score2.setTextFill(javafx.scene.paint.Color.RED);
            score2.setText("Score2 cannot be empty");
        }
        //this should never occur because players are at least 2
        /*
        else if(username2.getText().isEmpty()){
            score2.setText("");
        }
         */
        else{
            //button to show the score of the player
            // score2.setText(String.valueOf((int) clients.get(1).calculateScore()));
        }

    }
    public void onStartButtonClicked3(){
        if(username3.getText().isEmpty() && clients.size()==3){
            score3.setTextFill(javafx.scene.paint.Color.RED);
            score3.setText("Score3 cannot be empty");
        }
        else if(username3.getText().isEmpty()){
            score3.setText("");
        }
        else{
            //button to show the score of the player
            // score3.setText(String.valueOf((int) clients.get(2).calculateScore()));
        }
    }

    public void onStartButtonClicked4(){
        if(username4.getText().isEmpty() && clients.size()==4){
            score3.setTextFill(javafx.scene.paint.Color.RED);
            score3.setText("Score4 cannot be empty");
        }
        else if(username4.getText().isEmpty()){
            score2.setText("");
        }
        else{
            //button to show the score of the player
            // score4.setText(String.valueOf((int) clients.get(3).calculateScore()));
        }
    }
}
