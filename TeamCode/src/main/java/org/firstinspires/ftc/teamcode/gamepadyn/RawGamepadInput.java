package org.firstinspires.ftc.teamcode.gamepadyn;

import static org.firstinspires.ftc.teamcode.gamepadyn.InputType.*;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.Raw;
import org.checkerframework.checker.units.qual.A;

import java.util.Locale;

/**
 * An enum representing the different inputs on a standard competition-legal controller.
 */
public enum RawGamepadInput {
    /// A (Face Button Down)
    FD  (DIGITAL),
    /// B (Face Button Right)
    FR  (DIGITAL),
    /// X (Face Button Left)
    FL  (DIGITAL),
    /// Y (Face Button Up)
    FU  (DIGITAL),
    /// D-Pad Up
    DU  (DIGITAL),
    /// D-Pad Down
    DD  (DIGITAL),
    /// D-Pad Left
    DL  (DIGITAL),
    /// D-Pad Right
    DR  (DIGITAL),
    /// Right Trigger
    TR  (ANALOG, 1),
    /// Left Trigger
    TL  (ANALOG, 1),
    /// Right Bumper
    BR  (DIGITAL),
    /// Left Bumper
    BL  (DIGITAL),
    /// Left Stick
    SL  (ANALOG, 2),
    /// Left Stick Button
    SLB (DIGITAL),
    /// Right Stick
    SR  (ANALOG, 2),
    /// Right Stick Button
    SRB (DIGITAL);

    public final InputType inputType;
    public final int axes;

    // Returns the JSON key for the enum as used in configuration files
    public String getStringKey() { return this.toString().toLowerCase(Locale.ENGLISH); }

    public static AnalogValue getAnalogValueFromGamepad(com.qualcomm.robotcore.hardware.Gamepad ftcGamepad, RawGamepadInput gamepadEnum) throws IllegalArgumentException {
        switch (gamepadEnum) {
            case SL: { return new AnalogValue(ftcGamepad.left_stick_x,  ftcGamepad.left_stick_y ); }
            case SR: { return new AnalogValue(ftcGamepad.right_stick_x, ftcGamepad.right_stick_y); }
            case TL: { return new AnalogValue(ftcGamepad.left_trigger                           ); }
            case TR: { return new AnalogValue(ftcGamepad.right_trigger                          ); }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }

    public static boolean getDigitalValueFromGamepad(com.qualcomm.robotcore.hardware.Gamepad ftcGamepad, RawGamepadInput gamepadEnum) {
        switch (gamepadEnum) {
            case FU:  { return ftcGamepad.y;                  }
            case FD:  { return ftcGamepad.a;                  }
            case FL:  { return ftcGamepad.x;                  }
            case FR:  { return ftcGamepad.b;                  }
            case DU:  { return ftcGamepad.dpad_up;            }
            case DD:  { return ftcGamepad.dpad_down;          }
            case DL:  { return ftcGamepad.dpad_left;          }
            case DR:  { return ftcGamepad.dpad_right;         }
            case BL:  { return ftcGamepad.left_bumper;        }
            case BR:  { return ftcGamepad.right_bumper;       }
            case SLB: { return ftcGamepad.left_stick_button;  }
            case SRB: { return ftcGamepad.right_stick_button; }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }

    @Nullable
    public static RawGamepadInput fromStringKey(String string) {
        try {
            return RawGamepadInput.valueOf(RawGamepadInput.class, string.toUpperCase(Locale.ENGLISH));
        } catch (Exception iar) {
            return null;
        }
    }

    RawGamepadInput(InputType inputType) {
        this.inputType = inputType;
        this.axes = (inputType == DIGITAL ? 0 : 1);
    }
    RawGamepadInput(InputType inputType, int axes) { this.inputType = inputType; this.axes = axes; }
}
