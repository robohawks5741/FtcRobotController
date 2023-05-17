package org.firstinspires.ftc.teamcode.gamepadyn;

import static org.firstinspires.ftc.teamcode.gamepadyn.InputType.ANALOG;
import static org.firstinspires.ftc.teamcode.gamepadyn.InputType.DIGITAL;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.firstinspires.ftc.teamcode.gamepadyn.user.UserAction;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Logger;

// Returned by Gamepadyn.getGamepad()
public final class Gamepad {

    /**
     * An enum representing the different inputs on a standard competition-legal controller.
     */
    public enum RawInput {
        /// A (Face Button Down)
        FD  (DIGITAL),
        /// B (Face Button Right)
        FR  (DIGITAL),
        /// X (Face Button Left)
        FL  (DIGITAL),
        /// Y (Face Button Up)
        FU  (DIGITAL),
        /// D-Pad Up
        DU  (DIGITAL),
        /// D-Pad Down
        DD  (DIGITAL),
        /// D-Pad Left
        DL  (DIGITAL),
        /// D-Pad Right
        DR  (DIGITAL),
        /// Right Trigger
        TR  (ANALOG, 1),
        /// Left Trigger
        TL  (ANALOG, 1),
        /// Right Bumper
        BR  (DIGITAL),
        /// Left Bumper
        BL  (DIGITAL),
        /// Left Stick
        SL  (ANALOG, 2),
        /// Left Stick Button
        SLB (DIGITAL),
        /// Right Stick
        SR  (ANALOG, 2),
        /// Right Stick Button
        SRB (DIGITAL);

        // Analog or digital
        public final InputType inputType;
        // Amount of analog axes
        public final int axes;

        // Returns the JSON key for the enum as used in configuration files
        public String getStringKey() { return this.toString().toLowerCase(Locale.ENGLISH); }

        public static AnalogValue getAnalogValueFromGamepad(com.qualcomm.robotcore.hardware.Gamepad ftcGamepad, RawInput gamepadEnum) throws IllegalArgumentException {
            switch (gamepadEnum) {
                case SL: { return new AnalogValue(ftcGamepad.left_stick_x,  ftcGamepad.left_stick_y ); }
                case SR: { return new AnalogValue(ftcGamepad.right_stick_x, ftcGamepad.right_stick_y); }
                case TL: { return new AnalogValue(ftcGamepad.left_trigger                           ); }
                case TR: { return new AnalogValue(ftcGamepad.right_trigger                          ); }
                default: {
                    throw new IllegalArgumentException();
                }
            }
        }

        public static boolean getDigitalValueFromGamepad(com.qualcomm.robotcore.hardware.Gamepad ftcGamepad, RawInput gamepadEnum) {
            switch (gamepadEnum) {
                case FU:  { return ftcGamepad.y;                  }
                case FD:  { return ftcGamepad.a;                  }
                case FL:  { return ftcGamepad.x;                  }
                case FR:  { return ftcGamepad.b;                  }
                case DU:  { return ftcGamepad.dpad_up;            }
                case DD:  { return ftcGamepad.dpad_down;          }
                case DL:  { return ftcGamepad.dpad_left;          }
                case DR:  { return ftcGamepad.dpad_right;         }
                case BL:  { return ftcGamepad.left_bumper;        }
                case BR:  { return ftcGamepad.right_bumper;       }
                case SLB: { return ftcGamepad.left_stick_button;  }
                case SRB: { return ftcGamepad.right_stick_button; }
                default: {
                    throw new IllegalArgumentException();
                }
            }
        }

        @Nullable
        public static RawInput fromStringKey(String string) {
            try {
                return RawInput.valueOf(RawInput.class, string.toUpperCase(Locale.ENGLISH));
            } catch (Exception iar) {
                return null;
            }
        }

        RawInput(InputType inputType) {
            this.inputType = inputType;
            this.axes = (inputType == DIGITAL ? 0 : 1);
        }
        RawInput(InputType inputType, int axes) { this.inputType = inputType; this.axes = axes; }
    }

    @NonNull
    public Action action(UserAction ua) {
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
        for (RawInput i : RawInput.values()) {
            switch (i.inputType) {
                case ANALOG:  { stateCache.put(i, RawInput.getAnalogValueFromGamepad (ftcGamepad, i)); break; }
                case DIGITAL: { stateCache.put(i, RawInput.getDigitalValueFromGamepad(ftcGamepad, i)); break; }
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
    final EnumMap<UserAction, Action> actionSources = new EnumMap<>(UserAction.class);

    /**
     * Map of buttons -> mapping actions
     */
    @SuppressWarnings("FieldMayBeFinal")
    Mapping mapping = null;
    // In order to create event-driven input, we need to determine change. Tracking change requires recording state.
    // Object is of type RawGamepadInput.inputType.inputClass depending on the key.
    EnumMap<RawInput, Object> stateCache = new EnumMap<>(RawInput.class);

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
        for (UserAction ua : UserAction.values()) {
             actionSources.put(ua, new Action(ua.type));
        }
        for (RawInput rin : RawInput.values()) {
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
