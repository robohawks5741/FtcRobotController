package org.firstinspires.ftc.teamcode.gamepadyn;

public final class ActionAnalog {
    public enum Mode {
//        Maps directly onto an X-dimensional action.
        ONE_TO_ONE_AXES,
//        Each axis is mapped onto an analog action's axis, with up to X axis->action maps.
//        Like ONE_TO_ONE_AXES, but you have control over each axis (and can leave axes unmapped)
//        This could be used for having DOOM-like controls (i.e. horizontal axis to rotation, vertical axis to movement).
        SPLIT_AXES,
//        Maps different polar X-dimensional positions (i.e. (0, 1), (-1), (1, 1, 1))
//        onto digital inputs (button is held while analog axis is in position, is released while it isn't).
        HOLD_DIGITAL
    }

    public final int axes;
    public final ActionAnalog.Mode mode;

    ActionAnalog(int a, ActionAnalog.Mode m) {
        axes = a;
        mode = m;
    }
}
