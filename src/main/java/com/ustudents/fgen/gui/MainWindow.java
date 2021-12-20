package com.ustudents.fgen.gui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

public class MainWindow extends Window {
    public MainWindow(double width, double height) {
        super(width, height);
    }

    @Override
    public void initialize() {
        GridPane root = new GridPane();

        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setMinWidth(250);
        root.getColumnConstraints().add(columnConstraints1);

        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setMinWidth(250);
        root.getColumnConstraints().add(columnConstraints2);

        ColumnConstraints columnConstraints3 = new ColumnConstraints();
        columnConstraints3.setPercentWidth(100);
        root.getColumnConstraints().add(columnConstraints3);

        {
            HBox leftBox = new HBox();
            leftBox.setStyle("-fx-background-color: red;");
            root.add(leftBox, 0, 0);

            HBox middleBox = new HBox();
            middleBox.setStyle("-fx-background-color: green;");
            root.add(middleBox, 1, 0);

            HBox rightBox = new HBox();
            GridPane.setVgrow(rightBox, Priority.ALWAYS);
            GridPane.setHgrow(rightBox, Priority.ALWAYS);
            rightBox.setStyle("-fx-background-color: blue;");
            root.add(rightBox, 2, 0);

            HBox statusBar = new HBox();
            statusBar.setMinHeight(20);
            statusBar.setStyle("-fx-background-color: black;");
            root.add(statusBar, 0, 1, 3, 1);
        }

        setRoot(root);
    }
}
