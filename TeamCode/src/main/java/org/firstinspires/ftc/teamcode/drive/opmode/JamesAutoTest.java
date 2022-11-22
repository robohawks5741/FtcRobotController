package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
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
                .forward(2)
                .build();
        waitForStart();


        while(opModeIsActive() && !isStopRequested() && ctr == 1){
            drive.followTrajectory(straightLine);
            ctr++;
        }
    }


}
