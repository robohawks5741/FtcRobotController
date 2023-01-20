package org.firstinspires.ftc.teamcode.gamepadyn;

import static org.firstinspires.ftc.teamcode.gamepadyn.InputType.*;

public enum RawGamepadInput {
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
    TR  (ANALOG),
    /// Left Trigger
    TL  (ANALOG, 1),
    /// Right Bumper
    BR  (DIGITAL),
    /// Left Bumper
    BL  (DIGITAL),
    /// Left Stick
    SL  (ANALOG, 2),
    /// Right Stick
    SR  (ANALOG, 2),
    /// Left Stick Button
    SBL (DIGITAL),
    /// Right Stick Button
    SBR (DIGITAL);

    public final InputType inputType;
    public final int axes;

    RawGamepadInput(InputType inputType) { this.inputType = inputType; this.axes = 0; }
    RawGamepadInput(InputType inputType, int axes) { this.inputType = inputType; this.axes = axes; }
}
