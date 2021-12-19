package com.ustudents.fgen.generators;

import com.ustudents.fgen.common.json.Json;
import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.json.JsonSerializableConstructor;
import com.ustudents.fgen.common.json.JsonSerializableType;
import com.ustudents.fgen.common.logs.Out;
import com.ustudents.fgen.handlers.calculation.CalculationHandler;

import java.util.List;
import java.util.Map;

@JsonSerializable
@SuppressWarnings("unchecked")
public abstract class SingleGenerator extends Generator {
    @JsonSerializable(type = JsonSerializableType.SerializableOnly)
    public CalculationHandler calculationHandler = null;

    public SingleGenerator() {

    }

    public SingleGenerator(int width, int height, double offsetX, double offsetY, CalculationHandler calculationHandler) {
        super(width, height, offsetX, offsetY);
        this.calculationHandler = calculationHandler;
    }

    @JsonSerializableConstructor
    public void deserialize(Map<String, Object> elements) {
        Map<String, Object> calculationHandlerMap = (Map<String, Object>)elements.get("calculationHandler");
        try {
            Class<CalculationHandler> calculationHandlerClass =
                    (Class<CalculationHandler>)Class.forName("com.ustudents.fgen.handlers.calculation." + calculationHandlerMap.get("class"));
            calculationHandlerMap.remove("class");
            calculationHandler = Json.deserialize(calculationHandlerMap, calculationHandlerClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
