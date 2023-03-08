package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.RobohawksMecanumDrive;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

/* TODO: Replace old stuff with the wrapped stuff
 * [] Halo (lazySuasan) -> HD
 * [] Linear Slide (linearSlide) -> LSD
 */

public abstract class PseudoAutoOpMode extends LinearOpMode {

    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;
    RobohawksMecanumDrive drive = null;
    protected Servo clawServo;
    protected DcMotorEx linearSlide, lazySusan;

    static final double FEET_PER_METER = 3.28084;

    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    double tagsize = 0.166;

    int ID_TAG_OF_INTEREST = 18;
    int TAG2 = 2;
    int TAG3 = 10;

    public int NumberOfTag = 0;

    int conesUp = 0;


    AprilTagDetection tagOfInterest = null;
    // Trajectories
    Trajectory forwards = null;
    Trajectory left = null;
    Trajectory right = null;
    Trajectory toCones = null;
    Trajectory toPole = null;
    Trajectory parkPosition1 = null;
    Trajectory parkPosition2 = null;
    Trajectory parkPosition3 = null;
    Trajectory returnus = null;
    protected boolean slide = false;

    // LinearSlide positions
    protected static int bottomStop = 0;
    protected static int lowStop = 1150;
    protected static int midStop = 1950;
    protected static int tallStop= 2700;
    protected static int target =     0;
    protected static int hopStop =  270;
    protected static int AutoMove = 523;
    protected static int coneStack= 360;
    protected static int insert  = 2500;

    public void susan(int pos){
        lazySusan.setTargetPositionTolerance(20);
        lazySusan.setTargetPosition(pos);
        lazySusan.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lazySusan.setPower(.4);
    }

    public void claw(double pos){
        clawServo.setPosition(pos);
    }

    public boolean LinearSlideToStop2(int stop, int tolerance, int conesUp){

        switch (stop) {
            case 0: { target = bottomStop; break; }
            case 1: { target = lowStop; break; }
            case 2: { target = midStop; break; }
            case 3: { target = tallStop; break; }
            case 6: { target = AutoMove; break; }
            case 7: { target = coneStack - conesUp * 75; break; }
            case 9: { target = insert; break; }
            case 10: { target = hopStop; break; }
        }

        linearSlide.setTargetPositionTolerance(tolerance);
        linearSlide.setTargetPosition(target);
        linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlide.setPower(.7);

        //        (pos + tolerance) leq target
        //        (pos - tolerance) greq target
        slide = ((linearSlide.getCurrentPosition() <= (target - tolerance)) || (linearSlide.getCurrentPosition() >= (target + tolerance)));
        return slide;
    }

    protected abstract void FirstCone() throws InterruptedException;


    protected abstract void setTrajectories();

    @Override
    public void runOpMode() throws InterruptedException {

        drive = new RobohawksMecanumDrive(hardwareMap);



        lazySusan = hardwareMap.get(DcMotorEx.class,"lazySusan");

        linearSlide = hardwareMap.get(DcMotorEx.class, "linearSlide");
        lazySusan.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        clawServo = hardwareMap.get(Servo.class, "clawServo");

        RobohawksMecanumDrive drive = new RobohawksMecanumDrive(hardwareMap);

        linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //Start encoders at position 0.
        lazySusan.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lazySusan.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //Sets HALO to brake mode.

        setTrajectories();


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        telemetry.setMsTransmissionInterval(50);

        //Init loop!

        while (!isStarted() && !isStopRequested())
        {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(currentDetections.size() != 0)
            {
                boolean tagFound = false;

                for(AprilTagDetection tag : currentDetections)
                {
                    if(tag.id == ID_TAG_OF_INTEREST)
                    {
                        tagOfInterest = tag;
                        tagFound = true;
                        NumberOfTag = 1;
                        break;
                    }
                    if(tag.id == TAG2){
                        tagOfInterest = tag;
                        tagFound = true;
                        NumberOfTag = 2;
                        break;
                    }
                    if(tag.id == TAG3){
                        tagOfInterest = tag;
                        tagFound = true;
                        NumberOfTag = 3;
                        break;
                    }
                }

                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
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

        //start here!!

        if(tagOfInterest != null)
        {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        FirstCone();

    }

    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }
}
