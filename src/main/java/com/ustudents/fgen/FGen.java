package com.ustudents.fgen;

import com.ustudents.fgen.common.Program;
import com.ustudents.fgen.common.options.Command;
import com.ustudents.fgen.common.options.Option;
import com.ustudents.fgen.fractals.*;
import com.ustudents.fgen.generators.JpegGenerator;
import com.ustudents.fgen.generators.MemoryGenerator;
import com.ustudents.fgen.generators.PngGenerator;
import com.ustudents.fgen.handlers.CalculationHandler;
import com.ustudents.fgen.handlers.SingleCalculationHandler;
import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;

@Command(name = "fractals", version = "1.0.0", description = "A tool to generate various fractals.")
public class FGen extends Program {
    @Option(names = {"--view"}, description = "Defines which type of view to use.", usage = "<gui> or <cli>")
    protected static String viewString = "cli";

    @Override
    protected int main(String[] args) {
        if (viewString.equals("gui")) {
            FGenGui.launchFgen(args);
        } else {
            Fractal fractal = new MandelbrotSet(new Complex(0.285, 0.01));
            ComplexPlane plane = new ComplexPlane(new Complex(-1,1), new Complex(1,-1), 0.001);
            CalculationHandler handler = new SingleCalculationHandler(fractal, plane, 1000, 2);
            MemoryGenerator generator = new JpegGenerator(handler, 4096, 4096, "fractal.jpeg");

            generator.generate();
        }

        return 0;
    }

    public static FGen get() {
        return (FGen)Program.get();
    }
}