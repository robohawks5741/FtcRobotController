package org.firstinspires.ftc.teamcode.drive.opmode;

import static java.lang.Math.abs;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
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
public class DriverControl extends LinearOpMode implements localinterface {

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
    private boolean manualSlide = false;
    private boolean manualSusan = false;
    private Servo clawServo;

    private DcMotorEx linearSlide, lazySusan;

    @Override
    public boolean LinearSlideToStop2(int stop, int tolerance){

        // These if statements set the target location for the slide based on user input.

        if(stop == 3)
            target = tallStop;

        else if(stop == 0)
            target = bottomStop;

        else if(stop == 10)
            target = hopStop;

        else if(stop == 2)
            target = midStop;

        else if(stop == 1)
            target = lowStop;


        linearSlide.setTargetPositionTolerance(tolerance); // This actually moves the motors.
        linearSlide.setTargetPosition(target);
        linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlide.setPower(.7);


        if(linearSlide.getCurrentPosition()<=target-tolerance||linearSlide.getCurrentPosition()>=target+tolerance) // This is how the program knows if it needs to call the function in the next loop to complete the move.
            return false;
        else
            return true;


    }

    @Override
    public void susanToPosition(int targetPosition) { //TODO cleanup

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

        // Declare the used non-drive motors.

        lazySusan = hardwareMap.get(DcMotorEx.class,"lazySusan");

        linearSlide = hardwareMap.get(DcMotorEx.class, "linearSlide");

        clawServo = hardwareMap.get(Servo.class, "clawServo");


        double speed = 1; //Speed multiplier for the drivetrain.

        boolean slideY = false; //state of movement (yes or no) for different target slide positions.
        boolean slideX = false;
        boolean slideA = false;
        boolean slideB = false;

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap); //Instance of SampleMecanum drive to access methods in the file.

        linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //Start encoders at position 0.
        lazySusan.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lazySusan.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //Sets HALO to brake mode.



        waitForStart();


        while (opModeIsActive()) { //!!Main loop!!


            if(gamepad1.left_bumper) //Change drive speed coefficient.
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


            if (gamepad2.left_trigger >= .05 || gamepad2.right_trigger >=.05){  //manual LinearSlide controls via triggers.

                manualSlide = true; //Reports that slide is operating manually, not via a macro.

                if (gamepad2.right_trigger >= .05)
                    linearSlide.setPower(gamepad2.right_trigger);
                else
                    linearSlide.setPower(-gamepad2.left_trigger);
            }
            else if(manualSlide) { //Tests to see if its operating via a macro, doesn't interrupt macro if the test is positive.

                linearSlide.setPower(0); //todo PROBLEM (might be resolved)
                manualSlide = false;

            }


            if(gamepad2.right_bumper) //Operate end effector (claw)
                clawServo.setPosition(.7);

            if(gamepad2.left_bumper)
                clawServo.setPosition(1);


            //Following if statements operate LinearSlide macros.


            if(gamepad2.y || slideY){
                if(LinearSlideToStop2(3,35))

                    slideY = false;
                else
                    slideY = true;
            }

            else if(gamepad2.b || slideB){
                if(LinearSlideToStop2(0,35))

                    slideB = false;
                else
                    slideB = true;
            }

            else if(gamepad2.x || slideX){
                if(LinearSlideToStop2(1,35))

                    slideX = false;
                else
                    slideX = true;
            }

            else if(gamepad2.a || slideA){
                if(LinearSlideToStop2(2,35))

                    slideA = false;
                else
                    slideA = true;
            }


            // Code for manual operation of HALO device, the big long line with lots of "? :" statements is to curve the stick input to a controllable amount.

            if(gamepad2.left_stick_x>=.05 || gamepad2.left_stick_x<=-.05){
                lazySusan.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                lazySusan.setPower(speed);

                manualSusan = true; // Reports that LazySusan is operating manually, and requires stopping.

                lazySusan.setPower(gamepad2.left_stick_x >= .05 ? -Math.pow(gamepad2.left_stick_x, 2)<=-.25 ? -.25 : -Math.pow(gamepad2.left_stick_x, 2) >= -.05 ? -.05 : -Math.pow(gamepad2.left_stick_x, 2) : Math.pow(gamepad2.left_stick_x, 2)>=.25 ? .25 : Math.pow(gamepad2.left_stick_x, 2)<=.05 ? .05 : Math.pow(gamepad2.left_stick_x, 2));
            }
            else if(manualSusan){ // If Susan was moved manually, stop when no longer receiving input.

                lazySusan.setPower(0);
                manualSusan = false;
            }


            // Macros for LazySusan positions.

            if(gamepad2.dpad_up)
                susanToPosition(0);
            else if(gamepad2.dpad_left)
                susanToPosition(1);
            else if(gamepad2.dpad_down)
                susanToPosition(2);
            else if(gamepad2.dpad_right)
                susanToPosition(3);


            //UPDATE stuff

            drive.update();
            telemetry.update();


            // Telemetry for localization data.

            Pose2d poseEstimate = drive.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());

            // Telemetry for gamepad input.

            telemetry.addData("leftX",gamepad1.left_stick_x);
            telemetry.addData("leftY",gamepad1.left_stick_y);
            telemetry.addData("rightX",gamepad1.right_stick_x);

            // Telemetry for LazySusan and LinearSlide positions/

            telemetry.addData("RawLsPos",linearSlide.getCurrentPosition());
            telemetry.addData("susan", lazySusan.getCurrentPosition());

            telemetry.update();
        }
    }
}
