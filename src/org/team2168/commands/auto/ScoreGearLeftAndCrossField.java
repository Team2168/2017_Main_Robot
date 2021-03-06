package org.team2168.commands.auto;

import org.team2168.RobotMap;
import org.team2168.commands.drivetrain.PIDCommands.DriveXDistance;
import org.team2168.commands.drivetrain.PIDCommands.RotateXDistancePIDZZZ;
import org.team2168.commands.drivetrain.PIDCommands.RotateXDistancePIDZZZCameraWithGyro;
import org.team2168.commands.gearintake.DriveGearIntakeRollerWithConstant;
import org.team2168.commands.gearintake.LowerGearArmDANGEROUS;
import org.team2168.commands.gearintake.RaiseGearArm;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreGearLeftAndCrossField extends CommandGroup {

    public ScoreGearLeftAndCrossField() {
        addSequential(new DriveXDistance(7.0,0.7,0.1));
        addSequential(new RotateXDistancePIDZZZ(48,0.7,0.2),0.7);
        addSequential(new RotateXDistancePIDZZZCameraWithGyro(0, RobotMap.ROTATE_POSITION_CAMERA_MAX, RobotMap.ROTATE_POSITION_CAMERA_MIN, 1.0),1);
        addSequential(new DriveXDistance(7.0,0.7,0.1),2);
        addSequential(new RotateXDistancePIDZZZCameraWithGyro(0, RobotMap.ROTATE_POSITION_CAMERA_MAX, RobotMap.ROTATE_POSITION_CAMERA_MIN, 1.0),1);
    	addSequential(new DriveXDistance(3.2, 0.7,0.1),1);
    	addSequential(new DriveXDistance(0.3, 0.7,0.1),2);
    	addSequential(new LowerGearArmDANGEROUS(),0.5);
    	addSequential(new Sleep(), 0.4);
    	addSequential(new DriveXDistance(-3.0,0.7,0.1));
    	addParallel(new DriveGearIntakeRollerWithConstant(-1.0));
    	addSequential(new RaiseGearArm());
    	addSequential(new RotateXDistancePIDZZZ(-48,0.7,0.2),0.7);
    	addSequential(new DriveXDistance(32,0.7,0.3));
    }
}
