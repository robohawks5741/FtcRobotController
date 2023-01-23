package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;

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
//            this.telemetry.addData(logcat,
            boolean hasParent = true;
            int i = 0;
            ThreadGroup r = threadGroup;
            while (hasParent) {
                try {
                    r.checkAccess();
                    this.telemetry.addData(logcat, "Thread has access to Thread Group iteration #" + i);
                    this.telemetry.addData(logcat, "Thread Group iteration #" + i + " has name" + r.getName());
                    Thread[] threads = new Thread[16];
                    r.enumerate(threads, true);
                    this.telemetry.addData(logcat, "Thread Group iteration #" + i + " has " + threads.length + " child threads");
                    r = r.getParent();
                } catch (SecurityException se) {
                    this.telemetry.addData(logcat, "Security Exception (thread cannot access parent): " + se.getMessage());
                    hasParent = false;
                }
                i++;
            }
        }
    }

    @Override
    public void init() {
        this.telemetry.addData("!!! THREAD DEBUG OPMODE !!!", "init() called!");
        this.telemetry.addData("!!! THREAD DEBUG OPMODE !!!", "attemp 2");
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
