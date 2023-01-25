package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;

public interface RightConeDepositorAutonInterface {
    void LeftAndDump();

    void RightAndPickup();

    void CenterOnTile(Pose2d position);

    void Park(int location);

    //void firstCone();

    void FirstCone();
}
