package org.firstinspires.ftc.teamcode.gamepadyn;

import androidx.annotation.NonNull;

public final class Event<T> {
    public final String action;
    public final T data;
    private Event(String a, @NonNull T d) {
        action = a;
        data = d;
    }
}