package org.firstinspires.ftc.teamcode.hd;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.hardware.DcMotor;

public class HD {
    public static void setSpeed(double speed) {

    }

    public static void reset() {
    }

    /**
     * Gets the current rotation of the halo.
     * @return the current position of the halo as reported by the motor.
     */
    public static double getCurrentRotation() {

    }

    /**
     * Gets the desired rotation (not the <i>current</i> rotation).
     * @return the current desired (target) rotation of the halo.
     */
    public static double getTargetRotation() {

    }

    /**
     * Sets the left encoder's zero power behavior to brake.
     */
    public static void brake() {

    }

    /**
     *
     * @param target The target
     * @param tolerance (optional) Defaults to 45.0. Use 20.0 for "encoderPosition."
     * @throws InterruptedException
     */
    public static void setRotation(int target, @Nullable Double tolerance) {
        if (tolerance == null) tolerance = 45.0;
    }
}
