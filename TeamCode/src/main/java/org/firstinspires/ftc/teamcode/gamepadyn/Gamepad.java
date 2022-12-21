package org.firstinspires.ftc.teamcode.gamepadyn;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qualcomm.ftccommon.FtcRobotControllerSettingsActivity;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcontroller.internal.PermissionValidatorWrapper;
import org.firstinspires.ftc.teamcode.Opticon.Event;
import org.firstinspires.ftc.teamcode.gamepadyn.user.UserActions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Objects;

// Returned by Gamepadyn.getGamepad()
public final class Gamepad {

    @NonNull
    public ActionSource action(UserActions ua) {
        return Objects.requireNonNull(_actions.get(ua));
    }

    /**
     * Loads a configuration from a JSON file.
     * @param cfgName The configuration name.
     */
    public void loadFileConfiguration(@Nullable String cfgName) throws FileNotFoundException {
        if (cfgName == null) {
            _configJson = null;
            _configName = null;
        } else {
            Context ctx = Gamepadyn._currentOpmode.hardwareMap.appContext;
            File file = new File(ctx.getFilesDir(), cfgName);
            FileReader fr = new FileReader(file);
            Gson gs = new Gson();
            InterConfig ic = gs.fromJson(fr, InterConfig.class);
        }


//        throw new FileNotFoundException();
    }

    /**
     * Where {@link #loadFileConfiguration(String) loadFileConfiguration()} loads a configuration with a file,
     * {@link #loadJsonConfiguration(Gson) loadJsonConfiguration()} loads a configuration from a GSON object.
     * You probably won't find much use for this, {@link #loadJsonConfiguration(Gson)  loadJsonConfiguration()} uses it internally but that's about it.
     * @param gs The configuration JSON.
     */
    @SuppressWarnings("JavaDoc")
    public void loadJsonConfiguration(InterConfig gs) throws Exception{
        return;
    }

    /**
     * @return The name/path/identifier of the configuration file.
     */
    @Nullable
    public String getConfiguration() { return _configName; }

    public final int index;

    private final EnumMap<UserActions, ActionSource> _actions = new EnumMap<>(UserActions.class);

    /**
     * Map of buttons -> mapping actions
     */
    @SuppressWarnings("FieldMayBeFinal")
    private EnumMap<RawGamepadInput, MappingAction> _mapping = new EnumMap<>(RawGamepadInput.class);

    // The name/path/identifier of the configuration file.
    @SuppressWarnings("FieldMayBeFinal")
    private String _configName = null;
    private String _configJson = null;

    private Gamepad(int id) {
        index = id;
        // fill
        for (UserActions ua : UserActions.values()) {
             _actions.put(ua, new ActionSource(ua.type));
        }
    }
}
