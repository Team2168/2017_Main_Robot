package org.team2168.commands.auto;

import org.team2168.commands.drivetrain.PIDCommands.DriveXDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Drives straight out of launch pad
 */
public class DriveStraightOverLine extends CommandGroup {

    public DriveStraightOverLine() {
        addSequential(new DriveXDistance(-9.2,0.7,0.1),10);
    }
}
