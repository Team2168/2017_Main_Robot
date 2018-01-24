package org.team2168.commands.auto;

import org.team2168.commands.drivetrain.PIDCommands.DriveXDistance;
import org.team2168.commands.drivetrain.PIDCommands.RotateXDistancePIDZZZ;
import org.team2168.commands.gearintake.DriveGearIntakeRollerWithConstant;
import org.team2168.commands.gearintake.LowerGearArmDANGEROUS;
import org.team2168.commands.gearintake.RaiseGearArm;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DriveToLeftSwitchAndRightScale extends CommandGroup {

    public DriveToLeftSwitchAndRightScale() {
    	
    	addSequential(new DriveXDistance(3.0,1.0,0.05));
   	 	addSequential(new RotateXDistancePIDZZZ(-45,1.0,0.22));
   	 	addSequential(new DriveXDistance(6.0,1.0,0.05));
   	 	addSequential(new RotateXDistancePIDZZZ(45,1.0,0.22));
   	 	addSequential(new DriveXDistance(2.5,1.0,0.1));
   	 	
   	 
   	 
   	 	//score on switch (Spit Intake)
   	 	addSequential(new LowerGearArmDANGEROUS(),0.3); 
   	 	addSequential(new DriveGearIntakeRollerWithConstant(-1.0),0.4);
   	 	addSequential(new DriveGearIntakeRollerWithConstant(0.0),0.4);
	    addSequential(new RaiseGearArm(),0.2);
	    
	    //Go Around Switch
	    addSequential(new RotateXDistancePIDZZZ(-90,1.0,.22));
	    addSequential(new DriveXDistance(4.0,1.0,0.1));
	    addSequential(new RotateXDistancePIDZZZ(90,1.0,.22));
	    addSequential(new DriveXDistance(8.0,1.0,0.1));
	    addSequential(new RotateXDistancePIDZZZ(105,1.0,.22));
	    addSequential(new DriveXDistance(2.5, 1.0,0.1));
	    
	    //pick up 2nd cube
	    addParallel(new LowerGearArmDANGEROUS(),0.3); 
	 	addParallel(new DriveGearIntakeRollerWithConstant(1.0));
	 	addParallel(new RaiseGearArm(),0.5);
	 	addSequential(new DriveXDistance(-2.5,1.0,0.1));
	 	
	 	//crossing platform zone
	 	addSequential(new RotateXDistancePIDZZZ(-30,1.0,.22));
	 	addSequential(new DriveXDistance(17.0,1.0,0.1));
	 	addSequential(new RotateXDistancePIDZZZ(-75,1.0,.22));
	 	addSequential(new DriveXDistance(5.0,1.0,0.1));
	 	addSequential(new RotateXDistancePIDZZZ(-90,1.0,.22));
	 	
	 	//score on scale (Spit Intake)
   	 	addSequential(new LowerGearArmDANGEROUS(),0.3); 
   	 	addSequential(new DriveGearIntakeRollerWithConstant(-1.0),0.4);
   	 	addSequential(new DriveGearIntakeRollerWithConstant(0.0),0.4);
	    addSequential(new RaiseGearArm(),0.2);
    }
}
