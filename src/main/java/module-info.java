module com.ustudents.fgen {
    requires java.desktop;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.swing;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.management;

    exports com.ustudents.fgen;
    exports com.ustudents.fgen.common;
    exports com.ustudents.fgen.common.benchmark;
    exports com.ustudents.fgen.common.json;
    exports com.ustudents.fgen.common.logs;
    exports com.ustudents.fgen.common.options;
    exports com.ustudents.fgen.common.utils;
    exports com.ustudents.fgen.fractals;
    exports com.ustudents.fgen.generators;
    exports com.ustudents.fgen.gui;
    exports com.ustudents.fgen.gui.controller;
    exports com.ustudents.fgen.gui.controls;
    exports com.ustudents.fgen.gui.views;
    exports com.ustudents.fgen.handlers.calculation;
    exports com.ustudents.fgen.handlers.color;
    exports com.ustudents.fgen.handlers.image;
    exports com.ustudents.fgen.maths;
}