package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

@Autonomous
public class LeftAUTO extends LinearOpMode implements AUTOinterface {

    // Declare all the things...

    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;
    SampleMecanumDrive drive = null;
    private Servo clawServo;
    private DcMotorEx linearSlide, lazySusan;

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
    Trajectory forwards = null;
    Trajectory left = null;
    Trajectory right = null;
    Trajectory toCones = null;
    Trajectory toPole = null;
    Trajectory parkPosition1 = null;
    Trajectory parkPosition2 = null;
    Trajectory parkPosition3 = null;
    Trajectory returnus = null;
    private boolean slide = false;

    // LinearSlide positions
    private int bottomStop = 0;
    private int lowStop = 1150;
    private int midStop = 1950;
    private int tallStop= 2700;
    private int target =     0;
    private int hopStop =  270;
    private int AutoMove = 523;
    private int coneStack= 360;
    private int insert  = 2500;




    @Override
    public boolean LinearSlideToStop2(int stop, int tolerance, int conesUp){

        if (stop == 1) {
            target = lowStop;
        }
        else if (stop == 7){
            target = coneStack-conesUp*75;
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
        else if(stop == 10){
            target = hopStop;
        }
        else if(stop == 9){
            target = insert;
        }
        else if(stop == 6){
            target = AutoMove;
        }


        linearSlide.setTargetPositionTolerance(tolerance);
        linearSlide.setTargetPosition(target);
        linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlide.setPower(.7);

        if(linearSlide.getCurrentPosition()<=target-tolerance||linearSlide.getCurrentPosition()>=target+tolerance)
            slide = false;
        else
            slide = true;

        if(slide)
            return true;
        else
            return false;
    }

    @Override
    public void FirstCone() throws InterruptedException {

        drive.moveTestServo(.6);
        Thread.sleep(700);
        drive.susanToEncoderPosition(80);
        Thread.sleep(500);
        drive.susanToEncoderPosition(0);
        Thread.sleep(600);
        LinearSlideToStop2(6,30,0);
        drive.followTrajectory(forwards);
        Thread.sleep(100);
        drive.followTrajectory(returnus);
        drive.turn(Math.toRadians(90));
        LinearSlideToStop2(3,35,0);
        drive.followTrajectory(right);
        drive.susanToEncoderPosition(462);
        Thread.sleep(1000);
        drive.followTrajectory(left);
        Thread.sleep(500);
        LinearSlideToStop2(9,35,0);
        Thread.sleep(200);
        drive.moveTestServo(.25);
        Thread.sleep(500);
        LinearSlideToStop2(7,10,conesUp);
        conesUp++;
        drive.susanToEncoderPosition(0);
        drive.followTrajectory(toCones);
        Thread.sleep(250);
        drive.moveTestServo(.6);
        Thread.sleep(500);
        LinearSlideToStop2(1,20,conesUp);
        Thread.sleep(500);
        drive.followTrajectory(toPole);
        LinearSlideToStop2(3,30,0);
        Thread.sleep(1500);
        drive.susanToEncoderPosition(440);
        Thread.sleep(500);
        LinearSlideToStop2(9,30,0);
        Thread.sleep(750);
        drive.moveTestServo(.25);
        Thread.sleep(750);
        if(NumberOfTag == 2)
            drive.followTrajectory(parkPosition1);
        else if(NumberOfTag == 1)
            drive.followTrajectory(parkPosition2);
        else if(NumberOfTag == 3)
            drive.followTrajectory(parkPosition3);
        else
            drive.followTrajectory(parkPosition2);
        Thread.sleep(250);
        drive.susanToEncoderPosition(0);
        Thread.sleep(1000);
        LinearSlideToStop2(0,35,0);
        Thread.sleep(1000);
        drive.moveTestServo(.32);
        Thread.sleep(300);

    }


    @Override
    public void runOpMode() throws InterruptedException {


        drive = new SampleMecanumDrive(hardwareMap);

        drive.ResetSusan();


        lazySusan = hardwareMap.get(DcMotorEx.class,"lazySusan");

        linearSlide = hardwareMap.get(DcMotorEx.class, "linearSlide");

        clawServo = hardwareMap.get(Servo.class, "clawServo");

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //Start encoders at position 0.
        lazySusan.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lazySusan.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //Sets HALO to brake mode.

        forwards = drive.trajectoryBuilder(new Pose2d())

                .lineTo( new Vector2d(55,0))
                .build();

        returnus = drive.trajectoryBuilder(forwards.end())
                .lineTo(new Vector2d(50,0))
                .build();

        right = drive.trajectoryBuilder(returnus.end().plus(new Pose2d(0, 0, Math.toRadians(90))))
                .lineTo( new Vector2d(50,-7.2),
                        drive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        drive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        left = drive.trajectoryBuilder(right.end())
                .lineTo( new Vector2d(51,-9.9),
                        drive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        drive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        toCones = drive.trajectoryBuilder(left.end())
                .lineTo( new Vector2d(46.8,24.7),
                    drive.getVelocityConstraint(24, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                    drive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        toPole = drive.trajectoryBuilder(toCones.end())
                .lineTo(new Vector2d(51,-10.4),
                        drive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        drive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        parkPosition1 = drive.trajectoryBuilder(toPole.end())
                .lineTo(new Vector2d(48,24))
                .build();

        parkPosition3 = drive.trajectoryBuilder(toPole.end())
                .lineTo(new Vector2d(48,-24))
                .build();

        parkPosition2 = drive.trajectoryBuilder(toPole.end())
                .lineTo(new Vector2d(48,0))
                .build();

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

