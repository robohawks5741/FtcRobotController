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

@Autonomous
public class PseudoAutoRight extends PseudoAutoOpMode {

    protected void FirstCone() throws InterruptedException {

        claw(.6);
        Thread.sleep(700);
        susan(80);
        Thread.sleep(500);
        susan(0);
        Thread.sleep(600);
        LinearSlideToStop2(6,30,0);
        drive.followTrajectory(forwards);
        Thread.sleep(100);
        drive.followTrajectory(returnus);
        drive.turn(Math.toRadians(-90));
        LinearSlideToStop2(3,35,0);
        drive.followTrajectory(right);
        susan(-462);
        Thread.sleep(1400);
        drive.followTrajectory(left);
        Thread.sleep(500);
        LinearSlideToStop2(9,35,0);
        Thread.sleep(700);
        claw(.25);
        Thread.sleep(500);
        LinearSlideToStop2(7,10,conesUp);
        conesUp++;
        susan(0);
        drive.followTrajectory(toCones);
        Thread.sleep(250);
        claw(.6);
        Thread.sleep(500);
        LinearSlideToStop2(1,20,conesUp);
        Thread.sleep(500);
        drive.followTrajectory(toPole);
        LinearSlideToStop2(3,30,0);
        Thread.sleep(1500);
        susan(-391);
        Thread.sleep(500);
        LinearSlideToStop2(9,30,0);
        Thread.sleep(750);
        claw(.25);
        Thread.sleep(750);
        susan(0);
        LinearSlideToStop2(0,35,0);
        claw(.32);
        if(NumberOfTag == 2)
            drive.followTrajectory(parkPosition1);
        else if(NumberOfTag == 1)
            drive.followTrajectory(parkPosition2);
        else if(NumberOfTag == 3)
            drive.followTrajectory(parkPosition3);
        else
            drive.followTrajectory(parkPosition2);
        Thread.sleep(500);
    }

    protected void setTrajectories() {
        forwards = drive.trajectoryBuilder(new Pose2d())

                .lineTo( new Vector2d(55,0),
                        drive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        drive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        returnus = drive.trajectoryBuilder(forwards.end())
                .lineTo(new Vector2d(50,0),
                        drive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        drive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        right = drive.trajectoryBuilder(returnus.end().plus(new Pose2d(0, 0, Math.toRadians(-90))))
                .lineTo( new Vector2d(50,7.6),
                        drive.getVelocityConstraint(13, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        drive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        left = drive.trajectoryBuilder(right.end())
                .strafeTo( new Vector2d(52.75,12.2),
                        drive.getVelocityConstraint(4, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        drive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        toCones = drive.trajectoryBuilder(left.end())
                .lineTo( new Vector2d(49.8,-24.4),
                        drive.getVelocityConstraint(24, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        drive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        toPole = drive.trajectoryBuilder(toCones.end())
                .lineTo(new Vector2d(52.5,12.4),

                        drive.getVelocityConstraint(12, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        drive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        parkPosition1 = drive.trajectoryBuilder(toPole.end())
                .lineToLinearHeading(new Pose2d(48,24,Math.toRadians(270)))
                .build();

        parkPosition3 = drive.trajectoryBuilder(toPole.end())
                .lineToLinearHeading(new Pose2d(48,-24,Math.toRadians(270)))
                .build();

        parkPosition2 = drive.trajectoryBuilder(toPole.end())
                .lineToLinearHeading(new Pose2d(48,0,Math.toRadians(270)))
                .build();
    }

}


