package com.ustudents.fgen.common.utils;

import com.ustudents.fgen.common.json.JsonSerializable;

@JsonSerializable
public enum AliasingType {
    X1(1),
    X2(2),
    X4(4),
    X8(8),
    X16(16);

    public final int multiplicator;

    AliasingType() {
        this.multiplicator = 1;
    }

    AliasingType(final int multiplicator) {
        this.multiplicator = multiplicator;
    }

    public int getMultiplier() {
        return this.multiplicator;
    }
}
