package org.firstinspires.ftc.teamcode.Opticon;

import java.util.Set;

public class Event<T> {
    @FunctionalInterface
    public interface Listener {
        void run(T data);
    }

    final void on(Listener f) {

    }

    private Set<Listener> _listeners;
    private final Event() {}
    public final Event() {

    }
}
