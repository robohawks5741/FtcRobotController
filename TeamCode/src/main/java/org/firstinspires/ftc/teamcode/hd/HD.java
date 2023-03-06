package org.firstinspires.ftc.teamcode.hd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public final class HD {

    private static final String logCategory = "Halo Driver (HD)";
    private static OpMode opMode;
    private static HardwareMap hardwareMap;
    private static DcMotorEx motorHalo;

    private static int targetRotation;

    public static void reset() {

    }

    /**
     * Gets the current rotation of the halo.
     * @return the current position of the halo as reported by the motor.
     */
    public static double getCurrentRotation() {
        return 0;
    }

    /**
     * Sets
     */
    public static void setTargetRotation(int rot, @Nullable Integer tolerance) {
        motorHalo.setTargetPositionTolerance(((tolerance == null) ? 20 : tolerance));
        motorHalo.setTargetPosition(rot);
        targetRotation = rot;
        motorHalo.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorHalo.setPower(.4);
    }

    /**
     * Gets the desired rotation (not the <i>current</i> rotation).
     * @return the current desired (target) rotation of the halo.
     */
    public static double getTargetRotation() {
        return targetRotation;
    }

    /**
     * Sets the left encoder's zero power behavior to brake.
     * Currently NON-FUNCTIONAL!
     */
    public static void brake() {
        // motorEncoderLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public static void setSpeed(double speed) {
        motorHalo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorHalo.setPower(speed);
    }

    public static void init(@NonNull OpMode opMode) {
        HD.opMode = opMode;
        HD.hardwareMap = opMode.hardwareMap;
        // TODO: change hardware map name to match
        HD.motorHalo = hardwareMap.get(DcMotorEx.class, "lazySusan");
        opMode.telemetry.addData(logCategory, "Initializing!");
    }

    public static void cleanup() {
        if (opMode != null) opMode.telemetry.addData(logCategory, "Cleaning up!");
        HD.opMode = null;
        HD.hardwareMap = null;
        HD.motorHalo = null;
    }
}
