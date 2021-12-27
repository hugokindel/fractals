package com.ustudents.fgen.gui.views;

import com.ustudents.fgen.common.utils.TextFieldUtil;
import com.ustudents.fgen.generators.SingleImageGenerator;
import com.ustudents.fgen.gui.Window;
import com.ustudents.fgen.gui.views.types.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

/** View of the main window. */
public class MainWindow extends Window {
    public BorderPane root = new BorderPane();
    public MenuBar menuBar = new MenuBar();
    public Menu fileMenu = new Menu("File");
    public MenuItem newItem = new MenuItem("New");
    public MenuItem closeItem = new MenuItem("Close");
    public MenuItem openItem = new MenuItem("Open...");
    public MenuItem saveItem = new MenuItem("Save");
    public MenuItem saveAsItem = new MenuItem("Save As...");
    public MenuItem exportItem = new MenuItem("Export...");
    public MenuItem quitItem = new MenuItem("Quit");
    public Menu viewMenu = new Menu("View");
    public CheckMenuItem changePreviewItem = new CheckMenuItem("Play Preview");
    public Menu helpMenu = new Menu("Help");
    public MenuItem aboutItem = new MenuItem("About");

    public GridPane contentGrid = new GridPane();

    public TabPane generatorTabPane = new TabPane();
    public Tab generatorTab = new Tab("Generators");
    public VBox generatorBox = new VBox();
    public ToolBar generatorToolbar = new ToolBar();
    public Button generatorPlusButton = new Button("Add Generator");
    public ListView<SingleImageGenerator> generatorsList = new ListView<>();

    public TabPane propertiesTabPane = new TabPane();
    public Tab propertiesTab = new Tab("Properties");
    public ScrollPane propertiesScrollPane = new ScrollPane();
    public VBox propertiesEmptyBox = new VBox();
    public VBox propertiesBox = new VBox();
    public ComboBox<AvailableGenerators> propertiesGeneratorType = new ComboBox<>(FXCollections.observableArrayList(AvailableGenerators.values()));
    public TextField propertiesGeneratorPath = new TextField();
    public TextField propertiesGeneratorWidth = new TextField();
    public TextField propertiesGeneratorHeight = new TextField();
    public TextField propertiesGeneratorOffsetX = new TextField();
    public TextField propertiesGeneratorOffsetY = new TextField();
    public ComboBox<AvailableAliasings> propertiesGeneratorAliasing = new ComboBox<>(FXCollections.observableArrayList(AvailableAliasings.values()));
    public HBox propertiesFractalBox = new HBox();
    public ComboBox<AvailableFractals> propertiesFractalType = new ComboBox<>(FXCollections.observableArrayList(AvailableFractals.values()));
    public HBox propertiesFractalPolynomialFunctionBox = new HBox();
    public TextField propertiesFractalPolynomialFunction = new TextField();
    public HBox propertiesFractalPolynomialFunctionStaticZ0Box = new HBox();
    public CheckBox propertiesFractalPolynomialFunctionStaticZ0 = new CheckBox();
    public HBox propertiesFractalComplexRealBox = new HBox();
    public TextField propertiesFractalComplexReal = new TextField();
    public HBox propertiesFractalComplexImaginaryBox = new HBox();
    public TextField propertiesFractalComplexImaginary = new TextField();
    public TextField propertiesPlaneStartReal = new TextField();
    public TextField propertiesPlaneStartImaginary = new TextField();
    public TextField propertiesPlaneEndReal = new TextField();
    public TextField propertiesPlaneEndImaginary = new TextField();
    public TextField propertiesPlaneStep = new TextField();
    public ComboBox<AvailableCalculationHandlers> propertiesCalculationHandlerType = new ComboBox<>(FXCollections.observableArrayList(AvailableCalculationHandlers.values()));
    public HBox propertiesCalculationHandlerRadiusBox = new HBox();
    public TextField propertiesCalculationHandlerMaxIterations = new TextField();
    public TextField propertiesCalculationHandlerRadius = new TextField();
    public TextField propertiesParallelismLevel = new TextField();
    public TextField propertiesParallelismThreshold = new TextField();
    public HBox propertiesParallelismLevelBox = new HBox();
    public HBox propertiesParallelismThresholdBox = new HBox();
    public ComboBox<AvailableColorHandlers> propertiesColorHandlerType = new ComboBox<>(FXCollections.observableArrayList(AvailableColorHandlers.values()));

