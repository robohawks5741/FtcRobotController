package org.firstinspires.ftc.teamcode.gamepadyn;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.checkerframework.common.value.qual.IntVal;
import org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions;

import java.util.Arrays;

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
//        NOTE: Currently scrapped due to time constraints.
//        Maps different polar X-dimensional positions (i.e. (0, 1), (-1), (1, 1, 1))
//        onto digital inputs (button is held while analog axis is in position, is released while it isn't).
//        POSITION_DIGITAL
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

//    One to One Axis settings

    // The action with the SAME AMOUNT OF AXIS to map onto.
    UserActions action;
    // Multiplies the values of the input axes by each element.
    final Float[] inputScale;

//    Split Axis settings

    public static final class AxisMap {
        final float axisScale;
        // Pair of the action and the axis of that action.
        final Pair<UserActions, Integer> target;
        final AnalogMode mode;
        AxisMap(@Nullable Float axisScale, @NonNull Pair<UserActions, Integer> target, AnalogMode mode) {
            this.axisScale = (axisScale == null) ? 1 : axisScale;
            this.target = target;
            this.mode = mode;
        }
    }

    /**
     * The mappings for each axis. It MUST have {@link MappingActionAnalog#axes}-many elements, but they may be {@code null}.
     */
    AxisMap[] axisMaps;

//    Position Digital settings
//
//    public static final class PositionMap {
//        final double axisScale;
//        // Actions are only triggered if the axis value * inputScale passes the threshold of (1 - tolerance) (after scaling is applied)
//        final double tolerance;
//        PositionMap(Float axisScale, Float tolerance) {
//            if (axisScale == null) this.axisScale = 1;
//            else this.axisScale = axisScale;
//            if (tolerance == null) this.tolerance = 0.125;
//            else this.tolerance = tolerance;
//
//        }
//    }
//
//    PositionMap[] positionMaps;

    /**
     * THIS IS THE CONSTRUCTOR FOR THE ONE TO ONE AXES MODE.
     * @param axes The number of axes. Must be positive.
     * @param inputScale A multiplier to scale the input by.
     * @param inputOffset A value to offset the value by.
     *
     * @param action The action.
     */
    public MappingActionAnalog(int axes, @Nullable Float[] inputScale, @Nullable Float[] inputOffset,
                               UserActions action
   ) throws Exception {
        this(axes, Mode.ONE_TO_ONE_AXES, inputScale, inputOffset);
        this.action = action;
        this.axisMaps = null;
   }

    /**
     * THIS IS THE CONSTRUCTOR FOR THE SPLIT AXES MODE.
     * @param axes The number of axes. Must be positive.
     * @param inputScale A multiplier to scale the input by.
     * @param inputOffset A value to offset the value by.
     *
     * @param axisMaps The maps for each axis.
     */
    public MappingActionAnalog(int axes, @Nullable Float[] inputScale, @Nullable Float[] inputOffset,
                               AxisMap[] axisMaps
    ) throws Exception {
        this(axes, Mode.ONE_TO_ONE_AXES, inputScale, inputOffset);
        this.axisMaps = axisMaps;
        this.action = null;
    }

    private MappingActionAnalog(int axes, Mode mode, @Nullable Float[] inputScale, @Nullable Float[] inputOffset) throws Exception {
        this.axes = axes;
        if (axes <= 0) throw new Exception();
        this.mode = mode;
        if (inputScale == null) {
            this.inputScale = new Float[axes];
            Arrays.fill(this.inputScale, 1.f);
        } else this.inputScale = inputScale;
        this.inputOffset = inputOffset;
        if (
                (inputScale != null && inputScale.length > axes) &&
                        (inputOffset != null && inputOffset.length > axes)
        ) throw new ArrayIndexOutOfBoundsException("Axis map length mismatches axis count!");
    }
}
