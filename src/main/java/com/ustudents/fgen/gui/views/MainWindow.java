package com.ustudents.fgen.gui.views;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.common.utils.TextFieldUtil;
import com.ustudents.fgen.fractals.JuliaSet;
import com.ustudents.fgen.generators.Generator;
import com.ustudents.fgen.generators.SingleImageGenerator;
import com.ustudents.fgen.gui.Application;
import com.ustudents.fgen.gui.Window;
import com.ustudents.fgen.handlers.calculation.PoolCalculationHandler;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;

import java.util.Objects;

public class MainWindow extends Window {
    private enum GeneratorTypes {
        JPEG,
        PNG
    }

    private enum FractalTypes {
        JULIA("Julia"),
        MANDELBROT("Mandelbrot");

        private String name;

        private FractalTypes(String theType) {
            this.name = theType;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private enum CalculationHandlerTypes {
        SIMPLE("Simple"),
        POOL("Pool"),
        STREAM("Stream"),
        BUDDHA_SIMPLE("Simple Buddha"),
        BUDDHA_STREAM("Simple Stream");

        private String name;

        private CalculationHandlerTypes(String theType) {
            this.name = theType;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private enum ImageHandlerTypes {
        SIMPLE("Simple"),
        POOL("Pool"),
        STREAM("Stream");

        private String name;

        private ImageHandlerTypes(String theType) {
            this.name = theType;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private enum ColorHandlerTypes {
        BASIC("Basic"),
        VERY_RED("Very Red"),
        HSB("Hsb"),
        DARK_PSYCHEDELIC("Dark Psychedelic"),
        LIGHT_PSYCHEDELIC("Light Psychedelic"),
        BUDDHA("Buddha");

        private String name;

        private ColorHandlerTypes(String theType) {
            this.name = theType;
        }

        @Override
        public String toString() {
            return name;
        }
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
            columnConstraints.setPercentWidth(20);
            contentGrid.getColumnConstraints().add(columnConstraints);
        }

        {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(30);
            contentGrid.getColumnConstraints().add(columnConstraints);
        }

        {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(50);
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

    public void reloadProperties(SingleImageGenerator generator) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

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
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            ComboBox<GeneratorTypes> comboBox = new ComboBox<>(FXCollections.observableArrayList(GeneratorTypes.values()));
            comboBox.setValue(GeneratorTypes.JPEG);
            HBox.setHgrow(comboBox, Priority.ALWAYS);
            comboBox.setMaxWidth(Double.MAX_VALUE);
            hBox.getChildren().add(comboBox);

            vBox.getChildren().add(hBox);
        }

        // Generator path.
        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Path");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            TextField textField = new TextField("fractal.jpeg");
            HBox.setHgrow(textField, Priority.ALWAYS);
            hBox.getChildren().add(textField);

            vBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Width");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            TextField textField = new TextField("256");
            HBox.setHgrow(textField, Priority.ALWAYS);
            textField.setTextFormatter(new TextFormatter<String>(TextFieldUtil.intWithNegFilter));
            hBox.getChildren().add(textField);

            vBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Height");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            TextField textField = new TextField("256");
            HBox.setHgrow(textField, Priority.ALWAYS);
            textField.setTextFormatter(new TextFormatter<String>(TextFieldUtil.intWithNegFilter));
            hBox.getChildren().add(textField);

            vBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Offset (X)");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            TextField textField = new TextField("0.0");
            HBox.setHgrow(textField, Priority.ALWAYS);
            textField.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleWithNegFilter));
            hBox.getChildren().add(textField);

            vBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Offset (Y)");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            TextField textField = new TextField("0.0");
            HBox.setHgrow(textField, Priority.ALWAYS);
            textField.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleWithNegFilter));
            hBox.getChildren().add(textField);

            vBox.getChildren().add(hBox);
        }

        {
            Label title = new Label("Fractal");
            title.setPadding(new Insets(0, 0, 8, 0));
            title.setStyle("-fx-font-weight: bold");
            vBox.getChildren().add(title);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Type");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            ComboBox<FractalTypes> comboBox = new ComboBox<>(FXCollections.observableArrayList(FractalTypes.values()));
            comboBox.setValue(FractalTypes.JULIA);
            HBox.setHgrow(comboBox, Priority.ALWAYS);
            comboBox.setMaxWidth(Double.MAX_VALUE);
            hBox.getChildren().add(comboBox);

            vBox.getChildren().add(hBox);
        }

        if (generator.calculationHandler.fractal instanceof JuliaSet) {
            {
                HBox hBox = new HBox();
                hBox.setPadding(new Insets(0, 0, 8, 0));
                hBox.setAlignment(Pos.CENTER_LEFT);

                Label label = new Label("Complex (Real)");
                label.setMinWidth(130);
                label.setPadding(new Insets(0, 10, 0, 0));
                hBox.getChildren().add(label);

                TextField textField = new TextField("-0.7269");
                HBox.setHgrow(textField, Priority.ALWAYS);
                textField.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleWithNegFilter));
                hBox.getChildren().add(textField);

                vBox.getChildren().add(hBox);
            }

