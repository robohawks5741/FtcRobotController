package org.firstinspires.ftc.teamcode;

public interface SampleMecanumDrive2testJames {


    void setLinearSlide(double linearPower);

    //void LinearSlideToStop(int stop);

  //  void LinearSlideToStop(int stop, int power, int tolerance);

    void LinearSlideToStop(int stop, double power, int tolerance);

    void LinearSlideToStop2(int stop, int tolerance);

    void penetrate(double pos);

    int LinearSlidePos();

    void LinearSlideResetEnc();

   // void moveTestServo(float pos);

    void moveTestServo(double pos);
}
