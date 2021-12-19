package com.ustudents.fgen;

import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.json.JsonSerializableConstructor;
import com.ustudents.fgen.common.logs.Out;
import com.ustudents.fgen.generators.Generator;

import java.util.Map;

@JsonSerializable
public class Configuration {
    Generator[] generators;

    @JsonSerializableConstructor
    public void constructor(Map<String, Object> elements) {
        Out.print(elements);
    }
}
