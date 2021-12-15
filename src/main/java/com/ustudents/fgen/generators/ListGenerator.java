package com.ustudents.fgen.generators;

import com.ustudents.fgen.handlers.CalculationHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class ListGenerator extends Generator {
    public List<CalculationHandler> handlers = new ArrayList<>();

    public void addCalculationHandler(CalculationHandler handler) {
        handlers.add(handler);
    }
}