    public TabPane previewTabPane = new TabPane();
    public VBox previewTabEmptyBox = new VBox();
    public Tab previewTab = new Tab("Fractal Preview");
    public ImageView fractalPreviewImageView = new ImageView();

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
        newItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        openItem.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
        saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        exportItem.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        quitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        fileMenu.getItems().addAll(
                newItem,
                new SeparatorMenuItem(),
                openItem,
                new SeparatorMenuItem(),
                closeItem,
                new SeparatorMenuItem(),
                saveItem,
                saveAsItem,
                new SeparatorMenuItem(),
                exportItem,
                new SeparatorMenuItem(),
                quitItem
        );
        changePreviewItem.setSelected(true);
        changePreviewItem.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
        viewMenu.getItems().add(changePreviewItem);
        helpMenu.getItems().add(aboutItem);
        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);
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
        generatorToolbar.setStyle("-fx-border-width: 0 1 0 0; -fx-border-color: #C8C8C8;");
        generatorBox.getChildren().add(generatorToolbar);

        VBox.setVgrow(generatorsList, Priority.ALWAYS);
        generatorsList.setEditable(true);
        generatorBox.getChildren().add(generatorsList);
        generatorTab.setContent(generatorBox);

        generatorTabPane.getTabs().add(generatorTab);
        contentGrid.add(generatorTabPane, 0, 0);
    }

    public void createGeneratorPropertiesTab() {
        propertiesTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        propertiesTabPane.getTabs().add(propertiesTab);

        {
            propertiesEmptyBox.setStyle("-fx-border-width: 0 1 1 0; -fx-border-color: #C8C8C8;");
            Label title = new Label("No Generator Selected");
            title.setPadding(new Insets(0, 0, 8, 0));
            title.setStyle("-fx-font-weight: bold");
            title.setTextAlignment(TextAlignment.CENTER);
            propertiesEmptyBox.getChildren().add(title);
            propertiesEmptyBox.setAlignment(Pos.CENTER);
        }

        propertiesBox.setPadding(new Insets(8, 10, 10, 10));

        {
            Label title = new Label("Generator");
            title.setPadding(new Insets(0, 0, 8, 0));
            title.setStyle("-fx-font-weight: bold");
            propertiesBox.getChildren().add(title);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Type");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            HBox.setHgrow(propertiesGeneratorType, Priority.ALWAYS);
            propertiesGeneratorType.setMaxWidth(Double.MAX_VALUE);
            hBox.getChildren().add(propertiesGeneratorType);

            propertiesBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Path");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            HBox.setHgrow(propertiesGeneratorPath, Priority.ALWAYS);
            hBox.getChildren().add(propertiesGeneratorPath);

            propertiesBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Width");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            HBox.setHgrow(propertiesGeneratorWidth, Priority.ALWAYS);
            propertiesGeneratorWidth.setTextFormatter(new TextFormatter<String>(TextFieldUtil.intWithNegFilter));
            hBox.getChildren().add(propertiesGeneratorWidth);

            propertiesBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Height");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            HBox.setHgrow(propertiesGeneratorHeight, Priority.ALWAYS);
            propertiesGeneratorHeight.setTextFormatter(new TextFormatter<String>(TextFieldUtil.intWithNegFilter));
            hBox.getChildren().add(propertiesGeneratorHeight);

            propertiesBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Offset (X)");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            HBox.setHgrow(propertiesGeneratorOffsetX, Priority.ALWAYS);
            propertiesGeneratorOffsetX.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleWithNegFilter));
            hBox.getChildren().add(propertiesGeneratorOffsetX);

            propertiesBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Offset (Y)");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            HBox.setHgrow(propertiesGeneratorOffsetY, Priority.ALWAYS);
            propertiesGeneratorOffsetY.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleWithNegFilter));
            hBox.getChildren().add(propertiesGeneratorOffsetY);

            propertiesBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Anti-Aliasing");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            HBox.setHgrow(propertiesGeneratorAliasing, Priority.ALWAYS);
            propertiesGeneratorAliasing.setMaxWidth(Double.MAX_VALUE);
            hBox.getChildren().add(propertiesGeneratorAliasing);

            propertiesBox.getChildren().add(hBox);
        }

        {
            Label title = new Label("Fractal");
            title.setPadding(new Insets(0, 0, 8, 0));
            title.setStyle("-fx-font-weight: bold");
            propertiesBox.getChildren().add(title);
        }

        {
            propertiesFractalBox.setPadding(new Insets(0, 0, 8, 0));
            propertiesFractalBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Type");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            propertiesFractalBox.getChildren().add(label);

            HBox.setHgrow(propertiesFractalType, Priority.ALWAYS);
            propertiesFractalType.setMaxWidth(Double.MAX_VALUE);
            propertiesFractalBox.getChildren().add(propertiesFractalType);

            propertiesBox.getChildren().add(propertiesFractalBox);
        }

        {
            propertiesFractalPolynomialFunctionBox.setPadding(new Insets(0, 0, 8, 0));
            propertiesFractalPolynomialFunctionBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Function");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            propertiesFractalPolynomialFunctionBox.getChildren().add(label);

            HBox.setHgrow(propertiesFractalPolynomialFunction, Priority.ALWAYS);
            propertiesFractalPolynomialFunctionBox.getChildren().add(propertiesFractalPolynomialFunction);

            propertiesBox.getChildren().add(propertiesFractalPolynomialFunctionBox);
        }

        {
            propertiesFractalPolynomialFunctionStaticZ0Box.setPadding(new Insets(0, 0, 8, 0));
            propertiesFractalPolynomialFunctionStaticZ0Box.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Set z0 = 0");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            propertiesFractalPolynomialFunctionStaticZ0Box.getChildren().add(label);

            HBox.setHgrow(propertiesFractalPolynomialFunctionStaticZ0, Priority.ALWAYS);
            propertiesFractalPolynomialFunctionStaticZ0Box.getChildren().add(propertiesFractalPolynomialFunctionStaticZ0);

            propertiesBox.getChildren().add(propertiesFractalPolynomialFunctionStaticZ0Box);
        }

        {
            propertiesFractalComplexRealBox.setPadding(new Insets(0, 0, 8, 0));
            propertiesFractalComplexRealBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Complex (Real)");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            propertiesFractalComplexRealBox.getChildren().add(label);

            HBox.setHgrow(propertiesFractalComplexReal, Priority.ALWAYS);
            propertiesFractalComplexReal.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleWithNegFilter));
            propertiesFractalComplexRealBox.getChildren().add(propertiesFractalComplexReal);

            propertiesBox.getChildren().add(propertiesFractalComplexRealBox);
        }

        {
            propertiesFractalComplexImaginaryBox.setPadding(new Insets(0, 0, 8, 0));
            propertiesFractalComplexImaginaryBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Complex (Imaginary)");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            propertiesFractalComplexImaginaryBox.getChildren().add(label);

            HBox.setHgrow(propertiesFractalComplexImaginary, Priority.ALWAYS);
            propertiesFractalComplexImaginary.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleWithNegFilter));
            propertiesFractalComplexImaginaryBox.getChildren().add(propertiesFractalComplexImaginary);

            propertiesBox.getChildren().add(propertiesFractalComplexImaginaryBox);
        }

        {
            Label title = new Label("Complex Plane");
            title.setPadding(new Insets(0, 0, 8, 0));
            title.setStyle("-fx-font-weight: bold");
            propertiesBox.getChildren().add(title);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Start (Real)");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            HBox.setHgrow(propertiesPlaneStartReal, Priority.ALWAYS);
            propertiesPlaneStartReal.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleWithNegFilter));
            hBox.getChildren().add(propertiesPlaneStartReal);

            propertiesBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Start (Imaginary)");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            HBox.setHgrow(propertiesPlaneStartImaginary, Priority.ALWAYS);
            propertiesPlaneStartImaginary.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleWithNegFilter));
            hBox.getChildren().add(propertiesPlaneStartImaginary);

            propertiesBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("End  (Real)");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            HBox.setHgrow(propertiesPlaneEndReal, Priority.ALWAYS);
            propertiesPlaneEndReal.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleWithNegFilter));
            hBox.getChildren().add(propertiesPlaneEndReal);

            propertiesBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("End (Imaginary)");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            HBox.setHgrow(propertiesPlaneEndImaginary, Priority.ALWAYS);
            propertiesPlaneEndImaginary.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleWithNegFilter));
            hBox.getChildren().add(propertiesPlaneEndImaginary);

            propertiesBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Step");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            HBox.setHgrow(propertiesPlaneStep, Priority.ALWAYS);
            propertiesPlaneStep.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleFilter));
            hBox.getChildren().add(propertiesPlaneStep);

            propertiesBox.getChildren().add(hBox);
        }

        {
            Label title = new Label("Calculation Handler");
            title.setPadding(new Insets(0, 0, 8, 0));
            title.setStyle("-fx-font-weight: bold");
            propertiesBox.getChildren().add(title);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Type");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            HBox.setHgrow(propertiesCalculationHandlerType, Priority.ALWAYS);
            propertiesCalculationHandlerType.setMaxWidth(Double.MAX_VALUE);
            hBox.getChildren().add(propertiesCalculationHandlerType);

            propertiesBox.getChildren().add(hBox);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Max Iterations");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            HBox.setHgrow(propertiesCalculationHandlerMaxIterations, Priority.ALWAYS);
            propertiesCalculationHandlerMaxIterations.setTextFormatter(new TextFormatter<String>(TextFieldUtil.intFilter));
            hBox.getChildren().add(propertiesCalculationHandlerMaxIterations);

            propertiesBox.getChildren().add(hBox);
        }

        {
            propertiesCalculationHandlerRadiusBox.setPadding(new Insets(0, 0, 8, 0));
            propertiesCalculationHandlerRadiusBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Radius");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            propertiesCalculationHandlerRadiusBox.getChildren().add(label);

            HBox.setHgrow(propertiesCalculationHandlerRadius, Priority.ALWAYS);
            propertiesCalculationHandlerRadius.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleFilter));
            propertiesCalculationHandlerRadiusBox.getChildren().add(propertiesCalculationHandlerRadius);

            propertiesBox.getChildren().add(propertiesCalculationHandlerRadiusBox);
        }

        {
            propertiesParallelismLevelBox.setPadding(new Insets(0, 0, 8, 0));
            propertiesParallelismLevelBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Parallelism Level");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            propertiesParallelismLevelBox.getChildren().add(label);

            HBox.setHgrow(propertiesParallelismLevel, Priority.ALWAYS);
            propertiesParallelismLevel.setTextFormatter(new TextFormatter<String>(TextFieldUtil.intFilter));
            propertiesParallelismLevelBox.getChildren().add(propertiesParallelismLevel);

            propertiesBox.getChildren().add(propertiesParallelismLevelBox);
        }

        {
            propertiesParallelismThresholdBox.setPadding(new Insets(0, 0, 8, 0));
            propertiesParallelismThresholdBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Parallelism Threshold");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            propertiesParallelismThresholdBox.getChildren().add(label);

            HBox.setHgrow(propertiesParallelismThreshold, Priority.ALWAYS);
            propertiesParallelismThreshold.setTextFormatter(new TextFormatter<String>(TextFieldUtil.doubleFilter));
            propertiesParallelismThresholdBox.getChildren().add(propertiesParallelismThreshold);

            propertiesBox.getChildren().add(propertiesParallelismThresholdBox);
        }

        {
            Label title = new Label("Color Handler");
            title.setPadding(new Insets(0, 0, 8, 0));
            title.setStyle("-fx-font-weight: bold");
            propertiesBox.getChildren().add(title);
        }

        {
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Type");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            HBox.setHgrow(propertiesColorHandlerType, Priority.ALWAYS);
            propertiesColorHandlerType.setMaxWidth(Double.MAX_VALUE);
            hBox.getChildren().add(propertiesColorHandlerType);

            propertiesBox.getChildren().add(hBox);
        }

        propertiesScrollPane.setStyle("-fx-border-width: 0 1 1 0; -fx-border-color: #C8C8C8;");
        propertiesScrollPane.setFitToHeight(true);
        propertiesScrollPane.setFitToWidth(true);
        propertiesScrollPane.setContent(propertiesBox);

        contentGrid.add(propertiesTabPane, 1, 0);
    }

    public void createFractalPreviewTab() {
        previewTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        previewTabEmptyBox.setStyle("-fx-border-width: 0 0 1 0; -fx-border-color: #C8C8C8;");
        previewTabEmptyBox.setPadding(new Insets(8, 10, 10, 10));
        Label title = new Label("No Preview Available");
        title.setPadding(new Insets(0, 0, 8, 0));
        title.setStyle("-fx-font-weight: bold");
        title.setTextAlignment(TextAlignment.CENTER);
        previewTabEmptyBox.getChildren().add(title);
        previewTabEmptyBox.setAlignment(Pos.CENTER);

        fractalPreviewImageView.fitWidthProperty().bind(previewTabPane.widthProperty());
        fractalPreviewImageView.fitHeightProperty().bind(previewTabPane.heightProperty());

        previewTab.setContent(previewTabEmptyBox);
        previewTabPane.getTabs().add(previewTab);
        contentGrid.add(previewTabPane, 2, 0);
    }

    public void createToolbar() {
        toolbar.getItems().add(statusLabel);
        root.setBottom(toolbar);
    }
}
