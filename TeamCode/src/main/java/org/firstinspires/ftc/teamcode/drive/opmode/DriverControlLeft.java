package org.firstinspires.ftc.teamcode.drive.opmode;

import static java.lang.Math.abs;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.RobohawksMecanumDrive;

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 */
@TeleOp(group = "drive")
public class DriverControlLeft extends DriverControlSuperOpMode {

    void susanToPosition(int targetPosition) { // TODO cleanup

        int desiredPosition;
        // Sets target encoder position.
        switch (targetPosition) {
            case 0: desiredPosition = 0; break;
            case 1: desiredPosition = -462; break;
            case 2: desiredPosition = -923; break;
            default: desiredPosition = -1385; break;
        }

        if (linearSlide.getCurrentPosition() <= 270) {
            if(!(lazySusan.getCurrentPosition() + 50 > desiredPosition & lazySusan.getCurrentPosition() - 50 < desiredPosition)) {
                LinearSlideToStop2(10, 30);
                down1 = true;
            }
        }


        lazySusan.setTargetPositionTolerance(35);
        lazySusan.setTargetPosition(desiredPosition);
        lazySusan.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lazySusan.setPower(.5);

        if (down1) {
            if (lazySusan.getCurrentPosition() + 50 > desiredPosition & lazySusan.getCurrentPosition() - 50 < desiredPosition & linearSlide.getCurrentPosition() > 250) {
                LinearSlideToStop2(0, 20);
                if(linearSlide.getCurrentPosition()<25) {
                    down1 = false;
                }
            }
        }
    }
}
