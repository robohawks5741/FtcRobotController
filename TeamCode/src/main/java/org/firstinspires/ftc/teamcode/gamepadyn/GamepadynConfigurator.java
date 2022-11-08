package org.firstinspires.ftc.teamcode.gamepadyn;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;

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

//@TeleOp(name = "Gamepad Configuration")
public class GamepadynConfigurator {

    private static GamepadynConfigurator instance;

    public boolean hasInstance() { return (instance != null); }

    @WebHandlerRegistrar
    public static void registerWebHandler(Context context, WebHandlerManager manager) {
        if (instance != null) {
//            TODO
        }
    }
}
