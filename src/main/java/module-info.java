module com.ustudents.fgen {
    requires java.desktop;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.management;

    exports com.ustudents.fgen;
    exports com.ustudents.fgen.gui;
    exports com.ustudents.fgen.format;
}