package org.firstinspires.ftc.teamcode.gamepadyn;

import static org.firstinspires.ftc.teamcode.gamepadyn.InputType.ANALOG;
import static org.firstinspires.ftc.teamcode.gamepadyn.InputType.DIGITAL;

/**
 * Saves the state of
 */
class RawGamepadState {
    /// A (Face Button Down)
    boolean FD;
    /// B (Face Button Right)
    boolean FR;
    /// X (Face Button Left)
    boolean FL;
    /// Y (Face Button Up)
    boolean FU;
    /// D-Pad Up
    boolean DU;
    /// D-Pad Down
    boolean DD;
    /// D-Pad Left
    boolean DL;
    /// D-Pad Right
    boolean DR;
    /// Right Trigger
    float TR;
    /// Left Trigger
    float TL;
    /// Right Bumper
    boolean BR;
    /// Left Bumper
    boolean BL;
    /// Left Stick
    float[] SL;
    /// Left Stick Button
    boolean SLB;
    /// Right Stick
    float[] SR;
    /// Right Stick Button
    boolean SRB;

    public final InputType inputType;
    public final int axes;

    RawGamepadState(InputType inputType) { this.inputType = inputType; this.axes = 0; }
    RawGamepadState(InputType inputType, int axes) { this.inputType = inputType; this.axes = axes; }
}
