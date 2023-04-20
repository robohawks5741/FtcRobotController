package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.RobohawksMecanumDrive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

@Autonomous
public class parkAuto extends AutoSuperOpMode {

    protected void runAuto() {
        claw(.6);
        debugSplit("claw");
        sleep(600);

        susan(100);
        debugSplit("susan");
        sleep(300);

        susan(0);
        debugSplit("susan");
        sleep(200); /*

        linearSlideToStop(SlidePosition.AUTOMOVE,30,0);
        debugSplit("slide");
        drive.followTrajectory(forwards);
        debugSplit("followTrajectory");
        //sleep(100);

        drive.followTrajectory(returnus);
        debugSplit("followTrajectory");
        drive.turn(Math.toRadians(-84)); // TODO
        debugSplit("turn");
        linearSlideToStop(SlidePosition.TALL,35,0);
        drive.followTrajectory(right);
        debugSplit("followTrajectory");
        susan(-462);
        debugSplit("susan");
        //sleep(1400);

        drive.followTrajectory(left);
        sleep(10);

        linearSlideToStop(SlidePosition.INSERT, 35, 0);
        debugSplit("slide");
        //sleep(700);

        claw(.25);
        debugSplit("claw");
        sleep(250);

        susan(0);
        debugSplit("susan");
        sleep(450);
        linearSlideToStop(SlidePosition.CONESTACK, 10, conesUp++);
        debugSplit("slide");
        drive.followTrajectory(toCones);
        debugSplit("followTrajectory");
        //sleep(250);

        claw(.6);
        debugSplit("claw");
        sleep(400);

        linearSlideToStop(SlidePosition.LOW,20,conesUp++);
        debugSplit("slide");
        sleep(500);

        drive.followTrajectory(toPole);
        debugSplit("followTrajectory");
        linearSlideToStop(SlidePosition.TALL,35,0);
        debugSplit("slide");
        //sleep(1500);

        susan(-462);
        debugSplit("susan");
        sleep(1100);

        //linearSlideToStop(SlidePosition.INSERT,30,0);
        //sleep(400);

        claw(.25);
        debugSplit("claw");
        sleep(750);

        susan(0);
        debugSplit("susan");
        sleep(250);

        ////////////////////////////////////////////////////////////////// 3rd cone

        claw(.25);
        debugSplit("claw");
        sleep(250);

        susan(0);
        debugSplit("susan");
        sleep(450);
        linearSlideToStop(SlidePosition.CONESTACK, 10, conesUp++);
        debugSplit("slide");
        drive.followTrajectory(toCones);
        debugSplit("followTrajectory");
        //sleep(250);

        claw(.6);
        debugSplit("claw");
        sleep(400);

        linearSlideToStop(SlidePosition.LOW,20,conesUp++);
        debugSplit("slide");
        sleep(500);

        drive.followTrajectory(toPole);
        debugSplit("followTrajectory");
        linearSlideToStop(SlidePosition.TALL,35,0);
        debugSplit("slide");

        susan(-462);
        sleep(1500);
        debugSplit("susan");
        claw(.32);


        //////////////////////////////////////////////////////////

        susan(0);
        linearSlideToStop(SlidePosition.BOTTOM,35,0);
        debugSplit("slide");
        claw(.32);

 */
        sleep(15000);

        drive.followTrajectory(forwards);

        switch (numberOfTag) {
            case 2: drive.followTrajectory(parkPosition2); break;
            case 3: drive.followTrajectory(parkPosition3); break;
            case 1:
            default: drive.followTrajectory(parkPosition1); break;
        }
        debugSplit("followTrajectory");
        sleep(1000);
    }

    protected void setTrajectories() {
        forwards = drive.trajectoryBuilder(new Pose2d())

                .lineTo( new Vector2d(49,0),
                        RobohawksMecanumDrive.getVelocityConstraint(55, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        returnus = drive.trajectoryBuilder(forwards.end())
                .lineTo(new Vector2d(50,0),
                        RobohawksMecanumDrive.getVelocityConstraint(50, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        right = drive.trajectoryBuilder(returnus.end().plus(new Pose2d(0, 0, Math.toRadians(-84))))
                .lineTo( new Vector2d(50,7.6),
                        RobohawksMecanumDrive.getVelocityConstraint(50, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        // first cone
        left = drive.trajectoryBuilder(right.end())
                .strafeTo(new Vector2d(49.75, 12.75),
                        RobohawksMecanumDrive.getVelocityConstraint(50, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        toCones = drive.trajectoryBuilder(left.end())
                .lineTo(new Vector2d(54.5,-24.2),
                        RobohawksMecanumDrive.getVelocityConstraint(50, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();


        toPole = drive.trajectoryBuilder(toCones.end())
                .lineTo(new Vector2d(49.75, 12.7),
                        RobohawksMecanumDrive.getVelocityConstraint(50, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();
        toCones2 = drive.trajectoryBuilder(toPole.end())
                .lineTo( new Vector2d(54.5,-24.2),
                        RobohawksMecanumDrive.getVelocityConstraint(50, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();
        toPole2 = drive.trajectoryBuilder(toCones.end())
                .lineTo(new Vector2d(50,12.75),

                        RobohawksMecanumDrive.getVelocityConstraint(50, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        RobohawksMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();


        parkPosition2 = drive.trajectoryBuilder(toPole.end())
                .lineToLinearHeading(new Pose2d(48, 24, Math.toRadians(270)))
                .build();

        parkPosition3 = drive.trajectoryBuilder(toPole.end())
                .lineToLinearHeading(new Pose2d(48, -24, Math.toRadians(270)))
                .build();

        parkPosition1 = drive.trajectoryBuilder(toPole.end())
                .lineToLinearHeading(new Pose2d(48, 0, Math.toRadians(270)))
                .build();
    }

}


