package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class DcMotorExSplit implements DcMotorEx {
    public DcMotorEx smPrimary;
    public DcMotorEx smSecondary;
    public DcMotorExSplit(@NonNull DcMotorEx primary, @NonNull DcMotorEx secondary) {
        smPrimary = primary;
        smSecondary = secondary;
    }
    private DcMotorExSplit() { }

    @Override
    public MotorConfigurationType getMotorType() {
        return smPrimary.getMotorType();
    }

    @Override
    public void setMotorType(MotorConfigurationType motorType) {
        smPrimary.setMotorType(motorType);
        smSecondary.setMotorType(motorType);
    }

    @Override
    public DcMotorController getController() {
        return null;
    }

    @Override
    public int getPortNumber() {
        return 0;
    }

    @Override
    public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        smPrimary.setZeroPowerBehavior(zeroPowerBehavior);
        smSecondary.setZeroPowerBehavior(zeroPowerBehavior);
    }

    @Override
    public ZeroPowerBehavior getZeroPowerBehavior() {
        return smPrimary.getZeroPowerBehavior();
    }

    @Override
    @Deprecated
    public void setPowerFloat() {
        //noinspection deprecation
        smPrimary.setPowerFloat();
    }

    @Override
    public boolean getPowerFloat() {
        return smPrimary.getPowerFloat();
    }

    @Override
    public void setTargetPosition(int position) {
        smPrimary.setTargetPosition(position);
        smSecondary.setTargetPosition(position);
    }

    @Override
    public int getTargetPosition() {
        return smPrimary.getTargetPosition();
    }

    @Override
    public boolean isBusy() {
        return smPrimary.isBusy() || smSecondary.isBusy();
    }

    @Override
    public int getCurrentPosition() {
        return smPrimary.getCurrentPosition();
    }

    @Override
    public void setMode(RunMode mode) {
        smPrimary.setMode(mode);
        smSecondary.setMode(mode);
    }

    @Override
    public RunMode getMode() {
        return smPrimary.getMode();
    }

    @Override
    public void setDirection(Direction direction) {
        smPrimary.setDirection(direction);
        smSecondary.setDirection(direction);
    }

    @Override
    public Direction getDirection() {
        return smPrimary.getDirection();
    }

    @Override
    public void setPower(double power) {
        smPrimary.setPower(power);
        smSecondary.setPower(power);
    }

    @Override
    public double getPower() {
        return smPrimary.getPower();
    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Other;
    }

    @Override
    public String getDeviceName() {
        return "Split Motors: " + smPrimary.getDeviceName() + "/" + smSecondary.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return "Split Motors:\nPrimary: " + smPrimary.getConnectionInfo() + "\nSecondary: " + smSecondary.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return smPrimary.getVersion();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        smPrimary.resetDeviceConfigurationForOpMode();
        smSecondary.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        smPrimary.close();
        smSecondary.close();
    }

    @Override
    public void setMotorEnable() {
        smPrimary.setMotorEnable();
        smSecondary.setMotorEnable();
    }

    @Override
    public void setMotorDisable() {
        smPrimary.setMotorDisable();
        smSecondary.setMotorDisable();
    }

    @Override
    public boolean isMotorEnabled() {
        return smPrimary.isMotorEnabled();
    }

    @Override
    public void setVelocity(double angularRate) {
        smPrimary.setVelocity(angularRate);
        smSecondary.setVelocity(angularRate);
    }

    @Override
    public void setVelocity(double angularRate, AngleUnit unit) {
        smPrimary.setVelocity(angularRate, unit);
        smSecondary.setVelocity(angularRate, unit);
    }

    @Override
    public double getVelocity() {
        return smPrimary.getVelocity();
    }

    @Override
    public double getVelocity(AngleUnit unit) {
        return smPrimary.getVelocity(unit);
    }

    @Override
    @Deprecated
    public void setPIDCoefficients(RunMode mode, PIDCoefficients pidCoefficients) {
        //noinspection deprecation
        smPrimary.setPIDCoefficients(mode, pidCoefficients);
        //noinspection deprecation
        smSecondary.setPIDCoefficients(mode, pidCoefficients);
    }

    @Override
    public void setPIDFCoefficients(RunMode mode, PIDFCoefficients pidfCoefficients) throws UnsupportedOperationException {
        smPrimary.setPIDFCoefficients(mode, pidfCoefficients);
        smSecondary.setPIDFCoefficients(mode, pidfCoefficients);
    }

    @Override
    public void setVelocityPIDFCoefficients(double p, double i, double d, double f) {
        smPrimary.setVelocityPIDFCoefficients(p, i, d, f);
        smSecondary.setVelocityPIDFCoefficients(p, i, d, f);
    }

    @Override
    public void setPositionPIDFCoefficients(double p) {
        smPrimary.setPositionPIDFCoefficients(p);
        smSecondary.setPositionPIDFCoefficients(p);
    }

    @Override
    @Deprecated
    public PIDCoefficients getPIDCoefficients(RunMode mode) {
        //noinspection deprecation
        return smPrimary.getPIDCoefficients(mode);
    }

    @Override
    public PIDFCoefficients getPIDFCoefficients(RunMode mode) {
        return smPrimary.getPIDFCoefficients(mode);
    }

    @Override
    public void setTargetPositionTolerance(int tolerance) {
        smPrimary.setTargetPositionTolerance(tolerance);
        smSecondary.setTargetPositionTolerance(tolerance);
    }

    @Override
    public int getTargetPositionTolerance() {
        return smPrimary.getTargetPositionTolerance();
    }

    @Override
    public double getCurrent(CurrentUnit unit) {
        // TODO: sum?
        return smPrimary.getCurrent(unit);// + smSecondary.getCurrent(unit);
    }

    @Override
    public double getCurrentAlert(CurrentUnit unit) {
        return smPrimary.getCurrentAlert(unit);
    }

    @Override
    public void setCurrentAlert(double current, CurrentUnit unit) {
        smPrimary.setCurrentAlert(current, unit);
        smSecondary.setCurrentAlert(current, unit);
    }

    @Override
    public boolean isOverCurrent() {
        return smPrimary.isOverCurrent() || smSecondary.isOverCurrent();
    }
}
