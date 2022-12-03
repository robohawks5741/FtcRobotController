package org.firstinspires.ftc.teamcode.gamepadyn;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.google.gson.Gson;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.firstinspires.ftc.teamcode.Opticon.*;

// Singleton class, private constructor
public final class Gamepadyn {

    /* Remnants of old singleton code, might change later (but probably not) */

//    public static boolean has() { return (instance != null); }
///**
// * Returns the Gamepadyn instance that was initialized or {@code null} if Gamepadyn was already initialized (explicitly or implicitly)
// */
//    public static Gamepadyn init() {
//        if (instance != null) {
//            return null;
//        } else {
//            return instance = new Gamepadyn();
//        }
//    }
//
//    public static Gamepadyn get() { return instance; }
//
//    private static Gamepadyn instance = null;

    private static Thread inputThread = null;

    private static void inputThreadLoop() {
//        TODO: fill this in
    }

    static {
        inputThread = new Thread(Gamepadyn::inputThreadLoop);
    }

// Singleton constructor. Should never be called.
    private Gamepadyn() throws Exception {
        Log.wtf("Gamepadyn", "Singleton constructor called!");
        throw new IllegalAccessException("Singleton constructor called!");
    }

}
