package it.polimi.ingsw.client.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
       /*
        Parent demo=FXMLLoader.load(getClass().getResource("demo.fxml"));
       // scene.getStylesheets().add(String.valueOf(this.getClass().getResource("sample.css")));
        stage.setTitle("prova");
        stage.setScene(new Scene(demo));

        stage.show();

        */
        HelloController controller = new HelloController();
        URL url = this.getClass().getResource("demo.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(controller);
        // loader.setRoot(controller);
        loader.load();
        stage.setTitle("prova");
        stage.setScene(new Scene(loader.getRoot()));
        stage.show();
    }
}