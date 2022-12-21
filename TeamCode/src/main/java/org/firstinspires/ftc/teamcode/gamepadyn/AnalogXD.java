package org.firstinspires.ftc.teamcode.gamepadyn;

import androidx.annotation.NonNull;

// Analog input value.
public class AnalogXD {
    public final int axes;
    public final float[] values;
//    @SafeVarargs
    protected AnalogXD(@NonNull float... v) {
        values = v;
        axes = v.length;
    }
}
