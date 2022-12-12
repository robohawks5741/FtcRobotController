# Gamepadyn

*The "Input Type" may vary depending on controller model. This table represents the Logitech F310 gamepad since it's competition-legal and I had one on hand.*

| Button             | JSON Reference | Java Enum | Input Type |
|--------------------|----------------|-----------|------------|
| A                  | `fd`           | `FD`      | Digital    |
| B                  | `fr`           | `FR`      | Digital    |
| X                  | `fl`           | `FL`      | Digital    |
| Y                  | `fu`           | `FU`      | Digital    |
| D-Pad Up           | `du`           | `DU`      | Digital    |
| D-Pad Down         | `dd`           | `DD`      | Digital    |
| D-Pad Left         | `dl`           | `DL`      | Digital    |
| D-Pad Right        | `dr`           | `DR`      | Digital    |
| Right Trigger      | `tr`           | `TR`      | Analog<1d> | <!--my HTML brain is screaming at me-->
| Left Trigger       | `tl`           | `TL`      | Analog<1d> |
| Right Bumper       | `br`           | `BR`      | Digital    | <!--more screaming-->
| Left Bumper        | `bl`           | `BL`      | Digital    |
| Left Stick         | `sl`           | `SL`      | Analog<2d> |
| Right Stick        | `sr`           | `SR`      | Analog<2d> |
| Left Stick Button  | `sbl`          | `SBL`     | Digital    |
| Right Stick Button | `sbr`          | `SBR`     | Digital    |