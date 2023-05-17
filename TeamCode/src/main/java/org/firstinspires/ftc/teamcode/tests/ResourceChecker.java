package org.firstinspires.ftc.teamcode.tests;

import android.content.Context;
import android.content.res.Resources;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@TeleOp
public class ResourceChecker extends LinearOpMode {
    @Override
    public void runOpMode() {
        final String resId = "lower";
        Context ctx = hardwareMap.appContext;
        try {
            String[] l = ctx.getAssets().list("gamepadyn/");
            for (String e : l) {
                Logger.getAnonymousLogger().log(Level.INFO, "Path: " + e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
