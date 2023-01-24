package org.firstinspires.ftc.teamcode.drive;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.drive.DriveSignal;
import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.followers.HolonomicPIDVAFollower;
import com.acmerobotics.roadrunner.followers.TrajectoryFollower;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceRunner;
import org.firstinspires.ftc.teamcode.util.LynxModuleUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.firstinspires.ftc.teamcode.drive.DriveConstants.MAX_ACCEL;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.MAX_ANG_ACCEL;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.MAX_ANG_VEL;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.MAX_VEL;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.MOTOR_VELO_PID;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.RUN_USING_ENCODER;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.TRACK_WIDTH;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.encoderTicksToInches;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.kA;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.kStatic;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.kV;
import static java.lang.Math.abs;

/*
 * Simple mecanum drive hardware implementation for REV hardware.
 */
@Config
public class SampleMecanumDrive extends MecanumDrive implements SampleMecanumDrive2testJames {
    //public int ctr = 0;
    //public double Lpos = 0;
//done: fix values to reflect real numbers
    public int bottomStop = 15;//bottom, stop here
    public int lowStop = 1000;
    public int midStop = 1800;
    public int tallStop= 2550;//placeholder value because8 slide isn't currently tall enough to reach the "tallStop"
    public int tooTall = 2970;//max height
    public int target = 0;//placeholder here, gets used in function LinearSlideToStop()
    public boolean slide = false;
    public int hopStop = 270;
    public boolean off = false;
    public boolean turn = false;
    public boolean down1 = false;

    public static PIDCoefficients TRANSLATIONAL_PID = new PIDCoefficients(0, 0, 0);
    public static PIDCoefficients HEADING_PID = new PIDCoefficients(0, 0, 0);

    public static double LATERAL_MULTIPLIER = 1;

    public static double VX_WEIGHT = 1;
    public static double VY_WEIGHT = 1;
    public static double OMEGA_WEIGHT = 1;

    public TrajectorySequenceRunner trajectorySequenceRunner;

    private static final TrajectoryVelocityConstraint VEL_CONSTRAINT = getVelocityConstraint(MAX_VEL, MAX_ANG_VEL, TRACK_WIDTH);
    private static final TrajectoryAccelerationConstraint ACCEL_CONSTRAINT = getAccelerationConstraint(MAX_ACCEL);

    public TrajectoryFollower follower;

    private DcMotorEx lazySusan, leftRear, rightRear, rightFront, linearSlide, leftEncoder, rightEncoder, frontEncoder;
    private List<DcMotorEx> motors;
    private Servo clawServo, penetrationServo;

    private BNO055IMU imu;
    private VoltageSensor batteryVoltageSensor;

    public SampleMecanumDrive(HardwareMap hardwareMap) {
        super(kV, kA, kStatic, TRACK_WIDTH, TRACK_WIDTH, LATERAL_MULTIPLIER);

        follower = new HolonomicPIDVAFollower(TRANSLATIONAL_PID, TRANSLATIONAL_PID, HEADING_PID,
                new Pose2d(0.5, 0.5, Math.toRadians(5.0)), 0.5);

        LynxModuleUtil.ensureMinimumFirmwareVersion(hardwareMap);

        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();


        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        // done: adjust the names of the following hardware devices to match your configuration
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);

        // not needed: If the hub containing the IMU you are using is mounted so that the "REV" logo does
        // not face up, remap the IMU axes so that the z-axis points upward (normal to the floor.)
        //
        //             | +Z axis
        //             |
        //             |
        //             |
        //      _______|_____________     +Y axis
        //     /       |_____________/|__________
        //    /   REV / EXPANSION   //
        //   /       / HUB         //
        //  /_______/_____________//
        // |_______/_____________|/
        //        /
        //       / +X axis
        //
        // This diagram is derived from the axes in section 3.4 https://www.bosch-sensortec.com/media/boschsensortec/downloads/datasheets/bst-bno055-ds000.pdf
        // and the placement of the dot/orientation from https://docs.revrobotics.com/rev-control-system/control-system-overview/dimensions#imu-location
        //
        // For example, if +Y in this diagram faces downwards, you would use AxisDirection.NEG_Y.
        // BNO055IMUUtil.remapZAxis(imu, AxisDirection.NEG_Y);

