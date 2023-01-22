package org.firstinspires.ftc.teamcode.lsd;

import static java.lang.Math.abs;
import static java.lang.Math.max;

import android.util.Log;
import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.teamcode.eventd.EventEmitter;

// Linear Slide Driver
public class LSD {
    public static EventEmitter<Double> TargetReached;

    public enum SlideMode {
        // Absolute control, using stops and encoder positions.
        ABSOLUTE,
        // Manual control, using offsets.
        RELATIVE
    }

    // Enum for the position of the slide
    public enum SlideHeight {
        UNKNOWN (0),
        PRECISE (1),
        BOTTOM (-100), // bottom, stop here
        LOW (-1350),
        MIDDLE (-2133),
        HIGH (-2133), //placeholder value because slide isn't currently tall enough to reach this
        TOP (-2375);

        private final double stopPower;

        SlideHeight(double v) {
            stopPower = v;
        }
    }

    // (getter) Whether or not the slide is currently moving towards a target.
    public static boolean isMoving() { return _isMoving; }
    // (getter) The current target
    public static SlideHeight currentHeightTarget() { return _heightTargetEnum; }

    // (getter) The current motor height.
    public static double currentHeightPrecise() {
        // Lpos = linearSlide.getCurrentPosition();
        // ctr = ctr+4;
        // return linearSlide.getCurrentPosition();  This does not return an accurate reading but rather seemingly arbitrary numbers.
        // I believe the physical encoder is not working or the connection is messed up.

        // testing this to see if a different encoder will work in place of the the LinearSlide Motor.

        // couldn't get telemetry to work in this file, must be OpMode only.
        // ctr = ctr +2;
        return _motor.getCurrentPosition();
    }

    public static void cleanup() {
        mkuAgent.interrupt();
    }

    // Moves the slide to a precise height.
    public static void setSpeed(double speed) {
        if (_mode != SlideMode.RELATIVE) {
            Log.wtf("LSD", "Slide mode is not relative!");
        }
        if (abs(speed) > 1) {
            Log.wtf("LSD", "Slide velocity is greater than 1!");
        }
        _heightTargetEnum = SlideHeight.UNKNOWN;
    }

    // Moves the slide to an enumerated height.
    public static void setPosition(@NonNull SlideHeight target) {
        _heightTargetEnum = target;
        _setPosition(target.stopPower);
    }

    // Moves the slide to a precise height.
    public static void setPosition(double target) {
        if (target >= 0) {
            Log.wtf("LSD", "Error moving slide! Target cannot be greq 0!");
        }
        _heightTargetEnum = SlideHeight.PRECISE;
        _setPosition(target);
    }

    private static void _setPosition(double target) {
        _hasEmitMove = false;
        // (this is 90% James' code)

        double height = _motor.getCurrentPosition();

        // finds how close slide is to target (unit is encoder ticks)
        double rawProximity = abs( (abs(height) - abs(_heightTarget)) );
        // if within 500 ticks of target, output 350, if not, output one.
        double proximityMultiplier1 = (rawProximity < 500 ? 350 : 1 );
        // If the slide is under 500 ticks to its destination, the variable calculatedPower is set to: the distance to the destination divided by 350. If the slide is not within 500 ticks, The value is just 1.
        double calculatedPower = rawProximity / proximityMultiplier1;
        // This lets the next line know if the multiplier value is over 1, and sends the correct value to change it to exactly one if it is over one.
        double proximityMultiplierCorrector = max(calculatedPower, 1);
        // This turns multiplier values over 1 to one using the value provided by the above line.
        double velo = calculatedPower / proximityMultiplierCorrector;

        // this makes sure that the multiplier value doesn't go below .3.
        double correctedVelocity = max(velo, (height > _heightTarget ? .3 : .1 ));

        if (height > _heightTarget) {
            while (height > _heightTarget) {
                // velo = (proximityMultiplier1/proximityMultiplierCorrector)*power;
                _motor.setPower(-target * correctedVelocity);
            }
        } else {
            while (height < _heightTarget) {
                _motor.setPower(target * correctedVelocity);
            }
        }

//        if ( (_height + tolerance) < _heightTarget ||
//                _heightTarget < (_height - tolerance)
//        ) {
//            moveSlide(target, (power / 2), (tolerance - 2) );
//        }

        _heightTarget = target;
    }

