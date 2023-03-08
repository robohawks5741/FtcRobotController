package org.firstinspires.ftc.teamcode.drive.opmode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
@Disabled
@Autonomous
public class RightConeDepositorAuton extends LinearOpMode implements RightConeDepositorAutonInterface {

    SampleMecanumDrive drive = null;

    public int conesUp = 0;

    Trajectory forwards = null;
    Trajectory right1 = null;
    Trajectory left = null;
    Trajectory right = null;


    @Override
    public void LeftAndDump(){
        drive.followTrajectory(left);
        drive.moveTestServo(1);
        drive.LinearSlideToStop(0,conesUp,25,35);
        drive.MoveSusan(0);
    }

    @Override
    public void RightAndPickup(){
        drive.followTrajectory(right);
        drive.moveTestServo(.5);
        drive.LinearSlideToStop(3,conesUp,25,35);
        drive.MoveSusan(60);
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
            destination = new Pose2d(24,24); //todo : fix coordinates
        else if(location == 2)
            destination = new Pose2d(24,48);
        else if(location == 3)
            destination = new Pose2d(24,72);
        else if(true)
            destination = new Pose2d(24,48);


        Vector2d x = new Vector2d(destination.getX() , drive.getPoseEstimate().getY());
        Vector2d y = new Vector2d(drive.getPoseEstimate().getX() , destination.getY());


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
        drive.susanToEncoderPosition(2000);
        drive.LinearSlideToStop(3,0,25,35);
        drive.followTrajectory(forwards);
        drive.moveTestServo(1);
        drive.susanToEncoderPosition(0);
        drive.LinearSlideToStop(0,0,25,35);
    }


    @Override
    public void runOpMode() throws InterruptedException {

        drive = new SampleMecanumDrive(hardwareMap);

        Trajectory forwards = drive.trajectoryBuilder(new Pose2d()) //todo fix coordinates
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


        while (!isStarted() && !isStopRequested()){

        }

        while(opModeIsActive()) {
            FirstCone();
            LeftAndDump();
            RightAndPickup();
            LeftAndDump();
            RightAndPickup();
            LeftAndDump();
            RightAndPickup();
            LeftAndDump();
            Park(1);

        }
    }
}
