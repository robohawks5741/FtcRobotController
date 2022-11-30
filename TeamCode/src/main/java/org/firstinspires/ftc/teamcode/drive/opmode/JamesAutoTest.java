package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
@Config
@Autonomous(group = "drive")
public class JamesAutoTest extends LinearOpMode{

    public void runOpMode() throws InterruptedException{
        int ctr = 1;
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Trajectory straightLine = drive.trajectoryBuilder(new Pose2d())
                 //.splineTo(new Vector2d(8,8),6)
                //.splineToSplineHeading(new Pose2d(36,-36,Math.toRadians(90)),Math.toRadians(90))
                .forward(24)
                .build();
        waitForStart();
        Trajectory sideways = drive.trajectoryBuilder(new Pose2d())
                .strafeRight(24)
                .build();

        while(opModeIsActive() && !isStopRequested() && ctr == 1){
            drive.followTrajectory(straightLine);
            drive.followTrajectory(sideways);
            ctr++;
        }
    }


}
