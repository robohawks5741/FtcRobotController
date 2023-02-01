package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;

public interface AUTOinterface {
    void LeftAndDump();

    void RightAndPickup();

    void CenterOnTile(Pose2d position);

    void Park(int location);

    void FirstCone() throws InterruptedException;
}
