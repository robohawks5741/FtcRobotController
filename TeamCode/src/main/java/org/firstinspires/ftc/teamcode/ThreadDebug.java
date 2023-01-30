package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.gamepadyn.Gamepadyn;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Arrays;

@TeleOp()
public class ThreadDebug extends OpMode {
    Thread t = null;

    public void threadReport() {
        String logcat = "!!! THREAD DEBUG REPORT !!!";
        this.telemetry.addData(logcat, "Current Thread Name: " + Thread.currentThread().getName());
        this.telemetry.addData(logcat, "Current Thread ID: " + Thread.currentThread().getId());
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        if (threadGroup == null) {
            this.telemetry.addData(logcat, "Not in a thread group.");
        } else {
            this.telemetry.addData(logcat, "");
            Thread[] threads = new Thread[64];
            int s = threadGroup.enumerate(threads);
            threads = Arrays.copyOf(threads, s);
            for (int i = 0; i < threads.length; i++ ) {
                String n = threads[i].getName();
                this.telemetry.addData(logcat, "Child #" + i + " name: " + (n == null ? "<no name>" : n));
            }
        }
    }

    @Override
    public void init() {
        Gamepadyn.opmodeInit(this);
        this.telemetry.addData("!!! THREAD DEBUG OPMODE !!!", "init() called!");
        this.telemetry.addData("!!! THREAD DEBUG OPMODE !!!", "attempt 2");
        threadReport();
        t = new Thread(new TelemThread(this.telemetry));
        this.telemetry.addData("!!! THREAD DEBUG OPMODE !!!", "after thread creation");
    }

    @Override
    public void start() {
        this.telemetry.addData("!!! THREAD DEBUG OPMODE !!!", "start() called!");
        t.start();
        threadReport();
    }

    @Override
    public void stop() {
        t = null;
        this.telemetry.addData("!!! THREAD DEBUG OPMODE !!!", "stop() called!");
        Gamepadyn.cleanup();
    }

    public static class TelemThread implements Runnable {
        public Telemetry telem;

        public TelemThread(Telemetry telem) {
            this.telem = telem;
        }

        @Override
        public void run() {
            telem.addData("!!! THREAD DEBUG THREAD !!!", "ran!");
        }
    }

    /**
     * User defined loop method
     * <p>
     * This method will be called repeatedly in a loop while this op mode is running
     */
    @Override
    public void loop() { }
}
