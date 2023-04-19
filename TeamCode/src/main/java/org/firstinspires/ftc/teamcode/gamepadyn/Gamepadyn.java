package org.firstinspires.ftc.teamcode.gamepadyn;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions;
import org.jetbrains.annotations.Contract;

import java.util.Map;

// Singleton class, private constructor
@SuppressWarnings("rawtypes")
public final class Gamepadyn {
    public static void opmodeInit(@NonNull OpMode op) {
//        inputThread = null;
        currentOpmode = op;
//        gamepads = null;
        gamepads = new Gamepad[]{ new Gamepad(0, op.gamepad1), new Gamepad(1, op.gamepad2) };
    }

    public static void opmodeStart() {
        if (currentOpmode != null) throw new NullPointerException("opmodeStart was run without calling opmodeInit");
        isRunning = true;
        inputThread = new Thread(() -> {
            while (isRunning) Gamepadyn.inputThreadLoop();
        });
        inputThread.setUncaughtExceptionHandler(_threadExceptionHandler);
    }

    public static void opmodeStart(OpMode op) {
        if (currentOpmode != null) {
            if (op == null) throw new NullPointerException("opmodeStart was run without calling opmodeInit, and an OpMode was not provided to opmodeStart");
            opmodeInit(op);
        }
        opmodeStart();
    }


    public static void cleanup() {
        isRunning = false;
        inputThread.interrupt();
        currentOpmode = null;
        gamepads = null;
    }

    @NonNull
    @Contract(pure = true)
    public static ActionSource getGamepadAction(int index, @NonNull UserActions ua) {
        if (index < 0 || index > 1) throw new ArrayIndexOutOfBoundsException();
        return gamepads[index].action(ua);
    }

    /**
     * Gets a reference to a gamepad.
     * @param index The 0-based index of the gamepad. 0 is gamepad 1, 1 is gamepad 2.
     * @return a reference to the gamepad
     */
    @Nullable
    @Contract(pure = true)
    public static Gamepad getGamepad(int index) { return gamepads[index]; }

    private static final Thread.UncaughtExceptionHandler _threadExceptionHandler = (Thread t, Throwable e) -> { inputThread = null; };

    private static void inputThreadLoop() {

        // TODO: fill this in
        for (Gamepad gp : gamepads) {
            if (!gp.hasConfiguration()) continue;
            for (Map.Entry<RawGamepadInput, MappingAction> entry : gp.mapping.entrySet()) {
                if (entry == null) continue;
                RawGamepadInput key = entry.getKey();
                switch (key.inputType) {
                    case ANALOG: {
                        MappingActionAnalog value = (MappingActionAnalog) entry.getValue();
                        switch (value.mode) {
                            case ONE_TO_ONE_AXES: {
                                // TODO
                            }
                            case SPLIT_AXES: {
                                // TODO
                            }
                        }
                        break;
                    }
                    case DIGITAL: {
                        MappingActionDigital value = (MappingActionDigital) entry.getValue();
                        switch (value.mode) {
                            case TRIGGER: {
                                // TODO: test this
                                boolean current = RawGamepadInput.getDigitalValueFromGamepad(gp.ftcGamepad, key);
                                boolean last = (boolean) gp.stateCache.get(key);
                                if (current != last) {
                                    gp.action(value.action).internalValue = current;
                                    gp.action(value.action).emitter.emit(current);
                                }
                                break;
                            }
                            case ANALOG_MAP: {
                                // TODO
                            }
                            case ANALOG_OFFSET: {
                                // TODO
                            }
                        }
                        break;
                    }
                }
            }
            gp.updateCache();
        }

    }

    // The worker thread
    private static Thread inputThread;

    // The currently active opmode
    static OpMode currentOpmode;

    // Array of Gamepadyn gamepads
    private static Gamepad[] gamepads;

    static boolean isRunning = false;

    // Singleton constructor. Should never be called.
    private Gamepadyn() throws IllegalAccessException {
        Log.wtf("Gamepadyn", "Singleton constructor called!");
        throw new IllegalAccessException("Singleton constructor called!");
    }

}
