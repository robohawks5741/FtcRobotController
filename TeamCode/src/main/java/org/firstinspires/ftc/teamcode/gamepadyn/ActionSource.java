package org.firstinspires.ftc.teamcode.gamepadyn;
import org.firstinspires.ftc.teamcode.eventd.EventEmitter;

// ""emitter""
public final class ActionSource {
    public final EventEmitter<Object> emitter = new EventEmitter<>();
    public Object getValue() { return internalValue; }

    @SuppressWarnings("FieldMayBeFinal")
    Object internalValue = null;
    public final InputType type;

    public ActionSource(InputType t) {
        type = t;
//        Map<UserActions, ActionSource>

//        UserActions.values();
    }
}
