package org.firstinspires.ftc.teamcode.gamepadyn;

@SuppressWarnings("rawtypes")
public enum InputType {
//        Try not to add any more.
        DIGITAL   (boolean.class),
        ANALOG_1D (AnalogXD.class, 1),
        ANALOG_2D (AnalogXD.class, 2),
        ANALOG_3D (AnalogXD.class, 3);

        public final Class inputClass;
        public final int axes;

        InputType(Class c) { this.inputClass = c; this.axes = 0; }
        InputType(Class c, int a) { this.inputClass = c; this.axes = a; }
}