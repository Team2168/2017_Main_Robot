package org.team2168.commands.auto;

import org.team2168.commands.drivetrain.ShiftHigh;
import org.team2168.commands.drivetrain.PIDCommands.DriveXDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Drive straight boi
 */
public class DriveOverBaseline extends CommandGroup {

    public DriveOverBaseline() {
    	 addSequential(new DriveXDistance(9.0,0.7,0.05));
    	 addSequential(new Sleep(),25);
    }
}
