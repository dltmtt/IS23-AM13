// Configuration file for GUI

module it.polimi.ingsw.client.view {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires json.simple;
    requires java.rmi;
    requires org.jetbrains.annotations;
    requires commons.cli;
    requires com.google.gson;

    opens it.polimi.ingsw.client.view to javafx.fxml, javafx.graphics;
    exports it.polimi.ingsw.client to java.rmi, javafx.fxml;

    // exports it.polimi.ingsw.client.view to it.polimi.ingsw.client;

    exports it.polimi.ingsw.server to java.rmi;
    exports it.polimi.ingsw.server.model to javafx.fxml;
}