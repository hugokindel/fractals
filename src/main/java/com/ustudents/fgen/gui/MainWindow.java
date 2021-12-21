package com.ustudents.fgen.gui;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.format.Configuration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class MainWindow extends Window {
    public MainWindow(double width, double height) {
        super(width, height);
    }

    @Override
    public void initialize() {
        BorderPane root = new BorderPane();

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem quitItem = new MenuItem("Quit");
        quitItem.setOnAction(event -> Application.get().getMainStage().close());
        fileMenu.getItems().add(quitItem);
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About FGen");
        aboutItem.setOnAction(event -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(Application.get().getMainStage());
            dialog.getIcons().add(new Image(Objects.requireNonNull(FGen.class.getResourceAsStream("/icon.png"))));
            dialog.setTitle("About FGen");
            dialog.setResizable(false);
            dialog.setScene(new AboutWindow(400, 300));
            dialog.show();
        });
        helpMenu.getItems().add(aboutItem);
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
            ListView<String> generatorsList = new ListView<>();
            generatorPlusButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    generatorsList.getItems().add("Default");
                }
            });

            for (int i = 0; i < FGen.get().loadedConfiguration.generators.size(); i++) {
                generatorsList.getItems().add(FGen.get().loadedConfiguration.generators.get(i).name);
            }

            VBox.setVgrow(generatorsList, Priority.ALWAYS);
            generatorBox.getChildren().add(generatorsList);
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
