package com.ustudents.fgen.gui.controller;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.common.logs.Out;
import com.ustudents.fgen.format.Configuration;
import com.ustudents.fgen.generators.EmptyGenerator;
import com.ustudents.fgen.generators.Generator;
import com.ustudents.fgen.gui.Application;
import com.ustudents.fgen.gui.controls.GeneratorListCell;
import com.ustudents.fgen.gui.views.AboutWindow;
import com.ustudents.fgen.gui.views.MainWindow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class MainWindowController {
    public MainWindow view = new MainWindow(1024, 720);

    public Configuration model = FGen.get().loadedConfiguration;

    public MainWindowController() {
        setupEvents();
    }

    public void setupEvents() {
        view.quitItem.setOnAction(event -> Application.get().close());
        view.aboutItem.setOnAction(event -> Application.get().showPopup(new AboutWindow(400, 300)));

        view.generatorPlusButton.setOnMouseClicked(event -> view.generatorsList.getItems().add(new EmptyGenerator()));

        view.generatorsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> view.reloadProperties(newValue));
        view.generatorsList.setCellFactory(listView -> {
            GeneratorListCell cell = new GeneratorListCell();
            cell.setOnEdited(event -> view.reloadProperties(cell.getItem()));

            ContextMenu contextMenu = new ContextMenu();
            cell.setContextMenu(contextMenu);

            MenuItem addItem = new MenuItem("Add Generator");
            addItem.setOnAction(event -> listView.getItems().add(new EmptyGenerator()));
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
    }
}
