package org.firstinspires.ftc.teamcode.gamepadyn;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Opticon.Event;
import org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Objects;

// Returned by Gamepadyn.getGamepad()
public final class Gamepad {

    @NonNull
    public ActionSource action(UserActions ua) {
        return Objects.requireNonNull(_actions.get(ua));
    }

    private final EnumMap<UserActions, ActionSource> _actions = new EnumMap<>(UserActions.class);

    private Gamepad() {
        // fill
        for (UserActions ua : UserActions.values()) {
             _actions.put(ua, new ActionSource(ua.type));
        }
    }
}
