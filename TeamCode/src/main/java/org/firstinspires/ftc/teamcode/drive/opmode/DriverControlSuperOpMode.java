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

    /** @see DriverControlSuperOpMode#linearSlideToStop(SlidePositionMacro) */
    protected int slideTarget = 0;
    // what do these do?
    protected static boolean off = false;
    protected static boolean turn = false;
    protected static boolean down1 = false;
    // Whether the slide has been set manually or by a macro
    protected static boolean useSlideManualControls = false;
    // Whether the halo has been set manually or by a macro
    protected static boolean useHaloManualControls = false;
    protected static Servo clawServo;


    // LinearSlide positions
    enum SlidePositionMacro {
        // The lowest possible position for the motor
        BOTTOM (0),
        // Minimum height required to rotate the HALO freely without colliding with the wheels/sides
        HOP (270),
        // Low pole cone deposit position
        LOW (1150),
        // Medium pole cone deposit position
        MID (1950),
        // I don't quite know what this is for
        INSERT (2500),
        // Tall pole cone deposit position
        TALL (2700),
        // Maximum (?) motor encoder tick
        MAX (2970);

        // Encoder Tick
        final int height;

        SlidePositionMacro(int i) { height = i; }
    }

    protected static DcMotorEx linearSlideMotor, haloMotor;

    protected RobohawksMecanumDrive drive;

    double speed = .5; // Speed multiplier for the drivetrain.

    int slideTolerance = 35;
    boolean slideActive = false;
    // Minimum input value required to perform analog actions
    static double analogInputThreshold = 0.05;

    // Move the linear slide
    public void linearSlideToStop(SlidePositionMacro stop, int tolerance) {
        // disable manual operation (this is the macro call)
        useSlideManualControls = false;
        // enable the slide (will be checked on next update)
        slideActive = true;
        // set the motor tolerance
        slideTolerance = tolerance;
        // set the precise slide target using the enum member
        slideTarget = stop.height;
    }

    /**
     * Different signature of {@link DriverControlSuperOpMode#linearSlideToStop(SlidePositionMacro stop, int tolerance)}
     */
    public void linearSlideToStop(SlidePositionMacro stop) { linearSlideToStop(stop, 35); }

    // Updates the linear slide movement
    void linearSlideUpdate() {
        // If the slide is supposed to be moving,
        if (slideActive) {
            // check if it is where it's supposed to be
            int cpos = linearSlideMotor.getCurrentPosition();
            int v = abs(slideTarget - cpos);
            if (v >= slideTolerance) {
                linearSlideMotor.setTargetPositionTolerance(slideTolerance); // This actually moves the motors.
                linearSlideMotor.setTargetPosition(slideTarget);
                linearSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linearSlideMotor.setPower(0.85);
            } else slideActive = false;
        }
    }

    enum HaloPositionMacro {
        FRONT,
        BACK,
        LEFT,
        RIGHT
    }

    /**
     * @see DriverControlSuperOpMode#setAbstractConstants()
     */
    int haloConstantFront = 0, haloConstantBack, haloConstantLeft, haloConstantRight;

    /**
     * The only differences between the left and right driver control OpModes are a few constants- yet Java can't virtualize class members, only methods.
     * <b>A</b> solution (not <b>THE</b> solution) is setting the variables (some HALO constants) at init & start time (done twice for redundancy) using an abstract method.
     */
    abstract void setAbstractConstants();

    void haloToPosition(HaloPositionMacro targetPosition) {

        int desiredPosition;
        switch (targetPosition) {
            case FRONT: desiredPosition = haloConstantFront; break;
            case BACK:  desiredPosition = haloConstantBack;  break;
            case LEFT:  desiredPosition = haloConstantLeft;  break;
            case RIGHT: desiredPosition = haloConstantRight; break;
            default: return;
        }

        // TODO: Rewrite this.
        // The claw will collide with the wheels and sides while rotating the HALO if the slide is not raised enough.
        if(linearSlideMotor.getCurrentPosition() <= SlidePositionMacro.HOP.height &&
            !(
                (( haloMotor.getCurrentPosition() + 50) > desiredPosition)
                & (haloMotor.getCurrentPosition() - 50  < desiredPosition)
                // is this a tolerance check? below is an easily-readable version (if so)
                // abs(lazySusan.getCurrentPosition() - desiredPosition) < 50
            )
        ) {
            linearSlideToStop(SlidePositionMacro.HOP, 30);
            down1 = true;
        }

        haloMotor.setTargetPositionTolerance(35);
        haloMotor.setTargetPosition(desiredPosition);
        haloMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        haloMotor.setPower(.5);

        if (down1 && (
                ((haloMotor.getCurrentPosition() + 50) > desiredPosition)
                & (haloMotor.getCurrentPosition() - 50 < desiredPosition)
                & (linearSlideMotor.getCurrentPosition() > 250))
        ) {
            linearSlideToStop(SlidePositionMacro.BOTTOM, 20);
            if (linearSlideMotor.getCurrentPosition() < 25) down1 = false;
        }
    }

    public void init() {
        setAbstractConstants(); // virtualize variables
        // Declare the used non-drive motors.
        haloMotor = hardwareMap.get(DcMotorEx.class, "lazySusan");
        linearSlideMotor = hardwareMap.get(DcMotorEx.class, "linearSlide");
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        drive = new RobohawksMecanumDrive(hardwareMap); //Instance of SampleMecanum drive to access methods in the file.
    }

    @Override
    public void start() {
        setAbstractConstants(); // virtualize variables
        linearSlideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //Start encoders at position 0.
        haloMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        haloMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //Sets HALO to brake mode.
        haloMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void loop() {
            if (linearSlideMotor.getCurrentPosition() >= 1500) speed = .25;
            // Change drive speed coefficient.
            else if (gamepad1.a) speed = .25;
            else if (gamepad1.b) speed = .5;
            else if (gamepad1.y) speed = .75;


            // These lines translate the raw code from the sticks into
            // a curved +/- input for the motors. It sends the values to a
            // function in roadrunner.

            // Desmos (LaTeX)
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
                linearSlideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                useSlideManualControls = true; // Reports that slide is operating manually, not via a macro.

                linearSlideMotor.setPower(((gamepad2.right_trigger >= gamepad2.left_trigger) ? gamepad2.right_trigger : -gamepad2.left_trigger));
            }
            // else if (useSlideManualControls) { // Tests to see if its operating via a macro, doesn't interrupt macro if the test is positive.
            //     linearSlideMotor.setPower(0); // todo PROBLEM (might be resolved)
            //     useSlideManualControls = false;
            // }

            // Operate end effector (claw)
            if (gamepad2.right_bumper) clawServo.setPosition(.5);
            if (gamepad2.left_bumper ) clawServo.setPosition(.25);

            // Linear slide macros
            // TODO: this will always cause priority issues because changes in state are ignored
            if (gamepad2.y) linearSlideToStop(SlidePositionMacro.TALL);
            if (gamepad2.b) linearSlideToStop(SlidePositionMacro.BOTTOM);
            if (gamepad2.a) linearSlideToStop(SlidePositionMacro.LOW);
            if (gamepad2.x) linearSlideToStop(SlidePositionMacro.MID);

            linearSlideUpdate();
            // Code for manual operation of the HALO, the big long line with lots of "? :" statements is to curve the stick input to a controllable amount.

            if (abs(gamepad2.left_stick_x) >= .05) {
                haloMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                useHaloManualControls = true; // Reports that the HALO is operating manually, and requires stopping.

                haloMotor.setPower(
                    signum(gamepad2.left_stick_x) *
                    ((Math.pow(gamepad2.left_stick_x, 2) >= .25) ?
                        .25 :
                        Math.max(Math.pow(gamepad2.left_stick_x, 2), .05)
                    )
                );
            } else if (useHaloManualControls) { // If the HALO is moving manually, stop when no longer receiving input.
                haloMotor.setPower(0);
                useHaloManualControls = false;
            }

            // Macros for LazySusan positions.

            if      (gamepad2.dpad_up)    haloToPosition(HaloPositionMacro.FRONT);
            else if (gamepad2.dpad_down)  haloToPosition(HaloPositionMacro.BACK);
            else if (gamepad2.dpad_left)  haloToPosition(HaloPositionMacro.LEFT);
            else if (gamepad2.dpad_right) haloToPosition(HaloPositionMacro.RIGHT);

            // Update stuff
            drive.update();
            telemetry.update();

            // Telemetry for localization data.

            Pose2d poseEstimate = drive.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());

            // Telemetry for gamepad input.

            telemetry.addData("Gamepad Left X", gamepad1.left_stick_x);
            telemetry.addData("Gamepad Left Y", gamepad1.left_stick_y);
            telemetry.addData("Gamepad Right X", gamepad1.right_stick_x);
            telemetry.addData("Gamepad Right Y", gamepad1.right_stick_x);

            // Telemetry for HALO and linear slide positions

            telemetry.addData("Current Slide Pos", linearSlideMotor.getCurrentPosition());
            telemetry.addData("TargetSlidePos", slideTarget);
            telemetry.addData("Susan Position", haloMotor.getCurrentPosition());
            telemetry.addData("Slide Macro Status", (slideActive ? "active" : "inactive"));

            telemetry.update();
    }
}
