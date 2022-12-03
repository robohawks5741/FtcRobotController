package org.firstinspires.ftc.teamcode.Opticon;

import android.util.Log;
import java.util.ArrayList;
public final class Event<T> {
    @FunctionalInterface
    public interface Listener<T> {
        default void run() { run(); }
        void run(T data);
    }

    void on(Listener<T> f) {
        _listeners.add(f);
    }

    public void emit(T data) {
        Log.d("Opticon", "Emit event, remove me later");
        for (Listener<T> listener : _listeners) {
            listener.run(data);
        }
    }

    private ArrayList<Listener<T>> _listeners = new ArrayList<>();
    private Event() { }
}
