package org.firstinspires.ftc.teamcode.gamepadyn;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.gamepadyn.user.UserAction;

import java.util.logging.Logger;

@TeleOp
public class LoadTester extends OpMode {

    @Override
    public void init() {
        Logger.getLogger("Gamepadyn").info("init");
        Gamepadyn.opmodeInit(this);
        // Gamepadyn.getGamepad(0)
        try {
            Gamepadyn.getGamepad(0).loadConfigurationResource(this, "gamepadyn/debug.json");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        Gamepadyn.opmodeStart();
        assert Gamepadyn.getGamepad(0) != null;
        Gamepadyn.getGamepad(0).action(UserAction.DEBUG_DIGITAL1).emitter.on((ev) -> SoundPlayer.getInstance().startPlaying(
            hardwareMap.appContext,
            ((boolean)ev ? R.raw.debug_press : R.raw.debug_release),
            new SoundPlayer.PlaySoundParams(),
            null,
            null
        ));
    }

    /**
     * User defined loop method
     * <p>
     * This method will be called repeatedly in a loop while this op mode is running
     */
    @Override
    public void loop() {
    }

    @Override
    public void stop() {
        Gamepadyn.cleanup();
        Logger.getLogger("Gamepadyn").info("Stopping");
    }

}
