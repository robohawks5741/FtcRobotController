package org.firstinspires.ftc.teamcode.gamepadyn;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions;

import java.util.logging.Logger;

@TeleOp
public class LoadTester extends OpMode {
    /**
     * User defined init method
     * <p>
     * This method will be called once when the INIT button is pressed.
     */
    @Override
    public void init() {
        Logger.getLogger("Gamepadyn").info("init");
        Gamepadyn.opmodeInit(this);
        // Gamepadyn.getGamepad(0)
        try {
            ConfigLoader.loadConfigurationResource(this, "gamepadyn/upper.json");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * User defined start method.
     * <p>
     * This method will be called once when the PLAY button is first pressed.
     * This method is optional. By default this method takes not action.
     * Example usage: Starting another thread.
     *
     */
    public void start() {
        Gamepadyn.opmodeStart();
        Gamepadyn.getGamepad(0).action(UserActions.DEBUG).emitter.on((ev) -> {

        });
    }

    /**
     * User defined loop method
     * <p>
     * This method will be called repeatedly in a loop while this op mode is running
     */
    @Override
    public void loop() {

    }

    /**
     * User defined stop method
     * <p>
     * This method will be called when this op mode is first disabled.
     * <p>
     * The stop method is optional. By default this method takes no action.
     */
    @Override
    public void stop() {
        Gamepadyn.cleanup();
        Logger.getLogger("Gamepadyn").info("Stopping");
    }

}