        lazySusan = hardwareMap.get(DcMotorEx.class, "lazySusan");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");

        linearSlide = hardwareMap.get(DcMotorEx.class, "linearSlide");
        leftEncoder =  hardwareMap.get(DcMotorEx.class, "leftEncoder");
        rightEncoder = hardwareMap.get(DcMotorEx.class, "rightEncoder");
        frontEncoder = hardwareMap.get(DcMotorEx.class,"frontEncoder");

        clawServo = hardwareMap.get(Servo.class, "clawServo");
        penetrationServo = hardwareMap.get(Servo.class,"penetrationServo");

        //added, testing
        motors = Arrays.asList(lazySusan, leftRear, rightRear, rightFront, linearSlide, leftEncoder, rightEncoder, frontEncoder);
        //added linear slide to these.
        for (DcMotorEx motor : motors) {
            MotorConfigurationType motorConfigurationType = motor.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
            motor.setMotorType(motorConfigurationType);
        }

        if (RUN_USING_ENCODER) {
            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        for (DcMotorEx motor :motors) {
            setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        if (RUN_USING_ENCODER && MOTOR_VELO_PID != null) {
            setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, MOTOR_VELO_PID);
        }

        // done: reverse any motors using DcMotor.setDirection()
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightRear.setDirection(DcMotorSimple.Direction.REVERSE);

        // TODO: if desired, use setLocalizer() to change the localization method
        setLocalizer(new StandardTrackingWheelLocalizer(hardwareMap));

        trajectorySequenceRunner = new TrajectorySequenceRunner(follower, HEADING_PID);
    }

    public TrajectoryBuilder trajectoryBuilder(Pose2d startPose) {
        return new TrajectoryBuilder(startPose, VEL_CONSTRAINT, ACCEL_CONSTRAINT);
    }

    public TrajectoryBuilder trajectoryBuilder(Pose2d startPose, boolean reversed) {
        return new TrajectoryBuilder(startPose, reversed, VEL_CONSTRAINT, ACCEL_CONSTRAINT);
    }

    public TrajectoryBuilder trajectoryBuilder(Pose2d startPose, double startHeading) {
        return new TrajectoryBuilder(startPose, startHeading, VEL_CONSTRAINT, ACCEL_CONSTRAINT);
    }

    public TrajectorySequenceBuilder trajectorySequenceBuilder(Pose2d startPose) {
        return new TrajectorySequenceBuilder(
                startPose,
                VEL_CONSTRAINT, ACCEL_CONSTRAINT,
                MAX_ANG_VEL, MAX_ANG_ACCEL
        );
    }

