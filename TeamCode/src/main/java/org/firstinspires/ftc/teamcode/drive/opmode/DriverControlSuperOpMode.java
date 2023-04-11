package org.firstinspires.ftc.teamcode.drive.opmode;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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
public abstract class DriverControlSuperOpMode extends OpMode {

    protected int slideTarget = 0;//placeholder here, gets used in function LinearSlideToStop()
    protected static int hopStop = 270;
    protected static boolean off = false;
    protected static boolean turn = false;
    protected static boolean down1 = false;
    protected static boolean manualSlide = false;
    protected static boolean manualSusan = false;
    protected static Servo clawServo;


    // LinearSlide positions
    enum SlidePosition {
        BOTTOM (0),
        LOW (1150),
        MID (1950),
        TALL (2700),
        MAX (2970),
        HOP (270),
        INSERT (2500);

        final int height;

        SlidePosition(int i) { height = i; }
    }

//    enum SusanPosition {
//        FRONT (0),
//        RIGHT (1150),
//        BACK (1950),
//        LEFT (1950);
//
//        int position;
//
//        SusanPosition(int i) { position = i; }
//    }

    protected static DcMotorEx linearSlide, lazySusan;

    protected RobohawksMecanumDrive drive;

    double speed = .5; // Speed multiplier for the drivetrain.

    int slideTolerance = 35;
    boolean slideActive = false;
    static double analogInputThreshold = 0.05;

    public void linearSlideToStop(SlidePosition stop, int tolerance) {

        // These if statements set the target location for the slide based on user input.
        manualSlide = false;
        slideActive = true;
        slideTolerance = tolerance;
        slideTarget = stop.height;

        // This is how the program knows if it needs to call the function in the next loop to complete the move.
    }

    /**
     * Different signature of {@link DriverControlSuperOpMode#linearSlideToStop(SlidePosition stop, int tolerance)}
     */
    public void linearSlideToStop(SlidePosition stop) { linearSlideToStop(stop, 35); }

    void linearSlideUpdate() {
        if (slideActive) {
            int cpos = linearSlide.getCurrentPosition();
            int v = abs(slideTarget - cpos);
            if (v >= slideTolerance) {
                linearSlide.setTargetPositionTolerance(slideTolerance); // This actually moves the motors.
                linearSlide.setTargetPosition(slideTarget);
                linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linearSlide.setPower(.85);
            } else slideActive = false;
        }
    }

    abstract void susanToPosition(int targetPosition);

    public void init() {
        // Declare the used non-drive motors.

        lazySusan = hardwareMap.get(DcMotorEx.class, "lazySusan");
        linearSlide = hardwareMap.get(DcMotorEx.class, "linearSlide");
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        drive = new RobohawksMecanumDrive(hardwareMap); //Instance of SampleMecanum drive to access methods in the file.
    }

