package org.firstinspires.ftc.teamcode.gamepadyn;
import org.firstinspires.ftc.teamcode.Opticon.EventEmitter;

// ""emitter""
public final class ActionSource {

    public final EventEmitter<Object> emitter = new EventEmitter<>();

    public Object getValue() { return _value; }

    @SuppressWarnings("FieldMayBeFinal")
    private Object _value = null;
    public final InputType type;

    public ActionSource(InputType t) {
        type = t;
//        Map<UserActions, ActionSource>

//        UserActions.values();
    }
}
