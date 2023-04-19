package org.firstinspires.ftc.teamcode.gamepadyn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Objects;

// Returned by Gamepadyn.getGamepad()
public final class Gamepad {

    @NonNull
    public ActionSource action(UserActions ua) {
        return Objects.requireNonNull(actionSources.get(ua));
    }

    enum switchInput {
        UNKNOWN,
        DIGITAL,
        ANALOG
    }

    /**
     * @return The name/path/identifier of the configuration file.
     */
    @Nullable
    public String getConfiguration() { return configName; }

    // ActionSources (emitters + values) for each of the actions.
    private final EnumMap<UserActions, ActionSource> actionSources = new EnumMap<>(UserActions.class);

    /**
     * Map of buttons -> mapping actions
     */
    @SuppressWarnings("FieldMayBeFinal")
    EnumMap<RawGamepadInput, MappingAction> mapping = new EnumMap<>(RawGamepadInput.class);
    // In order to create event-driven input, we need to determine change. Tracking change requires recording state.
    // Object is of type RawGamepadInput.inputType.inputClass depending on the key.
    EnumMap<RawGamepadInput, Object> stateCache = new EnumMap<>(RawGamepadInput.class);

    // The name/path/identifier of the configuration file.
    @SuppressWarnings("FieldMayBeFinal")
    String configName = null;
    String configJson = null;

    // The index of the gamepad.
    public final int index;

    @SuppressWarnings("FieldMayBeFinal")
    private com.qualcomm.robotcore.hardware.Gamepad ftcGamepad;

    Gamepad(int index, com.qualcomm.robotcore.hardware.Gamepad ftcGamepad) {
        this.index = index;
        this.ftcGamepad = ftcGamepad;
        // fill
        for (UserActions ua : UserActions.values()) {
             actionSources.put(ua, new ActionSource(ua.type));
        }
        for (RawGamepadInput rin : RawGamepadInput.values()) {
            switch (rin.inputType) {
                case ANALOG: {
                    float[] av = new float[rin.axes];
                    Arrays.fill(av, 0);
                    stateCache.put(rin, new AnalogValue(av));
                    break;
                }
                case DIGITAL: {
                    stateCache.put(rin, false);
                    break;
                }
            }
        }
    }
}
