package org.firstinspires.ftc.teamcode.drive.opmode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

// Subclassed OpMode (Driver Control Left)
@TeleOp(group = "drive")
public class DriverControlLeft extends DriverControlSuperOpMode {

    @Override
    void setAbstractConstants() {
        // front is always 0
        haloConstantBack  = -923;
        haloConstantLeft  = -1385;
        haloConstantRight = -462;
    }

}
