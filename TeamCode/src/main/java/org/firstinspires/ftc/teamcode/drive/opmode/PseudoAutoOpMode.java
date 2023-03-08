package org.firstinspires.ftc.teamcode.drive.opmode;

public interface PseudoAutoOpMode {
    void susan(int pos);

    void claw(double pos);

    boolean LinearSlideToStop2(int stop, int tolerance, int conesUp);
    void FirstCone() throws InterruptedException;
}
