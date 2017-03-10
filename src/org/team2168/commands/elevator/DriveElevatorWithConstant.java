package org.team2168.commands.elevator;

import org.team2168.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *Drives Elevator at a constant speed 
 */
public class DriveElevatorWithConstant extends Command {
	double speed;
   
	/**
	 * Runs Elevator at a constant speed, -1.0 is down, 1.0 is up
	 * @param speed
	 */
	public DriveElevatorWithConstant(double inputspeed) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.ballConvelator);
        speed = inputspeed;   
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.ballConvelator.driveElevator(speed);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
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
