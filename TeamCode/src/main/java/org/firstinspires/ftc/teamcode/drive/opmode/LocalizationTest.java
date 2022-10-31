package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 */
@TeleOp(group = "drive")
public class LocalizationTest extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        SampleMecanumDrive LinearPosition = new SampleMecanumDrive(hardwareMap);

        LinearPosition.LinearSlideResetEnc();

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        while (!isStopRequested()) {
            drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y,
                            -gamepad1.left_stick_x,
                            -gamepad1.right_stick_x
                    )
            );

            if (gamepad1.left_trigger >= .05 || gamepad1.right_trigger >=.05){
                drive.setLinearSlide(gamepad1.left_trigger);
                if (gamepad1.left_trigger >= .05){
                    drive.setLinearSlide(gamepad1.left_trigger);
                }
                else drive.setLinearSlide(-gamepad1.right_trigger);
            }
            else drive.setLinearSlide(0);


            //drive.moveTestServo(.5);

            if(gamepad1.left_bumper) {
                drive.penetrate(0);
            }//drive.LinearSlideToStop(1,25,10); //low pole
            if(gamepad1.right_bumper) {
                drive.penetrate(.525);
            }//drive.LinearSlideToStop(2,25,10); //mid pole
            if(gamepad1.b){
                drive.moveTestServo(.65);
                //drive.LinearSlideToStop(0,25,50);
            }
            //drive.LinearSlideToStop(3,25,10); //high pole
            if(gamepad1.y) {
                drive.moveTestServo(0);
                //drive.LinearSlideToStop(3,25,50);        //drive.LinearSlideToStop(4,25,10); //bottom
            }



            drive.update();


            telemetry.update();

            Pose2d poseEstimate = drive.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());

            telemetry.addData("RawLsPos",drive.LinearSlidePos());

            telemetry.update();

        }
    }
}
