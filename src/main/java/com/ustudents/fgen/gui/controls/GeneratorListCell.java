package com.ustudents.fgen.gui.controls;

import com.ustudents.fgen.common.logs.Out;
import com.ustudents.fgen.generators.Generator;
import com.ustudents.fgen.generators.SingleImageGenerator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

// Implementation in part from: https://stackoverflow.com/questions/36436358/javafx-listview-edit-textfieldlistcell
public class GeneratorListCell extends ListCell<SingleImageGenerator> {
    private final TextField textField = new TextField();

    private EventHandler<Event> onEdited = null;

    public GeneratorListCell() {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });

        textField.setOnAction(e -> {
            setContentDisplay(ContentDisplay.TEXT_ONLY);

            getItem().name = textField.getText();
            setText(textField.getText());

            onEdited.handle(new Event(this, null, null));
        });

        setGraphic(textField);
    }

    @Override
    protected void updateItem(SingleImageGenerator generator, boolean empty) {
        super.updateItem(generator, empty);

        if (isEditing()) {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            textField.setText(generator.name);
        } else {
            setContentDisplay(ContentDisplay.TEXT_ONLY);

            if (empty) {
                setText(null);
            } else {
                setText(generator.name);
            }
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        textField.setText(getItem().name);
        textField.requestFocus();
        textField.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setContentDisplay(ContentDisplay.TEXT_ONLY);

        setText(getItem().name);
    }

    public void setOnEdited(EventHandler<Event> eventHandler) {
        onEdited = eventHandler;
    }
}