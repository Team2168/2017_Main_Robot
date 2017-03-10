package org.team2168.commands.ballIntake;

import org.team2168.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *Raises the arm of the Ball Intake.
 *@author Elijah Reeds
 */
public class RaiseBallIntakeArm extends Command {

    public RaiseBallIntakeArm() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.ballIntakeArm);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
		Robot.ballIntakeArm.Raise();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    		return Robot.ballIntakeArm.isArmRaised();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
