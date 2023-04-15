package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.RobohawksMecanumDrive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

@Autonomous
public class AutonomousLeft extends AutoSuperOpMode {

    @Override
    public void firstCone() throws InterruptedException {
        claw(.6);
        sleep(700);

        susan(80);
        sleep(500);

        susan(0);
        sleep(600);

        linearSlideToStop(SlidePosition.AUTOMOVE, 30, 0);
        drive.followTrajectory(forwards);
        sleep(100);

        drive.followTrajectory(returnus);
        drive.turn(Math.toRadians(84));
        linearSlideToStop(SlidePosition.TALL, 35, 0);
        drive.followTrajectory(right);
        susan(462);
        sleep(1000);

        drive.followTrajectory(left);
        sleep(500);

        linearSlideToStop(SlidePosition.INSERT, 35, 0);
        sleep(200);

        claw(.25);
        sleep(237);

        susan(0);
        sleep(500);
        linearSlideToStop(SlidePosition.CONESTACK, 10, conesUp++);

        drive.followTrajectory(toCones);
        sleep(250);

        claw(.6);
        sleep(500);

        linearSlideToStop(SlidePosition.LOW, 20, conesUp);
        sleep(500);

        drive.followTrajectory(toPole);
        linearSlideToStop(SlidePosition.TALL, 30, 0);
        sleep(1500);

        susan(440);
        sleep(500);

        linearSlideToStop(SlidePosition.INSERT, 30, 0);
        sleep(750);

        claw(.25);
        sleep(750);

        switch (numberOfTag) {
            case 2: drive.followTrajectory(parkPosition1); break;
            case 3: drive.followTrajectory(parkPosition3); break;
            default:
            case 1: drive.followTrajectory(parkPosition2); break;
        }
        sleep(250);

        susan(0);
        sleep(1000);

        linearSlideToStop(SlidePosition.BOTTOM, 35, 0);
        sleep(1000);

        claw(.32);
        sleep(300);

    }

    @Override
    protected void setTrajectories() {
        forwards = drive.trajectoryBuilder(new Pose2d())

                .lineTo(new Vector2d(55, 0))
                .build();

        returnus = drive.trajectoryBuilder(forwards.end())
                .lineTo(new Vector2d(50, 0))
                .build();

        right = drive.trajectoryBuilder(returnus.end().plus(new Pose2d(0, 0, Math.toRadians(84))))
                .lineTo(new Vector2d(50, -8.2),
                        RobohawksMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        left = drive.trajectoryBuilder(right.end())
                .lineTo(new Vector2d(50, -9.3),
                        RobohawksMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        toCones = drive.trajectoryBuilder(left.end())
                .lineTo(new Vector2d(46.4, 26.2),
                        RobohawksMecanumDrive.getVelocityConstraint(24, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        toPole = drive.trajectoryBuilder(toCones.end())
                .lineTo(new Vector2d(51, -9.4),
                        RobohawksMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
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