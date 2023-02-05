package org.firstinspires.ftc.teamcode.gamepadyn;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions;
import org.firstinspires.ftc.teamcode.gamepadyn.MappingObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

// Returned by Gamepadyn.getGamepad()
public final class Gamepad {

    @NonNull
    public ActionSource action(UserActions ua) {
        return Objects.requireNonNull(actionSources.get(ua));
    }

    @SuppressWarnings("rawtypes")
    public void loadConfigurationResource(String id) {
        try {
            Context ctx = Gamepadyn.currentOpmode.hardwareMap.appContext;
//        ctx.getResources().openRawResource()
            Gson gs = new Gson();
            MappingObject ic = gs.fromJson(fr, MappingObject.class);
            System.out.println("Made it past creating a mapping object");
            System.out.println(ic);
            String json = gs.toJson(ic);
            System.out.println(json);

            for (Map.Entry<String, Map<String, Object>> e : ic.maps.entrySet()) {
                Class c;
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
                        c = MappingObject.DigitalMap.class;
                        break;
                    case "tr":
                    case "tl":
                    case "sl":
                    case "sr":
                        c = MappingObject.AnalogMap.class;
                        break;
                    default:
                        // this is bad
                        c = Object.class;
                }

                Object o;

                // TODO: add cases for every mode
                switch (c) {
                    case MappingObject.DigitalMap.class: {
                        MappingObject.DigitalMap dm = new MappingObject.DigitalMap();
                        String mode = (String) e.getValue().get("mode");
                        switch (Objects.requireNonNull(mode)) {
                            case "TRIGGER": {
                                String a = (String) e.getValue().get("action");
                                o = new MappingActionDigital(
                                        MappingActionDigital.Mode.TRIGGER,
                                        UserActions.valueOf(a)
                                );
                                break;
                            }
                            case "ANALOG_MAP": {
                                String a = Objects.requireNonNull((String) e.getValue().get("action"));
                                String dv = Objects.requireNonNull((String) e.getValue().get("depressedValue"));
                                String rv = (String) e.getValue().get("releasedValue");
                                String ax = Objects.requireNonNull((String) e.getValue().get("axis"));
                                o = new MappingActionDigital(
                                        MappingActionDigital.Mode.ANALOG_MAP,
                                        UserActions.valueOf(a)
                                        Float.parseFloat() || null,
                                        Integer.parseInt(ax)
                                );
                                break;
                            }
                        }
                    }
                }

                System.out.println(gs.toJson(c.cast(e.getValue())));
            }
        } catch (Exception e) {
            System.err.println(e);
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
