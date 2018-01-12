package org.team2168.commands.auto;

import org.team2168.commands.drivetrain.ShiftHigh;
import org.team2168.commands.drivetrain.PIDCommands.DriveXDistance;
import org.team2168.commands.drivetrain.PIDCommands.RotateXDistancePIDZZZ;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Drive straight boiii
 */
public class StartLeftDriveToLeftScale extends CommandGroup {

    public StartLeftDriveToLeftScale() {
    	 addSequential(new DriveXDistance(4.0,0.7,0.1));
    	 addSequential(new RotateXDistancePIDZZZ(-30,0.7,0.2));
    	 addSequential(new DriveXDistance(6.0,0.7,0.1));
    	 addSequential(new RotateXDistancePIDZZZ(30,0.7,0.2));
    	 addSequential(new DriveXDistance(6.0,0.7,0.1));
    	 addSequential(new RotateXDistancePIDZZZ(15,0.7,0.2));
    	 addSequential(new DriveXDistance(4.0,0.7,0.1));
    	 addSequential(new RotateXDistancePIDZZZ(-15,0.7,0.2));



    	 
    }
}
