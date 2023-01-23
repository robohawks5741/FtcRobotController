package org.firstinspires.ftc.teamcode.drive;

public interface SampleMecanumDrive2testJames {


    void holdSusan();

    void ResetSusan();

    double SusanEncoderPosition();

    void holdSlides();

    void setLinearSlide(double linearPower);

    //void LinearSlideToStop(int stop);

  //  void LinearSlideToStop(int stop, int power, int tolerance);

    //void LinearSlideToStop(int stop, double power, int tolerance);

    void susanToPosition(int targetPosition);

    boolean LinearSlideToStop(int stop, int conesUp, double power, int tolerance);

    boolean LinearSlideToStop2(int stop, int tolerance);

    void penetrate(double pos);

    int LinearSlidePos();

    void LinearSlideResetEnc();

   // void moveTestServo(float pos);

    void moveTestServo(double pos);

    void MoveSusan(double speed);

   // void zHopSusan();

    //void up();

   // void zHopSusan(double input);
}
