package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions.LINEAR_SLIDE_BOTTOM;
import static org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions.LINEAR_SLIDE_HIGH;
import static org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions.LINEAR_SLIDE_LOW;
import static org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions.LINEAR_SLIDE_MIDDLE;
import static org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions.LINEAR_SLIDE_TOP;
import static java.lang.Math.abs;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.gamepadyn.Gamepad;
import org.firstinspires.ftc.teamcode.gamepadyn.Gamepadyn;
import org.firstinspires.ftc.teamcode.lsd.LSD;

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 */
@TeleOp()
public class RobohawksTeleOp extends OpMode {
    RobohawksMecanumDrive drive = new RobohawksMecanumDrive(hardwareMap);
    RobohawksMecanumDrive LinearPosition = new RobohawksMecanumDrive(hardwareMap);

    /**
     * User defined init method
     * <p>
     * This method will be called once when the INIT button is pressed.
     */
    @Override
    public void init() {

        // collect garbage
        System.gc();

        LSD.opmodeInit(hardwareMap, this);
        Gamepadyn.opmodeInit(hardwareMap, this);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Gamepad gamepad0 = Gamepadyn.getGamepad(0);
        // callbacks
        gamepad0.action(LINEAR_SLIDE_TOP    ).emitter.on(d -> LSD.setPosition(LSD.SlideHeight.TOP));
        gamepad0.action(LINEAR_SLIDE_HIGH   ).emitter.on(d -> LSD.setPosition(LSD.SlideHeight.HIGH));
        gamepad0.action(LINEAR_SLIDE_MIDDLE ).emitter.on(d -> LSD.setPosition(LSD.SlideHeight.MIDDLE));
        gamepad0.action(LINEAR_SLIDE_LOW    ).emitter.on(d -> LSD.setPosition(LSD.SlideHeight.LOW));
        gamepad0.action(LINEAR_SLIDE_BOTTOM ).emitter.on(d -> LSD.setPosition(LSD.SlideHeight.BOTTOM));
    }

    public void start() {};

    /**
     * User defined loop method
     * <p>
     * This method will be called repeatedly in a loop while this op mode is running
     */
    @Override
    public void loop() {
        drive.setWeightedDrivePower(
                new Pose2d(
                        -(gamepad1.right_stick_y >= 0 ? 1 : -1) * Math.pow(abs((double)gamepad1.right_stick_y),1.7)+(abs(gamepad1.right_stick_y) > .05 ? (gamepad1.right_stick_y >= 0 ? .05 : -.05) : 0), // These lines translate the raw code from the sticks into
                        -(gamepad1.right_stick_x >= 0 ? 1 : -1) * Math.pow(abs((double)gamepad1.right_stick_x),1.7)+(abs(gamepad1.right_stick_x) > .05 ? (gamepad1.right_stick_x >= 0 ? .05 : -.05) : 0),   //  a curved +/- input for the motors. It sends the values to a
                        -(gamepad1.left_stick_x >= 0 ? 1 : -1) * Math.pow(abs((double)gamepad1.left_stick_x),1.7)+(abs(gamepad1.left_stick_x) > .05 ? (gamepad1.left_stick_x >= 0 ? .05 : -.05) : 0)       // function in roadrunner.
                )
        );

//            if (gamepad1.left_trigger >= .05 || gamepad1.right_trigger >=.05){
//                drive.setLinearSlide(gamepad1.left_trigger);
//                if (gamepad1.left_trigger >= .05){
//                    drive.setLinearSlide(gamepad1.left_trigger);
//                }
//                else drive.setLinearSlide(-gamepad1.right_trigger);
//            } else drive.setLinearSlide(0);


        //drive.moveTestServo(.5);

        //drive.LinearSlideToStop(1,25,10); //low pole
        // drive.LinearSlideToStop(2,25,10); //mid pole
        if (gamepad1.dpad_up) {
            drive.moveTestServo(.65);
            //drive.LinearSlideToStop(0,25,50);
        }
        // drive.LinearSlideToStop(3,25,10); //high pole
        if (gamepad1.dpad_down) {
            drive.moveTestServo(0);
            //drive.LinearSlideToStop(3,25,50);        //drive.LinearSlideToStop(4,25,10); //bottom
        }

        drive.update();

        telemetry.update();

        Pose2d poseEstimate = drive.getPoseEstimate();
        telemetry.addData("x", poseEstimate.getX());
        telemetry.addData("y", poseEstimate.getY());
        telemetry.addData("heading", poseEstimate.getHeading());
        telemetry.addData("leftX", gamepad1.left_stick_x);
        telemetry.addData("leftY", gamepad1.left_stick_y);
        telemetry.addData("rightX", gamepad1.right_stick_x);
        telemetry.addData("RawLsPos", LSD.currentHeightPrecise());

        telemetry.update();
    }
}
