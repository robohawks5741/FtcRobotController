package org.firstinspires.ftc.teamcode.gamepadyn;

import androidx.annotation.NonNull;

public class GamepadynEvent<T> {
    public final String action;
    public final T data;
    public final Class<T> type;
    @SuppressWarnings("unchecked")
    protected GamepadynEvent(String a, T d) {
        action = a;
        data = d;
        type = ((Class<T>) d.getClass());
    }
}
