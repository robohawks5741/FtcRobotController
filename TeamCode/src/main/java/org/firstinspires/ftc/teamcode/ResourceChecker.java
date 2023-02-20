package org.firstinspires.ftc.teamcode;

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
    /**
     * Override this method and place your code here.
     * <p>
     * Please do not swallow the InterruptedException, as it is used in cases
     * where the op mode needs to be terminated early.
     *
     * @throws InterruptedException
     */
    @Override
    public void runOpMode() throws InterruptedException {
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
//        try {
//            InputStream ins = ctx.getAssets().open(resId);
////            InputStream ins = hardwareMap.appContext.getResources().raw(resId);
//            int len = ins.available();
//            byte[] buf = new byte[len];
//            int alen = ins.read(buf);
//            String s = new String(buf);
//            telemetry.addData("ResourceChecker", s);
//            ins.close();
//        } catch (Resources.NotFoundException | IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
