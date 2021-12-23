package com.ustudents.fgen.gui.views;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.generators.Generator;
import com.ustudents.fgen.gui.Application;
import com.ustudents.fgen.gui.Window;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;

import java.util.Objects;

public class MainWindow extends Window {
    public BorderPane root = new BorderPane();
    public MenuBar menuBar = new MenuBar();
    Menu fileMenu = new Menu("File");
    MenuItem quitItem = new MenuItem("Quit");
    Menu helpMenu = new Menu("Help");
    MenuItem aboutItem = new MenuItem("About FGen");
    GridPane contentGrid = new GridPane();
    TabPane generatorTabPane = new TabPane();
    Tab generatorTab = new Tab("Generators");
    VBox generatorBox = new VBox();
    ToolBar generatorToolbar = new ToolBar();
    Button generatorPlusButton = new Button("Add Generator");
    ListView<Generator> generatorsList = new ListView<>();
    TabPane parametersTabPane = new TabPane();
    Tab parametersTab = new Tab("Generator Properties");
    TabPane previewTabPane = new TabPane();
    Tab previewTab = new Tab("Fractal Preview");
    StackPane fractalPreviewPane = new StackPane();
    Image fractalPreviewImage = new Image(Objects.requireNonNull(FGen.class.getResourceAsStream("/icon.png")));
    ImageView fractalPreviewImageView = new ImageView(fractalPreviewImage);
    ToolBar toolbar = new ToolBar();
    Label statusLabel = new Label("Ready.");

    public MainWindow(double width, double height) {
        super(width, height);

        createMenuBar();
        createMainGrid();
        createToolbar();

        setRoot(root);
    }

    public void createMenuBar() {
        quitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        quitItem.setOnAction(event -> Application.get().getMainStage().close());
        fileMenu.getItems().add(quitItem);
        helpMenu.getItems().add(aboutItem);
        menuBar.getMenus().addAll(fileMenu, helpMenu);
        root.setTop(menuBar);
    }

    public void createMainGrid() {
        for (int i = 0; i < 2; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(20);
            contentGrid.getColumnConstraints().add(columnConstraints);
        }

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(60);
        contentGrid.getColumnConstraints().add(columnConstraints);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100);
        contentGrid.getRowConstraints().add(rowConstraints);

        createGeneratorsTab();
        createGeneratorPropertiesTab();
        createFractalPreviewTab();

        root.setCenter(contentGrid);
    }

    public void createGeneratorsTab() {
        generatorTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        generatorToolbar.getItems().add(generatorPlusButton);
        generatorBox.getChildren().add(generatorToolbar);
        VBox.setVgrow(generatorsList, Priority.ALWAYS);
        generatorBox.getChildren().add(generatorsList);
        generatorTab.setContent(generatorBox);
        generatorTabPane.getTabs().add(generatorTab);
        contentGrid.add(generatorTabPane, 0, 0);
    }

    public void createGeneratorPropertiesTab() {
        parametersTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        parametersTabPane.getTabs().add(parametersTab);
        contentGrid.add(parametersTabPane, 1, 0);
    }

    public void createFractalPreviewTab() {
        previewTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        fractalPreviewImageView.fitWidthProperty().bind(fractalPreviewPane.widthProperty());
        fractalPreviewImageView.fitHeightProperty().bind(fractalPreviewPane.heightProperty());
        fractalPreviewPane.getChildren().add(fractalPreviewImageView);
        previewTab.setContent(fractalPreviewPane);
        previewTabPane.getTabs().add(previewTab);
        contentGrid.add(previewTabPane, 2, 0);
    }

    public void createToolbar() {
        toolbar.getItems().add(statusLabel);
        root.setBottom(toolbar);
    }
}
