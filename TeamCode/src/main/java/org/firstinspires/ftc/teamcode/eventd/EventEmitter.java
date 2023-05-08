package org.firstinspires.ftc.teamcode.eventd;

import android.util.Log;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public final class EventEmitter<T> {
    @FunctionalInterface
    public interface Listener<T> {
        default void run() { }
        void run(T data);
    }

    public void on(Listener<T> f) {
        listeners.add(f);
    }
    public void off(Listener<T> f) { listeners.remove(f); }

    public void emit(T data) {
        Log.d("eventd", "Emit event, remove me later");
        for (Listener<T> listener : listeners) listener.run(data);
        if (baseListeners != null) for (Listener<T> listener : baseListeners) listener.run(data);
    }

    private final ArrayList<Listener<T>> listeners = new ArrayList<>();
    private final Listener<T>[] baseListeners;
    public EventEmitter() { baseListeners = null; }
    @SafeVarargs
    public EventEmitter(@NonNull Listener<T>... bases) throws ClassCastException {
        for (Listener<T> b : bases) //noinspection ConstantConditions
            if (!(b instanceof Listener)) throw new ClassCastException();
        baseListeners = bases;
    }
}
