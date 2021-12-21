package com.ustudents.fgen.gui;

import com.ustudents.fgen.FGen;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) {
        stage.getIcons().add(new Image(Objects.requireNonNull(FGen.class.getResourceAsStream("/icon.png"))));
        stage.setTitle("FGen");
        stage.setScene(new MainWindow(1024, 720));
        stage.setMinHeight(280);
        stage.setMinWidth(640);
        stage.show();
    }

    public static void mainApplication(String... args) {
        launch(args);
    }
}
