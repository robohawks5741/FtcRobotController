package org.firstinspires.ftc.teamcode.gamepadyn;

import androidx.annotation.NonNull;
import org.firstinspires.ftc.teamcode.Opticon.Event;

public final class GamepadynEvent<T> {
    public final String action;
    public final T data;
    private GamepadynEvent(String a, @NonNull T d) {
        action = a;
        data = d;
    }
}