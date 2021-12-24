package com.ustudents.fgen.gui.views;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.generators.Generator;
import com.ustudents.fgen.gui.Application;
import com.ustudents.fgen.gui.Window;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.util.Objects;

public class MainWindow extends Window {
    private enum GeneratorTypes {
        JPEG,
        PNG
    }

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
        {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(15);
            contentGrid.getColumnConstraints().add(columnConstraints);
        }

        {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(30);
            contentGrid.getColumnConstraints().add(columnConstraints);
        }

        {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(55);
            contentGrid.getColumnConstraints().add(columnConstraints);
        }

        {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100);
            contentGrid.getRowConstraints().add(rowConstraints);
        }

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
        fractalPreviewImageView.fitWidthProperty().bind(previewTabPane.widthProperty());
        fractalPreviewImageView.fitHeightProperty().bind(previewTabPane.heightProperty());
        previewTab.setContent(fractalPreviewImageView);
        previewTabPane.getTabs().add(previewTab);
        contentGrid.add(previewTabPane, 2, 0);
    }

    public void createToolbar() {
        toolbar.getItems().add(statusLabel);
        root.setBottom(toolbar);
    }

    public void reloadProperties(Generator generator) {
        ScrollPane scrollPane = new ScrollPane();

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(8, 10, 10, 10));

        {
            Label title = new Label("Generator");
            title.setPadding(new Insets(0, 0, 8, 0));
            title.setStyle("-fx-font-weight: bold");
            vBox.getChildren().add(title);
        }

        // Generator type.
        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Type");
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            ComboBox<GeneratorTypes> comboBox = new ComboBox<>(FXCollections.observableArrayList( GeneratorTypes.values()));
            comboBox.setValue(GeneratorTypes.JPEG);
            HBox.setHgrow(comboBox, Priority.ALWAYS);
            comboBox.setMaxWidth(Double.MAX_VALUE);
            hBox.getChildren().add(comboBox);

            vBox.getChildren().add(hBox);
        }

        // Generator type.
        {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Path");
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            TextField textField = new TextField("fractal.jpeg");
            HBox.setHgrow(textField, Priority.ALWAYS);
            hBox.getChildren().add(textField);

            vBox.getChildren().add(hBox);
        }

        scrollPane.setContent(vBox);

        propertiesTab.setContent(scrollPane);
    }
}
