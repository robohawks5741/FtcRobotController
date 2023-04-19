package org.firstinspires.ftc.teamcode.gamepadyn;

@SuppressWarnings("rawtypes")
public enum InputType {
//        Don't change this.
        DIGITAL (boolean.class),
        ANALOG  (AnalogValue.class);
        public final Class inputClass;

        InputType(Class c) { this.inputClass = c; }
}