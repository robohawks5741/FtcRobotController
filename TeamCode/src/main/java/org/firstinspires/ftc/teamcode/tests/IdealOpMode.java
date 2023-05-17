package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.gamepadyn.Gamepadyn;

import java.util.EnumMap;

import kotlin.Triple;

@TeleOp(name = "Ideal OpMode")
public class IdealOpMode extends OpMode {

    SoundPlayer soundPlayer = null;
    Thread phWorker;

    @FunctionalInterface
    public interface DigitalActionCallback {
        void run(boolean value);
    }

    enum Button {
        BUTTON_A,
        BUTTON_B
    }

    enum DigitalAction {
        DEBUG
    }

    private static class phWorkerRoutine implements Runnable {
        private final OpMode opMode;

        private final EnumMap<Button, DigitalAction> mappings = new EnumMap<>(Button.class);
        private final EnumMap<DigitalAction, DigitalActionCallback> digitalRoutines = new EnumMap<>(DigitalAction.class);

        phWorkerRoutine(OpMode opMode) { this.opMode = opMode; }

        // overload for analog

        public void bindInput(Button button, DigitalAction action) { mappings.put(button, action); }

        public void bindAction(DigitalAction action, DigitalActionCallback routine) { digitalRoutines.put(action, routine); }

        @Override
        public void run() {
            boolean stateA = opMode.gamepad1.a;
            boolean stateB = opMode.gamepad1.b;

            // This is effectively Thread.currentThread().isAlive()
            //noinspection InfiniteLoopStatement
            while (true) {

                // *** read state ***

                boolean updateA = opMode.gamepad1.a;
                boolean updateB = opMode.gamepad1.b;

                @SuppressWarnings("rawtypes")
                Triple[] buttons = {
                    new Triple<>(Button.BUTTON_A, updateA, stateA),
                    new Triple<>(Button.BUTTON_B, updateB, stateB)
                };

                // *** compare state ***

                //noinspection rawtypes
                for (Triple e : buttons) {
                    @SuppressWarnings("unchecked")
                    Triple<Button, Boolean, Boolean> triple = (Triple<Button, Boolean, Boolean>) e;

                    if (triple.component2() != triple.component3()) {
                        DigitalAction a = mappings.get(triple.component1());
                        if (a != null) {
                            DigitalActionCallback r = digitalRoutines.get(a);
                            if (r != null) r.run(triple.component2());
                        }
                    }
                }

                // *** cache state ***

                stateA = updateA;
                stateB = updateB;

            }
        }
    }

    private static void phWorkerExceptionHandler(Thread t, Throwable e) { throw new RuntimeException(e); }

    @Override
    public void init() {
        soundPlayer = SoundPlayer.getInstance();
        Gamepadyn.opmodeInit(this);

        phWorkerRoutine routine = new phWorkerRoutine(this);
        phWorker = new Thread(routine);
        phWorker.setUncaughtExceptionHandler(IdealOpMode::phWorkerExceptionHandler);

        // this would be done using JSON configs, which makes this dynamic
        routine.bindInput(Button.BUTTON_A, DigitalAction.DEBUG);

        routine.bindAction(DigitalAction.DEBUG, (value) -> {
            soundPlayer.startPlaying(
                hardwareMap.appContext,
                (value ? R.raw.debug_press : R.raw.debug_release)/*,
                new SoundPlayer.PlaySoundParams(), null,
                () -> { } */
            );
        });

    }

    @Override
    public void start() {
        Gamepadyn.opmodeStart(this);
    }

    @Override
    public void stop() {
        Gamepadyn.opmodeStop();
    }

    @Override
    public void loop() { }

}
