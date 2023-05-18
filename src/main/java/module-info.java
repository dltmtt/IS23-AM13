module it.polimi.ingsw.client.view {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires json.simple;
    requires java.rmi;
    requires org.jetbrains.annotations;
    requires commons.cli;

    opens it.polimi.ingsw.client.view to javafx.fxml;
    exports it.polimi.ingsw.client.view;

    exports it.polimi.ingsw.server to java.rmi;
}