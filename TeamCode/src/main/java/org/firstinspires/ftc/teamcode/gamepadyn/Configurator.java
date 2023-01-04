package org.firstinspires.ftc.teamcode.gamepadyn;

import android.content.Context;
import android.util.Log;

import org.firstinspires.ftc.ftccommon.external.WebHandlerRegistrar;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.internal.webserver.WebHandler;

import com.qualcomm.ftccommon.FtcRobotControllerService;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.WebHandlerManager;
import com.qualcomm.robotcore.util.WebServer;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

// TODO: fix this
// see https://github.com/acmerobotics/ftc-dashboard/tree/master/FtcDashboard/src/main/java/com/acmerobotics/dashboard

@TeleOp(name = "Gamepad Configurator")
public class Configurator extends OpMode {

    @SuppressWarnings("UnnecessaryLocalVariable")
    static WebHandler webHandler = session -> {
        NanoHTTPD.Response res = NanoHTTPD.newFixedLengthResponse(/*NanoHTTPD.Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT,*/ "Hello from Gamepadyn!");
        return res;
    };

    @WebHandlerRegistrar
    public static void registerWebHandler(Context context, WebHandlerManager manager) {
        WebServer ws = manager.getWebServer();
        if (!ws.wasStarted()) {
            ws.start();
        }
        ws.getWebHandlerManager().register("gamepadyn", webHandler);
    }

    @Override
    public void init() {
//        FtcRobotControllerService.getWebServer();
//        this.hardwareMap.appContext.
//        getActivity()
//        FtcRobotControllerService
//        this.internalOpModeServices
//        this.hardwareMap.appContext
    }

    @Override
    public void loop() { }
}
