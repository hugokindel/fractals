package com.ustudents.fgen.gui.views;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.generators.Generator;
import com.ustudents.fgen.gui.Application;
import com.ustudents.fgen.gui.Window;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.util.Objects;

public class MainWindow extends Window {
    public BorderPane root = new BorderPane();
    public MenuBar menuBar = new MenuBar();
    public Menu fileMenu = new Menu("File");
    public MenuItem quitItem = new MenuItem("Quit");
    public Menu helpMenu = new Menu("Help");
    public MenuItem aboutItem = new MenuItem("About FGen");
    public GridPane contentGrid = new GridPane();
    public TabPane generatorTabPane = new TabPane();
    public Tab generatorTab = new Tab("Generators");
    public VBox generatorBox = new VBox();
    public ToolBar generatorToolbar = new ToolBar();
    public Button generatorPlusButton = new Button("Add Generator");
    public ListView<Generator> generatorsList = new ListView<>();
    public TabPane parametersTabPane = new TabPane();
    public Tab propertiesTab = new Tab("Generator Properties");
    public TabPane previewTabPane = new TabPane();
    public Tab previewTab = new Tab("Fractal Preview");
    public StackPane fractalPreviewPane = new StackPane();
    public Image fractalPreviewImage = new Image(Objects.requireNonNull(FGen.class.getResourceAsStream("/icon.png")));
    public ImageView fractalPreviewImageView = new ImageView(fractalPreviewImage);
    public ToolBar toolbar = new ToolBar();
    public Label statusLabel = new Label("Ready.");

    public MainWindow(double width, double height) {
        super(width, height);

        createMenuBar();
        createMainGrid();
        createToolbar();

        setRoot(root);
    }

    public void createMenuBar() {
        quitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        quitItem.setOnAction(event -> Application.get().getCurrentStage().close());
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
        generatorsList.setEditable(true);
        generatorBox.getChildren().add(generatorsList);
        generatorTab.setContent(generatorBox);

        generatorTabPane.getTabs().add(generatorTab);
        contentGrid.add(generatorTabPane, 0, 0);
    }

    public void createGeneratorPropertiesTab() {
        parametersTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        parametersTabPane.getTabs().add(propertiesTab);
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

    public void reloadProperties(Generator generator) {
        VBox vBox = new VBox();
        Label label = new Label(generator.name);
        vBox.getChildren().add(label);
        propertiesTab.setContent(vBox);
    }
}
