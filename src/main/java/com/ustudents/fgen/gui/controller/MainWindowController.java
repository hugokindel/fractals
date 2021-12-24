package com.ustudents.fgen.gui.controller;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.common.json.Json;
import com.ustudents.fgen.format.Configuration;
import com.ustudents.fgen.generators.SingleImageGenerator;
import com.ustudents.fgen.gui.Application;
import com.ustudents.fgen.gui.controls.GeneratorListCell;
import com.ustudents.fgen.gui.views.AboutWindow;
import com.ustudents.fgen.gui.views.MainWindow;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class MainWindowController {
    public MainWindow view = new MainWindow(1280, 720);

    public Configuration model = FGen.get().loadedConfiguration;

    public MainWindowController() {
        setupEvents();
    }

    public void setupEvents() {
        view.newItem.setOnAction(event -> {
            clearAll();
            createGenerator();
        });
        view.closeItem.setOnAction(event -> clearAll());
        view.quitItem.setOnAction(event -> Application.get().close());
        view.aboutItem.setOnAction(event -> Application.get().showPopup(new AboutWindow(400, 250)));

        view.generatorPlusButton.setOnMouseClicked(event -> createGenerator());

        view.generatorsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> view.reloadProperties((SingleImageGenerator)newValue));
        view.generatorsList.setCellFactory(listView -> {
            GeneratorListCell cell = new GeneratorListCell();
            cell.setOnEdited(event -> view.reloadProperties((SingleImageGenerator)cell.getItem()));

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
                    deleteItem.setOnAction(event -> listView.getItems().remove(cell.getItem()));
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

        for (int i = 0; i < FGen.get().loadedConfiguration.generators.size(); i++) {
            view.generatorsList.getItems().add(FGen.get().loadedConfiguration.generators.get(i));
        }

        if (view.generatorsList.getItems().size() > 0) {
            view.generatorsList.getSelectionModel().select(0);
        }

        Application.get().getCurrentStage().widthProperty().addListener((observable, oldValue, newValue) -> view.previewTab.setText(String.format("Fractal Preview (%dx%d)", (int)view.previewTabPane.getWidth(), (int)view.previewTabPane.getHeight())));
        Application.get().getCurrentStage().heightProperty().addListener((observable, oldValue, newValue) -> view.previewTab.setText(String.format("Fractal Preview (%dx%d)", (int)view.previewTabPane.getWidth(), (int)view.previewTabPane.getHeight())));
    }

    private void clearAll() {
        model.generators.clear();
        view.generatorsList.getItems().clear();
        view.reloadProperties(null);
    }

    private void createGenerator() {
        Configuration configuration = Json.deserializeFromResources("/presets/sample.json", Configuration.class);
        assert configuration != null;
        view.generatorsList.getItems().add(configuration.generators.get(0));
        view.generatorsList.getSelectionModel().select(view.generatorsList.getItems().size() - 1);
        view.reloadProperties((SingleImageGenerator)view.generatorsList.getItems().get(view.generatorsList.getItems().size() - 1));
    }
}
