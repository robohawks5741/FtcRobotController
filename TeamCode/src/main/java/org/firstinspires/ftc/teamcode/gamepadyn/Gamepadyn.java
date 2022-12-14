package org.firstinspires.ftc.teamcode.gamepadyn;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions;
import org.jetbrains.annotations.Contract;

// Singleton class, private constructor
public final class Gamepadyn {

    public static void opmodeInit(@NonNull HardwareMap hmap, @NonNull OpMode op) {
        _inputThread = new Thread(Gamepadyn::_inputThreadLoop);
        _inputThread.setUncaughtExceptionHandler(_threadExceptionHandler);
        _currentOpmode = op;

        Gamepad gp[] = new Gamepad[]{ op.gamepad1, op.gamepad2 };
        for (int i = 0; i < 2; i++) {

//            _gamepads[i] = new ActionSource();
        }
    }

    public static void cleanup() {
        _inputThread.interrupt();
        _currentOpmode = null;
    }

    @Nullable
    @Contract(pure = true)
    public static UserActions getGamepad(int index) {
        if (index < 0 || index > 1) throw new ArrayIndexOutOfBoundsException();
        return _gamepads[index];
    }

    private static final Thread.UncaughtExceptionHandler _threadExceptionHandler = (Thread t, Throwable e) -> { _inputThread = null; };

    private static void _inputThreadLoop() {

        // TODO: fill this in
    }

    private static Thread _inputThread;

    private static OpMode _currentOpmode;

    private static UserActions _gamepads[];

    // Singleton constructor. Should never be called.
    private Gamepadyn() throws IllegalAccessException {
        Log.wtf("Gamepadyn", "Singleton constructor called!");
        throw new IllegalAccessException("Singleton constructor called!");
    }

}
