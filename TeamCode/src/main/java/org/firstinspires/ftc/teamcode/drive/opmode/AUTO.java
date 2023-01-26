package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import java.util.ArrayList;

@Autonomous
public class AUTO extends LinearOpMode implements AUTOinterface {
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    SampleMecanumDrive drive = null;

    int ctr = 1;

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    int ID_TAG_OF_INTEREST = 18; // Tag ID 18 from the 36h11 family

    int TAG2 = 2;
    int TAG3 = 10;

    public int NumberOfTag = 0;

    AprilTagDetection tagOfInterest = null;

    Trajectory one = null;
    Trajectory four = null;


    Trajectory two = null;


    Trajectory three = null;
    Trajectory five = null;

    int conesUp = 0;

    Trajectory forwards = null;
    Trajectory right1 = null;
    Trajectory left = null;
    Trajectory right = null;

    @Override
    public void LeftAndDump(){
        drive.followTrajectory(left);
        drive.moveTestServo(1);
        drive.LinearSlideToStop2(0,35, conesUp);
        drive.susanToEncoderPosition(0);
    }

    @Override
    public void RightAndPickup(){
        drive.followTrajectory(right);
        drive.moveTestServo(.5);
        drive.LinearSlideToStop2(3,35,conesUp);
        drive.susanToEncoderPosition(60);
        conesUp++;
    }

    @Override
    public void CenterOnTile(Pose2d position){

        double targetX = 0;
        double targetY = 0;

        double centerX1 = 12 - position.getX();
        double centerX2 = 36 - position.getX();
        double centerX3 = 60 - position.getX();
        double centerX4 = -12 - position.getX();
        double centerX5 = -36 - position.getX();
        double centerX6 = -60 - position.getX();

        double centerY1 = 12 - position.getY();
        double centerY2 = 36 - position.getY();
        double centerY3 = 60 - position.getY();
        double centerY4 = -12 - position.getY();
        double centerY5 = -36 - position.getY();
        double centerY6 = -60 - position.getY();

        if(Math.abs(centerX1)<=12)
            targetX = centerX1;
        else if(Math.abs(centerX2)<=12)
            targetX = centerX2;
        else if(Math.abs(centerX3)<=12)
            targetX = centerX3;
        else if(Math.abs(centerX4)<=12)
            targetX = centerX4;
        else if(Math.abs(centerX5)<=12)
            targetX = centerX5;
        else if(Math.abs(centerX6)<=12)
            targetX = centerX6;

        if(Math.abs(centerY1)<=12)
            targetY = centerY1;
        else if(Math.abs(centerY2)<=12)
            targetY = centerY2;
        else if(Math.abs(centerY3)<=12)
            targetY = centerY3;
        else if(Math.abs(centerY4)<=12)
            targetY = centerY4;
        else if(Math.abs(centerY5)<=12)
            targetY = centerY5;
        else if(Math.abs(centerY6)<=12)
            targetY = centerY6;



        Trajectory Center = drive.trajectoryBuilder(position)
                .lineTo(new Vector2d(targetX,targetY))
                .build();

        drive.followTrajectory(Center);

    }

    @Override
    public void Park(int location){

        drive.LinearSlideResetEnc();

        Pose2d destination = null;


        if(location == 1)
            destination = new Pose2d(24,24);
        else if(location == 2)
            destination = new Pose2d(24,48);
        else if(location == 3)
            destination = new Pose2d(24,72);
        else if(true)
            destination = new Pose2d(24,48);


        Vector2d x = new Vector2d(destination.getX() , drive.getPoseEstimate().getY()+.001);
        Vector2d y = new Vector2d(drive.getPoseEstimate().getX()+.001 , destination.getY());



        Trajectory parkX = drive.trajectoryBuilder(drive.getPoseEstimate())
                .lineTo(x)
                .build();

        Trajectory parkY = drive.trajectoryBuilder(drive.getPoseEstimate())
                .lineTo(y)
                .build();


        drive.followTrajectory(parkY);
        drive.followTrajectory(parkX);
    }


    @Override
    public void FirstCone(){


        drive.followTrajectory(forwards);
        drive.LinearSlideToStop2(3,35,0);
        drive.susanToEncoderPosition(2000);
        //Thread.sleep(x);
        drive.moveTestServo(1);
        drive.susanToEncoderPosition(0);
        drive.LinearSlideToStop2(0,35,0);
    }


    @Override
    public void runOpMode() {

        drive = new SampleMecanumDrive(hardwareMap);

        /*Trajectory one = drive.trajectoryBuilder(new Pose2d())
                .strafeRight(24)
                .build();
        Trajectory four = drive.trajectoryBuilder(new Pose2d())
                .forward(30)
                .build();


        Trajectory two = drive.trajectoryBuilder(new Pose2d())
                .forward(24)
                .build();


        Trajectory three = drive.trajectoryBuilder(new Pose2d())
                .strafeRight(24)
                .build();
        Trajectory five = drive.trajectoryBuilder(new Pose2d())
                .forward(30)
                .build(); */


        Trajectory forwards = drive.trajectoryBuilder(new Pose2d())
                .lineTo( new Vector2d(0,48))
                .build();
        Trajectory right1 = drive.trajectoryBuilder(forwards.end())
                .lineTo(new Vector2d(0,72))
                .build();
        Trajectory left = drive.trajectoryBuilder(right1.end())
                .lineTo(new Vector2d(0,24))
                .build();
        Trajectory right = drive.trajectoryBuilder(left.end())
                .lineTo(new Vector2d(0,72))
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

        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        while (!isStarted() && !isStopRequested())
        {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            //camera.startStreaming(800,448); //TODO does this work?

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

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
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

        /* Actually do something useful */
        if(tagOfInterest == null)
        {
            FirstCone();
            LeftAndDump();
            RightAndPickup();
            LeftAndDump();
            RightAndPickup();
            LeftAndDump();
            RightAndPickup();
            LeftAndDump();
            Park(2);
        }
        else {
            FirstCone();
            LeftAndDump();
            RightAndPickup();
            LeftAndDump();
            RightAndPickup();
            LeftAndDump();
            RightAndPickup();
            LeftAndDump();
            Park(NumberOfTag);


        }


        /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
        while (opModeIsActive()) {sleep(20);}
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

