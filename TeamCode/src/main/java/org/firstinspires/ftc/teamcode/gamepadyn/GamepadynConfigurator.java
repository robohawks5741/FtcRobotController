package org.firstinspires.ftc.teamcode.gamepadyn;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.ftccommon.external.WebHandlerRegistrar;
import com.qualcomm.robotcore.util.WebHandlerManager;
import com.qualcomm.robotcore.util.WebServer;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotserver.internal.webserver.MimeTypesUtil;
import org.firstinspires.ftc.robotserver.internal.webserver.AppThemeColors;
import org.firstinspires.ftc.robotserver.internal.webserver.CoreRobotWebServer;

// TODO: fix this
// see https://github.com/acmerobotics/ftc-dashboard/tree/master/FtcDashboard/src/main/java/com/acmerobotics/dashboard

//@TeleOp(name = "Gamepad Configuration")
public class GamepadynConfigurator {

    public boolean hasInstance() { return (_instance != null); }

    @WebHandlerRegistrar
    public static void registerWebHandler(Context context, WebHandlerManager manager) {
        if (_instance == null) {
        } else {
            Log.i("Gamepadyn", "Web handler instance already created");
        }
    }
    private GamepadynConfigurator() { }
    private static GamepadynConfigurator _instance;
}
