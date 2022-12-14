package org.firstinspires.ftc.teamcode.gamepadyn.user;

import org.firstinspires.ftc.teamcode.gamepadyn.ActionSource;
import org.firstinspires.ftc.teamcode.gamepadyn.InputType;
import static org.firstinspires.ftc.teamcode.gamepadyn.InputType.*;

/**
 * This is user-made code for the Robohawks. There's a 99.9% chance it won't work without configuration.
 * But, hey! It's meant to be configured! Change this however you want.
 *
 * In detail, this enum defines the possible actions in your codebase and their parameters/types.
 *
 * <table>
 *     <tr>
 *         <th>Action</th> <th><pre>Action Name  </pre></th> <th><pre>Data Type </pre></th> <th>Notes</th>
 *     </tr>
 *     <tr>
 *         <td>Linear Slide</td> <td><code>LINEAR_SLIDE</code></td> <td><code>ANALOG_1D</code></td> <td>Uses LSD (see the LSD documentation)</td>
 *     </tr>
 *     <tr>
 *         <td>Movement</td> <td><code>MOVEMENT</code></td> <td><code>ANALOG_2D</code></td> <td>X and Y aren't different actions, they're combined as a 2D analog action.</td>
 *     </tr>
 *     <tr>
 *         <td>Rotation</td> <td><code>ROTATION</code></td> <td><code>ANALOG_1D</code></td> <td>Yaw, measured in degrees (Euler rotation)</td>
 *     </tr>
 *     <tr>
 *          <td>Claw</td> <td><code>CLAW</code></td> <td><code>ANALOG_1D</code></td> <td>Yaw, measured in degrees (Euler rotation)</td>
 *     </tr>
 * </table>
 */
public enum UserActions {
    LINEAR_SLIDE    (ANALOG_1D),
    CLAW            (ANALOG_1D),
    MOVEMENT        (ANALOG_2D),
    ROTATION        (ANALOG_1D);

    public final InputType type;
    public final ActionSource source;

    UserActions(InputType t, ActionSource s) { type = t; source = s; }
}


// Controller 1:
//      left stick x = Yaw
//      right stick x/y = lateral
//      left bumper = set speed to max
//      right bumper = set speed to half

// Controller 2:
//      dpad-u = open claw
//      dpad-d = close claw
//      lt = ls up
//      rt = ls down
//      y = top
//      b = pickup pos/lowest/0
//      x = low pole
//      a = medium pole