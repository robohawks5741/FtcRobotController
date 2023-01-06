package org.firstinspires.ftc.teamcode.gamepadyn;

import android.util.Pair;

import org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions;

// Configuration "Parameter"
public final class MappingActionAnalog extends MappingAction {

    public enum Mode {
//        Maps directly onto an X-dimensional action. This is the simplest and probably what you want for standard uses.
//        Unless you're the Robohawks because then you'll use SPLIT_AXIS. Don't ask why.
        ONE_TO_ONE_AXES,
//        Each axis is mapped onto an analog action's axis, with up to X axis->action maps.
//        Like ONE_TO_ONE_AXES, but you have control over each axis (and can leave axes unmapped)
//        This could be used for having DOOM-like controls (i.e. horizontal axis to rotation, vertical axis to movement).
        SPLIT_AXES,
//        Maps different polar X-dimensional positions (i.e. (0, 1), (-1), (1, 1, 1))
//        onto digital inputs (button is held while analog axis is in position, is released while it isn't).
        POSITION_DIGITAL
    }

    public enum AnalogMode {
        // Sets the value of the axis
        SET,
        // Adds the value to the axis.
        OFFSET
    }

    public enum AxisPolarity {
        // The relaxed/neutral state of the axis
        NO_POLARITY (0),

        POLAR_MINIMUM (-1),

        POLAR_MAXIMUM (1);

        AxisPolarity(int value) { }
    };

    public final int axes;
    public final MappingActionAnalog.Mode mode;

//    Shared Settings

    // Multiplies the values of axis. Depending on the AnalogMode where applicable, it may be clamped.
    final float[] inputScale;
    // Adds a constant value to each axis. May be clamped (see inputScale)
    final float[] inputOffset;

//    One to One Axis settings

    // The action with the SAME AMOUNT OF AXIS to map onto.
    final UserActions action;

//    Split Axis settings

    public static final class AxisMap {
        final float axisScale;
        // Pair of the action and the axis of that action.
        final Pair<UserActions, Integer> target;
        AxisMap(Float axisScale, Pair<UserActions, Integer> target) {
            if (axisScale == null) this.axisScale = 1;
            else this.axisScale = axisScale;
            this.target = target;
        }
    }

    AxisMap[] axisMaps;

//    Position Digital settings

    public static final class PositionMap {
        final double axisScale;
        // Actions are only triggered if the axis value * inputScale passes the threshold of (1 - tolerance) (after scaling is applied)
        final double tolerance;
        PositionMap(Float axisScale, Float tolerance) {
            if (axisScale == null) this.axisScale = 1;
            else this.axisScale = axisScale;
            if (tolerance == null) this.tolerance = 0.125;
            else this.tolerance = tolerance;

        }
    }

    PositionMap[] positionMaps;

    MappingActionAnalog(int axes, Mode mode, float[] inputScale, UserActions action) throws ArrayIndexOutOfBoundsException {
        this.axes = axes;
        this.mode = mode;
        this.inputScale = inputScale;
        this.action = action;

        if (
                (inputScale != null && inputScale.length > axes) &&
//              (inputScale != null && inputScale.length > axes)
        ) {
            throw new ArrayIndexOutOfBoundsException("Axis map length mismatches axis count!");
        }
    }
}
