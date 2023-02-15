package org.firstinspires.ftc.teamcode.gamepadyn;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class BadGamepadConfigException extends RuntimeException {
    public final String config;
    // this should never be called
    private BadGamepadConfigException() { this.config = null; }
    private BadGamepadConfigException(String message) { super(message); this.config = null; }
    private BadGamepadConfigException(Throwable cause) { super(cause); this.config = null; }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private BadGamepadConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace); this.config = null; }

    /**
     * @param   configPath   the path where the invalid configuration is located
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public BadGamepadConfigException(String configPath, String message) { super(message); this.config = configPath; }

    /**
     * @param   configPath   the path where the invalid configuration is located
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A {@code null} value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public BadGamepadConfigException(String configPath, String message, Throwable cause) { super(message, cause); this.config = configPath; }

    /**
     * @param   configPath   the path where the invalid configuration is located
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A {@code null} value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public BadGamepadConfigException(String configPath, Throwable cause) { super(cause); this.config = configPath; }

    /**
     * @param   configPath   the path where the invalid configuration is located
     * @param  message the detail message.
     * @param cause the cause.  (A {@code null} value is permitted,
     * and indicates that the cause is nonexistent or unknown.)
     * @param enableSuppression whether or not suppression is enabled
     *                          or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected BadGamepadConfigException(String configPath, String message, Throwable cause,
                                        boolean enableSuppression,
                                        boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.config = configPath;
    }
}
