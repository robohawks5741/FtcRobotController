package org.firstinspires.ftc.teamcode.gamepadyn;

public final class ActionDigital {
    public enum Mode {
//        Trigger a digital action.
        TRIGGER,
//        Directly set the value of a digital
        ANALOG_MAP,
//        Offset the value of a digital axis by a specified value.
//        The offset is applied in only one direction, and is clamped to -1, 1.
        ANALOG_OFFSET
    }

    public final int axes;
    public final ActionDigital.Mode mode;

    ActionDigital(int a, ActionDigital.Mode m) {
        axes = a;
        mode = m;
    }
}
