package org.firstinspires.ftc.teamcode.lsd;

import com.qualcomm.robotcore.hardware.HardwareMap;

// Linear Slide Driver
public class LSD {

    // Enum for the position of the slide
    enum SlideHeight {
        UNKNOWN,
        BOTTOM,
        MIDDLE,
        TOP
    }

    LSD(HardwareMap hmap) {
        this._hardwareMap = hmap;
    }

    public boolean isMoving() { return _isMoving; }
    public SlideHeight currentHeight() { return _currentHeight; }
    public int currentHeightPrecise() { return _currentMotorHeight; }

    // TODO: should this be void?
    public Thread moveSlide(SlideHeight target) throws Exception {
        if (_isMoving) { }

        Thread t = new Thread(() -> {
            switch (target) {
                case TOP:
                    break;
                case MIDDLE:
                    break;
                case BOTTOM:
                    break;
                case UNKNOWN:
                default:
//                    TODO: use notifiers to properly throw an exception
                    System.out.println("Invalid LSD Slide Position!");
//                    throw new Exception("Invalid LSD Slide Position!");
                    // break;
            }
        });
        t.notifyAll();
    }

//
    protected final HardwareMap _hardwareMap;
//    Whether or not we're currently moving the slide
    protected boolean _isMoving;
//    The slide's current position, as a height enumerator
    protected SlideHeight _currentHeight = SlideHeight.UNKNOWN;
    protected int _currentMotorHeight;
//    The thread (agent) in charge of managing the LSD's "state machine."
//    The LSD functions can be interrupted (they're asynchronous, not blocking)
//    so we need a thread agent in charge of actually controlling the motor.
//    The public methods/setters set the TARGET of the driver, and the agent moves the motor to the
//    target position. If the target position changes while it's moving, no biggie,
//    the agent will just change course and move it to the new target location.
    protected Thread mkuAgent;
}
