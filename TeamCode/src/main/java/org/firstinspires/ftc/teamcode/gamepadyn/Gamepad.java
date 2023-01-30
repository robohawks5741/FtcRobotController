package org.firstinspires.ftc.teamcode.gamepadyn;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.EnumMap;
import java.util.Objects;

// Returned by Gamepadyn.getGamepad()
public final class Gamepad {

    @NonNull
    public ActionSource action(UserActions ua) {
        return Objects.requireNonNull(actionSources.get(ua));
    }

    /**
     * Loads a configuration from a JSON file.
     * @param cfgName The configuration name.
     */
    public void loadFileConfiguration(@Nullable String cfgName) throws FileNotFoundException {
        if (cfgName == null) {
            configJson = null;
            configName = null;
        } else {
            Context ctx = Gamepadyn.currentOpmode.hardwareMap.appContext;
            File file = new File(ctx.getFilesDir(), cfgName);
            FileReader fr = new FileReader(file);
            Gson gs = new Gson();
            MappingObject ic = gs.fromJson(fr, MappingObject.class);
        }


//        throw new FileNotFoundException();
    }

    /**
     * Where {@link #loadFileConfiguration(String) loadFileConfiguration()} loads a configuration with a file,
     * {@link #loadJsonConfiguration(MappingObject)  loadJsonConfiguration()} loads a configuration from a pre-parsed object.
     * You probably won't find much use for this, {@link #loadJsonConfiguration(MappingObject)   loadJsonConfiguration()} uses it internally but that's about it.
     * @param mo The mapping object.
     */
    @SuppressWarnings("JavaDoc")
    public void loadJsonConfiguration(MappingObject mo) throws Exception {
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
    private EnumMap<RawGamepadInput, Object> stateCache = new EnumMap<>(RawGamepadInput.class);

    // The name/path/identifier of the configuration file.
    @SuppressWarnings("FieldMayBeFinal")
    private String configName = null;
    private String configJson = null;

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
                    stateCache.put(rin, new AnalogValue(new float[rin.axes]));
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
