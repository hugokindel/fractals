package com.ustudents.fgen.generators;

import com.ustudents.fgen.common.json.Json;
import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.json.JsonSerializableConstructor;
import com.ustudents.fgen.common.json.JsonSerializableType;
import com.ustudents.fgen.handlers.calculation.CalculationHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonSerializable
@SuppressWarnings("unchecked")
public abstract class ListGenerator extends Generator {
    @JsonSerializable(type = JsonSerializableType.SerializableOnly)
    public List<CalculationHandler> calculationHandlers = new ArrayList<>();

    public void addCalculationHandler(CalculationHandler calculationHandler) {
        calculationHandlers.add(calculationHandler);
    }

    @JsonSerializableConstructor
    public void deserialize(Map<String, Object> elements) {
        for (Map<String, Object> calculationHandlerMap : (List<Map<String, Object>>)elements.get("calculationHandlers")) {
            try {
                Class<CalculationHandler> calculationHandlerClass =
                        (Class<CalculationHandler>)Class.forName("com.ustudents.fgen.handlers.calculation." + calculationHandlerMap.get("class"));
                calculationHandlerMap.remove("class");
                calculationHandlers.add(Json.deserialize(calculationHandlerMap, calculationHandlerClass));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
