package org.firstinspires.ftc.teamcode.gamepadyn;

import static org.firstinspires.ftc.teamcode.gamepadyn.InputType.*;

public enum Gamepad {
    /// A (Face Button Down)
    FD  (DIGITAL),
    /// B (Face Button Right)
    FR  (DIGITAL),
    /// X (Face Button Left)
    FL  (DIGITAL),
    /// Y (Face Button Up)
    FU  (DIGITAL),
    /// D-Pad Up
    DU  (DIGITAL),
    /// D-Pad Down
    DD  (DIGITAL),
    /// D-Pad Left
    DL  (DIGITAL),
    /// D-Pad Right
    DR  (DIGITAL),
    /// Right Trigger
    TR  (ANALOG_1D),
    /// Left Trigger
    TL  (ANALOG_1D),
    /// Right Bumper
    BR  (DIGITAL),
    /// Left Bumper
    BL  (DIGITAL),
    /// Left Stick
    SL  (ANALOG_2D),
    /// Right Stick
    SR  (ANALOG_2D),
    /// Left Stick Button
    SBL (DIGITAL),
    /// Right Stick Button
    SBR (DIGITAL);

    public final InputType inputType;

    Gamepad(InputType t) { this.inputType = t; }
}
