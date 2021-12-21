package com.ustudents.fgen.gui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MainWindow extends Window {
    public MainWindow(double width, double height) {
        super(width, height);
    }

    @Override
    public void initialize() {
        BorderPane root = new BorderPane();

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu helpMenu = new Menu("Help");
        menuBar.getMenus().addAll(fileMenu, helpMenu);
        root.setTop(menuBar);

        GridPane gridPane = new GridPane();

        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setMinWidth(250);
        gridPane.getColumnConstraints().add(columnConstraints1);

        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setMinWidth(250);
        gridPane.getColumnConstraints().add(columnConstraints2);

        ColumnConstraints columnConstraints3 = new ColumnConstraints();
        columnConstraints3.setPercentWidth(100);
        gridPane.getColumnConstraints().add(columnConstraints3);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100);
        gridPane.getRowConstraints().add(rowConstraints);

        {
            TabPane generatorTabPane = new TabPane();
            generatorTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            Tab generatorTab = new Tab("Generators");
            VBox generatorBox = new VBox();
            ToolBar generatorToolbar = new ToolBar();
            Button generatorPlusButton = new Button("Add Generator");
            generatorToolbar.getItems().add(generatorPlusButton);
            generatorBox.getChildren().add(generatorToolbar);
            TreeView<String> generatorTree = new TreeView<>();
            VBox.setVgrow(generatorTree, Priority.ALWAYS);
            generatorBox.getChildren().add(generatorTree);
            generatorTab.setContent(generatorBox);
            generatorTabPane.getTabs().add(generatorTab);
            gridPane.add(generatorTabPane, 0, 0);

            TabPane parametersTabPane = new TabPane();
            parametersTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            Tab parametersTab = new Tab("Generator parameters");
            parametersTabPane.getTabs().add(parametersTab);
            gridPane.add(parametersTabPane, 1, 0);

            TabPane previewTabPane = new TabPane();
            previewTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            Tab previewTab = new Tab("Fractal preview");
            previewTabPane.getTabs().add(previewTab);
            gridPane.add(previewTabPane, 2, 0);
        }

        root.setCenter(gridPane);

        ToolBar toolbar = new ToolBar();
        Label statusLabel = new Label("Ready.");
        toolbar.getItems().add(statusLabel);
        root.setBottom(toolbar);

        setRoot(root);
    }
}