    // Was LinearSlideToStop2() pre-migration. This seems better than _setPosition... was this a test implementation?
    // TODO: James, can you say what this does?
    public static void moveSlideAlt(int target) {
        _motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        _motor.setTargetPositionTolerance(_tolerance);
        _heightTarget = target;
        _motor.setTargetPosition((int) _heightTarget);
    }

    // Resets the encoder.
    public static void resetEncoder(){
        _motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public static void setPower(double p) { _power = p; }
    public static double getPower() { return _power; }

    public static void setTolerance(int t) { _tolerance = t; }
    public static double getTolerance() { return _tolerance; }

    public static void setMode(SlideMode m) { _mode = m; }
    // Getter, gets the current mode.
    public static SlideMode getMode() { return _mode; }

    // Pseudo-constructor for a singleton pattern. Can be called multiple times without throwing (please try to avoid it though!)
    @SuppressWarnings("UnusedReturnValue")
    public static boolean opmodeInit(@NonNull HardwareMap hmap, @NonNull OpMode op) {
        _hardwareMap = hmap;
        _currentOpmode = op;
        _motor = _hardwareMap.get(DcMotorEx.class, "linearSlide");

        mkuAgent = new Thread(LSD::_mkuAgentUpdate);

        MotorConfigurationType motorConfigurationType = _motor.getMotorType().clone();
        motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
        _motor.setMotorType(motorConfigurationType);
        //TODO: Add recursion with a tolerance var to make this as accurate as possible, requires good rope tension for up and down.
        _motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        _motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        return true;
    }

    // SINGLETON constructor, NEVER call this!
    private LSD() throws IllegalAccessException {
        Log.wtf("LSD", "Singleton constructor called!");
        throw new IllegalAccessException("Singleton constructor called!");
    }

    // Singleton instance, currently unused (everything is static!)
    // private static final LSD _instance = new LSD();

    /* [ignore this]
     * TO\DO use notifiers to properly throw an exception
     * System.out.println("Invalid LSD Slide Position!");
     * // throw new Exception("Invalid LSD Slide Position!");
     */

    // Update loop for the MKU Agent
    private static void _mkuAgentUpdate() {
        // boolean usePrecise = (_currentHeightTargetEnum == SlideHeight.PRECISE);
        // TODO: maybe this should be a bit more detailed?
        _motor.setPower(_power);

        double height = _motor.getCurrentPosition();
        // If nearly equal
        if (!_motor.isBusy() && (Math.round(height / 2) * 2 == Math.round(_heightTarget / 2) * 2)) {
            _isMoving = false;
            if (!_hasEmitMove) {
                _hasEmitMove = true;
                TargetReached.emit(_heightTarget);
            }
        } else {
            _isMoving = true;
        }
    }

    // Motor power.
    // TODO: Document me
    private static double _power = 0.5;
    // Motor tolerance.
    // TODO: Document me
    private static int _tolerance = 25;

    // The motor that the LSD controls.
    private static DcMotorEx _motor;
    // The hardware map of the opmode.
    @SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
    // Would be final, but we wouldn't be able to guarantee that it would be valid forever.
    private static HardwareMap _hardwareMap = null;
    @SuppressWarnings("FieldCanBeLocal")
    private static OpMode _currentOpmode = null;

    // Whether or not we're currently moving the slide.
    private static boolean _isMoving = false;
    private static boolean _hasEmitMove = true;

    private static SlideMode _mode = SlideMode.ABSOLUTE;
    // The motor's current speed.
    private static double _speed = -1;
    // The current slide target as a motor position.
    private static double _heightTarget = SlideHeight.UNKNOWN.stopPower;
    // The current slide target as a height enumerator.
    private static SlideHeight _heightTargetEnum = SlideHeight.UNKNOWN;

    // The Motor Kinematics Unifier (MKU) is the thread/agent that manages the LSD "state machine."
    // The LSD functions can be interrupted (they're asynchronous, not blocking)
    // so we need a thread agent in charge of actually controlling the motor.
    // The public methods/setters set the TARGET of the driver, and the agent moves the motor to the
    // target position. If the target position changes while it's moving, no biggie,
    // the agent will just change course and move it to the new target location.
    private static Thread mkuAgent = null;
}
