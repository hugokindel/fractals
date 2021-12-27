package com.ustudents.fgen.gui.controller;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.common.json.Json;
import com.ustudents.fgen.format.Configuration;
import com.ustudents.fgen.fractals.JuliaSet;
import com.ustudents.fgen.fractals.PolynomialFunction;
import com.ustudents.fgen.generators.Generator;
import com.ustudents.fgen.generators.JpegGenerator;
import com.ustudents.fgen.generators.PngGenerator;
import com.ustudents.fgen.generators.SingleImageGenerator;
import com.ustudents.fgen.gui.Application;
import com.ustudents.fgen.gui.controls.GeneratorListCell;
import com.ustudents.fgen.gui.views.AboutWindow;
import com.ustudents.fgen.gui.views.ExportingWindow;
import com.ustudents.fgen.gui.views.MainWindow;
import com.ustudents.fgen.gui.views.types.*;
import com.ustudents.fgen.handlers.calculation.PoolCalculationHandler;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class MainWindowController {
    public class PreviewService extends Service<Void> {
        public boolean isWorking = false;

        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    isWorking = true;
                    Platform.runLater(() -> updatePreview(true));

                    SingleImageGenerator generator = new SingleImageGenerator();
                    generator.name = view.generatorsList.getSelectionModel().getSelectedItem().name;
                    generator.width = (int)view.previewTabPane.getWidth();
                    generator.height = (int)view.previewTabPane.getHeight();
                    generator.offsetX = view.generatorsList.getSelectionModel().getSelectedItem().offsetX;
                    generator.offsetY = view.generatorsList.getSelectionModel().getSelectedItem().offsetY;
                    generator.calculationHandler = view.generatorsList.getSelectionModel().getSelectedItem().calculationHandler;
                    generator.aliasingType = view.generatorsList.getSelectionModel().getSelectedItem().aliasingType;
                    generator.imageHandler = view.generatorsList.getSelectionModel().getSelectedItem().imageHandler;
                    generator.generate();

                    if (isCancelled()) {
                        return null;
                    }

                    previewImage = SwingFXUtils.toFXImage(generator.bufferedImage, null);
                    isWorking = false;
                    Platform.runLater(() -> updatePreview(true));

                    return null;
                }
            };
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
    public boolean isUpdatingProperties = false;

    public MainWindowController() {
        loadModel();
        updateView();
        setupEvents();
    }

    public void setupEvents() {
        setupStageEvents();
        setupMenuEvents();
        setupGeneratorsTabEvents();
        setupPropertiesTabEvents();
    }

    public void setupStageEvents() {
        final ChangeListener<Number> listener = new ChangeListener<>()
        {
            final Timer timer = new Timer();
            TimerTask task = null;
            final long delayTime = 250;

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
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Export Configuration Files");
            File file = directoryChooser.showDialog(Application.get().getCurrentStage());

            if (file != null) {
                ExportingWindow window = new ExportingWindow(256, 128);
                Stage exportWindow = Application.get().showPopup(window, "Task in progress...");

                Service<Void> exportService = new Service<Void>() {
                    @Override
                    protected Task<Void> createTask() {
                        return new Task<>() {
                            @Override
                            protected Void call() {
                                for (Generator generator : view.generatorsList.getItems()) {
                                    if (isCancelled()) {
                                        return null;
                                    }

                                    String oldPath;

                                    if (generator instanceof JpegGenerator) {
                                        oldPath = ((JpegGenerator)generator).path;
                                        ((JpegGenerator)generator).path = file.getAbsolutePath() + "/" + ((JpegGenerator)generator).path;
                                    } else {
                                        oldPath = ((PngGenerator)generator).path;
                                        ((PngGenerator)generator).path = file.getAbsolutePath() + "/" + ((PngGenerator)generator).path;
                                    }

                                    generator.generate();

                                    if (generator instanceof JpegGenerator) {
                                        ((JpegGenerator)generator).path = oldPath;
                                    } else {
                                        ((PngGenerator)generator).path = oldPath;
                                    }
                                }

                                Platform.runLater(exportWindow::close);

                                return null;
                            }
                        };
                    }
                };

                exportWindow.setOnCloseRequest(event -> {
                    exportService.cancel();
                    exportWindow.close();
                });

                window.cancelButton.setOnAction(event -> {
                    exportService.cancel();
                    exportWindow.close();
                });

                exportService.start();
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

        view.aboutItem.setOnAction(event -> Application.get().showPopup(new AboutWindow(400, 250), "About FGen (Fractal Generator)"));
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
            updateTitle();
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

    public void setupPropertiesTabEvents() {
        final Timer timer = new Timer();
        final TimerTask[] task = {null};
        final long delayTime = 250;

        view.propertiesGeneratorType.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            int index = view.generatorsList.getSelectionModel().getSelectedIndex();
            view.generatorsList.getItems().set(view.generatorsList.getSelectionModel().getSelectedIndex(), newValue.toGenerator(getSelectedGenerator()));
            view.generatorsList.getSelectionModel().select(index);
        });

        view.propertiesGeneratorPath.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            if (getSelectedGenerator() instanceof PngGenerator) {
                ((PngGenerator)getSelectedGenerator()).path = newValue;
            } else {
                ((JpegGenerator)getSelectedGenerator()).path = newValue;
            }
        });

        view.propertiesGeneratorWidth.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            getSelectedGenerator().width = Integer.valueOf(newValue);
        });

        view.propertiesGeneratorHeight.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            getSelectedGenerator().height = Integer.valueOf(newValue);
        });

        view.propertiesGeneratorOffsetX.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            getSelectedGenerator().offsetX = Double.valueOf(newValue);

            if (task[0] != null) {
                task[0].cancel();
            }

            task[0] = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> updateView());
                }
            };

            timer.schedule(task[0], delayTime);
        });

        view.propertiesGeneratorOffsetY.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            getSelectedGenerator().offsetY = Double.valueOf(newValue);

            if (task[0] != null) {
                task[0].cancel();
            }

            task[0] = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> updateView());
                }
            };

            timer.schedule(task[0], delayTime);
        });

        view.propertiesGeneratorAliasing.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            getSelectedGenerator().aliasingType = newValue.toAliasingType();

            updateView();
        });

        view.propertiesFractalType.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            getSelectedGenerator().calculationHandler.fractal = newValue.toFractal();

            updateView();
        });

        view.propertiesFractalComplexReal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            if (getSelectedGenerator().calculationHandler.fractal instanceof JuliaSet) {
                ((JuliaSet)getSelectedGenerator().calculationHandler.fractal).c.real = Double.valueOf(newValue);
            } else {
                ((PolynomialFunction)getSelectedGenerator().calculationHandler.fractal).c.real = Double.valueOf(newValue);
            }

            if (task[0] != null) {
                task[0].cancel();
            }

            task[0] = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> updateView());
                }
            };

            timer.schedule(task[0], delayTime);
        });

        view.propertiesFractalComplexImaginary.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            if (getSelectedGenerator().calculationHandler.fractal instanceof JuliaSet) {
                ((JuliaSet)getSelectedGenerator().calculationHandler.fractal).c.imaginary = Double.valueOf(newValue);
            } else {
                ((PolynomialFunction)getSelectedGenerator().calculationHandler.fractal).c.imaginary = Double.valueOf(newValue);
            }

            if (task[0] != null) {
                task[0].cancel();
            }

            task[0] = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> updateView());
                }
            };

            timer.schedule(task[0], delayTime);
        });

        view.propertiesFractalPolynomialFunction.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            ((PolynomialFunction)getSelectedGenerator().calculationHandler.fractal).f = newValue;

            if (task[0] != null) {
                task[0].cancel();
            }

            task[0] = new TimerTask() {
                @Override
                public void run() {
                    if (((PolynomialFunction)getSelectedGenerator().calculationHandler.fractal).reloadFunction()) {
                        Platform.runLater(() -> updateView());
                    }
                }
            };

            timer.schedule(task[0], delayTime);
        });

        view.propertiesFractalPolynomialFunctionStaticZ0.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            ((PolynomialFunction)getSelectedGenerator().calculationHandler.fractal).staticZ0 = newValue;

            if (task[0] != null) {
                task[0].cancel();
            }

            task[0] = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> updateView());
                }
            };

            timer.schedule(task[0], delayTime);
        });

        view.propertiesPlaneStartReal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            getSelectedGenerator().calculationHandler.plane.start.real = Double.valueOf(newValue);

            if (task[0] != null) {
                task[0].cancel();
            }

            task[0] = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> updateView());
                }
            };

            timer.schedule(task[0], delayTime);
        });

        view.propertiesPlaneStartImaginary.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            getSelectedGenerator().calculationHandler.plane.start.imaginary = Double.valueOf(newValue);

            if (task[0] != null) {
                task[0].cancel();
            }

            task[0] = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> updateView());
                }
            };

            timer.schedule(task[0], delayTime);
        });

        view.propertiesPlaneEndReal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            getSelectedGenerator().calculationHandler.plane.end.real = Double.valueOf(newValue);

            if (task[0] != null) {
                task[0].cancel();
            }

            task[0] = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> updateView());
                }
            };

            timer.schedule(task[0], delayTime);
        });

        view.propertiesPlaneEndImaginary.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            getSelectedGenerator().calculationHandler.plane.end.imaginary = Double.valueOf(newValue);

            if (task[0] != null) {
                task[0].cancel();
            }

            task[0] = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> updateView());
                }
            };

            timer.schedule(task[0], delayTime);
        });

        view.propertiesPlaneStep.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            getSelectedGenerator().calculationHandler.plane.step = Double.valueOf(newValue);

            if (task[0] != null) {
                task[0].cancel();
            }

            task[0] = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> updateView());
                }
            };

            timer.schedule(task[0], delayTime);
        });

        view.propertiesCalculationHandlerType.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            getSelectedGenerator().calculationHandler = newValue.toCalculationHandler(getSelectedGenerator().calculationHandler);

            updateView();
        });

        view.propertiesCalculationHandlerMaxIterations.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            getSelectedGenerator().calculationHandler.maxIterations = Integer.valueOf(newValue);

            if (task[0] != null) {
                task[0].cancel();
            }

            task[0] = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> updateView());
                }
            };

            timer.schedule(task[0], delayTime);
        });

        view.propertiesCalculationHandlerRadius.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            getSelectedGenerator().calculationHandler.radius = Double.valueOf(newValue);

            if (task[0] != null) {
                task[0].cancel();
            }

            task[0] = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> updateView());
                }
            };

            timer.schedule(task[0], delayTime);
        });

        view.propertiesParallelismLevel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            ((PoolCalculationHandler)getSelectedGenerator().calculationHandler).setParallelismLevel(Integer.valueOf(newValue));

            if (task[0] != null) {
                task[0].cancel();
            }

            task[0] = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> updateView());
                }
            };

            timer.schedule(task[0], delayTime);
        });

        view.propertiesParallelismThreshold.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            ((PoolCalculationHandler)getSelectedGenerator().calculationHandler).setParallelismThreshold(Integer.valueOf(newValue));

            if (task[0] != null) {
                task[0].cancel();
            }

            task[0] = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> updateView());
                }
            };

            timer.schedule(task[0], delayTime);
        });

        view.propertiesColorHandlerType.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdatingProperties) {
                return;
            }

            getSelectedGenerator().imageHandler.colorHandler = newValue.toColorHandler();

            updateView();
        });
    }

    public void updateView() {
        updateTitle();
        updateMenu();
        updateProperties();
        updatePreview(false);
    }

    public void updateTitle() {
        if (FGen.get().saveFilepath != null) {
            Application.get().getCurrentStage().setTitle("FGen (Fractal Generator) - " + Paths.get(FGen.get().saveFilepath).toAbsolutePath());
        } else {
            Application.get().getCurrentStage().setTitle("FGen (Fractal Generator)");
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
        isUpdatingProperties = true;

        if (getSelectedGenerator() == null) {
            view.propertiesTab.setContent(view.propertiesEmptyBox);
        } else {
            if (view.propertiesTab.getContent() != view.propertiesScrollPane) {
                view.propertiesTab.setContent(view.propertiesScrollPane);
            }

            view.propertiesGeneratorType.setValue(AvailableGenerators.fromGenerator(getSelectedGenerator()));

            String path = getSelectedGenerator() instanceof PngGenerator ? ((PngGenerator)getSelectedGenerator()).path : ((JpegGenerator)getSelectedGenerator()).path;

            if (path.endsWith(".jpeg") && getSelectedGenerator() instanceof PngGenerator) {
                path = path.replace(".jpeg", ".png");
            } else if (path.endsWith(".jpg") && getSelectedGenerator() instanceof PngGenerator) {
                path = path.replace(".jpg", ".png");
            } else if (path.endsWith(".jpg") && getSelectedGenerator() instanceof JpegGenerator) {
                path = path.replace(".png", ".jpeg");
            }

            view.propertiesGeneratorPath.setText(path);

            view.propertiesGeneratorWidth.setText(String.valueOf(getSelectedGenerator().width));
            view.propertiesGeneratorHeight.setText(String.valueOf(getSelectedGenerator().height));
            if (!view.propertiesGeneratorOffsetX.getText().equals(String.valueOf(getSelectedGenerator().offsetX))) {
                view.propertiesGeneratorOffsetX.setText(String.valueOf(getSelectedGenerator().offsetX));
            }
            if (!view.propertiesGeneratorOffsetY.getText().equals(String.valueOf(getSelectedGenerator().offsetY))) {
                view.propertiesGeneratorOffsetY.setText(String.valueOf(getSelectedGenerator().offsetY));
            }
            view.propertiesGeneratorAliasing.setValue(AvailableAliasings.fromAliasingType(getSelectedGenerator().aliasingType));
            view.propertiesFractalType.setValue(AvailableFractals.fromFractal(getSelectedGenerator().calculationHandler.fractal));

            if (view.propertiesFractalType.getValue() == AvailableFractals.JULIA) {
                if (!view.propertiesFractalComplexReal.getText().equals(String.valueOf(((JuliaSet)getSelectedGenerator().calculationHandler.fractal).c.real))) {
                    view.propertiesFractalComplexReal.setText(String.valueOf(((JuliaSet)getSelectedGenerator().calculationHandler.fractal).c.real));
                }
                if  (!view.propertiesBox.getChildren().contains(view.propertiesFractalComplexRealBox)) {
                    view.propertiesBox.getChildren().add(view.propertiesBox.getChildren().indexOf(view.propertiesFractalBox) + 1, view.propertiesFractalComplexRealBox);
                }
                if (!view.propertiesFractalComplexImaginary.getText().equals(String.valueOf(((JuliaSet)getSelectedGenerator().calculationHandler.fractal).c.imaginary))) {
                    view.propertiesFractalComplexImaginary.setText(String.valueOf(((JuliaSet)getSelectedGenerator().calculationHandler.fractal).c.imaginary));
                }
                if  (!view.propertiesBox.getChildren().contains(view.propertiesFractalComplexImaginaryBox)) {
                    view.propertiesBox.getChildren().add(view.propertiesBox.getChildren().indexOf(view.propertiesFractalBox) + 2, view.propertiesFractalComplexImaginaryBox);
                }
                view.propertiesBox.getChildren().remove(view.propertiesFractalPolynomialFunctionBox);
                view.propertiesBox.getChildren().remove(view.propertiesFractalPolynomialFunctionStaticZ0Box);
            } else if (view.propertiesFractalType.getValue() == AvailableFractals.POLYNOMIAL_FUNCTION) {
                if (!view.propertiesFractalPolynomialFunction.getText().equals(String.valueOf(((PolynomialFunction)getSelectedGenerator().calculationHandler.fractal).f))) {
                    view.propertiesFractalPolynomialFunction.setText(((PolynomialFunction)getSelectedGenerator().calculationHandler.fractal).f);
                }
                if  (!view.propertiesBox.getChildren().contains(view.propertiesFractalPolynomialFunctionBox)) {
                    view.propertiesBox.getChildren().add(view.propertiesBox.getChildren().indexOf(view.propertiesFractalBox) + 1, view.propertiesFractalPolynomialFunctionBox);
                }
                if (view.propertiesFractalPolynomialFunctionStaticZ0.isSelected() != ((PolynomialFunction)getSelectedGenerator().calculationHandler.fractal).staticZ0) {
                    view.propertiesFractalPolynomialFunctionStaticZ0.setSelected(((PolynomialFunction)getSelectedGenerator().calculationHandler.fractal).staticZ0);
                }
                if  (!view.propertiesBox.getChildren().contains(view.propertiesFractalPolynomialFunctionStaticZ0Box)) {
                    view.propertiesBox.getChildren().add(view.propertiesBox.getChildren().indexOf(view.propertiesFractalBox) + 2, view.propertiesFractalPolynomialFunctionStaticZ0Box);
                }
                if (!view.propertiesFractalPolynomialFunctionStaticZ0.isSelected()) {
                    if (!view.propertiesFractalComplexReal.getText().equals(String.valueOf(((PolynomialFunction)getSelectedGenerator().calculationHandler.fractal).c.real))) {
                        view.propertiesFractalComplexReal.setText(String.valueOf(((PolynomialFunction)getSelectedGenerator().calculationHandler.fractal).c.real));
                    }
                    if  (!view.propertiesBox.getChildren().contains(view.propertiesFractalComplexRealBox)) {
                        view.propertiesBox.getChildren().add(view.propertiesBox.getChildren().indexOf(view.propertiesFractalBox) + 3, view.propertiesFractalComplexRealBox);
                    }
                    if (!view.propertiesFractalComplexImaginary.getText().equals(String.valueOf(((PolynomialFunction)getSelectedGenerator().calculationHandler.fractal).c.imaginary))) {
                        view.propertiesFractalComplexImaginary.setText(String.valueOf(((PolynomialFunction)getSelectedGenerator().calculationHandler.fractal).c.imaginary));
                    }
                    if  (!view.propertiesBox.getChildren().contains(view.propertiesFractalComplexImaginaryBox)) {
                        view.propertiesBox.getChildren().add(view.propertiesBox.getChildren().indexOf(view.propertiesFractalBox) + 4, view.propertiesFractalComplexImaginaryBox);
                    }
                } else {
                    view.propertiesBox.getChildren().remove(view.propertiesFractalComplexRealBox);
                    view.propertiesBox.getChildren().remove(view.propertiesFractalComplexImaginaryBox);
                }
            } else {
                view.propertiesBox.getChildren().remove(view.propertiesFractalPolynomialFunctionBox);
                view.propertiesBox.getChildren().remove(view.propertiesFractalPolynomialFunctionStaticZ0Box);
                view.propertiesBox.getChildren().remove(view.propertiesFractalComplexRealBox);
                view.propertiesBox.getChildren().remove(view.propertiesFractalComplexImaginaryBox);
            }

            if (!view.propertiesPlaneStartReal.getText().equals(String.valueOf(getSelectedGenerator().calculationHandler.plane.start.real))) {
                view.propertiesPlaneStartReal.setText(String.valueOf(getSelectedGenerator().calculationHandler.plane.start.real));
            }
            if (!view.propertiesPlaneStartImaginary.getText().equals(String.valueOf(getSelectedGenerator().calculationHandler.plane.start.imaginary))) {
                view.propertiesPlaneStartImaginary.setText(String.valueOf(getSelectedGenerator().calculationHandler.plane.start.imaginary));
            }
            if (!view.propertiesPlaneEndReal.getText().equals(String.valueOf(getSelectedGenerator().calculationHandler.plane.end.real))) {
                view.propertiesPlaneEndReal.setText(String.valueOf(getSelectedGenerator().calculationHandler.plane.end.real));
            }
            if (!view.propertiesPlaneEndImaginary.getText().equals(String.valueOf(getSelectedGenerator().calculationHandler.plane.end.imaginary))) {
                view.propertiesPlaneEndImaginary.setText(String.valueOf(getSelectedGenerator().calculationHandler.plane.end.imaginary));
            }
            if (!view.propertiesPlaneStep.getText().equals(String.valueOf(getSelectedGenerator().calculationHandler.plane.step))) {
                view.propertiesPlaneStep.setText(String.valueOf(getSelectedGenerator().calculationHandler.plane.step));
            }

            view.propertiesCalculationHandlerType.setValue(AvailableCalculationHandlers.fromCalculationHandlers(getSelectedGenerator().calculationHandler));
            if (!view.propertiesCalculationHandlerMaxIterations.getText().equals(String.valueOf(getSelectedGenerator().calculationHandler.maxIterations))) {
                view.propertiesCalculationHandlerMaxIterations.setText(String.valueOf(getSelectedGenerator().calculationHandler.maxIterations));
            }
            if (!view.propertiesCalculationHandlerRadius.getText().equals(String.valueOf(getSelectedGenerator().calculationHandler.radius))) {
                view.propertiesCalculationHandlerRadius.setText(String.valueOf(getSelectedGenerator().calculationHandler.radius));
            }

            if (view.propertiesCalculationHandlerType.getValue() == AvailableCalculationHandlers.POOL) {
                if (!view.propertiesParallelismLevel.getText().equals(String.valueOf(((PoolCalculationHandler)getSelectedGenerator().calculationHandler).getParallelismLevel()))) {
                    view.propertiesParallelismLevel.setText(String.valueOf(((PoolCalculationHandler)getSelectedGenerator().calculationHandler).getParallelismLevel()));
                }
                if  (!view.propertiesBox.getChildren().contains(view.propertiesParallelismLevelBox)) {
                    view.propertiesBox.getChildren().add(view.propertiesBox.getChildren().indexOf(view.propertiesCalculationHandlerRadiusBox) + 1, view.propertiesParallelismLevelBox);
                }
                if (!view.propertiesParallelismThreshold.getText().equals(String.valueOf(((PoolCalculationHandler)getSelectedGenerator().calculationHandler).getParallelismThreshold()))) {
                    view.propertiesParallelismThreshold.setText(String.valueOf(((PoolCalculationHandler)getSelectedGenerator().calculationHandler).getParallelismThreshold()));
                }
                if  (!view.propertiesBox.getChildren().contains(view.propertiesParallelismThresholdBox)) {
                    view.propertiesBox.getChildren().add(view.propertiesBox.getChildren().indexOf(view.propertiesCalculationHandlerRadiusBox) + 2, view.propertiesParallelismThresholdBox);
                }
            } else {
                view.propertiesBox.getChildren().remove(view.propertiesParallelismLevelBox);
                view.propertiesBox.getChildren().remove(view.propertiesParallelismThresholdBox);
            }

            view.propertiesColorHandlerType.setValue(AvailableColorHandlers.fromColorHandler(getSelectedGenerator().imageHandler.colorHandler));
        }

        isUpdatingProperties = false;
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
            view.fractalPreviewImageView.setImage(null);
            view.previewTab.setContent(view.previewTabEmptyBox);
        } else {
            view.fractalPreviewImageView.setImage(previewImage);
            view.previewTab.setContent(view.fractalPreviewImageView);
        }
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
        Configuration configuration = Json.deserializeFromResources("/presets/gui/gui_new.json", Configuration.class);
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
