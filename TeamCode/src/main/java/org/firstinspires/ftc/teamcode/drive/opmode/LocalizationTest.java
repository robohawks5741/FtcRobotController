package org.firstinspires.ftc.teamcode.drive.opmode;

import static java.lang.Math.abs;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 */
@TeleOp(group = "drive")
public class LocalizationTest extends LinearOpMode implements localinterface {

    private int bottomStop = 0;//bottom, stop here
    private int lowStop = 1000;
    private int midStop = 1800;
    private int tallStop= 2550;//placeholder value because slide isn't currently tall enough to reach the "tallStop"
    private int tooTall = 2970;//max height
    private int target =     0;//placeholder here, gets used in function LinearSlideToStop()
    private boolean slide = false;
    private int hopStop = 270;
    private boolean off = false;
    private boolean turn = false;
    private boolean down1 = false;

    private DcMotorEx linearSlide, lazySusan;

    @Override
    public boolean LinearSlideToStop2(int stop, int tolerance){

        //linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if(stop == 1){
            target = lowStop;
        }
        else if(stop == 2){
            target = midStop;
        }
        else if(stop == 3){
            target = tallStop;
        }
        else if(stop == 10){
            target = hopStop;
        }

        linearSlide.setTargetPositionTolerance(tolerance);
        linearSlide.setTargetPosition(target);
        linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlide.setPower(.7);

        if(linearSlide.getCurrentPosition()<=target-tolerance||linearSlide.getCurrentPosition()>=target+tolerance)
            return false;
        else
            return true;


    }
    @Override
    public void susanToPosition(int targetPosition) {

        double desiredPosition = targetPosition == 0 ? 0 : targetPosition == 1 ? 1385 : targetPosition == 2 ? 923 : 462;

        //if(linearSlide.getCurrentPosition()+50<256||linearSlide.getCurrentPosition()-50>256 & off == false) {
        //if(!off) {
           LinearSlideToStop2(10, 40);

           // if(linearSlide.getCurrentPosition()+50>256 & linearSlide.getCurrentPosition()-50<256){
                //turn = true; off = true;}

       // }


        //else if(linearSlide.getCurrentPosition()+50>256 & linearSlide.getCurrentPosition()-50<256){
     //   if(turn){

            lazySusan.setTargetPositionTolerance(45);
            lazySusan.setTargetPosition((int) desiredPosition);
            lazySusan.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            lazySusan.setPower(.4);
           // if(lazySusan.getCurrentPosition()+50>desiredPosition & lazySusan.getCurrentPosition()-50<desiredPosition){
            //    down1 = true; turn = false;}
       // }


      //  if(down1) {
            LinearSlideToStop2(0, 20);
           // if(linearSlide.getCurrentPosition()<=20)
               // off = false;
       // }

    }



    @Override
    public void runOpMode() throws InterruptedException {

        lazySusan = hardwareMap.get(DcMotorEx.class,"lazySusan");

        linearSlide = hardwareMap.get(DcMotorEx.class, "linearSlide");


        double speed = 1;

        boolean slideY = false;
        boolean slideX = false;
        boolean slideA = false;
        boolean slideB = false;

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        //drive.ResetSusan();

        SampleMecanumDrive driveB = new SampleMecanumDrive(hardwareMap);

        driveB.LinearSlideResetEnc();

        //drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        drive.holdSusan();
        driveB.ResetSusan();


        waitForStart();


        while (opModeIsActive()) {



            if(gamepad1.left_bumper)
                 speed = .25;
            if(gamepad1.right_bumper)
                speed = .5;
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
                    driveB.setLinearSlide(gamepad2.right_trigger);
                }
                else driveB.setLinearSlide(-gamepad2.left_trigger);
            }
            //else driveB.setLinearSlide(0); //todo PROBLEM


            //drive.holdSlides();

            //drive.moveTestServo(.5);

            if(gamepad2.left_bumper) {
                driveB.penetrate(0);
            }
//drive.LinearSlideToStop(1,25,10); //low pole
            if(gamepad2.right_bumper) {
                driveB.penetrate(.525);
            }
//drive.LinearSlideToStop(2,25,10); //mid pole
            if(gamepad2.right_bumper){
                driveB.moveTestServo(.7);
                //drive.LinearSlideToStop(0,25,50);
            }

            //drive.holdSlides();
            //drive.LinearSlideToStop(3,25,10); //high pole
            if(gamepad2.left_bumper) {
                driveB.moveTestServo(1);
                //drive.LinearSlideToStop(3,25,50);        //drive.LinearSlideToStop(4,25,10); //bottom
            }




            if(gamepad2.y || slideY){
                if(LinearSlideToStop2(3,35))
                //if(driveB.LinearSlideToStop(3,0,25, 35))
                    slideY = false;
                else
                    slideY = true;
            }


            //drive.holdSlides();
            //if(gamepad2.b){
                //drive.LinearSlideToStop(0,1,25);
            //}
            if(gamepad2.x || slideX){
                if(driveB.LinearSlideToStop(1,0,25,35))
                    slideX = false;
                else
                    slideX = true;
            }

            if(gamepad2.a || slideA){
                driveB.LinearSlideToStop(2,0,25,35);

            }


            //drive.holdSlides();
            if(gamepad2.left_stick_x>=.05 || gamepad2.left_stick_x<=-.05){
                drive.MoveSusan(gamepad2.left_stick_x >= .05 ? -Math.pow(gamepad2.left_stick_x, 2)<=-.25 ? -.25 : -Math.pow(gamepad2.left_stick_x, 2) >= -.05 ? -.05 : -Math.pow(gamepad2.left_stick_x, 2) : Math.pow(gamepad2.left_stick_x, 2)>=.25 ? .25 : Math.pow(gamepad2.left_stick_x, 2)<=.05 ? .05 : Math.pow(gamepad2.left_stick_x, 2));
            }

            //else {
             //   drive.MoveSusan(0);
            //}


            //LAZYsUSAN

            if(gamepad2.dpad_up)
                susanToPosition(0);
            else if(gamepad2.dpad_left)
                susanToPosition(1);
            else if(gamepad2.dpad_down)
                susanToPosition(2);
            else if(gamepad2.dpad_right)
                susanToPosition(3);




            //drive.holdSlides();



            drive.update();
            driveB.update();


            telemetry.update();

            Pose2d poseEstimate = drive.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());



            telemetry.addData("leftX",gamepad1.left_stick_x);
            telemetry.addData("leftY",gamepad1.left_stick_y);
            telemetry.addData("rightX",gamepad1.right_stick_x);
            //drive.holdSlides();
            telemetry.addData("RawLsPos",drive.LinearSlidePos());

            telemetry.addData("susan", drive.SusanEncoderPosition());

            telemetry.update();
            //drive.holdSlides();
        }
    }
}
