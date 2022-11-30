package org.firstinspires.ftc.teamcode.gamepadyn;

import androidx.annotation.NonNull;

public class AnalogXD<T extends Number> {
    public final int axes;
    public final T[] values;
    @SafeVarargs
    protected AnalogXD(@NonNull T... v) {
        values = v;
        axes = v.length;
    }
}
