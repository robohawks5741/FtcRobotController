package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.gamepadyn.Gamepadyn;

import java.util.EnumMap;

import kotlin.Triple;

@TeleOp(name = "Ideal OpMode")
public class IdealOpMode extends StatefulOpMode {

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

    private static final class phWorker implements Runnable {
        private final StatefulOpMode opMode;

        private final EnumMap<Button, DigitalAction> mappings = new EnumMap<>(Button.class);
        private final EnumMap<DigitalAction, Boolean> digitalState = new EnumMap<>(DigitalAction.class);
        private final EnumMap<DigitalAction, DigitalActionCallback> digitalRoutines = new EnumMap<>(DigitalAction.class);

        phWorker(StatefulOpMode opMode) {
            this.opMode = opMode;
        }

        // overload for analog

        public void bindInput(Button button, DigitalAction action) { mappings.put(button, action); }

        public void bindAction(DigitalAction action, DigitalActionCallback routine) { digitalRoutines.put(action, routine); }

        public boolean getActionValue(DigitalAction action) {
            return Boolean.TRUE.equals(digitalState.get(action));
        }

        @Override
        public void run() {
//            try {
//                this.wait();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }

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
                        opMode.telemetry.addLine("state change");
                        opMode.telemetry.update();
                        // get action bound to input
                        DigitalAction a = mappings.get(triple.component1());
                        if (a != null) {
                            // update state
                            digitalState.put(a, triple.component2());
                            // get callback
                            DigitalActionCallback r = digitalRoutines.get(a);
                            // run if valid
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

    private static void phWorkerExceptionHandler(Thread t, Throwable e) {
//        if (e instanceof InterruptedException) return;
        throw new RuntimeException(e);
    }

    @Override
    public void init() {
        this.hasInit = true;
        soundPlayer = SoundPlayer.getInstance();
        Gamepadyn.opmodeInit(this);

        IdealOpMode.phWorker routine = new phWorker(this);
        phWorker = new Thread(routine);
        phWorker.setUncaughtExceptionHandler(IdealOpMode::phWorkerExceptionHandler);

        // this would be done using JSON configs, which makes this dynamic
        routine.bindInput(Button.BUTTON_A, DigitalAction.DEBUG);

        routine.bindAction(DigitalAction.DEBUG, (value) -> {
            telemetry.addLine("event DEBUG " + value);
            telemetry.update();
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
        this.isStarted = true;
        phWorker.start();
//        phWorker.notify();
        Gamepadyn.opmodeStart(this);
    }

    @Override
    public void stop() {
        this.isStarted = false;
        this.hasInit = false;
        phWorker.interrupt();
        phWorker = null;
        Gamepadyn.opmodeStop();
    }

    @Override
    public void loop() { }

}
