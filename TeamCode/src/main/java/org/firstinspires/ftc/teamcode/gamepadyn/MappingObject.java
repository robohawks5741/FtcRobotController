package org.firstinspires.ftc.teamcode.gamepadyn;

import java.util.Map;

// RawGamepadInput MappingAction
class MappingObject {
    public static class DummyMap { }

    public static class DigitalMap extends DummyMap {
        // one of MappingActionDigital.name()
        public String mode;
        public String action;
        //    Shared settings
        public Integer axis = null;
        //    Analog Map settings
        public Float depressedValue = null;
        public Float releasedValue = null;
        //    Analog Offset settings
        public Float offsetRate = null;
    }
    public static class AxisMap {
        public Float axisScale;
        // key is one of UserActions.name()
        public String targetAction;
        public Integer targetAxis;
        public String mode;
    }

    public static class AnalogMap extends DummyMap {
        // one of MappingActionAnalog.name()
        public String mode;
        public int axes;
        //    Shared Settings
        public Float[] inputScale;
        public Float[] inputOffset;
        //    One to One Axis settings
        public String action;
        //    Split Axis settings
        public AxisMap[] axisMaps;
    }

    public String description;
    public Map<String, Map<String, Object>> maps;
    // MappingObject() {}
}
