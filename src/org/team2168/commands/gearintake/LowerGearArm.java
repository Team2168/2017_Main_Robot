package org.team2168.commands.gearintake;

import edu.wpi.first.wpilibj.command.Command;

import org.team2168.Robot;

/**
 *Lowers the arm of the Gear Intake.
 *@Author Elijah Reeds
 */
public class LowerGearArm extends Command {

    public LowerGearArm() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.gearIntakeArm);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if(Robot.ballIntakeArm.isArmRaised()){
    		Robot.gearIntakeArm.Lower();
    	}

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return Robot.gearIntakeArm.isArmLowered();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
