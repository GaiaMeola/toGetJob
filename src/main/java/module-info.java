module org.example.togetjob {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires annotations;
    requires mysql.connector.j;
    requires java.desktop;
    requires java.naming;


    opens org.example.togetjob to javafx.fxml;
    opens org.example.togetjob.view.gui.controllergrafico to javafx.fxml;
    opens org.example.togetjob.state to javafx.fxml;

    exports org.example.togetjob;
    exports org.example.togetjob.bean;
    exports org.example.togetjob.exceptions;
    exports org.example.togetjob.state;
    exports org.example.togetjob.view.gui.controllergrafico;
}