    @Override
    public void start() {
        linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //Start encoders at position 0.
        lazySusan.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lazySusan.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //Sets HALO to brake mode.
        lazySusan.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void loop() {
            if (linearSlide.getCurrentPosition() >= 1500) speed = .25;
            // Change drive speed coefficient.
            else if (gamepad1.a) speed = .25;
            else if (gamepad1.b) speed = .5;
            else if (gamepad1.y) speed = .75;


            // These lines translate the raw code from the sticks into
            // a curved +/- input for the motors. It sends the values to a
            // function in roadrunner.

            // Desmos (LATeX)
            /*
             * -1\le x\le1
             * g=x\left\{-1\le x\le1\right\}
             * s=0.5
             * t=0.05
             * <NOTE: 0 <= t <= 0.99, step 0.01>
             * p=1.7
             * <NOTE: 0 <= p <= 10>
             * o=\left\{\left|g\right|>t:\operatorname{sign}\left(g\right)t,0\right\}
             * y=s\cdot\operatorname{sign}\left(g\right)\cdot\left|g\right|^{p}+o
             */
            Pose2d drivePower = new Pose2d(
                speed * -signum(gamepad1.left_stick_y ) * Math.pow(abs((double) gamepad1.left_stick_y ), 1.7) + (abs(gamepad1.left_stick_y ) > analogInputThreshold ? signum(gamepad1.left_stick_y ) * analogInputThreshold : 0 ),
                speed * -signum(gamepad1.left_stick_x ) * Math.pow(abs((double) gamepad1.left_stick_x ), 1.7) + (abs(gamepad1.left_stick_x ) > analogInputThreshold ? signum(gamepad1.left_stick_x ) * analogInputThreshold : 0 ),
                speed * -signum(gamepad1.right_stick_x) * Math.pow(abs((double) gamepad1.right_stick_x), 1.7) + (abs(gamepad1.right_stick_x) > analogInputThreshold ? signum(gamepad1.right_stick_x) * analogInputThreshold : 0 )
            );

            drive.setWeightedDrivePower(drivePower);

            if (gamepad2.left_trigger >= analogInputThreshold || gamepad2.right_trigger >= analogInputThreshold) {  // manual LinearSlide controls via triggers.
                linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                manualSlide = true; // Reports that slide is operating manually, not via a macro.


                linearSlide.setPower(((gamepad2.right_trigger >= gamepad2.left_trigger) ? gamepad2.right_trigger : -gamepad2.left_trigger));
            } else if (manualSlide) { //Tests to see if its operating via a macro, doesn't interrupt macro if the test is positive.

                linearSlide.setPower(0); //todo PROBLEM (might be resolved)
                manualSlide = false;

            }

            // Operate end effector (claw)
            if (gamepad2.right_bumper) clawServo.setPosition(.5);
            if (gamepad2.left_bumper ) clawServo.setPosition(.25);


            // Following if statements operate LinearSlide macros.
            // TODO: this will always cause priority issues because changes in state are ignored
            if (gamepad2.y) {
//                down1 = false;
                linearSlideToStop(SlidePosition.TALL);
            }
            if (gamepad2.b) {
//                down1 = false;
                linearSlideToStop(SlidePosition.BOTTOM);
            }
            if (gamepad2.a) {
//                down1 = false;
                linearSlideToStop(SlidePosition.LOW);
            }
            if (gamepad2.x) {
//                down1 = false;
                linearSlideToStop(SlidePosition.MID);
            }

            linearSlideUpdate();
            // Code for manual operation of HALO device, the big long line with lots of "? :" statements is to curve the stick input to a controllable amount.

            if (abs(gamepad2.left_stick_x) >= .05) {
                lazySusan.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                manualSusan = true; // Reports that LazySusan is operating manually, and requires stopping.

                lazySusan.setPower(
                    signum(gamepad2.left_stick_x) *
                    ((Math.pow(gamepad2.left_stick_x, 2) >= .25) ?
                        .25 :
                        Math.max(Math.pow(gamepad2.left_stick_x, 2), .05)
                    )
                );
            } else if (manualSusan) { // If Susan was moved manually, stop when no longer receiving input.

                lazySusan.setPower(0);
                manualSusan = false;
            }


            // Macros for LazySusan positions.

            if (gamepad2.dpad_up)         susanToPosition(0);
            else if (gamepad2.dpad_left)  susanToPosition(3);
            else if (gamepad2.dpad_down)  susanToPosition(2);
            else if (gamepad2.dpad_right) susanToPosition(1);

            if(gamepad2.right_stick_x>=.75) lazySusan.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


            //UPDATE stuff

            drive.update();
            telemetry.update();

            // Telemetry for localization data.

            Pose2d poseEstimate = drive.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());

            // Telemetry for gamepad input.

            telemetry.addData("Gamepad LeftX", gamepad1.left_stick_x);
            telemetry.addData("Gamepad LeftY", gamepad1.left_stick_y);
            telemetry.addData("Gamepad RightX", gamepad1.right_stick_x);

            // Telemetry for LazySusan and LinearSlide positions/

            telemetry.addData("Current Slide Pos", linearSlide.getCurrentPosition());
            telemetry.addData("TargetSlidePos", slideTarget);
            telemetry.addData("Susan Position", lazySusan.getCurrentPosition());

            telemetry.update();
    }
}
