package org.firstinspires.ftc.teamcode.eventd;

import android.util.Log;
import java.util.ArrayList;
public final class EventEmitter<T> {
    @FunctionalInterface
    public interface Listener<T> {
        default void run() { }
        void run(T data);
    }

    public void on(Listener<T> f) {
        _listeners.add(f);
    }
    public void off(Listener<T> f) { _listeners.remove(f); }

    public void emit(T data) {
        Log.d("eventd", "Emit event, remove me later");
        for (Listener<T> listener : _listeners) listener.run(data);
    }

    private final ArrayList<Listener<T>> _listeners = new ArrayList<>();
    public EventEmitter() { }
}
