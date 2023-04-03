package org.firstinspires.ftc.teamcode.gamepadyn;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class LoadTester extends LinearOpMode {

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
        Gamepadyn.opmodeInit(this);
//        Gamepadyn.getGamepad(0)
        try {
            ConfigLoader.loadConfigurationResource(Gamepadyn.currentOpmode.hardwareMap.appContext, "gamepadyn/upper.json");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        sleep(5000);
        Gamepadyn.cleanup();
    }

}