    public void turnAsync(double angle) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(
                trajectorySequenceBuilder(getPoseEstimate())
                        .turn(angle)
                        .build()
        );
    }

    public void turn(double angle) {
        turnAsync(angle);
        waitForIdle();
    }

    public void followTrajectoryAsync(Trajectory trajectory) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(
                trajectorySequenceBuilder(trajectory.start())
                        .addTrajectory(trajectory)
                        .build()
        );
    }

    public void followTrajectory(Trajectory trajectory) {
        followTrajectoryAsync(trajectory);
        waitForIdle();
    }

    public void followTrajectorySequenceAsync(TrajectorySequence trajectorySequence) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(trajectorySequence);
    }

    public void followTrajectorySequence(TrajectorySequence trajectorySequence) {
        followTrajectorySequenceAsync(trajectorySequence);
        waitForIdle();
    }

    public Pose2d getLastError() {
        return trajectorySequenceRunner.getLastPoseError();
    }

    public void update() {
        updatePoseEstimate();
        DriveSignal signal = trajectorySequenceRunner.update(getPoseEstimate(), getPoseVelocity());
        if (signal != null) setDriveSignal(signal);
    }

    public void waitForIdle() {
        while (!Thread.currentThread().isInterrupted() && isBusy())
            update();
    }

    public boolean isBusy() {
        return trajectorySequenceRunner.isBusy();
    }

    public void setMode(DcMotor.RunMode runMode) {
        for (DcMotorEx motor : motors) {
            motor.setMode(runMode);
        }
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        for (DcMotorEx motor : motors) {
            motor.setZeroPowerBehavior(zeroPowerBehavior);
        }
    }

    public void setPIDFCoefficients(DcMotor.RunMode runMode, PIDFCoefficients coefficients) {
        PIDFCoefficients compensatedCoefficients = new PIDFCoefficients(
                coefficients.p, coefficients.i, coefficients.d,
                coefficients.f * 12 / batteryVoltageSensor.getVoltage()
        );

        for (DcMotorEx motor : motors) {
            motor.setPIDFCoefficients(runMode, compensatedCoefficients);
        }
    }

    public void setWeightedDrivePower(Pose2d drivePower) {
        Pose2d vel = drivePower;

        if (abs(drivePower.getX()) + abs(drivePower.getY())
                + abs(drivePower.getHeading()) > 1) {
            // re-normalize the powers according to the weights
            double denom = VX_WEIGHT * abs(drivePower.getX())
                    + VY_WEIGHT * abs(drivePower.getY())
                    + OMEGA_WEIGHT * abs(drivePower.getHeading());

            vel = new Pose2d(
                    VX_WEIGHT * drivePower.getX(),
                    VY_WEIGHT * drivePower.getY(),
                    OMEGA_WEIGHT * drivePower.getHeading()
            ).div(denom);
        }

        setDrivePower(vel);
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        List<Double> wheelPositions = new ArrayList<>();
        for (DcMotorEx motor : motors) {
            wheelPositions.add(encoderTicksToInches(motor.getCurrentPosition()));
        }
        return wheelPositions;
    }

    @Override
    public List<Double> getWheelVelocities() {
        List<Double> wheelVelocities = new ArrayList<>();
        for (DcMotorEx motor : motors) {
            wheelVelocities.add(encoderTicksToInches(motor.getVelocity()));
        }
        return wheelVelocities;
    }

    @Override
    public void holdSusan(){
        leftEncoder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void setMotorPowers(double v, double v1, double v2, double v3) {
        frontEncoder.setPower(v);
        leftRear.setPower(v1);
        rightRear.setPower(v2);
        rightFront.setPower(v3);
    }

    @Override
    public void ResetSusan(){
        lazySusan.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    public double SusanEncoderPosition(){
        return lazySusan.getCurrentPosition();
    }

    @Override
    public void holdSlides(){
        if (LinearSlidePos()>512)
            setLinearSlide(0.17);
    }

    @Override
    public void setLinearSlide(double linearPower)  {
        //double LSstartPosition = linearSlide.getCurrentPosition();
        linearSlide.setPower(linearPower);
        leftEncoder.setPower(-linearPower);

    }

    @Override
    public void susanToPosition(int targetPosition) {

        double desiredPosition = targetPosition == 0 ? 0 : targetPosition == 1 ? 1385 : targetPosition == 2 ? 923 : 462;

        //if(linearSlide.getCurrentPosition()+50<256||linearSlide.getCurrentPosition()-50>256 & off == false) {
        if(!off) {
            LinearSlideToStop2(10, 40);

            if(linearSlide.getCurrentPosition()+50>256 & linearSlide.getCurrentPosition()-50<256){
                turn = true; off = true;}

        }


        //else if(linearSlide.getCurrentPosition()+50>256 & linearSlide.getCurrentPosition()-50<256){
        if(turn){

            lazySusan.setTargetPositionTolerance(45);
            lazySusan.setTargetPosition((int) desiredPosition);
            lazySusan.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            lazySusan.setPower(.4);
            if(lazySusan.getCurrentPosition()+50>desiredPosition & lazySusan.getCurrentPosition()-50<desiredPosition){
                down1 = true; turn = false;}
        }


        if(down1) {
            LinearSlideToStop2(0, 20);
            if(linearSlide.getCurrentPosition()<=20)
                off = false;
        }

    }

    @Override
    public boolean LinearSlideToStop(int stop, int conesUp, double power, int tolerance){ //TODO: Add recursion with a tolerance var to make this as accurate as possible, requires good rope tension for up and down.
        linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftEncoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linearSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftEncoder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        double rawProximity = 0;
        double proximityMultiplier1 = 0;
        double proximityMultiplierCorrector = 0;
        double velo = 0;
        double calculatedPower =0;
        double correctedVelocity = 0;
        double LSspeed = 0;

        if (conesUp == 0 || stop !=0) {


            if (stop == 1) {
                target = lowStop;
            }
            else if (stop == 2) {
                target = midStop;
            }
            else if (stop == 3) {
                target = tallStop;
            }
            else if (stop == 0) {
                target = bottomStop;
            }
        }
        else if (conesUp != 0 && stop == 0){
            target = lowStop-(25*conesUp);
        }
        //above sets the target encoder position specified by the user input "stop"
        //----------------------------------------------------------------------------------------
        if(LinearSlidePos()<=target){
            if(LinearSlidePos()<=target){
                rawProximity = abs(abs(LinearSlidePos())-abs(target)); //finds how close slide is to target (unit is encoder ticks)
                proximityMultiplier1 = (rawProximity < 350 ? 350 : 1 ); //if within 500 ticks of target, output 350, if not, output one.
                calculatedPower = rawProximity/proximityMultiplier1; // If the slide is under 500 ticks to its destination, the variable calculatedPower is set to: the distance to the destination divided by 350. If the slide is not within 500 ticks, The value is just 1.
                proximityMultiplierCorrector = (calculatedPower > 1 ? calculatedPower : 1); // This lets the next line know if the multiplier value is over 1, and sends the correct value to change it to exactly one if it is over one.
                velo = calculatedPower/proximityMultiplierCorrector; // This turns multiplier values over 1 to one using the value provided by the above line.
                correctedVelocity = (velo < .4 ? .4 : velo); // this makes sure that the multiplier value doesn't go below .4.
                //velo = (proximityMultiplier1/proximityMultiplierCorrector)*power;
                LSspeed = power*correctedVelocity;
                //LSspeed = 1;
                linearSlide.setPower(LSspeed);
                //leftEncoder.setPower(-LSspeed);

                if(LinearSlidePos()<=target-tolerance||LinearSlidePos()>=target+tolerance)
                    slide = false;
                else
                    slide = true;

            }
        }
        else{
            if(LinearSlidePos()>=target){
                rawProximity = abs(abs(LinearSlidePos())-abs(target));
                proximityMultiplier1 = (rawProximity < 500 ? 350 : 1 );
                calculatedPower = rawProximity/proximityMultiplier1;
                proximityMultiplierCorrector = (calculatedPower > 1 ? calculatedPower : 1);
                velo = calculatedPower/proximityMultiplierCorrector;
                correctedVelocity = (velo < .4 ? .4 : velo);
                velo = (proximityMultiplier1/proximityMultiplierCorrector)*power;
                LSspeed = power*correctedVelocity;
                //LSspeed = 1;
                linearSlide.setPower(-LSspeed);
                //leftEncoder.setPower(LSspeed);

                if(LinearSlidePos()<=target-tolerance||LinearSlidePos()>=target+tolerance)
                    slide = false;
                else
                    slide = true;


            }
        }

        //--------------------------------------------------------------------------------------------
     //   if (LinearSlidePos()+tolerance<target||LinearSlidePos()-tolerance>target){
     //       LinearSlideToStop(stop, power/2,tolerance-2);
     //   }
     //   holdSlides();
        if(slide)
            return true;
        else
            return false;
    }

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
        else if(stop == 0){
            target = bottomStop;
        }

        linearSlide.setTargetPositionTolerance(tolerance);
        linearSlide.setTargetPosition(target);
        linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlide.setPower(.7);

        if(LinearSlidePos()<=target-tolerance||LinearSlidePos()>=target+tolerance)
            slide = false;
        else
            slide = true;

        if(slide)
            return true;
        else
            return false;
    }

    @Override
    public void susanEncoderPosition(int pos){
        lazySusan.setTargetPositionTolerance(20);
        lazySusan.setTargetPosition(target);
        lazySusan.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lazySusan.setPower(.4);
    }


    @Override
    public void penetrate(double pos){
        penetrationServo.setPosition(pos);
    }

    @Override
    public int LinearSlidePos(){
        //Lpos = linearSlide.getCurrentPosition();
        //ctr = ctr+4;
        //return linearSlide.getCurrentPosition();  This does not return an accurate reading but rather seemingly arbitrary numbers.
        //I believe the physical encoder is not working or the connection is messed up.

        return linearSlide.getCurrentPosition();
        //testing this to see if a different encoder will work in place of the the LinearSlide Motor.

        //couldn't get telemetry to work in this file, must be OpMode only.
        //ctr = ctr +2;

    }
    @Override
    public void LinearSlideResetEnc(){
        linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    public void moveTestServo(double pos){

        clawServo.setPosition(pos);
    }

    @Override
    public void MoveSusan(double speed){
        lazySusan.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lazySusan.setPower(speed);
    }

    /*@Override
    public void up(){
        boolean yes = false;
        int wheel = 0;
        if(SusanEncoderPosition()<-127 && SusanEncoderPosition()>-356)
            yes = true;
        else if(SusanEncoderPosition()>-569 || SusanEncoderPosition()<-846)
            yes = true;
        else if(SusanEncoderPosition()>-1013 || SusanEncoderPosition()<-1278)
            yes = true;
        else if(SusanEncoderPosition()>-1476 || SusanEncoderPosition()<-1756)
            yes = true;

        if(yes)
            LinearSlideToStop(10,0,35,35);

        yes=false;

        if(SusanEncoderPosition()<-127 && SusanEncoderPosition()>-356){
            yes = true; wheel = 1;}

        else if(SusanEncoderPosition()>-569 || SusanEncoderPosition()<-846){
            yes = true; wheel = 2;}

        else if(SusanEncoderPosition()>-1013 || SusanEncoderPosition()<-1278){
            yes = true; wheel = 3;}

        else if(SusanEncoderPosition()>-1476 || SusanEncoderPosition()<-1756){
            yes = true; wheel = 4;}



    }

    @Override
    public void zHopSusan(double input){

        if(SusanEncoderPosition()<-127 && SusanEncoderPosition()>-356)
            up


        else if(SusanEncoderPosition()>-569 || SusanEncoderPosition()<-846)
            MoveSusan();

    }*/



    @Override
    public double getRawExternalHeading() {
        return imu.getAngularOrientation().firstAngle;
    }

    @Override
    public Double getExternalHeadingVelocity() {
        // To work around an SDK bug, use -zRotationRate in place of xRotationRate
        // and -xRotationRate in place of zRotationRate (yRotationRate behaves as 
        // expected). This bug does NOT affect orientation. 
        //
        // See https://github.com/FIRST-Tech-Challenge/FtcRobotController/issues/251 for details.
        return (double) -imu.getAngularVelocity().xRotationRate;
    }

    public static TrajectoryVelocityConstraint getVelocityConstraint(double maxVel, double maxAngularVel, double trackWidth) {
        return new MinVelocityConstraint(Arrays.asList(
                new AngularVelocityConstraint(maxAngularVel),
                new MecanumVelocityConstraint(maxVel, trackWidth)
        ));
    }

    public static TrajectoryAccelerationConstraint getAccelerationConstraint(double maxAccel) {
        return new ProfileAccelerationConstraint(maxAccel);
    }
}
