package org.firstinspires.ftc.teamcode.drive.opmode;

import static java.lang.Math.abs;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@TeleOp(group = "drive")
public class DriverControl extends LinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException {

        double speed = 1;

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);




        waitForStart();


        while (opModeIsActive()) {
            if(gamepad1.right_bumper)
                speed = .5;
            if(gamepad1.left_bumper)
                speed = 1;
            drive.setWeightedDrivePower(
                    new Pose2d(

                            -(gamepad1.right_stick_y>=0 ? 1 : -1) * Math.pow(abs((double)gamepad1.right_stick_y),1.7)*(speed)+(abs(gamepad1.right_stick_y) > .05 ? (gamepad1.right_stick_y >= 0 ? .05 : -.05) : 0), // These lines translate the raw code from the sticks into
                            -(gamepad1.right_stick_x>=0 ? 1 : -1) * Math.pow(abs((double)gamepad1.right_stick_x),1.7)*(speed)+(abs(gamepad1.right_stick_x) > .05 ? (gamepad1.right_stick_x >= 0 ? .05 : -.05) : 0),   //  a curved +/- input for the motors. It sends the values to a
                            (gamepad1.left_stick_x>=0 ? -1 : 1) * Math.pow(abs((double)gamepad1.left_stick_x),1.7)*(speed)+(abs(gamepad1.left_stick_x) > .05 ? (gamepad1.left_stick_x >= 0 ? .05 : -.05) : 0)       // function in roadrunner.
                    )
            );

            if (gamepad2.left_trigger >= .05 || gamepad2.right_trigger >=.05){
                //drive.setLinearSlide(gamepad1.left_trigger);
                if (gamepad2.right_trigger >= .05){
                    drive.setLinearSlide(gamepad2.right_trigger);
                }
                else drive.setLinearSlide(-gamepad2.left_trigger);
            }
            else drive.setLinearSlide(0);


            //drive.moveTestServo(.5);

            if(gamepad2.left_bumper) {
                drive.penetrate(0);
            }//drive.LinearSlideToStop(1,25,10); //low pole
            if(gamepad2.right_bumper) {
                drive.penetrate(.525);
            }//drive.LinearSlideToStop(2,25,10); //mid pole
            if(gamepad2.dpad_up){
                drive.moveTestServo(.7);
                //drive.LinearSlideToStop(0,25,50);
            }
            //drive.LinearSlideToStop(3,25,10); //high pole
            if(gamepad2.dpad_down) {
                drive.moveTestServo(1);
                //drive.LinearSlideToStop(3,25,50);        //drive.LinearSlideToStop(4,25,10); //bottom
            }
            if(gamepad2.y){
                drive.LinearSlideToStop(3,0,25, 35);
            }
            //if(gamepad2.b){
            //drive.LinearSlideToStop(0,1,25);
            //}
            if(gamepad2.x){
                drive.LinearSlideToStop(1,0,25,35);
            }
            if(gamepad2.a){
                drive.LinearSlideToStop(2,0,25,35);
            }
            if(gamepad2.dpad_left){
                drive.MoveSusan(.2);
            }
            if(gamepad2.dpad_right){
                drive.MoveSusan(-.2);
            }
            if(!gamepad2.dpad_left  && !gamepad2.dpad_right){
                drive.MoveSusan(0);
            }





            drive.update();


            telemetry.update();

            Pose2d poseEstimate = drive.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());

            telemetry.addData("leftX",gamepad1.left_stick_x);
            telemetry.addData("leftY",gamepad1.left_stick_y);
            telemetry.addData("rightX",gamepad1.right_stick_x);

            telemetry.addData("RawLsPos",drive.LinearSlidePos());

            telemetry.update();

        }
    }
}
