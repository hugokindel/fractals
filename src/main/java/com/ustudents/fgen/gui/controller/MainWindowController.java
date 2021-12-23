package com.ustudents.fgen.gui.controller;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.format.Configuration;
import com.ustudents.fgen.gui.views.MainWindow;

public class MainWindowController {
    public MainWindow view = new MainWindow(1024, 720);

    public Configuration model = FGen.get().loadedConfiguration;

    public MainWindowController() {

    }
}
