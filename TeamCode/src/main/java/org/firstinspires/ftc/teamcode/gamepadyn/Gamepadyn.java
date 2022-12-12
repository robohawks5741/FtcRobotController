package org.firstinspires.ftc.teamcode.gamepadyn;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.google.gson.Gson;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.firstinspires.ftc.teamcode.Opticon.*;

// Singleton class, private constructor
public final class Gamepadyn {

    private static Thread inputThread = new Thread(Gamepadyn::inputThreadLoop);

    private static void inputThreadLoop() {
        // TODO: fill this in
    }

    // Singleton constructor. Should never be called.
    private Gamepadyn() throws IllegalAccessException {
        Log.wtf("Gamepadyn", "Singleton constructor called!");
        throw new IllegalAccessException("Singleton constructor called!");
    }

}
