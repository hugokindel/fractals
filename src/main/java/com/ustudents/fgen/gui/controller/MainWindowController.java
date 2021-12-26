package com.ustudents.fgen.gui.controller;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.common.json.Json;
import com.ustudents.fgen.common.utils.TextFieldUtil;
import com.ustudents.fgen.format.Configuration;
import com.ustudents.fgen.fractals.JuliaSet;
import com.ustudents.fgen.generators.Generator;
import com.ustudents.fgen.generators.JpegGenerator;
import com.ustudents.fgen.generators.PngGenerator;
import com.ustudents.fgen.generators.SingleImageGenerator;
import com.ustudents.fgen.gui.Application;
import com.ustudents.fgen.gui.controls.GeneratorListCell;
import com.ustudents.fgen.gui.views.AboutWindow;
import com.ustudents.fgen.gui.views.MainWindow;
import com.ustudents.fgen.handlers.calculation.PoolCalculationHandler;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainWindowController {
    public class PreviewService extends Service<Void> {
        public boolean isWorking = false;

        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    logic();
                    return null;
                }
            };
        }

        public void logic() {
            isWorking = true;
            Platform.runLater(() -> updatePreview(true));

            view.generatorsList.getSelectionModel().getSelectedItem().width = (int)view.previewTabPane.getWidth();
            view.generatorsList.getSelectionModel().getSelectedItem().height = (int)view.previewTabPane.getHeight();
            view.generatorsList.getSelectionModel().getSelectedItem().generate();
            previewImage = SwingFXUtils.toFXImage(view.generatorsList.getSelectionModel().getSelectedItem().bufferedImage, null);
            isWorking = false;
            Platform.runLater(() -> updatePreview(true));
        }

        public void update() {
            if (shouldShowPreview && getSelectedGenerator() != null) {
                previewImage = null;
                restart();
            }
        }
    }

    public MainWindow view = new MainWindow(1280, 720);
    public PreviewService previewService = new PreviewService();
    public Image previewImage = null;
    public boolean shouldShowPreview = true;

    public MainWindowController() {
        loadModel();
        updateView();
        setupEvents();
    }

    public void setupEvents() {
        setupMenuEvents();
        setupGeneratorsTabEvents();
        setupStageEvents();
    }

    public void setupMenuEvents() {
        view.newItem.setOnAction(event -> {
            clearGenerators();
            addGenerator();
            updateView();
        });

        view.openItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Configuration Files");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Configuration Files (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(Application.get().getCurrentStage());

            if (file != null) {
                clearGenerators();
                FGen.get().load(file.getAbsolutePath(), true);
                loadModel();
                updateView();
            }
        });

        view.saveItem.setOnAction(e -> menuSaveAction());

        view.saveAsItem.setOnAction(e -> menuSaveAsAction());

        view.exportItem.setOnAction(e -> {
            // TODO: Width, Height, Exporting popup
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Export Configuration Files");
            File file = directoryChooser.showDialog(Application.get().getCurrentStage());

            if (file != null) {
                for (Generator generator : view.generatorsList.getItems()) {
                    if (generator instanceof JpegGenerator) {
                        ((JpegGenerator)generator).path = file.getAbsolutePath() + "/" + ((JpegGenerator)generator).path;
                    } else {
                        ((PngGenerator)generator).path = file.getAbsolutePath() + "/" + ((PngGenerator)generator).path;
                    }

                    generator.generate();
                }
            }
        });

        view.closeItem.setOnAction(event -> {
            clearGenerators();
            updateView();
        });

        view.quitItem.setOnAction(event -> Application.get().close());

        view.changePreviewItem.setOnAction(event -> {
            shouldShowPreview = view.changePreviewItem.isSelected();
            updateView();
        });

        view.aboutItem.setOnAction(event -> Application.get().showPopup(new AboutWindow(400, 250)));
    }

    private void menuSaveAction() {
        if (FGen.get().saveFilepath == null) {
            menuSaveAsAction();
            return;
        }

        Configuration configuration = new Configuration();
        configuration.generators.addAll(view.generatorsList.getItems());
        Json.serialize(FGen.get().saveFilepath, configuration);
    }

    private void menuSaveAsAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Configuration File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Configuration Files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(Application.get().getCurrentStage());

        if (file != null) {
            FGen.get().saveFilepath = file.getAbsolutePath();
            Application.get().getCurrentStage().setTitle("FGen - " + Paths.get(FGen.get().saveFilepath).toAbsolutePath());
            menuSaveAction();
        }
    }

    public void setupGeneratorsTabEvents() {
        view.generatorPlusButton.setOnMouseClicked(event -> {
            addGenerator();
            updateView();
        });

        view.generatorsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateView();
        });

        view.generatorsList.setCellFactory(listView -> {
            GeneratorListCell cell = new GeneratorListCell();
            cell.setOnEdited(event ->  {
                updateView();
            });

            ContextMenu contextMenu = new ContextMenu();
            cell.setContextMenu(contextMenu);

            MenuItem addItem = new MenuItem("Add Generator");
            addItem.setOnAction(event -> {
                addGenerator();
                updateView();
            });
            contextMenu.getItems().add(addItem);

            cell.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    cell.setText(newValue.name);

                    MenuItem renameItem = new MenuItem("Rename");
                    renameItem.setOnAction(event -> cell.startEdit());

                    MenuItem deleteItem = new MenuItem("Delete");
                    deleteItem.setOnAction(event -> {
                        listView.getItems().remove(cell.getItem());

                        if (listView.getItems().size() == 0) {
                            updateView();
                        }
                    });

                    contextMenu.getItems().clear();
                    contextMenu.getItems().addAll(renameItem, deleteItem);
                } else {
                    cell.setText(null);

                    contextMenu.getItems().clear();
                    contextMenu.getItems().add(addItem);
                }
            });

            return cell;
        });
    }

    public void setupStageEvents() {
        final ChangeListener<Number> listener = new ChangeListener<Number>()
        {
            final Timer timer = new Timer();
            TimerTask task = null;
            final long delayTime = 200;

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, final Number newValue)
            {
                updateTitle();

                if (task != null) {
                    task.cancel();
                }

                task = new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> previewService.update());
                    }
                };

                timer.schedule(task, delayTime);
            }
        };

        Application.get().getCurrentStage().widthProperty().addListener(listener);
        Application.get().getCurrentStage().heightProperty().addListener(listener);
    }

    public void updateView() {
        updateTitle();
        updateMenu();
        updateProperties();
        updatePreview(false);
    }

    public void updateTitle() {
        if (FGen.get().saveFilepath != null) {
            Application.get().getCurrentStage().setTitle("FGen - " + Paths.get(FGen.get().saveFilepath).toAbsolutePath());
        } else {
            Application.get().getCurrentStage().setTitle("FGen");
        }

        view.previewTab.setText(String.format("Fractal Preview (%dx%d)", (int)view.previewTabPane.getWidth(), (int)view.previewTabPane.getHeight()));
    }

    public void updateMenu() {
        boolean disabled = view.generatorsList.getItems().size() == 0;
        view.exportItem.setDisable(disabled);
        view.closeItem.setDisable(disabled);
        view.saveItem.setDisable(disabled);
        view.saveAsItem.setDisable(disabled);
    }

    public void updateProperties() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(8, 10, 10, 10));

        if (getSelectedGenerator() == null) {
            vBox.setStyle("-fx-border-width: 0 1 1 0; -fx-border-color: #C8C8C8;");
            Label title = new Label("No Generator Selected");
            title.setPadding(new Insets(0, 0, 8, 0));
            title.setStyle("-fx-font-weight: bold");
            title.setTextAlignment(TextAlignment.CENTER);
            vBox.getChildren().add(title);
            vBox.setAlignment(Pos.CENTER);
            view.propertiesTab.setContent(vBox);
            return;
        }

        {
            Label title = new Label("Generator");
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

            ComboBox<MainWindow.GeneratorTypes> comboBox = new ComboBox<>(FXCollections.observableArrayList(MainWindow.GeneratorTypes.values()));
            comboBox.setValue(MainWindow.GeneratorTypes.JPEG);
            HBox.setHgrow(comboBox, Priority.ALWAYS);
            comboBox.setMaxWidth(Double.MAX_VALUE);
            hBox.getChildren().add(comboBox);

            vBox.getChildren().add(hBox);
        }

        {
            String path;

            if (getSelectedGenerator() instanceof JpegGenerator) {
                path = ((JpegGenerator)getSelectedGenerator()).path;
            } else {
                path = ((PngGenerator)getSelectedGenerator()).path;
            }

            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Path");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            TextField textField = new TextField(path);
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
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 8, 0));
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label("Anti-Aliasing");
            label.setMinWidth(130);
            label.setPadding(new Insets(0, 10, 0, 0));
            hBox.getChildren().add(label);

            ComboBox<MainWindow.AliasingTypes> comboBox = new ComboBox<>(FXCollections.observableArrayList(MainWindow.AliasingTypes.values()));
            comboBox.setValue(MainWindow.AliasingTypes.X1);
            HBox.setHgrow(comboBox, Priority.ALWAYS);
            comboBox.setMaxWidth(Double.MAX_VALUE);
            hBox.getChildren().add(comboBox);

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

            ComboBox<MainWindow.FractalTypes> comboBox = new ComboBox<>(FXCollections.observableArrayList(MainWindow.FractalTypes.values()));
            comboBox.setValue(MainWindow.FractalTypes.JULIA);
            HBox.setHgrow(comboBox, Priority.ALWAYS);
            comboBox.setMaxWidth(Double.MAX_VALUE);
            hBox.getChildren().add(comboBox);

            vBox.getChildren().add(hBox);
        }

        if (getSelectedGenerator().calculationHandler.fractal instanceof JuliaSet) {
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

            ComboBox<MainWindow.CalculationHandlerTypes> comboBox = new ComboBox<>(FXCollections.observableArrayList(MainWindow.CalculationHandlerTypes.values()));
            comboBox.setValue(MainWindow.CalculationHandlerTypes.SIMPLE);
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

        if (getSelectedGenerator().calculationHandler instanceof PoolCalculationHandler) {
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

            ComboBox<MainWindow.ImageHandlerTypes> comboBox = new ComboBox<>(FXCollections.observableArrayList(MainWindow.ImageHandlerTypes.values()));
            comboBox.setValue(MainWindow.ImageHandlerTypes.SIMPLE);
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

            ComboBox<MainWindow.ColorHandlerTypes> comboBox = new ComboBox<>(FXCollections.observableArrayList(MainWindow.ColorHandlerTypes.values()));
            comboBox.setValue(MainWindow.ColorHandlerTypes.BASIC);
            HBox.setHgrow(comboBox, Priority.ALWAYS);
            comboBox.setMaxWidth(Double.MAX_VALUE);
            hBox.getChildren().add(comboBox);

            vBox.getChildren().add(hBox);
        }

        view.scrollPane.setContent(vBox);
        view.propertiesTab.setContent(view.scrollPane);
    }

    public void updatePreview(boolean calledFromService) {
        if (!calledFromService) {
            previewService.update();
        }

        updatePreviewTab();
        updatePreviewText();
    }

    public void updatePreviewTab() {
        if (!shouldShowPreview || previewImage == null) {
            VBox vBox = new VBox();
            vBox.setStyle("-fx-border-width: 0 0 1 0; -fx-border-color: #C8C8C8;");
            vBox.setPadding(new Insets(8, 10, 10, 10));
            Label title = new Label("No Preview Available");
            title.setPadding(new Insets(0, 0, 8, 0));
            title.setStyle("-fx-font-weight: bold");
            title.setTextAlignment(TextAlignment.CENTER);
            vBox.getChildren().add(title);
            vBox.setAlignment(Pos.CENTER);
            view.previewTab.setContent(vBox);
            return;
        }

        view.fractalPreviewImageView.setImage(previewImage);
        view.previewTab.setContent(view.fractalPreviewImageView);
    }

    public void updatePreviewText() {
        if (previewService.isWorking) {
            view.statusLabel.setText("Generating...");
        } else if (shouldShowPreview) {
            view.statusLabel.setText("Ready.");
        } else {
            view.statusLabel.setText("Paused.");
        }
    }

    private void loadModel() {
        for (int i = 0; i < FGen.get().loadedConfiguration.generators.size(); i++) {
            view.generatorsList.getItems().add((SingleImageGenerator)FGen.get().loadedConfiguration.generators.get(i));
        }

        if (view.generatorsList.getItems().size() > 0) {
            view.generatorsList.getSelectionModel().select(0);
        }
    }

    private void addGenerator() {
        Configuration configuration = Json.deserializeFromResources("/presets/gui_new.json", Configuration.class);
        assert configuration != null;
        ((JpegGenerator)configuration.generators.get(0)).path = ((JpegGenerator)configuration.generators.get(0)).path.replace("$", String.valueOf(view.generatorsList.getItems().size()));
        view.generatorsList.getItems().add((SingleImageGenerator)configuration.generators.get(0));
        view.generatorsList.getSelectionModel().select(view.generatorsList.getItems().size() - 1);
    }

    private void clearGenerators() {
        view.generatorsList.getItems().clear();
        previewImage = null;
    }

    public SingleImageGenerator getSelectedGenerator() {
        return view.generatorsList.getSelectionModel().getSelectedItem();
    }
}
