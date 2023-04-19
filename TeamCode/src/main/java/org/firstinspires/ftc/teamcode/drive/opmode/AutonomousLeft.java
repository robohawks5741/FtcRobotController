package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.RobohawksMecanumDrive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

@Autonomous
public class AutonomousLeft extends AutoSuperOpMode {

    @Override
    public void runAuto() throws InterruptedException {
        claw(.6);
        debugSplit("claw");
        sleep(700);

        susan(80);
        debugSplit("susan");
        sleep(200);

        susan(0);
        debugSplit("susan");
        sleep(300);

        linearSlideToStop(SlidePosition.AUTOMOVE, 30, 0);
        debugSplit("slide");
        drive.followTrajectory(forwards);
        debugSplit("followTrajectory");
        sleep(100);

        drive.followTrajectory(returnus);
        debugSplit("followTrajectory");
        drive.turn(Math.toRadians(84));
        debugSplit("turn");
        linearSlideToStop(SlidePosition.TALL, 35, 0);
        debugSplit("slide");
        drive.followTrajectory(right);
        debugSplit("followTrajectory");
        susan(462);
        debugSplit("susan");
        //sleep(1000);

        drive.followTrajectory(left);
        debugSplit("followTrajectory");
        sleep(250);

        linearSlideToStop(SlidePosition.INSERT, 35, 0);
        debugSplit("slide");
        sleep(200);

        claw(.25);
        debugSplit("claw");
        sleep(500);

        susan(0);
        debugSplit("susan");
        sleep(100);
        linearSlideToStop(SlidePosition.CONESTACK, 10, conesUp++);
        debugSplit("slide");

        drive.followTrajectory(toCones);
        debugSplit("followTrajectory");
        sleep(250);

        claw(.6);
        debugSplit("claw");
        sleep(300);

        linearSlideToStop(SlidePosition.LOW, 20, conesUp);
        debugSplit("slide");
        sleep(500);

        drive.followTrajectory(toPole);
        debugSplit("followTrajectory");
        linearSlideToStop(SlidePosition.TALL, 30, 0);
        debugSplit("slide");
        //sleep(1500);

        susan(440);
        debugSplit("susan");
        sleep(750);

        linearSlideToStop(SlidePosition.INSERT, 30, 0);
        debugSplit("slide");
        sleep(750);

        claw(.25);
        debugSplit("claw");
        sleep(250);

        switch (numberOfTag) {
            case 2: drive.followTrajectory(parkPosition1); break;
            case 3: drive.followTrajectory(parkPosition3); break;
            default:
            case 1: drive.followTrajectory(parkPosition2); break;
        }
        debugSplit("followTrajectory");
        sleep(250);

        susan(0);
        debugSplit("susan");
        sleep(1000);

        linearSlideToStop(SlidePosition.BOTTOM, 35, 0);
        debugSplit("slide");
        sleep(1000);

        claw(.32);
        debugSplit("claw");
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
                .lineTo(new Vector2d(46.4, 25.8),
                        RobohawksMecanumDrive.getVelocityConstraint(24, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        toPole = drive.trajectoryBuilder(toCones.end())
                .lineTo(new Vector2d(51, -9.8),
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