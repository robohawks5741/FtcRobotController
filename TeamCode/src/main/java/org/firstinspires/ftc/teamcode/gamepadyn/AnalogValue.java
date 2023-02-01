package org.firstinspires.ftc.teamcode.gamepadyn;

import androidx.annotation.NonNull;

// Analog input value.
public class AnalogValue {
    public final int axes;
    public final float[] values;
//    @SafeVarargs
    public AnalogValue(@NonNull float... v) {
        values = v;
        axes = v.length;
    }
}
