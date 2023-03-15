package org.firstinspires.ftc.teamcode.drive.opmode;

import static java.lang.Math.abs;

import androidx.annotation.Nullable;

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

    protected int target = 0;//placeholder here, gets used in function LinearSlideToStop()
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
    };

    protected static DcMotorEx linearSlide, lazySusan;

    protected RobohawksMecanumDrive drive;

    double speed = .5; // Speed multiplier for the drivetrain.

    //state of movement (yes or no) for different target slide positions.
    boolean slideY = false;
    boolean slideX = false;
    boolean slideA = false;
    boolean slideB = false;

   // @Override
   // public void susan90(int input)

    public boolean linearSlideToStop(SlidePosition stop, @Nullable Integer tolerance){

        // These if statements set the target location for the slide based on user input.
        if (tolerance == null) tolerance = 35;
        manualSlide = false;

        target = stop.height;
        linearSlide.setTargetPositionTolerance(tolerance); // This actually moves the motors.
        linearSlide.setTargetPosition(target);
        linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlide.setPower(.85);

        // This is how the program knows if it needs to call the function in the next loop to complete the move.
        return linearSlide.getCurrentPosition() > target - tolerance && linearSlide.getCurrentPosition() < target + tolerance;
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

    public void loop() { //!!Main loop!!

            if (linearSlide.getCurrentPosition() >= 1500) speed = .25;
                // Change drive speed coefficient.
            else if (gamepad1.a) speed = .25;
            else if (gamepad1.b) speed = .5;
            else if (gamepad1.y) speed = .75;

            drive.setWeightedDrivePower(
                    new Pose2d(

                            -(gamepad1.left_stick_y >= 0 ? 1 : -1) * Math.pow(abs((double) gamepad1.left_stick_y), 1.7) * (speed) + (abs(gamepad1.left_stick_y) > .05 ? (gamepad1.left_stick_y >= 0 ? .05 : -.05) : 0), // These lines translate the raw code from the sticks into
                            -(gamepad1.left_stick_x >= 0 ? 1 : -1) * Math.pow(abs((double) gamepad1.left_stick_x), 1.7) * (speed) + (abs(gamepad1.left_stick_x) > .05 ? (gamepad1.left_stick_x >= 0 ? .05 : -.05) : 0),   //  a curved +/- input for the motors. It sends the values to a
                            (gamepad1.right_stick_x >= 0 ? -1 : 1) * Math.pow(abs((double) gamepad1.right_stick_x), 1.7) * (speed) + (abs(gamepad1.right_stick_x) > .05 ? (gamepad1.right_stick_x >= 0 ? .05 : -.05) : 0)       // function in roadrunner.
                    )
            );


            if (gamepad2.left_trigger >= .05 || gamepad2.right_trigger >= .05) {  //manual LinearSlide controls via triggers.
                linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                manualSlide = true; //Reports that slide is operating manually, not via a macro.

                if (gamepad2.right_trigger >= .05)
                    linearSlide.setPower(gamepad2.right_trigger);
                else
                    linearSlide.setPower(-gamepad2.left_trigger);
            } else if (manualSlide) { //Tests to see if its operating via a macro, doesn't interrupt macro if the test is positive.

                linearSlide.setPower(0); //todo PROBLEM (might be resolved)
                manualSlide = false;

            }


            if (gamepad2.right_bumper) //Operate end effector (claw)
                clawServo.setPosition(.5);

            if (gamepad2.left_bumper)
                clawServo.setPosition(.25);


            //Following if statements operate LinearSlide macros.


            if (gamepad2.y || slideY) {
                slideB = false;
                slideA = false;
                slideX = false;
                down1 = false;
                slideY = !linearSlideToStop(SlidePosition.TALL, 35);
            } else if (gamepad2.b || slideB) {
                //lazySusan.setTargetPositionTolerance(50);
                //linearSlide.setTargetPosition(lazySusan.getCurrentPosition()+232 <= 462 ? 0 : lazySusan.getCurrentPosition()+232 <= 923 ? 462 : lazySusan.getCurrentPosition()+232 <= 1385 ? 923 : 1385);
                slideY = false;
                slideA = false;
                slideX = false;
                down1 = false;
                slideB = !linearSlideToStop(SlidePosition.BOTTOM, 35);
            } else if (gamepad2.a || slideX) {
                slideB = false;
                slideA = false;
                slideY = false;
                down1 = false;
                slideX = !linearSlideToStop(SlidePosition.LOW, 35);
            } else if (gamepad2.x || slideA) {
                slideB = false;
                slideY = false;
                slideX = false;
                down1 = false;
                slideA = !linearSlideToStop(SlidePosition.MID, 35);
            }


            // Code for manual operation of HALO device, the big long line with lots of "? :" statements is to curve the stick input to a controllable amount.

            if (gamepad2.left_stick_x >= .05 || gamepad2.left_stick_x <= -.05) {
                lazySusan.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                manualSusan = true; // Reports that LazySusan is operating manually, and requires stopping.

                lazySusan.setPower((gamepad2.left_stick_x >= .05 ? -Math.pow(gamepad2.left_stick_x, 2) <= -.25 ? -.25 : -Math.pow(gamepad2.left_stick_x, 2) >= -.05 ? -.05 : -Math.pow(gamepad2.left_stick_x, 2) : Math.pow(gamepad2.left_stick_x, 2) >= .25 ? .25 : Math.pow(gamepad2.left_stick_x, 2) <= .05 ? .05 : Math.pow(gamepad2.left_stick_x, 2)));
            } else if (manualSusan) { // If Susan was moved manually, stop when no longer receiving input.

                lazySusan.setPower(0);
                manualSusan = false;
            }


            // Macros for LazySusan positions.

            if (gamepad2.dpad_up)
                susanToPosition(0);
            else if (gamepad2.dpad_left)
                susanToPosition(3);
            else if (gamepad2.dpad_down)
                susanToPosition(2);
            else if (gamepad2.dpad_right)
                susanToPosition(1);


            //UPDATE stuff

            drive.update();
            telemetry.update();

            // Telemetry for localization data.

            Pose2d poseEstimate = drive.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());

            // Telemetry for gamepad input.

            telemetry.addData("leftX", gamepad1.left_stick_x);
            telemetry.addData("leftY", gamepad1.left_stick_y);
            telemetry.addData("rightX", gamepad1.right_stick_x);

            // Telemetry for LazySusan and LinearSlide positions/

            telemetry.addData("RawLsPos", linearSlide.getCurrentPosition());
            telemetry.addData("susan", lazySusan.getCurrentPosition());

            telemetry.update();
    }
}
