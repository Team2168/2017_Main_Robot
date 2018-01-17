package org.team2168.commands.auto;

import org.team2168.commands.drivetrain.ShiftHigh;
import org.team2168.commands.drivetrain.PIDCommands.DriveXDistance;
import org.team2168.commands.drivetrain.PIDCommands.RotateXDistancePIDZZZ;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Drive straight boi
 */
public class RotateOverBaseline extends CommandGroup {

    public RotateOverBaseline() {
    	 addSequential(new RotateXDistancePIDZZZ(25, 0.8, 0.25, 1));
    	 addSequential(new Sleep(),25);
    }
}
