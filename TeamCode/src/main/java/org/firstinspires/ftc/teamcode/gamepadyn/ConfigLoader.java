package org.firstinspires.ftc.teamcode.gamepadyn;

import android.content.Context;

import com.google.gson.Gson;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Logger;

@SuppressWarnings("RedundantSuppression")
class ConfigLoader {
    @SuppressWarnings("rawtypes")
    static void loadConfigurationResource(OpMode opmode, String id) {
        Context ctx = opmode.hardwareMap.appContext;
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
            opmode.telemetry.addData("JSON config", json);
            Logger.getLogger("Gamepadyn").info("JSON config:\n " +  json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Gson gs = new Gson();
        Mapping configObject1 = gs.fromJson(json, Mapping.class);
        opmode.telemetry.addData("Parsed Config", gs.toJson(configObject1, Mapping.class));
        Logger.getLogger("Gamepadyn").info(gs.toJson(configObject1, Mapping.class));
    }
}
