package com.ustudents.fractals;

import com.ustudents.fractals.common.Program;
import com.ustudents.fractals.common.cli.option.annotation.Command;
import com.ustudents.fractals.common.cli.option.annotation.Option;
import com.ustudents.fractals.common.cli.print.Out;
import com.ustudents.fractals.core.View;

@Command(name = "fractals", version = "1.0.0", description = "A tool to generate various fractals.")
public class Fractals extends Program {
    @Option(names = {"--view"}, description = "Defines which type of view to use.", usage = "<gui> or <cli>")
    protected static String viewString = "cli";

    public static Fractals get() {
        return (Fractals)Program.get();
    }

    @Override
    protected int main(String[] args) {
        View view;
        if (viewString.equals("gui")) {
            view = View.Gui;
        } else {
            view = View.Cli;
        }

        Out.println("Hello, World!");
        Out.println(String.format("Current used view: %s", View.values()[view.ordinal()]));

        if (view == View.Gui) {
            FractalsFX.launchGui();
        }

        return 0;
    }
}