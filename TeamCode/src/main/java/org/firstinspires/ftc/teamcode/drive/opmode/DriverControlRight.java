package org.firstinspires.ftc.teamcode.drive.opmode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

// Subclassed OpMode (Driver Control Right)
@TeleOp(group = "drive")
public class DriverControlRight extends DriverControlSuperOpMode {

    @Override
    void setAbstractConstants() {
        // front is always 0
        haloConstantBack  = 923;
        haloConstantLeft  = 462;
        haloConstantRight = 1385;
    }

}
