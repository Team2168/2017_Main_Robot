package org.team2168.commands.auto;

import org.team2168.commands.drivetrain.ShiftHigh;
import org.team2168.commands.drivetrain.PIDCommands.DriveXDistance;
import org.team2168.commands.drivetrain.PIDCommands.DriveXUntilGear;
import org.team2168.commands.drivetrain.PIDCommands.RotateXDistancePIDZZZ;
import org.team2168.commands.gearintake.AutomaticGearIntake;
import org.team2168.commands.gearintake.DriveGearIntakeRollerWithConstant;
import org.team2168.commands.gearintake.LowerGearArmDANGEROUS;
import org.team2168.commands.gearintake.RaiseGearArm;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Drive to right switch boiii
 */
public class DriveToLeftSwitch extends CommandGroup {

    public DriveToLeftSwitch() {
    	 addSequential(new DriveXDistance(3.0,1.0,0.05));
    	 addSequential(new RotateXDistancePIDZZZ(-45,1.0,0.22));
    	 addSequential(new DriveXDistance(6.0,1.0,0.05));
    	 addSequential(new RotateXDistancePIDZZZ(45,1.0,0.22));
    	 addSequential(new DriveXDistance(3.0,1.0,0.1));
    	 addSequential(new RotateXDistancePIDZZZ(90,1.0,0.22));
    	 addSequential(new DriveXDistance(1.0,1.0,0.1));
    	 
    	 
    	 //score on switch (Spit Intake)
    	addSequential(new LowerGearArmDANGEROUS(),0.3); 
    	addSequential(new DriveGearIntakeRollerWithConstant(-1.0),0.4);
    	 addSequential(new DriveGearIntakeRollerWithConstant(0.0),0.4);
 	     addSequential(new RaiseGearArm(),0.2);
    	 
    	 
    	 //find second cube
 	    addSequential(new RotateXDistancePIDZZZ(90,1.0,0.22));
 	   addSequential(new DriveXDistance(-7.0,1.0,0.1));
 	  addSequential(new RotateXDistancePIDZZZ(-45,1.0,0.22)); 
 	 
    	 
 	  //pick up second cube
 	 addParallel(new LowerGearArmDANGEROUS(),0.3); 
 	 addParallel(new DriveGearIntakeRollerWithConstant(1.0));
 	addSequential(new DriveXDistance(5.0,0.6,0.1));
 	addParallel(new RaiseGearArm(),0.5);
 	addSequential(new DriveXDistance(-4.5,1.0,0.1));
 	
 	addSequential(new RotateXDistancePIDZZZ(-150,1.0,0.22));
//    	 //addSequential(new DriveXDistance(-1.0,0.7,0.05));
//    	 addSequential(new RotateXDistancePIDZZZ(90,0.7,0.2));
//    	 addSequential(new DriveXDistance(5,0.6,0.1));
//    	 addSequential(new RotateXDistancePIDZZZ(-90,0.7,0.2));
    	 
    }
}
