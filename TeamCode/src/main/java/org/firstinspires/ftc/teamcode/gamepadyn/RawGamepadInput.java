package org.firstinspires.ftc.teamcode.gamepadyn;

import static org.firstinspires.ftc.teamcode.gamepadyn.InputType.*;

/**
 * An enum representing the different inputs on a standard competition-legal controller.
 */
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
    TR  (ANALOG, 1),
    /// Left Trigger
    TL  (ANALOG, 1),
    /// Right Bumper
    BR  (DIGITAL),
    /// Left Bumper
    BL  (DIGITAL),
    /// Left Stick
    SL  (ANALOG, 2),
    /// Left Stick Button
    SLB (DIGITAL),
    /// Right Stick
    SR  (ANALOG, 2),
    /// Right Stick Button
    SRB (DIGITAL);

    public final InputType inputType;
    public final int axes;

    RawGamepadInput(InputType inputType) {
        this.inputType = inputType;
        this.axes = (inputType == DIGITAL ? 0 : 1);
    }
    RawGamepadInput(InputType inputType, int axes) { this.inputType = inputType; this.axes = axes; }
}
