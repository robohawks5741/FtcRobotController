package org.firstinspires.ftc.teamcode.drive.opmode;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.DcMotorExSplit;
import org.firstinspires.ftc.teamcode.drive.RobohawksMecanumDrive;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/* TODO: Replace old stuff with the wrapped stuff
 * [] Halo (lazySusan) -> HD
 * [] Linear Slide (linearSlide) -> LSD
 */

public abstract class AutoSuperOpMode extends LinearOpMode {

    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;
    RobohawksMecanumDrive drive = null;
    protected Servo clawServo;
    protected DcMotorEx lazySusan;
    protected DcMotorEx linearSlide;

    static final double FEET_PER_METER = 3.28084;

    static final double fx = 578.272;
    static final double fy = 578.272;
    static final double cx = 402.145;
    static final double cy = 221.506;

    static final double tagSize = 0.166;

    static final int ID_TAG_OF_INTEREST = 18;
    static final int TAG2 = 2;
    static final int TAG3 = 10;

    public int numberOfTag = 0;

    int conesUp = 0;


    AprilTagDetection tagOfInterest = null;
    // Trajectories
    Trajectory forwards = null;
    Trajectory left = null;
    Trajectory right = null;
    Trajectory toCones = null;
    Trajectory toPole = null;

    Trajectory toCones2 = null;
    Trajectory toPole2 = null;
    Trajectory parkPosition2 = null;
    Trajectory parkPosition1 = null;
    Trajectory parkPosition3 = null;
    Trajectory returnus = null;
    protected boolean slide = false;

    // LinearSlide positions
    enum SlidePosition {
        BOTTOM (0),
        HOP (270),
        CONESTACK (360),
        // To keep the bot stable
        AUTOMOVE (523),
        LOW (1150),
        MID (1950),
        INSERT (2250),
        TALL (2700);

        final int height;

        SlidePosition(int i) { height = i; }
    }

    enum HaloPosition {
        FRONT (0),
        // placeholders
        BACK (-1),
        LEFT (-1),
        RIGHT (-1);

        final int height;

        HaloPosition(int height) { this.height = height; }
    }

    enum ClawPosition {
        FULL_OPEN(0.25),
        OPEN(0.32),
        CLOSED (0.6);

        final double pos;

        ClawPosition(double pos) { this.pos = pos; }
    }

    protected int target = 0;

    public void susan(int pos){
        susan(pos, 0.4);
    }

    public void susan(int pos, double pow){
        lazySusan.setTargetPositionTolerance(20);
        lazySusan.setTargetPosition(pos);
        lazySusan.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lazySusan.setPower(pow);
    }

    public void claw(ClawPosition pos){
        claw(pos.pos);
    }
    // Please use the enum macro instead!
    public void claw(double pos){
        clawServo.setPosition(pos);
    }

    public void linearSlideToStop(SlidePosition stop, int tolerance, int conesUp){

        if (stop == SlidePosition.CONESTACK) {
            target = SlidePosition.CONESTACK.height - conesUp * 75;
        } else target = stop.height;

        linearSlide.setTargetPositionTolerance(tolerance);
        linearSlide.setTargetPosition(target);
        linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlide.setPower(1);

        //        (pos + tolerance) leq target
        //        (pos - tolerance) greq target
        slide = ((linearSlide.getCurrentPosition() <= (target - tolerance)) || (linearSlide.getCurrentPosition() >= (target + tolerance)));
    }

    protected abstract void runAuto() throws InterruptedException;

    protected abstract void setTrajectories();

    @Override
    public void runOpMode() throws InterruptedException {
        counter = 0;
        drive = new RobohawksMecanumDrive(hardwareMap);

        lazySusan   = hardwareMap.get(DcMotorEx.class,"lazySusan");
        linearSlide = hardwareMap.get(DcMotorEx.class, "linearSlide");
        lazySusan.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        clawServo = hardwareMap.get(Servo.class, "clawServo");

        linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //Start encoders at position 0.
        lazySusan.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lazySusan.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //Sets HALO to brake mode.

        setTrajectories();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagSize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) { telemetry.addLine("CAMERA DEVICE ERROR!"); }
        });

        telemetry.setMsTransmissionInterval(50);

        // Init loop!

        while (!isStarted() && !isStopRequested()) {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(currentDetections.size() != 0) {
                boolean tagFound = false;

                for(AprilTagDetection tag : currentDetections)  {
                    int tagNum = -1;
                    switch (tag.id) {
                        case ID_TAG_OF_INTEREST: tagNum = 1; break;
                        case TAG2:               tagNum = 2; break;
                        case TAG3:               tagNum = 3; break;
                    }
                    if (tagNum != -1) {
                        tagOfInterest = tag;
                        numberOfTag = tagNum;
                        tagFound = true;
                    }
                }

                if (tagFound) {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                } else {
                    telemetry.addLine("Don't see tag of interest :(");
                    if (tagOfInterest == null)
                        telemetry.addLine("(The tag has never been seen)");
                    else {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }
            }
            else
            {
                telemetry.addLine("Don't see tag of interest :(");

                if(tagOfInterest == null)
                {
                    telemetry.addLine("(The tag has never been seen)");
                }
                else
                {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }
            }

            telemetry.update();
            sleep(20);
        }

        // start here!!

        if(tagOfInterest != null) {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        } else {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        runAuto();
    }

    @SuppressLint("DefaultLocale")
    void tagToTelemetry(AprilTagDetection detection) {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }

    // speedrun stuff

    static int counter = 0;

    void debugSplit(String splitName) {
        double stamp = getRuntime();
        Logger.getLogger("SPEEDRUN SPLITS").log(Level.INFO, splitName + " # " + counter + " @ " + stamp);
        counter++;
    }
}
