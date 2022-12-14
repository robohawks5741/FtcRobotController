package org.firstinspires.ftc.teamcode.gamepadyn;

import android.content.Context;
import android.util.Log;

import org.firstinspires.ftc.ftccommon.external.WebHandlerRegistrar;
import com.qualcomm.robotcore.util.WebHandlerManager;

// TODO: fix this
// see https://github.com/acmerobotics/ftc-dashboard/tree/master/FtcDashboard/src/main/java/com/acmerobotics/dashboard

//@TeleOp(name = "Gamepad Configuration")
public class Configurator {

    public boolean hasInstance() { return (_instance != null); }

    @WebHandlerRegistrar
    public static void registerWebHandler(Context context, WebHandlerManager manager) {
        if (_instance == null) {
        } else {
            Log.i("Gamepadyn", "Web handler instance already created");
        }
    }
    private Configurator() { }
    private static Configurator _instance;
}
