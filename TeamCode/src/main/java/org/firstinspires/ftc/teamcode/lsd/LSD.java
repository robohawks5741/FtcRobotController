package org.firstinspires.ftc.teamcode.lsd;

import static java.lang.Math.abs;

import android.util.Log;
import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.teamcode.Opticon.Event;

// Linear Slide Driver
public class LSD {
    static Event<Double> TargetReached;

    // Enum for the position of the slide
    public static enum SlideHeight {
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
    public boolean isMoving() { return _isMoving; }
    // (getter) The current target
    public SlideHeight currentHeightTarget() { return _currentHeightTargetEnum; }

    // (getter) The current motor height.
    public double currentHeightPrecise() {
        // Lpos = linearSlide.getCurrentPosition();
        // ctr = ctr+4;
        // return linearSlide.getCurrentPosition();  This does not return an accurate reading but rather seemingly arbitrary numbers.
        // I believe the physical encoder is not working or the connection is messed up.

        // testing this to see if a different encoder will work in place of the the LinearSlide Motor.

        // couldn't get telemetry to work in this file, must be OpMode only.
        // ctr = ctr +2;
        return _currentHeight;
    }

    public void moveSlide(@NonNull SlideHeight target) {
        _currentHeightTargetEnum = target;
        _moveSlide(target.stopPower);
    }

    public void moveSlide(double target) {
        if (target >= 0) {
            Log.wtf("LSD", "Error moving slide! Target cannot be greq 0!");
        }
        _currentHeightTargetEnum = SlideHeight.PRECISE;
        _moveSlide(target);
    }

    private void _moveSlide(double power) {
        _hasEmitMove = false;
        // (this is 90% James' code)
        double rawProximity,
                proximityMultiplier1,
                proximityMultiplierCorrector,
                velo,
                calculatedPower,
                correctedVelocity,
                LSspeed;

        if (_currentHeight > _currentHeightTarget) {
            while (_currentHeight > _currentHeightTarget) {
                rawProximity = abs(abs(_currentHeight)-abs(_currentHeightTarget)); //finds how close slide is to target (unit is encoder ticks)
                proximityMultiplier1 = (rawProximity < 500 ? 350 : 1 ); //if within 500 ticks of target, output 350, if not, output one.
                calculatedPower = rawProximity/proximityMultiplier1; // If the slide is under 500 ticks to its destination, the variable calculatedPower is set to: the distance to the destination divided by 350. If the slide is not within 500 ticks, The value is just 1.
                proximityMultiplierCorrector = (calculatedPower > 1 ? calculatedPower : 1); // This lets the next line know if the multiplier value is over 1, and sends the correct value to change it to exactly one if it is over one.
                velo = calculatedPower/proximityMultiplierCorrector; // This turns multiplier values over 1 to one using the value provided by the above line.
                correctedVelocity = (Math.max(velo, .3)); // this makes sure that the multiplier value doesn't go below .3.
                //velo = (proximityMultiplier1/proximityMultiplierCorrector)*power;
                LSspeed = power*correctedVelocity;
                _motor.setPower(-LSspeed);
            }
        } else {
            while (_currentHeight < _currentHeightTarget) {
                rawProximity = abs(abs(_currentHeight)-abs(_currentHeightTarget));
                proximityMultiplier1 = (rawProximity < 500 ? 350 : 1 );
                calculatedPower = rawProximity/proximityMultiplier1;
                proximityMultiplierCorrector = (calculatedPower > 1 ? calculatedPower : 1);
                velo = calculatedPower/proximityMultiplierCorrector;
                correctedVelocity = (Math.max(velo, .1));
                //velo = (proximityMultiplier1/proximityMultiplierCorrector)*power;
                LSspeed = power*correctedVelocity;
                _motor.setPower(LSspeed);
            }
        }

//        if ( (_currentHeight + tolerance) < _currentHeightTarget ||
//                _currentHeightTarget < (_currentHeight - tolerance)
//        ) {
//            moveSlide(target, (power / 2), (tolerance - 2) );
//        }

        _currentHeightTarget = power;
    }

    // Was LinearSlideToStop2() pre-migration. This seems better than _moveSlide... was this a test implementation?
    // TODO: James, can you say what this does?
    public void moveSlideAlt(int target) {
        _motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        _motor.setTargetPositionTolerance(tolerance);
        _currentHeightTarget = target;
        _motor.setTargetPosition((int) _currentHeightTarget);
    }

    // Pseudo-constructor for a singleton pattern. Can be called multiple times without throwing (please try to avoid it though!)
    @SuppressWarnings("UnusedReturnValue")
    public static boolean init(@NonNull HardwareMap hmap) {
        if (_hardwareMap == null) {
            _hardwareMap = hmap;
            _motor = _hardwareMap.get(DcMotorEx.class, "linearSlide");

            MotorConfigurationType motorConfigurationType = _motor.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
            _motor.setMotorType(motorConfigurationType);
            //TODO: Add recursion with a tolerance var to make this as accurate as possible, requires good rope tension for up and down.
            _motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            _motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            return true;
        } else {
            return false;
        }
    }

    // SINGLETON constructor, NEVER call this!
    private LSD() {
        Log.i("LSD", "LSD singleton initialized");
    }

    // Singleton instance, currently unused (everything is static!)
    private static final LSD _instance = new LSD();

    /* [ignore this]
     * TO\DO use notifiers to properly throw an exception
     * System.out.println("Invalid LSD Slide Position!");
     * // throw new Exception("Invalid LSD Slide Position!");
     */

    // Update loop for the MKU Agent
    private static void mkuAgentLoop() {
//        boolean usePrecise = (_currentHeightTargetEnum == SlideHeight.PRECISE);
        // TODO: maybe this should be a bit more detailed?
        _motor.setPower(_currentHeightTarget);
        _currentHeight = _motor.getCurrentPosition();
        // If nearly equal
        if (Math.round(_currentHeight / 2) * 2 == Math.round(_currentHeightTarget / 2) * 2) {
            _isMoving = false;
            if (!_hasEmitMove) {
                _hasEmitMove = true;
                TargetReached.emit(_currentHeight);
            }
        } else {
            _isMoving = true;
        }
    }

    // Motor power.
    // TODO: Document me
    public static double power = 0.5;
    // Motor tolerance.
    // TODO: Document me
    public static int tolerance = 25;

    // The motor that the LSD controls.
    private static DcMotorEx _motor;
    // The hardware map of the opmode.
    @SuppressWarnings("FieldMayBeFinal") // Would be final, but we wouldn't be able to guarantee that it would be valid forever.
    private static HardwareMap _hardwareMap;

    // Whether or not we're currently moving the slide.
    private static boolean _isMoving = false;
    private static boolean _hasEmitMove = true;

    // The slide's current position as a motor position.
    private static double _currentHeight = SlideHeight.UNKNOWN.stopPower;
    // The current slide target as a motor position.
    private static double _currentHeightTarget = SlideHeight.UNKNOWN.stopPower;
    // The current slide target as a height enumerator.
    private static SlideHeight _currentHeightTargetEnum = SlideHeight.UNKNOWN;

    // The Motor Kinematics Unifier (MKU) is the thread/agent that manages the LSD "state machine."
    // The LSD functions can be interrupted (they're asynchronous, not blocking)
    // so we need a thread agent in charge of actually controlling the motor.
    // The public methods/setters set the TARGET of the driver, and the agent moves the motor to the
    // target position. If the target position changes while it's moving, no biggie,
    // the agent will just change course and move it to the new target location.
    private static final Thread mkuAgent = new Thread(LSD::mkuAgentLoop);
}
