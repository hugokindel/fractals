package com.ustudents.fgen.format;

import com.ustudents.fgen.common.json.Json;
import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.json.JsonSerializableConstructor;
import com.ustudents.fgen.common.json.JsonSerializableType;
import com.ustudents.fgen.generators.Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonSerializable
@SuppressWarnings("unchecked")
public class Configuration {
    @JsonSerializable
    public Integer version = 1;

    @JsonSerializable(type = JsonSerializableType.SerializableOnly)
    public List<Generator> generators = new ArrayList<>();

    @JsonSerializableConstructor
    public void deserialize(Map<String, Object> elements) {
        for (Map<String, Object> generatorMap : (List<Map<String, Object>>)elements.get("generators")) {
            try {
                Class<Generator> generatorClass =
                        (Class<Generator>)Class.forName("com.ustudents.fgen.generators." + generatorMap.get("class"));
                generatorMap.remove("class");
                generators.add(Json.deserialize(generatorMap, generatorClass));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
