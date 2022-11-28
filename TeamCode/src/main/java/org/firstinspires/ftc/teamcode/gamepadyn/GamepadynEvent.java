package org.firstinspires.ftc.teamcode.gamepadyn;

public class AnalogXD<T> {
    final X;
    final T[] axes;
    private AnalogXD(int n) {
        X = n;
        axes = T[n];
    }
}

public class Digital {
    final boolean value;
    private Digital() {
    }
}

public class GamepadynEvent<T> {
    final String type;


    private GamepadynEvent(String t,) {
        type = t;
        return;
    }
}
