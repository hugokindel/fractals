package com.ustudents.fgen.gui;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.gui.controller.MainWindowController;
import com.ustudents.fgen.gui.views.MainWindow;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Application extends javafx.application.Application {
    private static Application instance = null;

    private Stage mainStage = null;

    @Override
    public void start(Stage stage) {
        instance = this;
        mainStage = stage;

        stage.getIcons().add(new Image(Objects.requireNonNull(FGen.class.getResourceAsStream("/icon.png"))));
        stage.setTitle("FGen");
        stage.setMinHeight(576);
        stage.setMinWidth(768);

        MainWindowController controller = new MainWindowController();
        stage.setScene(controller.view);

        stage.show();
    }

    public static void mainApplication(String... args) {
        launch(args);
    }

    public static Application get() {
        return instance;
    }

    public Stage getMainStage() {
        return mainStage;
    }
}
