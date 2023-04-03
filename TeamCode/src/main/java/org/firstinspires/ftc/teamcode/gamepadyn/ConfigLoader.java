package org.firstinspires.ftc.teamcode.gamepadyn;

import android.content.Context;

import com.google.gson.Gson;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

class ConfigLoader {
    @SuppressWarnings("rawtypes")
    static void loadConfigurationResource(OpMode opmode, String id) throws Exception {
        Context ctx = opmode.hardwareMap.appContext;
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        byte[] buffer = null;
        String json;
        try {
            inputStream = ctx.getAssets().open("gamepadyn/lower.json");
            inputStreamReader = new InputStreamReader(inputStream);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
                buffer = inputStream.readAllBytes();
            json = new String(buffer);
            opmode.telemetry.addData("JSON config", json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Gson gs = new Gson();
        MappingIm configObject1 = gs.fromJson(json, MappingIm.class);
        logger.info("Made it past creating a mapping object");
        //            TypeToken ctypet = new TypeToken<
//                Map<
//                    String, ArrayList<
//                        Map<String, PlayerObject>
//                    >
//                >
//            >();
        logger.info(configObject1.description);
        logger.info(gs.toJson(configObject1.maps, MappingIm.MapsField.class));
    }
}
