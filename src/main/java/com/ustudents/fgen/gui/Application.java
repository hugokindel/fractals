package com.ustudents.fgen.gui;

import com.ustudents.fgen.FGen;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.getIcons().add(new Image(Objects.requireNonNull(FGen.class.getResourceAsStream("/icon.png"))));
        stage.setTitle("FGen");
        stage.setScene(scene);
        stage.show();
    }

    public static void mainApplication(String... args) {
        launch(args);
    }
}
