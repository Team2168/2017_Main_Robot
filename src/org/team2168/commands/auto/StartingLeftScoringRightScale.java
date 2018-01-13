package org.team2168.commands.auto;

import org.team2168.commands.drivetrain.ShiftHigh;
import org.team2168.commands.drivetrain.PIDCommands.DriveXDistance;
import org.team2168.commands.drivetrain.PIDCommands.RotateXDistancePIDZZZ;
import org.team2168.commands.gearintake.RaiseGearArm;

import edu.wpi.first.wpilibj.command.CommandGroup;


public class StartingLeftScoringRightScale extends CommandGroup {

	public StartingLeftScoringRightScale() {
    	 addSequential(new DriveXDistance(4,0.7,0.1));
    	 addSequential(new RotateXDistancePIDZZZ(-30,0.7,0.2));
    	 addSequential(new DriveXDistance(6,0.7,0.1));
    	 addSequential(new RotateXDistancePIDZZZ(30,0.7,0.2));
    	 addSequential(new DriveXDistance(6,0.7,0.1));
    	 addSequential(new RotateXDistancePIDZZZ(90,0.7,0.2));
    	 addSequential(new DriveXDistance(15,0.7,0.1));
    	 addSequential(new RotateXDistancePIDZZZ(-90,0.7,0.2));
    	 addSequential(new Sleep(), 0.6);
    	 addSequential(new RaiseGearArm());
    }
}
