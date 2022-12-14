package org.firstinspires.ftc.teamcode.pd;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class pd {

    private pd() {}

    // Sets the servo position. This is manual.
    public static void penetrate(double pos){
        _servo.setPosition(pos);
    }

    // Pseudo-constructor for a singleton pattern. Can be called multiple times without throwing (please try to avoid it though!)
    public static boolean opmodeInit(@NonNull HardwareMap hmap, @NonNull OpMode op) {
        if (_hardwareMap == null) {
            _hardwareMap = hmap;
            _servo = _hardwareMap.get(Servo.class, "penetrationServo");
            return true;
        } else {
            return false;
        }
    }

    private static HardwareMap _hardwareMap = null;
    private static Servo _servo = null;
//    private static pd _instance = new pd();
}
