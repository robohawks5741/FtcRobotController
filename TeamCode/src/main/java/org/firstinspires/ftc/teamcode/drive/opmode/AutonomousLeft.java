package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

@Autonomous
public class AutonomousLeft extends AutoSuperOpMode {

    @Override
    public void firstCone() throws InterruptedException {

        claw(.6);
        Thread.sleep(700);
        susan(80);
        Thread.sleep(500);
        susan(0);
        Thread.sleep(600);
        linearSlideToStop(SlidePosition.AUTOMOVE, 30, 0);
        drive.followTrajectory(forwards);
        Thread.sleep(100);
        drive.followTrajectory(returnus);
        drive.turn(Math.toRadians(90));
        linearSlideToStop(SlidePosition.TALL, 35, 0);
        drive.followTrajectory(right);
        susan(462);
        Thread.sleep(1000);
        drive.followTrajectory(left);
        Thread.sleep(500);
        linearSlideToStop(SlidePosition.INSERT, 35, 0);
        Thread.sleep(200);
        claw(.25);
        Thread.sleep(500);
        linearSlideToStop(SlidePosition.CONESTACK, 10, conesUp);
        conesUp++;
        susan(0);
        drive.followTrajectory(toCones);
        Thread.sleep(250);
        claw(.6);
        Thread.sleep(500);
        linearSlideToStop(SlidePosition.LOW, 20, conesUp);
        Thread.sleep(500);
        drive.followTrajectory(toPole);
        linearSlideToStop(SlidePosition.TALL, 30, 0);
        Thread.sleep(1500);
        susan(440);
        Thread.sleep(500);
        linearSlideToStop(SlidePosition.INSERT, 30, 0);
        Thread.sleep(750);
        claw(.25);
        Thread.sleep(750);
        if (NumberOfTag == 2)
            drive.followTrajectory(parkPosition1);
        else if (NumberOfTag == 1)
            drive.followTrajectory(parkPosition2);
        else if (NumberOfTag == 3)
            drive.followTrajectory(parkPosition3);
        else
            drive.followTrajectory(parkPosition2);
        Thread.sleep(250);
        susan(0);
        Thread.sleep(1000);
        linearSlideToStop(SlidePosition.BOTTOM, 35, 0);
        Thread.sleep(1000);
        claw(.32);
        Thread.sleep(300);

    }

    @Override
    protected void setTrajectories() {
        forwards = drive.trajectoryBuilder(new Pose2d())

                .lineTo(new Vector2d(55, 0))
                .build();

        returnus = drive.trajectoryBuilder(forwards.end())
                .lineTo(new Vector2d(50, 0))
                .build();

        right = drive.trajectoryBuilder(returnus.end().plus(new Pose2d(0, 0, Math.toRadians(90))))
                .lineTo(new Vector2d(50, -7.2),
                        drive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        drive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        left = drive.trajectoryBuilder(right.end())
                .lineTo(new Vector2d(51, -9.9),
                        drive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        drive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        toCones = drive.trajectoryBuilder(left.end())
                .lineTo(new Vector2d(46.8, 25.9),
                        drive.getVelocityConstraint(24, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        drive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        toPole = drive.trajectoryBuilder(toCones.end())
                .lineTo(new Vector2d(51, -9.4),
                        drive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        drive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        parkPosition1 = drive.trajectoryBuilder(toPole.end())
                        .lineTo(new Vector2d(48, 24))
                        .build();

        parkPosition3 = drive.trajectoryBuilder(toPole.end())
                        .lineTo(new Vector2d(48, -24))
                        .build();

        parkPosition2 = drive.trajectoryBuilder(toPole.end())
                        .lineTo(new Vector2d(48, 0))
                        .build();
    }
}