            {
                HBox hBox = new HBox();
                hBox.setPadding(new Insets(0, 0, 8, 0));
                hBox.setAlignment(Pos.CENTER_LEFT);

                Label label = new Label("Complex (Imaginary)");
                label.setMinWidth(130);
                label.setPadding(new Insets(0, 10, 0, 0));
                hBox.getChildren().add(label);

                TextField textField = new TextField("0.1889");
                HBox.setHgrow(textField, Priority.ALWAYS);
                textField.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleWithNegFilter));
                hBox.getChildren().add(textField);

                vBox.getChildren().add(hBox);
            }
        }

        {
            Label title = new Label("Complex Plane");
            title.setPadding(new Insets(0, 0, 8, 0));
            title.setStyle("-fx-font-weight: bold");
            vBox.getChildren().add(title);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Start (Real)");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            TextField textField = new TextField("-1.0");
            HBox.setHgrow(textField, Priority.ALWAYS);
            textField.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleWithNegFilter));
            hBox.getChildren().add(textField);

            vBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Start (Imaginary)");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            TextField textField = new TextField("1.0");
            HBox.setHgrow(textField, Priority.ALWAYS);
            textField.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleWithNegFilter));
            hBox.getChildren().add(textField);

            vBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("End  (Real)");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            TextField textField = new TextField("1.0");
            HBox.setHgrow(textField, Priority.ALWAYS);
            textField.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleWithNegFilter));
            hBox.getChildren().add(textField);

            vBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("End (Imaginary)");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            TextField textField = new TextField("-1.0");
            HBox.setHgrow(textField, Priority.ALWAYS);
            textField.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleWithNegFilter));
            hBox.getChildren().add(textField);

            vBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Step");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            TextField textField = new TextField("0.001");
            HBox.setHgrow(textField, Priority.ALWAYS);
            textField.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleFilter));
            hBox.getChildren().add(textField);

            vBox.getChildren().add(hBox);
        }

        {
            Label title = new Label("Calculation Handler");
            title.setPadding(new Insets(0, 0, 8, 0));
            title.setStyle("-fx-font-weight: bold");
            vBox.getChildren().add(title);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Type");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            ComboBox<CalculationHandlerTypes> comboBox = new ComboBox<>(FXCollections.observableArrayList(CalculationHandlerTypes.values()));
            comboBox.setValue(CalculationHandlerTypes.SIMPLE);
            HBox.setHgrow(comboBox, Priority.ALWAYS);
            comboBox.setMaxWidth(Double.MAX_VALUE);
            hBox.getChildren().add(comboBox);

            vBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Max Iterations");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            TextField textField = new TextField("1000");
            HBox.setHgrow(textField, Priority.ALWAYS);
            textField.setTextFormatter(new TextFormatter<String>(TextFieldUtil.intFilter));
            hBox.getChildren().add(textField);

            vBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Radius");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            TextField textField = new TextField("2");
            HBox.setHgrow(textField, Priority.ALWAYS);
            textField.setTextFormatter(new TextFormatter<String>(TextFieldUtil.intFilter));
            hBox.getChildren().add(textField);

            vBox.getChildren().add(hBox);
        }

        if (generator.calculationHandler instanceof PoolCalculationHandler) {
            {
                HBox hBox = new HBox();
                hBox.setPadding(new Insets(0, 0, 8, 0));
                hBox.setAlignment(Pos.CENTER_LEFT);

                Label label = new Label("Parallelism Level");
                label.setMinWidth(130);
                label.setPadding(new Insets(0, 10, 0, 0));
                hBox.getChildren().add(label);

                TextField textField = new TextField(String.valueOf(Runtime.getRuntime().availableProcessors()));
                HBox.setHgrow(textField, Priority.ALWAYS);
                textField.setTextFormatter(new TextFormatter<String>(TextFieldUtil.intFilter));
                hBox.getChildren().add(textField);

                vBox.getChildren().add(hBox);
            }

            {
                HBox hBox = new HBox();
                hBox.setPadding(new Insets(0, 0, 8, 0));
                hBox.setAlignment(Pos.CENTER_LEFT);

                Label label = new Label("Parallelism Threshold");
                label.setMinWidth(130);
                label.setPadding(new Insets(0, 10, 0, 0));
                hBox.getChildren().add(label);

                TextField textField = new TextField(String.valueOf(PoolCalculationHandler.DEFAULT_PARALLELISM_THRESHOLD));
                HBox.setHgrow(textField, Priority.ALWAYS);
                textField.setTextFormatter(new TextFormatter<String>(TextFieldUtil.intFilter));
                hBox.getChildren().add(textField);

                vBox.getChildren().add(hBox);
            }
        }

        {
            Label title = new Label("Image Handler");
            title.setPadding(new Insets(0, 0, 8, 0));
            title.setStyle("-fx-font-weight: bold");
            vBox.getChildren().add(title);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Type");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            ComboBox<ImageHandlerTypes> comboBox = new ComboBox<>(FXCollections.observableArrayList(ImageHandlerTypes.values()));
            comboBox.setValue(ImageHandlerTypes.SIMPLE);
            HBox.setHgrow(comboBox, Priority.ALWAYS);
            comboBox.setMaxWidth(Double.MAX_VALUE);
            hBox.getChildren().add(comboBox);

            vBox.getChildren().add(hBox);
        }

        {
            Label title = new Label("Color Handler");
            title.setPadding(new Insets(0, 0, 8, 0));
            title.setStyle("-fx-font-weight: bold");
            vBox.getChildren().add(title);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Type");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            ComboBox<ColorHandlerTypes> comboBox = new ComboBox<>(FXCollections.observableArrayList(ColorHandlerTypes.values()));
            comboBox.setValue(ColorHandlerTypes.BASIC);
            HBox.setHgrow(comboBox, Priority.ALWAYS);
            comboBox.setMaxWidth(Double.MAX_VALUE);
            hBox.getChildren().add(comboBox);

            vBox.getChildren().add(hBox);
        }

        scrollPane.setContent(vBox);

        propertiesTab.setContent(scrollPane);
    }
}
