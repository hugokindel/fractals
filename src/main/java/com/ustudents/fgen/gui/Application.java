package com.ustudents.fgen.gui;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.gui.controller.MainWindowController;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class Application extends javafx.application.Application {
    private final Image WINDOW_ICON = new Image(Objects.requireNonNull(FGen.class.getResourceAsStream("/icon.png")));

    private static Application instance = null;

    private Stage currentStage = null;

    @Override
    public void start(Stage stage) {
        instance = this;
        currentStage = stage;

        stage.getIcons().add(WINDOW_ICON);
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

    public Stage getCurrentStage() {
        return currentStage;
    }

    public void showPopup(Scene scene) {
        final Stage dialog = new Stage();

        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(Application.get().getCurrentStage());
        dialog.getIcons().add(WINDOW_ICON);
        dialog.setTitle("About FGen");
        dialog.setResizable(false);
        dialog.setScene(scene);

        dialog.show();
    }

    public void close() {
        getCurrentStage().close();
    }
}
