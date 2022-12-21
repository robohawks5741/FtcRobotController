package org.firstinspires.ftc.teamcode.gamepadyn;
import org.firstinspires.ftc.teamcode.Opticon.Event;
import org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions;

// ""emitter""
public final class ActionSource {

    public final Event<Object> event = new Event<>();

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
