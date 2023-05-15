package org.firstinspires.ftc.teamcode.gamepadyn;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Objects;
import java.util.logging.Logger;

// Returned by Gamepadyn.getGamepad()
public final class Gamepad {

    @NonNull
    public ActionSource action(UserActions ua) {
        return Objects.requireNonNull(actionSources.get(ua));
    }

    public boolean hasConfiguration() { return (configName != null); }

    /**
     * @return The name/path/identifier of the configuration file.
     */
    @Nullable
    public String getConfigurationName() { return configName; }

    enum switchInput {
        UNKNOWN,
        DIGITAL,
        ANALOG
    }

    void updateCache() {
        for (RawGamepadInput i : RawGamepadInput.values()) {
            switch (i.inputType) {
                case ANALOG:  { stateCache.put(i, RawGamepadInput.getAnalogValueFromGamepad (ftcGamepad, i)); break; }
                case DIGITAL: { stateCache.put(i, RawGamepadInput.getDigitalValueFromGamepad(ftcGamepad, i)); break; }
            }
        }
    }

    static void loadConfigurationResource(String id) {
        Context ctx = Gamepadyn.currentOpmode.hardwareMap.appContext;
        InputStream inputStream;
        byte[] buffer;
        String json;
        try {
            inputStream = ctx.getAssets().open(id);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
                buffer = inputStream.readAllBytes();
            else {
                buffer = new byte[0];
                do {
                    buffer = Arrays.copyOf(buffer, buffer.length + inputStream.available());
                    int l = inputStream.read(buffer);
                } while (inputStream.available() > 0);
            }
            json = new String(buffer);
            Gamepadyn.currentOpmode.telemetry.addData("JSON config", json);
            Logger.getLogger("Gamepadyn").info("JSON config:\n " +  json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Gson gs = new Gson();
        Mapping configObject1 = new Mapping(gs.fromJson(json, IntermediateMapping.class));
        Gamepadyn.currentOpmode.telemetry.addData("Parsed Config", gs.toJson(configObject1, Mapping.class));
        Logger.getLogger("Gamepadyn").info(gs.toJson(configObject1, Mapping.class));
    }

    // ActionSources (emitters + values) for each of the actions.
    final EnumMap<UserActions, ActionSource> actionSources = new EnumMap<>(UserActions.class);

    /**
     * Map of buttons -> mapping actions
     */
    @SuppressWarnings("FieldMayBeFinal")
    Mapping mapping = null;
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
    com.qualcomm.robotcore.hardware.Gamepad ftcGamepad;

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
