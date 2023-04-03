package org.firstinspires.ftc.teamcode.gamepadyn;

// RawGamepadInput MappingAction
class IntermediateMapping {
    static class DigitalMap {
        // one of MappingActionDigital.name()
        String mode;
        String action;
        //    Shared settings
        Integer axis;
        //    Analog Map settings
        Float depressedValue;
        Float releasedValue;
        //    Analog Offset settings
        Float offsetRate;
    }

    static class AxisMap {
        Float axisScale;
        // key is one of UserActions.name()
        String targetAction;
        Integer targetAxis;
        String mode;
    }

    static class AnalogMap {
        // one of MappingActionAnalog.name()
        String mode;
        int axes;
        //    Shared Settings
        Float[] inputScale;
        Float[] inputOffset;
        //    One to One Axis settings
        String action;
        //    Split Axis settings
        AxisMap[] axisMaps;
    }

    static class MapsField {
        MappingActionDigital fd;
        MappingActionDigital fr;
        MappingActionDigital fl;
        MappingActionDigital fu;
        MappingActionDigital du;
        MappingActionDigital dd;
        MappingActionDigital dl;
        MappingActionDigital dr;
        MappingActionAnalog tr;
        MappingActionAnalog tl;
        MappingActionDigital br;
        MappingActionDigital bl;
        MappingActionAnalog sr;
        MappingActionDigital srb;
        MappingActionAnalog sl;
        MappingActionDigital slb;
//        DigitalMap fd;
//        DigitalMap fr;
//        DigitalMap fl;
//        DigitalMap fu;
//        DigitalMap du;
//        DigitalMap dd;
//        DigitalMap dl;
//        DigitalMap dr;
//        AnalogMap tr;
//        AnalogMap tl;
//        DigitalMap br;
//        DigitalMap bl;
//        AnalogMap sr;
//        DigitalMap srb;
//        AnalogMap sl;
//        DigitalMap slb;
    }

    String description;
    MapsField maps;
}
