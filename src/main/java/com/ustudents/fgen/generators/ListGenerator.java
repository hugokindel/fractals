package com.ustudents.fgen.generators;

import com.ustudents.fgen.handlers.calculation.CalculationHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class ListGenerator extends Generator {
    public List<CalculationHandler> calculationHandlers = new ArrayList<>();

    public void addCalculationHandler(CalculationHandler calculationHandler) {
        calculationHandlers.add(calculationHandler);
    }
}
