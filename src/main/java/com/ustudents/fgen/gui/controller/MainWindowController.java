package com.ustudents.fgen.gui.controller;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.common.json.Json;
import com.ustudents.fgen.common.logs.Out;
import com.ustudents.fgen.format.Configuration;
import com.ustudents.fgen.generators.Generator;
import com.ustudents.fgen.generators.JpegGenerator;
import com.ustudents.fgen.generators.PngGenerator;
import com.ustudents.fgen.generators.SingleImageGenerator;
import com.ustudents.fgen.gui.Application;
import com.ustudents.fgen.gui.controls.GeneratorListCell;
import com.ustudents.fgen.gui.views.AboutWindow;
import com.ustudents.fgen.gui.views.MainWindow;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

public class MainWindowController {
    public class PreviewUpdator extends Service<Void> {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    update();

                    return null;
                }
            };
        }

        public synchronized void update() {
            if (showPreview && view.generatorsList.getSelectionModel().getSelectedItem() != null) {
                Platform.runLater(() -> view.statusLabel.setText("Generating..."));

                view.generatorsList.getSelectionModel().getSelectedItem().width = (int)view.previewTabPane.getWidth();
                view.generatorsList.getSelectionModel().getSelectedItem().height = (int)view.previewTabPane.getHeight();
                view.generatorsList.getSelectionModel().getSelectedItem().generate();

                Platform.runLater(() -> {
                    view.fractalPreviewImage = SwingFXUtils.toFXImage(view.generatorsList.getSelectionModel().getSelectedItem().bufferedImage, null);
                    view.fractalPreviewImageView.setImage(view.fractalPreviewImage);
                    view.reloadPreview(view.generatorsList.getSelectionModel().getSelectedItem(), showPreview);
                    view.statusLabel.setText("Ready.");
                });
            }
        }

        public synchronized void sendUpdate() {
            if (service.getState() == State.RUNNING) {
                cancel();
            }

            reset();
            start();
        }
    }

    public MainWindow view = new MainWindow(1280, 720);

    public boolean showPreview = true;

    public PreviewUpdator service = new PreviewUpdator();

    public MainWindowController() {
        view.exportItem.setDisable(true);
        view.closeItem.setDisable(true);
        view.saveItem.setDisable(true);
        view.saveAsItem.setDisable(true);

        if (FGen.get().loadedConfiguration == null || FGen.get().loadedConfiguration.generators.size() == 0) {
            reload(null);
        }

        if (FGen.get().saveFilepath != null) {
            Application.get().getCurrentStage().setTitle("FGen - " + Paths.get(FGen.get().saveFilepath).toAbsolutePath());
        }

        loadModel();

        setupEvents();

        service.start();
    }

    public void setupEvents() {
        view.newItem.setOnAction(event -> {
            clearAll();
            createGenerator();
        });
        view.openItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Configuration Files");
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Configuration Files (*.json)", "*.json");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showOpenDialog(Application.get().getCurrentStage());

                if (file != null) {
                    clearAll();
                    FGen.get().load(file.getAbsolutePath(), true);
                    Application.get().getCurrentStage().setTitle("FGen - " + file.getAbsolutePath());
                    loadModel();
                    reload(getLastGenerator());
                }
            }
        });
        view.saveItem.setOnAction(e -> saveAction());
        view.saveAsItem.setOnAction(e -> saveAsAction());
        view.exportItem.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Export Configuration Files");
            File file = directoryChooser.showDialog(Application.get().getCurrentStage());

            if (file != null) {
                for (Generator generator : view.generatorsList.getItems()) {
                    if (generator instanceof JpegGenerator) {
                        ((JpegGenerator)generator).path = file.getAbsolutePath() + "/" + ((JpegGenerator)generator).path;
                    } else {
                        ((PngGenerator)generator).path = file.getAbsolutePath() + "/" + ((PngGenerator)generator).path;;
                    }
                    generator.generate();
                }
            }
        });
        view.closeItem.setOnAction(event -> {
            clearAll();
            view.exportItem.setDisable(true);
            view.closeItem.setDisable(true);
            view.saveItem.setDisable(true);
            view.saveAsItem.setDisable(true);
        });
        view.quitItem.setOnAction(event -> Application.get().close());

        view.changePreviewItem.setOnAction(event -> {
            showPreview = view.changePreviewItem.isSelected();
            view.reloadPreviewTitle((int)view.previewTabPane.getWidth(), (int)view.previewTabPane.getHeight(), showPreview);
            view.fractalPreviewImageView.setImage(null);
            view.reloadPreview(getLastGenerator(), showPreview);
            service.sendUpdate();
        });

        view.aboutItem.setOnAction(event -> Application.get().showPopup(new AboutWindow(400, 250)));

        view.generatorPlusButton.setOnMouseClicked(event -> createGenerator());

        view.generatorsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> reload((SingleImageGenerator)newValue));
        view.generatorsList.setCellFactory(listView -> {
            GeneratorListCell cell = new GeneratorListCell();
            cell.setOnEdited(event ->  reload(cell.getItem()));

            ContextMenu contextMenu = new ContextMenu();
            cell.setContextMenu(contextMenu);

            MenuItem addItem = new MenuItem("Add Generator");
            addItem.setOnAction(event -> createGenerator());
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
                            view.exportItem.setDisable(true);
                            view.closeItem.setDisable(true);
                            view.saveItem.setDisable(true);
                            view.saveAsItem.setDisable(true);
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

        Application.get().getCurrentStage().widthProperty().addListener((observable, oldValue, newValue) -> view.reloadPreviewTitle((int)view.previewTabPane.getWidth(), (int)view.previewTabPane.getHeight(), showPreview));
        Application.get().getCurrentStage().heightProperty().addListener((observable, oldValue, newValue) -> view.reloadPreviewTitle((int)view.previewTabPane.getWidth(), (int)view.previewTabPane.getHeight(), showPreview));
    }

    private void clearAll() {
        view.generatorsList.getItems().clear();
        Application.get().getCurrentStage().setTitle("FGen");
        reload(null);
    }

    private void createGenerator() {
        Configuration configuration = Json.deserializeFromResources("/presets/gui_new.json", Configuration.class);
        assert configuration != null;
        ((JpegGenerator)configuration.generators.get(0)).path = ((JpegGenerator)configuration.generators.get(0)).path.replace("$", String.valueOf(view.generatorsList.getItems().size()));
        view.generatorsList.getItems().add((SingleImageGenerator)configuration.generators.get(0));
        view.generatorsList.getSelectionModel().select(view.generatorsList.getItems().size() - 1);
        reload(getLastGenerator());
        view.exportItem.setDisable(false);
        view.closeItem.setDisable(false);
        view.saveItem.setDisable(false);
        view.saveAsItem.setDisable(false);
    }

    private void loadModel() {
        for (int i = 0; i < FGen.get().loadedConfiguration.generators.size(); i++) {
            view.generatorsList.getItems().add((SingleImageGenerator)FGen.get().loadedConfiguration.generators.get(i));
        }

        if (view.generatorsList.getItems().size() > 0) {
            view.generatorsList.getSelectionModel().select(0);
            view.exportItem.setDisable(false);
            view.closeItem.setDisable(false);
            view.saveItem.setDisable(false);
            view.saveAsItem.setDisable(false);
        }
    }

    private void reload(SingleImageGenerator generator) {
        view.reloadProperties(generator, showPreview);
        view.reloadPreview(generator, showPreview);
    }

    private SingleImageGenerator getLastGenerator() {
        if (view.generatorsList.getItems().size() == 0) {
            return null;
        }

        return view.generatorsList.getItems().get(view.generatorsList.getItems().size() - 1);
    }

    private void saveAction() {
        if (FGen.get().saveFilepath == null) {
            saveAsAction();
            return;
        }

        Configuration configuration = new Configuration();
        configuration.generators.addAll(view.generatorsList.getItems());
        Json.serialize(FGen.get().saveFilepath, configuration);
    }

    private void saveAsAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Configuration File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Configuration Files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(Application.get().getCurrentStage());

        if (file != null) {
            FGen.get().saveFilepath = file.getAbsolutePath();
            Application.get().getCurrentStage().setTitle("FGen - " + Paths.get(FGen.get().saveFilepath).toAbsolutePath());
            saveAction();
        }
    }
}
