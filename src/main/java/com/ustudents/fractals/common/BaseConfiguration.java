package com.ustudents.fractals.common;

import com.ustudents.fractals.common.json.annotation.JsonSerializable;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonSerializable
public class BaseConfiguration {
    @JsonSerializable(necessary = false)
    public Map<String, Object> global = new LinkedHashMap<>();
}
