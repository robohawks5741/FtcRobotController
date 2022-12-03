package org.firstinspires.ftc.teamcode.gamepadyn;

import androidx.annotation.NonNull;

// Used to be a template class, now is hardcoded to float
public class AnalogXD/*<T extends Number>*/ {
    public final int axes;
    public final float[] values;
//    @SafeVarargs
    protected AnalogXD(@NonNull float... v) {
        values = v;
        axes = v.length;
    }
}
