package org.team2168.commands.elevator;

import org.team2168.OI;
import org.team2168.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *Drive Elevator with joystick 
 */
public class DriveElevatorWithJoystick extends Command {

    public DriveElevatorWithJoystick() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.ballConvelator);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.ballConvelator.driveElevator(OI.getDriveElevatorJoystick() + OI.getDriveElevatorPIDTestJoystick());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.ballConvelator.driveElevator(0.0);
    }
    

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
