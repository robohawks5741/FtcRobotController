package org.firstinspires.ftc.teamcode.gamepadyn;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions;
import org.jetbrains.annotations.Contract;

import java.util.Map;

// Singleton class, private constructor
@SuppressWarnings("rawtypes")
public final class Gamepadyn {

    public static void opmodeInit(@NonNull HardwareMap hmap, @NonNull OpMode op) {
        _inputThread = new Thread(Gamepadyn::_inputThreadLoop);
        _inputThread.setUncaughtExceptionHandler(_threadExceptionHandler);
        _currentOpmode = op;

        com.qualcomm.robotcore.hardware.Gamepad[] gp = { op.gamepad1, op.gamepad2 };
        for (int i = 0; i < 2; i++) {

//            _gamepads[i] = new ActionSource();
        }
    }

    public static void cleanup() {
        _inputThread.interrupt();
        _currentOpmode = null;
    }

    public static void loadConfiguration(int gamepadIndex, String cfgName) {

    }

    @NonNull
    @Contract(pure = true)
    public static ActionSource getGamepadAction(int index, @NonNull UserActions ua) {
        if (index < 0 || index > 1) throw new ArrayIndexOutOfBoundsException();
        return _gamepads[index].action(ua);
    }

    @NonNull
    @Contract(pure = true)
    public static Gamepad getGamepad(int index) {
        return _gamepads[index];
    }

    private static final Thread.UncaughtExceptionHandler _threadExceptionHandler = (Thread t, Throwable e) -> { _inputThread = null; };

    private static void _inputThreadLoop() {

        // TODO: fill this in
    }

    private static Thread _inputThread;

    private static OpMode _currentOpmode;

    private static Gamepad _gamepads[];

    // Singleton constructor. Should never be called.
    private Gamepadyn() throws IllegalAccessException {
        Log.wtf("Gamepadyn", "Singleton constructor called!");
        throw new IllegalAccessException("Singleton constructor called!");
    }

}
