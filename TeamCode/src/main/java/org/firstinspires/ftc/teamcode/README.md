# TeamCode Module

## Anatomy

I ([Addison](https://github.com/addiment)) am a big fan of organization, so I've been pushing to modularize all of our code.
For anyone who isn't me though, it might get a bit confusing, not knowing where to find what you're looking for.
To remedy this (besides some other stuff), I wanted to document a high-level overview of the code anatomy.

### Java Files

Not packages, but classes/interfaces/enums that have the meat of our code.

```
───┐ org.firstinspires.ftc.teamcode
   └──┐ drive
      ├─── RobohawksDriveConstants
      ├─── RobohawksMecanumDrive
      └──┐ opmode
         ├─── RobohawksAutonomous
         └─── RobohawksTeleOp
```

### Roadrunner Packages

We use Roadrunner, which has a few packages in the teamcode module.

```
───┐ org.firstinspires.ftc.teamcode
   ├─── drive
   ├─── trajectorysequence
   └─── util
```

### EventD

EventD is a tiny package I threw together to allow for a more familiar event-driven architecture in Java.
It's used by both Gamepadyn and LSD.

```
───┐ org.firstinspires.ftc.teamcode
   └─── eventd
```

### Gamepadyn

EventD is a tiny package I threw together to allow for a more familiar event-driven architecture in Java.
It's used by both Gamepadyn and LSD.

```
───┐ org.firstinspires.ftc.teamcode
   └──┐ gamepadyn
      ├─── user
      └─── web
```

- The `web` package is where the WIP/old frontend for the configurator is stored. I don't plan on finishing it for the sake of time.
- The `user` package is where you/me/us/other teams/idk put their user-specific Action Configurations. It only has one Java Enum, which is where that all happens.


### LSD

The Linear Slide Driver (LSD). Wrapper for the linear slide.

```
───┐ org.firstinspires.ftc.teamcode
   └─── lsd
```

### HD

The Halo Driver (HD). Wrapper for the halo.

```
───┐ org.firstinspires.ftc.teamcode
   └─── hd
```
