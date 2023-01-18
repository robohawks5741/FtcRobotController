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
 *         <th>Action</th> <th><pre>Action Name</pre></th> <th><pre>Data Type </pre></th> <th>Notes</th>
 *     </tr>
 *     <tr>
 *         <td>Linear Slide</td> <td><code>LINEAR_SLIDE</code></td> <td><code>ANALOG_1D</code></td> <td>Uses LSD (see the LSD documentation)</td>
 *     </tr>
 *     <tr>
 *         <td>Linear Slide Bottom</td> <td><code>LINEAR_SLIDE_BOTTOM</code></td> <td><code>DIGITAL</code></td> <td></td>
 *     </tr>
 *     <tr>
 *         <td>Linear Slide Low</td> <td><code>LINEAR_SLIDE_LOW</code></td> <td><code>DIGITAL</code></td> <td></td>
 *     </tr>
 *     <tr>
 *         <td>Linear Slide Middle</td> <td><code>LINEAR_SLIDE_MIDDLE</code></td> <td><code>DIGITAL</code></td> <td></td>
 *     </tr>
 *     <tr>
 *         <td>Linear Slide High</td> <td><code>LINEAR_SLIDE_HIGH</code></td> <td><code>DIGITAL</code></td> <td></td>
 *     </tr>
 *     <tr>
 *         <td>Linear Slide Top</td> <td><code>LINEAR_SLIDE_TOP</code></td> <td><code>DIGITAL</code></td> <td></td>
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
    LINEAR_SLIDE        (ANALOG, 1),
    LINEAR_SLIDE_BOTTOM (DIGITAL),
    LINEAR_SLIDE_LOW    (DIGITAL),
    LINEAR_SLIDE_MIDDLE (DIGITAL),
    LINEAR_SLIDE_HIGH   (DIGITAL),
    LINEAR_SLIDE_TOP    (DIGITAL),
    CLAW                (ANALOG, 1),
    MOVEMENT            (ANALOG, 2),
    ROTATION            (ANALOG, 2);

    public final InputType type;
    public final int axes;

    UserActions(InputType type) { this.type = type; this.axes = 0; }
    UserActions(InputType type, int axes) { this.type = type; this.axes = axes; }
}