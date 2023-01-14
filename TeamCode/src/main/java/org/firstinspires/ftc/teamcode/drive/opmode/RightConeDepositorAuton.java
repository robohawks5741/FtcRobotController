package org.firstinspires.ftc.teamcode.drive.opmode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class RightConeDepositorAuton extends LinearOpMode implements RightConeDepositorAutonInterface {

    SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

    public int conesUp = 0;

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
    public void Park(int location){

        Pose2d destination = null;

        if(location == 1)
            destination = new Pose2d(24,24);
        else if(location == 2)
            destination = new Pose2d(24,48);
        else if(location == 3)
            destination = new Pose2d(24,72);

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
    public void runOpMode() throws InterruptedException {

        while (!isStarted() && !isStopRequested()){

        }

        drive.MoveSusan(60);
        drive.LinearSlideToStop(3,0,25,35);
        drive.followTrajectory(forwards);
        drive.moveTestServo(1);
        drive.MoveSusan(0);
        drive.LinearSlideToStop(0,0,25,35);
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
