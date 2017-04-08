package org.team2168.commands.elevator;

import org.team2168.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Runs elevator until a ball is not present
 * @author Wen Baid
 */
public class RunElevatorUntilBallNotPresent extends Command {
	double speed;
	
	/**
	 * 
	 * @param speed -1.0 to 1.0. Positive values bring a ball into the shooter.
	 */
    public RunElevatorUntilBallNotPresent(double speed) {
    	requires(Robot.ballConvelator);
    	this.speed = speed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		Robot.ballConvelator.driveElevator(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return Robot.shooterIndexer.isBallAbsent();
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
