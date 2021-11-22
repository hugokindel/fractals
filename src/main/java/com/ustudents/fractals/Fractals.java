package com.ustudents.fractals;

import com.ustudents.fractals.common.Program;
import com.ustudents.fractals.common.cli.option.annotation.Command;
import com.ustudents.fractals.common.cli.print.Out;

@Command(name = "fractals", version = "1.0.0", description = "A tool to generate various fractals.")
public class Fractals extends Program {
    @Override
    protected void main(String[] args) {
        Out.println("Hello, World!");
    }
}
