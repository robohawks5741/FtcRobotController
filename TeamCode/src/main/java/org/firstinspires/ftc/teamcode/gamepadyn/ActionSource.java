package org.firstinspires.ftc.teamcode.gamepadyn;
import org.firstinspires.ftc.teamcode.Opticon.Event;
import org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions;

public class ActionSource<T> {

    public final Event<T> event;

    public T getValue() { return _value; }

    private T _value = null;

    public ActionSource(Event<T> ev) {
        event = ev;
//        Map<UserActions, ActionSource>

//        UserActions.values();
    }
}
