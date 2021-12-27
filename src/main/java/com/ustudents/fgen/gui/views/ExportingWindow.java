package com.ustudents.fgen.gui.views;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.gui.Window;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.Objects;

public class ExportingWindow extends Window {
    VBox root = new VBox();
    public Button cancelButton = new Button("Cancel");

    public ExportingWindow(double width, double height) {
        super(width, height);

        createRoot();

        setRoot(root);
    }

    public void createRoot() {
        root.setStyle("-fx-border-width: 0 0 1 0; -fx-border-color: #C8C8C8;");
        root.setPadding(new Insets(8, 10, 10, 10));
        Label title = new Label("Exporting...");
        title.setPadding(new Insets(0, 0, 16, 0));
        title.setStyle("-fx-font-weight: bold");
        title.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(title);
        root.setAlignment(Pos.CENTER);

        root.getChildren().add(cancelButton);
    }
}
