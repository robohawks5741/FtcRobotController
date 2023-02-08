package org.firstinspires.ftc.teamcode.gamepadyn;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions;
import org.firstinspires.ftc.teamcode.gamepadyn.MappingObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    @SuppressWarnings("rawtypes")
    public void loadConfigurationResource(String id) throws Exception {
        try {
            Context ctx = Gamepadyn.currentOpmode.hardwareMap.appContext;
            //  R.raw.
            InputStreamReader inStream = new InputStreamReader(ctx.getAssets().open(id));
            Gson gs = new Gson();
            MappingObject ic = gs.fromJson(inStream, MappingObject.class);
            System.out.println("Made it past creating a mapping object");
            System.out.println(ic);
            String json = gs.toJson(ic);
            System.out.println(json);

            for (Map.Entry<String, Map<String, Object>> e : ic.maps.entrySet()) {
                switchInput c;
                switch (e.getKey()) {
                    case "fd":
                    case "fr":
                    case "fl":
                    case "fu":
                    case "du":
                    case "dd":
                    case "dl":
                    case "dr":
                    case "br":
                    case "bl":
                    case "slb":
                    case "srb":
                        c = switchInput.DIGITAL;
                        break;
                    case "tr":
                    case "tl":
                    case "sl":
                    case "sr":
                        c = switchInput.ANALOG;
                        break;
                    default:
                        // this is bad
                        c = switchInput.UNKNOWN;
                }

                MappingAction o = null;
                Map<String, Object> obj = e.getValue();
                String mode = (String) obj.get("mode");

                // TODO: add cases for every mode and change c to an enum
                switch (c) {
                    case DIGITAL: {
                        switch (Objects.requireNonNull(mode)) {
                            case "TRIGGER": {
                                String a = (String) obj.get("action");
                                o = new MappingActionDigital(
                                        MappingActionDigital.Mode.TRIGGER,
                                        UserActions.valueOf(a)
                                );
                                break;
                            }
                            case "ANALOG_MAP": {
                                String a = Objects.requireNonNull((String) obj.get("action"));
                                String dv = Objects.requireNonNull((String) obj.get("depressedValue"));
                                String rv = (String) obj.get("releasedValue");
                                String ax = Objects.requireNonNull((String) obj.get("axis"));
                                o = new MappingActionDigital(
                                        MappingActionDigital.Mode.ANALOG_MAP,
                                        UserActions.valueOf(a),
                                        Float.parseFloat(dv),
                                        (rv == null ? null : Float.parseFloat(rv)),
                                        Integer.parseInt(ax)
                                );
                                break;
                            }
                            case "ANALOG_OFFSET": {
                                String a = Objects.requireNonNull((String) obj.get("action"));
                                String or = Objects.requireNonNull((String) obj.get("offsetRate"));
                                String ax = Objects.requireNonNull((String) obj.get("axis"));
                                o = new MappingActionDigital(
                                        MappingActionDigital.Mode.ANALOG_OFFSET,
                                        UserActions.valueOf(a),
                                        Float.parseFloat(or),
                                        Integer.parseInt(ax)
                                );
                                break;
                            }
                        }
                    }
                    case ANALOG: {
                        RawGamepadInput rgi = RawGamepadInput.valueOf(e.getKey().toUpperCase(Locale.ENGLISH));
                        switch (Objects.requireNonNull(mode)) {
                            case "ONE_TO_ONE_AXES": {
                                String a = (String) obj.get("action");
                                Float[] is = (Float[]) obj.get("inputScale");
                                o = new MappingActionAnalog(
                                    rgi.axes,
                                    is,
                                    UserActions.valueOf(a)
                                );
                                break;
                            }
                            case "SPLIT_AXES": {
                                System.err.println("Split axes parsing not done yet");
//                                Float[] is = (Float[]) obj.get("inputScale");
//                                MappingActionAnalog.AxisMap[] am = new MappingActionAnalog.AxisMap();
//                                o = new MappingActionAnalog(
//                                    rgi.axes
//                                );
                                break;
                            }
                            default: throw new RuntimeException("Unknown analog input mode: " + mode);
                        }
                        break;
                    }
                    case UNKNOWN:
                    default:
                        Logger.getAnonymousLogger().log(Level.WARNING, "Bad map entry!");
                        break;
                }

                System.out.println(gs.toJson(o));
            }
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "exception thrown when loading configuration from resource", e);
        }
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
