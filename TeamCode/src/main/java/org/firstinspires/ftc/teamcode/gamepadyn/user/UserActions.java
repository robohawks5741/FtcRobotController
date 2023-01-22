package org.firstinspires.ftc.teamcode.gamepadyn.user;

import org.firstinspires.ftc.teamcode.gamepadyn.InputType;
import static org.firstinspires.ftc.teamcode.gamepadyn.InputType.*;

/**
 * This is user-made code for the Robohawks. There's a 99.9% chance it won't work without configuration.
 * But, hey! It's meant to be configured! Change this however you want.
 * <br><br>
 * This enum defines the possible actions in your codebase and their parameters/types.
 */
public enum UserActions {
    // Change these to fit your needs
    LINEAR_SLIDE        (ANALOG, 1),
    LINEAR_SLIDE_BOTTOM (DIGITAL),
    LINEAR_SLIDE_LOW    (DIGITAL),
    LINEAR_SLIDE_MIDDLE (DIGITAL),
    LINEAR_SLIDE_HIGH   (DIGITAL),
    LINEAR_SLIDE_TOP    (DIGITAL),
    MOTOR_SPEED_HALF    (DIGITAL),
    MOTOR_SPEED_FULL    (DIGITAL),
    CLAW                (ANALOG, 1),
    HALO                (ANALOG, 1),
    MOVEMENT            (ANALOG, 2),
    ROTATION            (ANALOG, 1);

    // DON'T CHANGE ANYTHING BELOW HERE!
    public final InputType type;
    public final int axes;
    UserActions(InputType type) { this.type = type; this.axes = 0; }
    UserActions(InputType type, int axes) { this.type = type; this.axes = axes; }
}