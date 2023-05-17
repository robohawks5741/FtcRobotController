package org.firstinspires.ftc.teamcode.gamepadyn;

// ""emitter""
public final class Action {
    public final EventEmitter<Object> emitter = new EventEmitter<>();
    public Object getValue() { return internalValue; }

    @SuppressWarnings("FieldMayBeFinal")
    Object internalValue = null;
    public final InputType type;

    public Action(InputType t) {
        type = t;
//        Map<UserActions, ActionSource>

//        UserActions.values();
    }
}
