package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;

public interface AUTOinterface {
    boolean LinearSlideToStop2(int stop, int tolerance, int conesUp);
    void FirstCone() throws InterruptedException;
}
