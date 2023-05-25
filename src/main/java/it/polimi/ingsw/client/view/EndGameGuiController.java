package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class EndGameGuiController {
    private final List<Client> clients;
  //  private final GuiView view;
    @FXML
    private Label username1;
    @FXML
    private Label username2;
    @FXML
    private Label username3;
    @FXML
    private Label username4;
    @FXML
    private Label score1;
    @FXML
    private Label score2;
    @FXML
    private Label score3;
    @FXML
    private Label score4;

    public EndGameGuiController(List<Client> clients, GuiView view) {
        this.clients = clients;
       // this.view = view;
    }

    //it changes the value of the labels with the usernames of the players
    public void onSliderChanged1() {
        if (clients.size() > 0) {
            username1.setText(clients.get(0).getUsername());
        } else {
            //this should never occur because players are at least 2
            username1.setText("");
        }
    }
    public void onSliderChanged2(){
        if(clients.size()>1) {
            username2.setText(clients.get(1).getUsername());
        }
        else{
            //this should never occur because players are at least 2
            username2.setText("");
        }
    }

    public void onSliderChanged3() {
        if(clients.size()==3) {
            username3.setText(clients.get(2).getUsername());
        }
        else{
            username3.setText("");
        }
    }
    public void onSliderChanged4() {
        if(clients.size()==4) {
            username4.setText(clients.get(3).getUsername());
        }
        else{
            username4.setText("");
        }
    }

    public void onScoreChanged1(){
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
        //this should never occur because players are at least 2
        else if(username1.getText().isEmpty()){
            score1.setText("");
        }
        else{
         //   score1.setText(String.valueOf((int) clients.get(0).calculateScore()));
        }
    }
    public void onScoreChanged2(){
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
            //   score1.setText(String.valueOf((int) clients.get(0).calculateScore()));
        }

    }
    public void onScoreChanged3(){
        if(username3.getText().isEmpty() && clients.size()==3){
            score3.setTextFill(javafx.scene.paint.Color.RED);
            score3.setText("Score3 cannot be empty");
        }
        else if(username3.getText().isEmpty()){
            score3.setText("");
        }
        else{
            //score3.setText(String.valueOf((int) clients.get(0).calculateScore()));
        }
    }

    public void onScoreChanged4(){
        if(username4.getText().isEmpty() && clients.size()==4){
            score3.setTextFill(javafx.scene.paint.Color.RED);
            score3.setText("Score4 cannot be empty");
        }
        else if(username4.getText().isEmpty()){
            score2.setText("");
        }
        else{
            //score4.setText(String.valueOf((int) clients.get(0).calculateScore()));
        }
    }
}
