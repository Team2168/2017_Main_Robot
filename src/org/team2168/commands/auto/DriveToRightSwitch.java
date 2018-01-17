package org.team2168.commands.auto;

import org.team2168.commands.drivetrain.ShiftHigh;
import org.team2168.commands.drivetrain.PIDCommands.DriveXDistance;
import org.team2168.commands.drivetrain.PIDCommands.RotateXDistancePIDZZZ;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Drive to right switch boiii
 */
public class DriveToRightSwitch extends CommandGroup {

    public DriveToRightSwitch() {
    	 addSequential(new DriveXDistance(2.41,0.7,0.05));
    	 addSequential(new RotateXDistancePIDZZZ(45,0.7,0.2));
    	 addSequential(new DriveXDistance(4.0,0.9,0.05));
    	 addSequential(new RotateXDistancePIDZZZ(-45,0.7,0.2));
    	 addSequential(new DriveXDistance(1.5,0.6,0.1));
    	 
    	 
    	 //get second cube
    	 
    	 //addSequential(new DriveXDistance(-1.0,0.7,0.05));
    	 addSequential(new RotateXDistancePIDZZZ(90,0.7,0.2));
    	 addSequential(new DriveXDistance(5,0.6,0.1));
    	 addSequential(new RotateXDistancePIDZZZ(-90,0.7,0.2));
    	 
    }
}
