package org.firstinspires.ftc.teamcode.gamepadyn;

import androidx.annotation.Nullable;

import org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions;

// Configuration "Parameter"
public final class MappingActionDigital extends MappingAction {
    public enum Mode {
//        Trigger a digital action.
        TRIGGER,
//        Directly set the value of a digital
        ANALOG_MAP,
//        Offset the value of a digital axis by a specified value.
//        The offset is applied in only one direction, and is clamped to -1, 1.
        ANALOG_OFFSET
    }

    public final MappingActionDigital.Mode mode;
    public final UserActions action;

//    Shared settings

    public Integer axis = null;

//    Analog Map settings

    public Float depressedValue = null;
    public Float releasedValue = null;

//    Analog Offset settings

    public Float offsetRate = null;

    MappingActionDigital(MappingActionDigital.Mode mode, UserActions action, float depressedValue, @Nullable Float releasedValue, int axis) throws Exception {
        if (mode != MappingActionDigital.Mode.ANALOG_MAP) throw new Exception("Mismatched mode, expected ANALOG_MAP!");
        this.mode = mode;
        this.action = action;
        this.depressedValue = depressedValue;
        this.releasedValue = releasedValue;
        this.axis = axis;
    }

    MappingActionDigital(MappingActionDigital.Mode mode, UserActions action, float offsetRate, int axis) throws Exception {
        if (mode != MappingActionDigital.Mode.ANALOG_OFFSET) throw new Exception("Mismatched mode, expected ANALOG_OFFSET!");
        this.mode = mode;
        this.action = action;
        this.offsetRate = offsetRate;
        this.axis = axis;
    }

    MappingActionDigital(MappingActionDigital.Mode mode, UserActions action) throws Exception {
        if (mode != MappingActionDigital.Mode.TRIGGER) throw new Exception("Mismatched mode, expected TRIGGER!");
        this.mode = mode;
        this.action = action;
    }
}
