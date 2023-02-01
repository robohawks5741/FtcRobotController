package org.firstinspires.ftc.teamcode.gamepadyn;

import android.util.Log;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions;
import org.jetbrains.annotations.Contract;

import java.util.Map;

// Singleton class, private constructor
@SuppressWarnings("rawtypes")
public final class Gamepadyn {

    public static void opmodeInit(@NonNull OpMode op) {
        inputThread = null;
        inputThread = new Thread(Gamepadyn::inputThreadLoop);
        inputThread.setUncaughtExceptionHandler(_threadExceptionHandler);
        currentOpmode = op;
        gamepads = null;
        gamepads = new Gamepad[]{ new Gamepad(0, op.gamepad1), new Gamepad(1, op.gamepad2) };
    }

    public static void cleanup() {
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

    @NonNull
    @Contract(pure = true)
    public static Gamepad getGamepad(int index) {
        return gamepads[index];
    }

    private static final Thread.UncaughtExceptionHandler _threadExceptionHandler = (Thread t, Throwable e) -> { inputThread = null; };

    private static void inputThreadLoop() {

        // TODO: fill this in

        for (Gamepad gp : gamepads) {
            for (Map.Entry<RawGamepadInput, MappingAction> entry : gp.mapping.entrySet()) {
                if (entry == null) continue;
                RawGamepadInput key = entry.getKey();
                // this could be an if statement but I like it better this way
                switch (key.inputType) {
                    case ANALOG: {
                        MappingActionAnalog value = (MappingActionAnalog) entry.getValue();
                        break;
                    }
                    case DIGITAL: {
                        MappingActionDigital value = (MappingActionDigital) entry.getValue();
                        switch (value.mode) {
                            case TRIGGER: {
                                gp.action(value.action).emitter.emit(true);
                            }
                            case ANALOG_MAP: {

                            }
                            case ANALOG_OFFSET: {

                            }
                        }
                        break;
                    }
                }
            }
//            gp.stateCache
        }

    }

    private static Thread inputThread;

    static OpMode currentOpmode;

    private static Gamepad[] gamepads;

    // Singleton constructor. Should never be called.
    private Gamepadyn() throws IllegalAccessException {
        Log.wtf("Gamepadyn", "Singleton constructor called!");
        throw new IllegalAccessException("Singleton constructor called!");
    }

}
