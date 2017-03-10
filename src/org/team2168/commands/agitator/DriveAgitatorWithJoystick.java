package org.team2168.commands.agitator;

import org.team2168.OI;
import org.team2168.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *Drive Agitator with joystick 
 */
public class DriveAgitatorWithJoystick extends Command {

    public DriveAgitatorWithJoystick() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.agitator);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.agitator.driveAgitator(OI.getDriveAgitatorJoystick());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.agitator.driveAgitator(0.0);
    }
    

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
