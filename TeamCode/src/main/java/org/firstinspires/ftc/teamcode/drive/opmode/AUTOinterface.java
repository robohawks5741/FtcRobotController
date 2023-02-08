package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;

public interface AUTOinterface {
    boolean LinearSlideToStop2(int stop, int tolerance, int conesUp);

    void LeftAndDump();

    void RightAndPickup();

    void CenterOnTile(Pose2d position);

    void Park(int location);

    void FirstCone() throws InterruptedException;
}
