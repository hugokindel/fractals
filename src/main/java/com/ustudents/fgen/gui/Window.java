package com.ustudents.fgen.gui;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public abstract class Window extends Scene {
    public Window(double width, double height) {
        super(new StackPane(), width, height);
        initialize();
    }

    public abstract void initialize();
}
