package org.team2168.commands.auto;

import org.team2168.commands.drivetrain.PIDCommands.DriveXDistance;
import org.team2168.commands.drivetrain.PIDCommands.RotateXDistancePIDZZZCameraWithGyro;
import org.team2168.commands.gearintake.LowerGearArm;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DriveStraightAndPlaceGearCenter extends CommandGroup {

    public DriveStraightAndPlaceGearCenter() {
    	addSequential(new DriveXDistance(7,0.7));
    	addSequential(new RotateXDistancePIDZZZCameraWithGyro(0, 0.4, 0.22, 0.1));
    	addSequential(new DriveXDistance(1,0.7));
    	addSequential(new LowerGearArm());
    	addSequential(new DriveXDistance(-2,0.7));
    }
}
