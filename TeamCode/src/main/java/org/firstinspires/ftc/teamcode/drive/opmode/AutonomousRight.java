package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.RobohawksMecanumDrive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

@Autonomous
public class AutonomousRight extends AutoSuperOpMode {

    protected void firstCone() throws InterruptedException {

        claw(.6);
        Thread.sleep(700);
        susan(80);
        Thread.sleep(500);
        susan(0);
        Thread.sleep(600);
        linearSlideToStop(SlidePosition.AUTOMOVE,30,0);
        drive.followTrajectory(forwards);
        Thread.sleep(100);
        drive.followTrajectory(returnus);
        drive.turn(Math.toRadians(-90));
        linearSlideToStop(SlidePosition.TALL,35,0);
        drive.followTrajectory(right);
        susan(-462);
        Thread.sleep(1400);
        drive.followTrajectory(left);
        Thread.sleep(500);
        linearSlideToStop(SlidePosition.INSERT,35,0);
        Thread.sleep(700);
        claw(.25);
        Thread.sleep(500);
        linearSlideToStop(SlidePosition.CONESTACK,10,conesUp);
        conesUp++;
        susan(0);
        drive.followTrajectory(toCones);
        Thread.sleep(250);
        claw(.6);
        Thread.sleep(500);
        linearSlideToStop(SlidePosition.LOW,20,conesUp);
        Thread.sleep(500);
        drive.followTrajectory(toPole);
        linearSlideToStop(SlidePosition.TALL,30,0);
        Thread.sleep(1500);
        susan(-391);
        Thread.sleep(500);
        linearSlideToStop(SlidePosition.INSERT,30,0);
        Thread.sleep(750);
        claw(.25);
        Thread.sleep(750);
        susan(0);
        linearSlideToStop(SlidePosition.BOTTOM,35,0);
        claw(.32);
        if(numberOfTag == 2)
            drive.followTrajectory(parkPosition1);
        else if(numberOfTag == 1)
            drive.followTrajectory(parkPosition2);
        else if(numberOfTag == 3)
            drive.followTrajectory(parkPosition3);
        else
            drive.followTrajectory(parkPosition2);
        Thread.sleep(500);
    }

    protected void setTrajectories() {
        forwards = drive.trajectoryBuilder(new Pose2d())

                .lineTo( new Vector2d(55,0),
                        RobohawksMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        returnus = drive.trajectoryBuilder(forwards.end())
                .lineTo(new Vector2d(50,0),
                        RobohawksMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        right = drive.trajectoryBuilder(returnus.end().plus(new Pose2d(0, 0, Math.toRadians(-90))))
                .lineTo( new Vector2d(50,7.6),
                        RobohawksMecanumDrive.getVelocityConstraint(13, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        left = drive.trajectoryBuilder(right.end())
                .strafeTo( new Vector2d(52.75,12.2),
                        RobohawksMecanumDrive.getVelocityConstraint(4, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        toCones = drive.trajectoryBuilder(left.end())
                .lineTo( new Vector2d(49.8,-24.4),
                        RobohawksMecanumDrive.getVelocityConstraint(24, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        toPole = drive.trajectoryBuilder(toCones.end())
                .lineTo(new Vector2d(52.5,12.4),

                        RobohawksMecanumDrive.getVelocityConstraint(12, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
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


